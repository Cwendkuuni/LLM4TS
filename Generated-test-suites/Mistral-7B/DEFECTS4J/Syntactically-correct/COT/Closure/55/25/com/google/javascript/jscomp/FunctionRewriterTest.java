import static com.google.javascript.jscomp.NodeUtil.isGetProp;
import static com.google.javascript.jscomp.NodeUtil.isImmutableValue;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isReduceableFunctionExpression;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.parser.bare.Parser;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {
  @Test
  public void testEmptyFunctionReducer() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeUtil.isName(functionNode.getFirstChild(), "emptyFn"), true);
  }

  @Test
  public void testIdentityReducer() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a() {return a;}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeUtil.isName(functionNode.getFirstChild(), "identityFn"), true);
  }

  @Test
  public void testReturnConstantReducer() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a() {return 10}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeUtil.isName(functionNode.getFirstChild(), "returnconst"), true);
  }

  @Test
  public void testGetterReducer() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a() {return this.a_}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeUtil.isName(functionNode.getFirstChild(), "getter"), true);
  }

  @Test
  public void testSetterReducer() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a(value) {this.a_ = value}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeUtil.isName(functionNode.getFirstChild(), "getter"), true);
  }

  @Test
  public void testIsReducibleFunctionExpression() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a() {}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    assertFalse(functionRewriter.isReduceableFunctionExpression(NodeOperations.findDescendantByType(root, Token.FUNCTION)));

    root = Parser.parseJS("function a() {return a;}", "").program;
    assertTrue(functionRewriter.isReduceableFunctionExpression(NodeOperations.findDescendantByType(root, Token.FUNCTION)));

    root = Parser.parseJS("function a() {return this.a_;}", "").program;
    assertTrue(functionRewriter.isReduceableFunctionExpression(NodeOperations.findDescendantByType(root, Token.FUNCTION)));

    root = Parser.parseJS("function a(value) {this.a_ = value}", "").program;
    assertTrue(functionRewriter.isReducibleFunctionExpression(NodeOperations.findDescendantByType(root, Token.FUNCTION)));
  }

  @Test
  public void testIdentityFunction() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a(value) {return value}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    functionRewriter.process(null, root);

    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertNull(functionRewriter.maybeGetSingleReturnRValue(functionNode));

    functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION, "identityFn");
    Node valueNode = functionRewriter.getValueNode(functionNode);
    assertTrue(valueNode != null);
    assertEquals(isImmutableValue(valueNode), true);
  }

  @Test
  public void testSingleReturnStatementReducer() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("function a() {" +
                              "  return 10;" +
                              "  return 20;" +
                              "}", "").program;

    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);

    functionRewriter.process(null, root);
    assertEquals(NodeOperations.getChildCount(functionNode), 1);

    root = Parser.parseJS("function a() {return 10;}", "").program;
    functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeOperations.getChildCount(functionNode), 1);

    root = Parser.parseJS("function a() {return 10; this.b_ = 20;}", "").program;
    functionNode = NodeOperations.findDescendantByType(root, Token.FUNCTION);
    assertEquals(NodeOperations.getChildCount(functionNode), 1);
  }

  @Test
  public void testIsImmutableValue() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("", "").program;

    assertEquals(isImmutableValue(NodeOperations.findDescendantByType(root, Token.INT_LITERAL, "10")), true);
    assertEquals(isImmutableValue(NodeOperations.findDescendantByType(root, Token.STRING_LITERAL, "\"10\"")), true);
    assertEquals(isImmutableValue(NodeOperations.findDescendantByType(root, Token.BOOLEAN, "true")), true);
    assertEquals(isImmutableValue(NodeOperations.findDescendantByType(root, Token.NULL)), true);

    assertEquals(isImmutableValue(NodeOperations.findDescendantByType(root, Token.IDENT, "a")), false);
    assertEquals(isImmutableValue(NodeOperations.findDescendantByType(root, Token.CALL, "foo()")), false);
  }

  @Test
  public void testIsName() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("", "").program;

    assertEquals(isName(NodeOperations.findDescendantByType(root, Token.IDENT, "a")), true);
    assertEquals(isName(NodeOperations.findDescendantByType(root, Token.IDENT, "foo")), true);
    assertEquals(isName(NodeOperations.findDescendantByType(root, Token.IDENT, "this")), true);

    assertEquals(isName(NodeOperations.findDescendantByType(root, Token.STRING_LITERAL, "\"foo\"")), false);
    assertEquals(isName(NodeOperations.findDescendantByType(root, Token.INT_LITERAL, "10")), false);
  }

  @Test
  public void testIsGetProp() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("", "").program;

    assertEquals(isGetProp(NodeOperations.findDescendantByType(root, Token.GETPROP, "a.b")), true);
    assertEquals(isGetProp(NodeOperations.findDescendantByType(root, Token.GETPROP, "this.b")), true);

    assertEquals(isGetProp(NodeOperations.findDescendantByType(root, Token.IDENT, "a")), false);
    assertEquals(isGetProp(NodeOperations.findDescendantByType(root, Token.STRING_LITERAL, "\"a.b\"")), false);
  }

  @Test
  public void testIsThis() throws Exception {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = Parser.parseJS("", "").program;

    assertEquals(isThis(NodeOperations.findDescendantByType(root, Token.THIS, "")), true);

    assertEquals(isThis(NodeOperations.findDescendantByType(root, Token.IDENT, "this")), true);
    assertEquals(isThis(NodeOperations.findDescendantByType(root, Token.STRING_LITERAL, "\"this\"")), false);
  }

  private boolean assertFalse(boolean condition) {
    assertFalse("Expected false, got " + condition, !condition);
    return !condition;
  }

  private boolean assertTrue(boolean condition) {
    assertTrue("Expected true, got " + condition, condition);
    return condition;
  }

  private static List<CompilerPass> passList(CompilerPass... passes) {
    return ImmutableList.copyOf(passes);
  }
}