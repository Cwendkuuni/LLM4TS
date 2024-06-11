package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Test cases for the MakeDeclaredNamesUnique class.
 */
public class MakeDeclaredNamesUniqueTest {

  private AbstractCompiler compiler;
  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

  @Before
  public void setUp() {
    compiler = new CompilerPassManager().getCompiler();
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
  }

  @Test
  public void testMakeDeclaredNamesUnique_simple() {
    String jsCode = "function foo() { var x = 1; var x = 2; }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar1 = traversal.getScope().getVar("x");
    assertEquals(1, xVar1.getIndex());

    Var xVar2 = traversal.getScope().getVar("x");
    assertEquals(2, xVar2.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("function foo() { var x_1 = 1; var x_2 = 2; }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithParameters() {
    String jsCode = "function foo(x, y) { var x = 1; }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar = traversal.getScope().getVar("x");
    assertEquals(1, xVar.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("function foo(x, y) { var x_1 = 1; }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithParametersAndDeclarations() {
    String jsCode = "function foo(x, y) { var x = 1; var y = 2; }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar = traversal.getScope().getVar("x");
    assertEquals(1, xVar.getIndex());

    Var yVar = traversal.getScope().getVar("y");
    assertEquals(2, yVar.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("function foo(x, y) { var x_1 = 1; var y_2 = 2; }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithNestedFunctions() {
    String jsCode = "function foo() { function bar() {} }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var barFunction = traversal.getScope().getVar("bar");
    assertEquals(1, barFunction.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("function foo() { function bar_1() {} }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithRecursiveDeclaration() {
    String jsCode = "function foo(x) { var x = x; }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar = traversal.getScope().getVar("x");
    assertEquals(0, xVar.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("function foo(x) { var x_0 = x; }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_functionWithCatchBlock() {
    String jsCode = "function foo() { try { throw new Error(); } catch (e) {} }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var eVar = traversal.getScope().getVar("e");
    assertNull(eVar);

    String newJsCode = compiler.getCode();
    assertEquals("function foo() { try { throw new Error(); } catch (e_1) {} }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_blockWithNestedBlocks() {
    String jsCode = "{ var x = 1; { var x = 2; } }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar1 = traversal.getScope().getVar("x");
    assertEquals(1, xVar1.getIndex());

    Scope innerScope = traversal.getScope().getChildScope();
    Var xVar2 = innerScope.getVar("x");
    assertEquals(2, xVar2.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("{ var x_1 = 1; { var x_2 = 2; } }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_blockWithVarDeclarationAndFunctionDeclaration() {
    String jsCode = "{ var x = 1; function foo() {} }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar = traversal.getScope().getVar("x");
    assertEquals(1, xVar.getIndex());

    Var fooFunction = traversal.getScope().getVar("foo");
    assertEquals(2, fooFunction.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("{ var x_1 = 1; function foo_2() {} }", newJsCode);
  }

  @Test
  public void testMakeDeclaredNamesUnique_blockWithNestedBlocksAndFunctionDeclaration() {
    String jsCode = "{ var x = 1; { function foo() {} } }";

    Node ast = new TokenStream(new ByteArrayInputStream(jsCode.getBytes()), null).parse();

    NodeTraversal traversal = new NodeTraversal(compiler, ast);
    makeDeclaredNamesUnique.traverse(traversal);

    Var xVar = traversal.getScope().getVar("x");
    assertEquals(1, xVar.getIndex());

    Scope innerScope = traversal.getScope().getChildScope();
    Var fooFunction = innerScope.getVar("foo");
    assertEquals(1, fooFunction.getIndex());

    String newJsCode = compiler.getCode();
    assertEquals("{ var x_1 = 1; { function foo_1() {} } }", newJsCode);
  }
}