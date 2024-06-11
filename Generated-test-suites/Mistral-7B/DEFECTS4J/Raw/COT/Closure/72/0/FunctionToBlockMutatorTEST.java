package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionArgumentInjector;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class FunctionToBlockMutatorTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private Supplier<String> safeNameIdSupplier;

  @InjectMocks
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setup() {
    when(compiler.getUniqueNameIdSupplier()).thenAnswer(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        return "test";
      }
    });
  }

  @Test
  public void testMutate_NullFnNode() {
    assertNull(functionToBlockMutator.mutate(null, null, null, null, false, false));
  }

  @Test
  public void testMutate_EmptyFunctionBody() {
    Node fnNode = NodeUtil.newBlockNode();
    Node callNode = NodeUtil.newCallNode("testFunction", fnNode, null, null);
    Node resultNode = functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false);

    assertNotNull(resultNode);
  }

  @Test
  public void testMutate_FunctionWithArguments() {
    Node fnNode = NodeUtil.newFunctionNode(NodeUtil.newNameNode("testFunction"), null, null, NodeUtil.newBlockNode());
    Node callNode = NodeUtil.newCallNode("testFunction", fnNode, null, null);
    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNameNode("arg1"));
    argMap.put("arg2", NodeUtil.newNameNode("arg2"));

    Node resultNode = functionToBlockMutator.mutate(null, fnNode, callNode, null, false, false);
    assertNotNull(resultNode);

    Node expectedResultNode = NodeUtil.newBlockNode();
    FunctionArgumentInjector.inject(expectedResultNode, null, argMap);
    assertNodesEqual(resultNode, expectedResultNode);
  }

  @Test
  public void testReplaceReturns_NullBlock() {
    assertNull(functionToBlockMutator.replaceReturns(null, null, null, null, false));
  }

  @Test
  public void testReplaceReturns_NoReturn() {
    Node block = NodeUtil.newBlockNode();
    Node result = functionToBlockMutator.replaceReturns(block, null, null, null, false);
    assertNull(result);
  }

  @Test
  public void testReplaceReturns_ReturnWithExpression() {
    Node block = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(NodeUtil.newNameNode("test"));
    block.addChildToBack(returnNode);

    Node result = functionToBlockMutator.replaceReturns(block, null, null, null, false);
    Node expectedResult = NodeUtil.newBlockNode();
    expectedResult.addChildToBack(NodeUtil.newExprNode(returnNode));
    assertNodesEqual(result, expectedResult);
  }

  @Test
  public void testReplaceReturns_ReturnWithLabel() {
    Node block = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(NodeUtil.newNameNode("test"));
    String labelName = "testLabel";
    block.addChildToBack(returnNode);
    block.addChildToBack(NodeUtil.newLabelNode(labelName));

    Node result = functionToBlockMutator.replaceReturns(block, null, labelName, false);
    Node expectedResult = NodeUtil.newBlockNode();
    expectedResult.addChildToBack(NodeUtil.newLabelNode(labelName));
    expectedResult.addChildToBack(NodeUtil.newBreakNode(labelName));
    assertNodesEqual(result, expectedResult);
  }

  @Test
  public void testReplaceReturns_ReturnWithLabelAndResultVariable() {
    Node block = NodeUtil.newBlockNode();
    Node returnNode = NodeUtil.newReturnNode(NodeUtil.newNameNode("test"));
    String labelName = "testLabel";
    String resultName = "testResult";
    block.addChildToBack(returnNode);
    block.addChildToBack(NodeUtil.newLabelNode(labelName));

    Node result = functionToBlockMutator.replaceReturns(block, resultName, labelName, true);
    Node expectedResult = NodeUtil.newBlockNode();
    expectedResult.addChildToBack(NodeUtil.newLabelNode(labelName));
    expectedResult.addChildToBack(NodeUtil.newAssignStatementNode(resultName, NodeUtil.newUndefinedNode()));
    expectedResult.addChildToBack(NodeUtil.newBreakNode(labelName));
    assertNodesEqual(result, expectedResult);
  }

  private void assertNodesEqual(Node expected, Node actual) {
    NodeTraversal.traverse(compiler, expected, new NodeDiffer());
    NodeTraversal.traverse(compiler, actual, new NodeDiffer());
  }

  private static class NodeDiffer extends NodeVisitor<Object, Object> {

    @Override
    public Object visitBlock(Node node, Object param) {
      Object result = super.visitBlock(node, param);
      for (int i = 0; i < node.getNumChildren(); i++) {
        Node child = node.getChild(i);
        Object childResult = visit(child, param);
        if (childResult != null) {
          result = childResult;
        }
      }
      return result;
    }

    @Override
    public Object visitLabel(Node node, Object param) {
      Object result = super.visitLabel(node, param);
      if (result == null && node.getName().equals("testLabel")) {
        result = NodeUtil.newLabelNode(node.getName());
      }
      return result;
    }

    @Override
    public Object visitName(Node node, Object param) {
      Object result = super.visitName(node, param);
      if (result == null && node.getName().equals("testLabel")) {
        result = NodeUtil.newLabelNameNode(node.getName());
      }
      return result;
    }

    @Override
    public Object visitBreak(Node node, Object param) {
      Object result = super.visitBreak(node, param);
      if (result == null && node.getString("label").equals("testLabel")) {
        result = NodeUtil.newBreakNode(NodeUtil.newLabelNameNode("testLabel"));
      }
      return result;
    }

    @Override
    public Object visitReturn(Node node, Object param) {
      Object result = super.visitReturn(node, param);
      if (result == null) {
        result = NodeUtil.newReturnNode(null);
      }
      return result;
    }

    @Override
    public Object visitUndefined(Node node, Object param) {
      Object result = super.visitUndefined(node, param);
      if (result == null) {
        result = NodeUtil.newUndefinedNode(node.getSourcePosition());
      }
      return result;
    }

    @Override
    public Object defaultAction(Node node, Object param) {
      return node;
    }
  }
}