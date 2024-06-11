package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.javascript.rhino.NodeUtil.isFunctionExpression;
import static com.google.javascript.rhino.NodeUtil.isName;
import static com.google.javascript.rhino.NodeUtil.isThis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FunctionRewriterTest {

  @Mock
  private AbstractCompiler compiler;

  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testProcess_EmptyProgram() {
    Node root = null;
    Node externs = null;

    functionRewriter.process(externs, root);

    assertFalse("Should not apply any reductions for empty program",
                functionRewriter.reductions.size() > 0);
  }

  @Test
  public void testProcess_NoReducibleFunctionExpressions() {
    Node externs = mock(Node.class);
    Node root = Node.newObject("{}");

    functionRewriter.process(externs, root);

    assertFalse("Should not apply any reductions for no reducible function expressions",
                functionRewriter.reductions.size() > 0);
  }

  @Test
  public void testProcess_EmptyFunction() {
    Node externs = mock(Node.class);
    Node root = Node.newObject("function() {}");

    functionRewriter.process(externs, root);

    assertEquals(1, functionRewriter.reductions.size());

    Reduction reduction = functionRewriter.reductions.get(0);
    assertEquals(root, reduction.parent);
    assertNull(reduction.oldChild);

    Node helperCode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_IdentityFunction() {
    Node externs = mock(Node.class);
    Node root = Node.newObject("function(x) {return x}");

    functionRewriter.process(externs, root);

    assertEquals(1, functionRewriter.reductions.size());

    Reduction reduction = functionRewriter.reductions.get(0);
    assertEquals(root, reduction.parent);
    assertNull(reduction.oldChild);

    Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_ConstantFunction() {
    Node externs = mock(Node.class);
    Node root = Node.newObject("function() {return 10}");

    functionRewriter.process(externs, root);

    assertEquals(1, functionRewriter.reductions.size());

    Reduction reduction = functionRewriter.reductions.get(0);
    assertEquals(root, reduction.parent);
    assertNull(reduction.oldChild);

    Node helperCode = functionRewriter.parseHelperCode(ReturnConstantReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_GetterFunction() {
    Node externs = mock(Node.class);
    Node root = Node.newObject("function() {return this.a}");

    functionRewriter.process(externs, root);

    assertEquals(1, functionRewriter.reductions.size());

    Reduction reduction = functionRewriter.reductions.get(0);
    assertEquals(root, reduction.parent);
    assertNull(reduction.oldChild);

    Node helperCode = functionRewriter.parseHelperCode(GetterReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_SetterFunction() {
    Node externs = mock(Node.class);
    Node root = Node.newObject("function(value) {this.a = value}");

    functionRewriter.process(externs, root);

    assertEquals(1, functionRewriter.reductions.size());

    Reduction reduction = functionRewriter.reductions.get(0);
    assertEquals(root, reduction.parent);
    assertNull(reduction.oldChild);

    Node helperCode = functionRewriter.parseHelperCode(SetterReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testParseHelperCode_EmptyFunction() {
    functionRewriter = new FunctionRewriter(compiler);

    assertNull(functionRewriter.parseHelperCode(EmptyFunctionReducer.class));
  }

  @Test
  public void testParseHelperCode_IdentityFunction() {
    functionRewriter = new FunctionRewriter(compiler);

    String helperSource = "function identity(value) {" +
            "return value}";
    Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);

    assertEquals(helperSource, functionRewriter.helperCodeToString(helperCode));
  }

  @Test
  public void testParseHelperCode_ReturnConstantFunction() {
    functionRewriter = new FunctionRewriter(compiler);

    String helperSource = "function returnconst(value) {" +
            "return function() {return value}" +
            "}";
    Node helperCode = functionRewriter.parseHelperCode(ReturnConstantReducer.class);

    assertEquals(helperSource, functionRewriter.helperCodeToString(helperCode));
  }

  @Test
  public void testParseHelperCode_GetterFunction() {
    functionRewriter = new FunctionRewriter(compiler);

    String helperSource = "function getter(name) {" +
            "return function() {return this[" + name + "]}" +
            "}";
    Node helperCode = functionRewriter.parseHelperCode(GetterReducer.class);

    assertEquals(helperSource, functionRewriter.helperCodeToString(helperCode));
  }

  @Test
  public void testParseHelperCode_SetterFunction() {
    functionRewriter = new FunctionRewriter(compiler);

    String helperSource = "function setter(name) {" +
            "return function(value) {" +
            "this[" + name + "] = value}" +
            "}";
    Node helperCode = functionRewriter.parseHelperCode(SetterReducer.class);

    assertEquals(helperSource, functionRewriter.helperCodeToString(helperCode));
  }

  private String functionRewriterHelperCodeToString(Node node) {
    StringBuilder stringBuilder = new StringBuilder();
    NodeTraversal.traverse(functionRewriter.compiler, node, new NodePrinter(stringBuilder));
    return stringBuilder.toString();
  }

  private static class NodePrinter implements Callback {
    private final StringBuilder stringBuilder;

    NodePrinter(StringBuilder stringBuilder) {
      this.stringBuilder = stringBuilder;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      stringBuilder.append(NodeUtil.nodeToString(node));
      stringBuilder.append("\n");
    }

    @Override
    public void visit(NodeTraversal traversal, NodeType nodeType, Node node, Node parent) {
      stringBuilder.append(NodeUtil.nodeToString(nodeType, node));
      stringBuilder.append("\n");
    }

    @Override
    public void visit(NodeTraversal traversal, NodeList nodeList, Node parent) {
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.getChildNodes().get(i);
        visit(traversal, node, node.getParentNode());
      }
    }
  }
}