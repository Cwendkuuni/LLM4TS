import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractPostOrder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traverse;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;
import java.util.Set;

/**
 * Tests for CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest {

  @org.junit.Test
  public void testCollapseVariableDeclarations() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CollapseVariableDeclarations(compiler));

    String sourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "  var e = 4;\n" +
        "  var f = 5;\n" +
        "  var g = 6;\n" +
        "  var h = 7;\n" +
        "  var i = 8;\n" +
        "  var j = 9;\n" +
        "  var k = 10;\n" +
        "  a = true;\n" +
        "  b = true;\n" +
        "}";

    Node root = AbstractNode.parse(sourceCode);
    passManager.runPasses(root);

    Traverse.traverse(root, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  var a, b = 1, c = 2, d = 3, e = 4, f = 5, g = 6, h = 7, i = 8, j = 9, k = 10;\n" +
              "  a = true;\n" +
              "  b = true;\n" +
              "}", traversal.getSource().getCode());
          return;
        }
        super.visit(traversal, node);
      }
    });
  }

  @org.junit.Test
  public void testCollapseAdjacentVariableDeclarations() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CollapseVariableDeclarations(compiler));

    String sourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b;\n" +
        "  a = true;\n" +
        "  b = true;\n" +
        "}";

    Node root = AbstractNode.parse(sourceCode);
    passManager.runPasses(root);

    Traverse.traverse(root, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  var a, b = true;\n" +
              "}", traversal.getSource().getCode());
          return;
        }
        super.visit(traversal, node);
      }
    });
  }

  @org.junit.Test
  public void testCollapseAdjacentVariableAssignments() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CollapseVariableDeclarations(compiler));

    String sourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  a = true;\n" +
        "  var b = true;\n" +
        "}";

    Node root = AbstractNode.parse(sourceCode);
    passManager.runPasses(root);

    Traverse.traverse(root, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  var a, b = true;\n" +
              "  a = true;\n" +
              "}", traversal.getSource().getCode());
          return;
        }
        super.visit(traversal, node);
      }
    });
  }

  @org.junit.Test
  public void testCollapseVariableWithJSDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CollapseVariableDeclarations(compiler));

    String sourceCode = "/**\n" +
        " * @const\n" +
        " */\n" +
        "function foo() {\n" +
        "  /** @type {number} */\n" +
        "  var a;\n" +
        "  /** @type {number} */\n" +
        "  var b = 1;\n" +
        "  /** @type {number} */\n" +
        "  var c = 2;\n" +
        "}";

    Node root = AbstractNode.parse(sourceCode);
    passManager.runPasses(root);

    Traverse.traverse(root, new NodeVisitor() {
      @Override
      public void visit(NodeTraversal traversal, Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  /** @type {number} */\n" +
              "  var a, /** @type {number} */ b = 1, /** @type {number} */ c = 2;\n" +
              "}", traversal.getSource().getCode());
          return;
        }
        super.visit(traversal, node);
      }
    });
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testCollapseVariableInsideIfStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CollapseVariableDeclarations(compiler));

    String sourceCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a;\n" +
        "  } else {\n" +
        "    var b;\n" +
        "  }\n" +
        "}";

    Node root = AbstractNode.parse(sourceCode);
    passManager.runPasses(root);

    // Should throw an AssertionError, as we can't collapse variable declarations
    // inside if statements
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testCollapseVariableWithDuplicateName() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new CollapseVariableDeclarations(compiler));

    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "}";

    Node root = AbstractNode.parse(sourceCode);
    passManager.runPasses(root);

    // Should throw an AssertionError, as we can't collapse variable declarations
    // with the same name
  }
}