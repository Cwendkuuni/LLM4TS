package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() {}, setA: function(value) {} } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    Node setA = NodeUtil.findChildByName(root, "prototype", "setA");

    assertEquals(Token.CALL, getA.getType());
    assertEquals("emptyFn", getA.getFirstChild().getString());

    assertEquals(Token.CALL, setA.getType());
    assertEquals("emptyFn", setA.getFirstChild().getString());
  }

  @Test
  public void testIdentityReducer() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function(a) { return a },Identity: function(a) { return a } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    Node identity = NodeUtil.findChildByName(root, "prototype", "Identity");

    assertEquals(Token.CALL, getA.getType());
    assertEquals("identityFn_value", getA.getFirstChild().getString());

    assertEquals(Token.FUNCTION, identity.getType());
    Node identityFunction = identity.getFirstChild();
    assertEquals(1, identityFunction.getNumChildren());
    assertEquals(Token.VAR, identityFunction.getFirstChild().getType());
    assertEquals("identityFn_value", identityFunction.getFirstChild().getString());
    assertEquals(Token.RETURN, identityFunction.getLastChild().getType());
    assertEquals("identityFn_value", identityFunction.getLastChild().getFirstChild().getString());
  }

  @Test
  public void testReturnConstantReducer() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() { return 1 } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    assertEquals(Token.CALL, getA.getType());
    assertEquals("returnArg_value", getA.getFirstChild().getString());
    assertEquals(1, getA.getFirstChild().getNumChildren());
    assertEquals(Token.NUMBER, getA.getFirstChild().getFirstChild().getType());
    assertEquals(1, getA.getFirstChild().getFirstChild().getNumber().intValue());
  }

  @Test
  public void testGetterReducer() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() { return this.a } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    assertEquals(Token.CALL, getA.getType());
    assertEquals("get", getA.getFirstChild().getString());
    assertEquals("a", getA.getFirstChild().getLastChild().getString());
  }

  @Test
  public void testSetterReducer() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { setA: function(value) { this.a = value } } }", null);
    functionRewriter.process(null, root);

    Node setA = NodeUtil.findChildByName(root, "prototype", "setA");
    assertEquals(Token.CALL, setA.getType());
    assertEquals("set", setA.getFirstChild().getString());
    assertEquals("a", setA.getFirstChild().getLastChild().getString());
  }

  @Test
  public void testNoReduction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function(a) { return a + 1 }, setA: function(value) { this.a = value + 1 } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    Node setA = NodeUtil.findChildByName(root, "prototype", "setA");

    assertSame(getA, NodeUtil.findChildByName(root, "prototype", "getA"));
    assertSame(setA, NodeUtil.findChildByName(root, "prototype", "setA"));
  }

  @Test
  public void testNoReductionWithRecursion() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() { return getA() }, setA: function(value) { this.a = setA(value) } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    Node setA = NodeUtil.findChildByName(root, "prototype", "setA");

    assertSame(getA, NodeUtil.findChildByName(root, "prototype", "getA"));
    assertSame(setA, NodeUtil.findChildByName(root, "prototype", "setA"));
  }

  @Test
  public void testNoReductionWithCycle() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() { return getB() }, setA: function(value) { this.b = value }, getB: function() { return this.a }, setB: function(value) { this.a = value } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    Node setA = NodeUtil.findChildByName(root, "prototype", "setA");
    Node getB = NodeUtil.findChildByName(root, "prototype", "getB");
    Node setB = NodeUtil.findChildByName(root, "prototype", "setB");

    assertSame(getA, NodeUtil.findChildByName(root, "prototype", "getA"));
    assertSame(setA, NodeUtil.findChildByName(root, "prototype", "setA"));
    assertSame(getB, NodeUtil.findChildByName(root, "prototype", "getB"));
    assertSame(setB, NodeUtil.findChildByName(root, "prototype", "setB"));
  }

  @Test
  public void testNoReductionWithStaticFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { static getA: function() { return 1 }, setA: function(value) { this.a = value } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "getA", null);
    Node setA = NodeUtil.findChildByName(root, "setA", null);

    assertNull(getA);
    assertNull(setA);
  }

  @Test
  public void testNoReductionWithPrivateFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() { return #a }, setA: function(value) { #a = value } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    Node setA = NodeUtil.findChildByName(root, "prototype", "setA");

    assertNull(getA);
    assertNull(setA);
  }

  @Test
  public void testReturnUndefinedReducer() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getA: function() { return undefined } } }", null);
    functionRewriter.process(null, root);

    Node getA = NodeUtil.findChildByName(root, "prototype", "getA");
    assertEquals(Token.CALL, getA.getType());
    assertEquals("undefined", getA.getFirstChild().getString());
  }

  @Test
  public void testSetterAndGetterReducerOnSameName() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = compiler.parseJavaScript("class C { prototype: { getX: function() { return this.x }, setX: function(value) { this.x = value } } }", null);
    functionRewriter.process(null, root);

    Node getX = NodeUtil.findChildByName(root, "prototype", "getX");
    Node setX = NodeUtil.findChildByName(root, "prototype", "setX");

    assertEquals(Token.CALL, getX.getType());
    assertEquals("get", getX.getFirstChild().getString());
    assertEquals("x", getX.getFirstChild().getLastChild().getString());

    assertEquals(Token.CALL, setX.getType());
    assertEquals("set", setX.getFirstChild().getString());
    assertEquals("x", setX.getFirstChild().getLastChild().getString());
  }

  private static class Compiler extends AbstractCompiler {
    @Override
    public Node parseJavaScript(String src, SourceRoot root) {
      return super.parseJavaScript(src, root);
    }
  }
}