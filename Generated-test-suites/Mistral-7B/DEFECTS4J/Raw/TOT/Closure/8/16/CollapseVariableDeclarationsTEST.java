package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSingleDeclaration() {
    String code = "function foo() {\n" +
            "  var a;\n" +
            "  var b = 42;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a, b = 42");
    Assert.assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapseMultipleDeclarations() {
    String code = "function foo() {\n" +
            "  var a;\n" +
            "  var b = 42;\n" +
            "  var c;\n" +
            "  var d = 43;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a, b = 42, c = 43");
    Assert.assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapseMultipleDeclarationsWithAssignment() {
    String code = "function foo() {\n" +
            "  var a = 1;\n" +
            "  var b = 2;\n" +
            "  var c = 3;\n" +
            "  var d = 4;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a = 1, b = 2, c = 3, d = 4");
    Assert.assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapseMultipleAssignments() {
    String code = "function foo() {\n" +
            "  var a = 1;\n" +
            "  var b = 2;\n" +
            "  a = 3;\n" +
            "  b = 4;\n" +
            "  var c = 5;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node assignA = NodeOperations.findFirstDescendantOfType(root, Node.ASSIGN);
    Node assignB = NodeOperations.findNextSiblingOfType(assignA, Node.ASSIGN);

    Node expected = new Node(Token.VAR, "a = 3, b = 4");
    Node expectedAssignA = new Node(Token.ASSIGN, "a");
    Node expectedAssignB = new Node(Token.ASSIGN, "b");

    expectedAssignA.addChildToBack(new Node(Token.NUM, 3));
    expectedAssignB.addChildToBack(new Node(Token.NUM, 4));
    expected.addChildToBack(expectedAssignA);
    expected.addChildToBack(expectedAssignB);

    Assert.assertEquals(expected.toString(), root.toString());
    Assert.assertEquals(expectedAssignA.toString(), assignA.toString());
    Assert.assertEquals(expectedAssignB.toString(), assignB.toString());
  }

  @Test
  public void testCollapseVariableDeclarationWithJSDoc() {
    String code = "/**\n" +
            " * @function foo\n" +
            " * @return {number} The result.\n" +
            " */\n" +
            "function foo() {\n" +
            "  /** @type {number} */\n" +
            "  var a;\n" +
            "  var b = 42;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "/** @type {number} */ a, b = 42");
    Assert.assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testCollapseVariableDeclarationWithJSDocAndAssignment() {
    String code = "/**\n" +
            " * @function foo\n" +
            " * @return {number} The result.\n" +
            " */\n" +
            "function foo() {\n" +
            "  /** @type {number} */\n" +
            "  var a;\n" +
            "  a = 42;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "/** @type {number} */ a = 42");
    Assert.assertEquals(expected.toString(), root.toString());
  }

  @Test
  public void testDoNotCollapseVariableDeclarationsWithinFunctionCall() {
    String code = "function foo(x) {\n" +
            "  function bar() {\n" +
            "    var a;\n" +
            "    var b = 42;\n" +
            "  }\n" +
            "  bar();\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node functionBar = NodeOperations.findFirstDescendantOfType(root, Node.FUNCTION);
    Node varA = NodeOperations.findFirstDescendantOfType(functionBar, Node.VAR);
    Node varB = NodeOperations.findNextSiblingOfType(varA, Node.VAR);

    Assert.assertNotNull(varA);
    Assert.assertNotNull(varB);

    Assert.assertEquals("function bar() { var a; var b = 42; }", functionBar.toString());
  }

  @Test
  public void testDoNotCollapseVariableDeclarationWithinConditional() {
    String code = "function foo() {\n" +
            "  if (true) {\n" +
            "    var a;\n" +
            "    var b = 42;\n" +
            "  }\n" +
            "  var c;\n" +
            "  var d = 43;\n" +
            "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    List<AbstractCompiler.CompilationUnit> units = compiler.compile(Arrays.asList(code), options);
    Assert.assertEquals(1, units.size());

    Node root = units.get(0).getTree();
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    collapseVariableDeclarations.process(null, root);

    Node ifStatement = NodeOperations.findFirstDescendantOfType(root, Node.IF);
    Node varA = NodeOperations.findFirstChildOfType(ifStatement, Node.VAR);
    Node varB = NodeOperations.findNextSiblingOfType(varA, Node.VAR);
    Node varC = NodeOperations.findNextSiblingOfType(ifStatement, Node.VAR);
    Node varD = NodeOperations.findNextSiblingOfType(ifStatement, Node.VAR);

    Assert.assertNotNull(varA);
    Assert.assertNotNull(varB);
    Assert.assertNotNull(varC);
    Assert.assertNotNull(varD);

    Assert.assertEquals("if (true) { var a; var b = 42; } var c; var d = 43;", root.toString());
  }
}