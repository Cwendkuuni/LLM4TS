import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checker;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    checker = new CheckSideEffects(compiler, level, false);
    nodeTraversal = new NodeTraversal(compiler, options, checker);
  }

  @Test
  public void testVisitVoidNode() {
    Node voidNode = NodeOperations.createVoidNode();
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), voidNode);
    // No side effect, so no warning should be generated.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testVisitQualifiedNameNodeWithJSDocInfo() {
    String name = "myFunction";
    Node qualifiedNameNode = NodeOperations.createQualifiedNameNode(name);
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), qualifiedNameNode);
    // No side effect, so no warning should be generated.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testVisitSimpleOperatorNodeWithUsedResult() {
    String left = "myVariable";
    String operator = "+";
    String right = "5";
    Node expressionNode = NodeOperations.createBinaryOpNode(Token.PLUS, NodeOperations.createNameNode(left), NodeOperations.createNumberNode(Integer.parseInt(right)));
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), expressionNode);
    // No side effect, since the result of the expression is used.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testVisitSimpleOperatorNodeWithUnusedResult() {
    String left = "myVariable";
    String operator = "+";
    String right = "5";
    Node expressionNode = NodeOperations.createBinaryOpNode(Token.PLUS, NodeOperations.createNameNode(left), NodeOperations.createNumberNode(Integer.parseInt(right)));
    Node parentNode = NodeOperations.createBlockNode(1);
    parentNode.addChild(expressionNode);
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), parentNode);
    // Since the result of the expression is not used, a warning should be generated.
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    when(compiler.report(any(Node.class), eq(level), eq(diagnosticTypeCaptor.capture()), eq(messageCaptor.capture()))).thenReturn(null);
    assertEquals(1, checker.problemNodes.size());
    assertEquals("The result of the '+' operator is not being used.", messageCaptor.getValue().getMessage());
  }

  @Test
  public void testVisitCallNodeWithProtectorFunction() {
    String protectorFunctionName = "JSCOMPILER_PRESERVE";
    Node protectorFunctionCallNode = NodeOperations.createCallNode(NodeOperations.createNameNode(protectorFunctionName), Lists.newArrayList());
    Node parentNode = NodeOperations.createBlockNode(1);
    parentNode.addChild(protectorFunctionCallNode);
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), parentNode);
    // Since the protector function call does not have any side effects,
    // no warning should be generated.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testVisitCallNodeWithEmptyArgumentList() {
    String functionName = "myFunction";
    Node functionCallNode = NodeOperations.createCallNode(NodeOperations.createNameNode(functionName), Lists.newArrayList());
    Node parentNode = NodeOperations.createBlockNode(1);
    parentNode.addChild(functionCallNode);
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), parentNode);
    // Since the call node does not have any side effects, no warning should be generated.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testVisitCallNodeWithSideEffect() {
    String functionName = "myFunction";
    String argument = "myArgument";
    Node functionCallNode = NodeOperations.createCallNode(NodeOperations.createNameNode(functionName), Lists.newArrayList(NodeOperations.createNameNode(argument)));
    Node parentNode = NodeOperations.createBlockNode(1);
    parentNode.addChild(functionCallNode);
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), parentNode);
    // Since the function call has a side effect, a warning should be generated.
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<CheckLevel> levelCaptor = ArgumentCaptor.forClass(CheckLevel.class);
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    when(compiler.report(any(Node.class), eq(level), eq(diagnosticTypeCaptor.capture()), eq(messageCaptor.capture()))).thenReturn(null);
    assertEquals(1, checker.problemNodes.size());
    assertEquals("Suspicious code. myFunction(myArgument)", messageCaptor.getValue().getMessage());
  }

  @Test
  public void testVisitCallNodeWithSideEffectProtectedByExtern() {
    String protectorFunctionName = "JSCOMPILER_PRESERVE";
    String functionName = "myFunction";
    String argument = "myArgument";
    Node protectorFunctionCallNode = NodeOperations.createCallNode(NodeOperations.createNameNode(protectorFunctionName), Lists.newArrayList());
    Node functionCallNode = NodeOperations.createCallNode(NodeOperations.createNameNode(functionName), Lists.newArrayList(NodeOperations.createNameNode(argument)));
    Node parentNode = NodeOperations.createBlockNode(1);
    parentNode.addChild(protectorFunctionCallNode);
    parentNode.addChild(functionCallNode);
    nodeTraversal.traverse(null, NodeOperations.createRootNode(), parentNode);
    // Since the call node is protected by the protector function, no warning should be generated.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testProtectSideEffectsMethod() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checker.process(null, NodeOperations.createRootNode());
    // No side effect free nodes were found, so no protection is necessary.
    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testProtectSideEffectsMethodWithSideEffectFreeNodes() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node expressionNode1 = NodeOperations.createBinaryOpNode(Token.MINUS, NodeOperations.createNumberNode(1), NodeOperations.createNumberNode(2));
    Node expressionNode2 = NodeOperations.createBinaryOpNode(Token.PLUS, NodeOperations.createNumberNode(2), NodeOperations.createNumberNode(3));
    Node blockNode = NodeOperations.createBlockNode(1);
    blockNode.addChild(expressionNode1);
    blockNode.addChild(expressionNode2);
    checker.process(null, NodeOperations.createRootNode());
    // Side effect free nodes were found, so they are protected.
    assertEquals(2, checker.problemNodes.size());
    for (Node problemNode : checker.problemNodes) {
      Node nameNode = problemNode.getFirstChild();
      assertEquals(PROTECTOR_FN, nameNode.getString());
    }
  }

  @Test
  public void testRemoveProtectionMethod() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node functionCallNode = NodeOperations.createCallNode(NodeOperations.createNameNode(PROTECTOR_FN), Lists.newArrayList());
    Node parentNode = NodeOperations.createBlockNode(1);
    parentNode.addChild(functionCallNode);
    checker.process(null, NodeOperations.createRootNode());
    checker.protectSideEffects();
    // Protected node was found, so it is removed.
    assertEquals(0, checker.problemNodes.size());
    // Check that the protected node is no longer present in the tree.
    assertEquals(0, parentNode.getChildCount());
  }
}