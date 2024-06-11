package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
  }

  @Test
  public void testScopedAliases() {
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "})";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().size() == 2);

    Node domElement = traversal.getAliasUsages().get(0).getNode();
    Node domAlias = traversal.getAliasDefinitionsInOrder().get(0).getNode();
    assertEquals(Node.NAME, domElement.getType());
    assertEquals("dom", domElement.getString());
    assertEquals(Node.NAME, domAlias.getType());
    assertEquals("goog.dom", domAlias.getString());

    Node divElement = traversal.getAliasUsages().get(1).getNode();
    Node divAlias = traversal.getAliasDefinitionsInOrder().get(1).getNode();
    assertEquals(Node.NAME, divElement.getType());
    assertEquals("DIV", divElement.getString());
    assertEquals(Node.NAME, divAlias.getType());
    assertEquals("goog.dom.TagName.DIV", divAlias.getString());

    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testScopedAliasesWithFunctionCall() {
    String code = "goog.scope(function() {" +
        "function test() {" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "}" +
        "})();" +
        "test();";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.getAliasDefinitionsInOrder().isEmpty());
    assertTrue(traversal.getScopeCalls().isEmpty());
    assertTrue(traversal.getAliasUsages().size() == 2);

    Node domElement = traversal.getAliasUsages().get(0).getNode();
    Node domAlias = traversal.getAliasDefinitionsInOrder().get(0).getNode();
    assertEquals(Node.NAME, domElement.getType());
    assertEquals("dom", domElement.getString());
    assertEquals(Node.NAME, domAlias.getType());
    assertEquals("goog.dom", domAlias.getString());

    Node divElement = traversal.getAliasUsages().get(1).getNode();
    Node divAlias = traversal.getAliasDefinitionsInOrder().get(1).getNode();
    assertEquals(Node.NAME, divElement.getType());
    assertEquals("DIV", divElement.getString());
    assertEquals(Node.NAME, divAlias.getType());
    assertEquals("goog.dom.TagName.DIV", divAlias.getString());

    assertEquals(0, traversal.getDiagnostics().size());
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    String code = "goog.scope(" +
        "function() {" +
        "}" +
        ");";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("goog.scope must be alone in a single statement.",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testScopedAliasesWithInvalidParameters() {
    String code = "goog.scope(" +
        "function() {" +
        "  var a = 1;" +
        "}" +
        ");";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The call to goog.scope must take only a single parameter.",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testScopedAliasesWithInvalidBody() {
    String code = "goog.scope(" +
        "function() {" +
        "  var a = 1;" +
        "  return;" +
        "}" +
        ");";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The body of a goog.scope function cannot use 'return'.",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testScopedAliasesWithInvalidThrow() {
    String code = "goog.scope(" +
        "function() {" +
        "  throw new Error('test');" +
        "}" +
        ");";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The body of a goog.scope function cannot use 'throw'.",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testScopedAliasesWithDuplicateAliases() {
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = goog.dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "var DIV = 123;" +
        "})";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The alias DIV is assigned a value more than once.",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    String code = "goog.scope(function() {" +
        "var a = 1;" +
        "})";

    List<Node> ast = compiler.parse(code, null, options);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, ast.get(0));

    Traversal traversal = scopedAliasesPass.getTraversal();
    assertTrue(traversal.hasErrors());
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("The local variable a is in a goog.scope and is not an alias.",
        traversal.getDiagnostics().get(0).getMessage());
  }
}