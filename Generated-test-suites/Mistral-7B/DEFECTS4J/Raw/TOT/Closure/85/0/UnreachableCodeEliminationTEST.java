package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.test.TestUtils.createCompiler;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveReturnStatementWithFollowingCode() {
    String sourceCode = "function foo() {" +
                          "if (true) {" +
                          "  return;" +
                          "  alert('This should be removed');" +
                          "}" +
                          "}";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = NodeFactory.parse(sourceCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, true /* removeNoOpStatements */);
    NodeTraversal traversal = new NodeTraversal(ast, eliminator);
    traversal.traverse();

    String resultCode = NodePrinter.printNode(traversal.getRootNode());
    assertThat(resultCode).doesNotContain("alert");;
  }

  @Test
  public void testRemoveDeadCodeBeforeConditional() {
    String sourceCode = "function foo() {" +
                          "if (x > 0) {" +
                          "  var y = a.b.MyClass.prototype.propertyName;" + // Dead code
                          "}" +
                          "}" +
                          "x = 1;";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = NodeFactory.parse(sourceCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, true /* removeNoOpStatements */);
    NodeTraversal traversal = new NodeTraversal(ast, eliminator);
    traversal.traverse();

    String resultCode = NodePrinter.printNode(traversal.getRootNode());
    assertThat(resultCode).doesNotContain("MyClass.prototype.propertyName");;
  }

  @Test
  public void testRemoveDeadCodeAfterConditional() {
    String sourceCode = "function foo() {" +
                          "if (x > 0) {" +
                          "  return;" +
                          "}" +
                          "alert('This should not be removed');" +
                          "}" +
                          "x = 1;";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = NodeFactory.parse(sourceCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, true /* removeNoOpStatements */);
    NodeTraversal traversal = new NodeTraversal(ast, eliminator);
    traversal.traverse();

    String resultCode = NodePrinter.printNode(traversal.getRootNode());
    assertThat(resultCode).contains("alert");;
  }

  @Test(expected = JSError.class)
  public void testErrorScenarioEmptyStatement() {
    String sourceCode = "function foo() {}";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = NodeFactory.parse(sourceCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, true /* removeNoOpStatements */);
    NodeTraversal traversal = new NodeTraversal(ast, eliminator);
    traversal.traverse();

    Node rootNode = traversal.getRootNode();
    eliminator.removeDeadExprStatementSafely(rootNode);
  }

  @Test(expected = JSError.class)
  public void testErrorScenarioFunctionCall() {
    String sourceCode = "function foo() {" +
                          "function bar() {" +
                          "} " + // Dead function
                          "}" +
                          "foo();";

    Compiler compiler = createCompiler();
    CompilerOptions options = new CompilerOptions();
    Node ast = NodeFactory.parse(sourceCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, true /* removeNoOpStatements */);
    NodeTraversal traversal = new NodeTraversal(ast, eliminator);
    traversal.traverse();

    Node rootNode = traversal.getRootNode();
    eliminator.removeDeadExprStatementSafely(rootNode);
  }
}