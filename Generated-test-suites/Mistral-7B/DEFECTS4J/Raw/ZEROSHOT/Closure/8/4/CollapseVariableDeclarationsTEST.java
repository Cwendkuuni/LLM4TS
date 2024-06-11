import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractPostOrder;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private static abstract class TestCompiler extends AbstractCompiler {
    @Override
    public CompilerPass getPass(String passName) {
      if (CollapseVariableDeclarations.class.getName().equals(passName)) {
        return new CollapseVariableDeclarations(this);
      }
      return super.getPass(passName);
    }
  }

  @org.junit.Test
  public void testCollapsingVariables() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "  var e = 3;\n" +
        "  var a = 5;\n" +
        "  var f = g = 6;\n" +
        "  var g = 7;\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  var a, b = 1, c = 2, d, e = 3, a = 5, f = g = 6, g = 7;\n",
              node.toSource(PrintFlags.syntaxSource)).isIdenticalTo(
                  node.toSource());
        }
      }
    });
  }

  @org.junit.Test
  public void testNoCollapsingVariablesAnnotatedWithJSDoc() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  /**\n" +
        "   * @type {number}\n" +
        "   */\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  /**\n" +
              "   * @type {number}\n" +
              "   */\n" +
              "  var a;\n" +
              "  var b = 1;\n" +
              "  var c = 2;\n" +
              "}",
              node.toSource(PrintFlags.syntaxSource)).isIdenticalTo(
                  node.toSource());
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithSameNameButDifferentTypes() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var a = \"foo\";\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  var a = 1,\n" +
              "      a = \"foo\";\n" +
              "}",
              node.toSource(PrintFlags.syntaxSource)).isIdenticalTo(
                  node.toSource());
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithAssignments() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  var a = 1;\n" +
        "  a = 5;\n" +
        "  var b = a;\n" +
        "  b = 6;\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          assertEquals("function foo() {\n" +
              "  var a = 1,\n" +
              "      a = 5,\n" +
              "      b = a,\n" +
              "      b = 6;\n" +
              "}",
              node.toSource(PrintFlags.syntaxSource)).isIdenticalTo(
                  node.toSource());
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithSameNameButDifferentScopes() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a;\n" +
        "    var a = 1;\n" +
        "  }\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          String expectedSource = "function foo() {\n" +
              "  function bar() {\n" +
              "    var a;\n" +
              "    var a = a = 1;\n" +
              "  }\n" +
              "}";
          assertEquals(expectedSource, node.toSource(PrintFlags.syntaxSource));
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithinIfStatements() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a = 1;\n" +
        "    var b = 2;\n" +
        "  } else {\n" +
        "    var a = 3;\n" +
        "    var b = 4;\n" +
        "  }\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          String expectedSource = "function foo() {\n" +
              "  if (true) {\n" +
              "    var a = 1, b = 2;\n" +
              "  } else {\n" +
              "    var a = 3, b = 4;\n" +
              "  }\n" +
              "}";
          assertEquals(expectedSource, node.toSource(PrintFlags.syntaxSource));
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithinLoops() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  for (var i = 0, j = 0; i < 10; i++) {\n" +
        "    var a = i;\n" +
        "    var b = i;\n" +
        "  }\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          String expectedSource = "function foo() {\n" +
              "  for (var i = 0, j = 0; i < 10; i++) {\n" +
              "    var a = i, b = i;\n" +
              "  }\n" +
              "}";
          assertEquals(expectedSource, node.toSource(PrintFlags.syntaxSource));
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithinConditionalStatements() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  var x = 5;\n" +
        "  if (x < 10) {\n" +
        "    var a = 1;\n" +
        "    var b = 2;\n" +
        "  } else {\n" +
        "    var a = 3;\n" +
        "    var b = 4;\n" +
        "  }\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          String expectedSource = "function foo() {\n" +
              "  var x = 5;\n" +
              "  var a, b;\n" +
              "  if (x < 10) {\n" +
              "    a = 1;\n" +
              "    b = 2;\n" +
              "  } else {\n" +
              "    a = 3;\n" +
              "    b = 4;\n" +
              "  }\n" +
              "}";
          assertEquals(expectedSource, node.toSource(PrintFlags.syntaxSource));
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithinFunctionCalls() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  var a = 1;\n" +
        "  bar(function(x) {\n" +
        "    var a = 2;\n" +
        "  });\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          String expectedSource = "function foo() {\n" +
              "  var a = 1;\n" +
              "  bar(function(x) {\n" +
              "    var a;\n" +
              "    a = 2;\n" +
              "  });\n" +
              "}";
          assertEquals(expectedSource, node.toSource(PrintFlags.syntaxSource));
        }
      }
    });
  }

  @org.junit.Test
  public void testCollapsingVariablesWithinSwitchStatements() {
    AbstractCompiler compiler = new TestCompiler();
    String source = "function foo() {\n" +
        "  switch (1) {\n" +
        "    case 1:\n" +
        "      var a = 1;\n" +
        "      break;\n" +
        "    case 2:\n" +
        "      var a = 2;\n" +
        "      break;\n" +
        "  }\n" +
        "}";

    Node root = parseTree(compiler, source);

    traverse(compiler, root, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        if (node.isFunction()) {
          String expectedSource = "function foo() {\n" +
              "  switch (1) {\n" +
              "    case 1:\n" +
              "      var a;\n" +
              "      a = 1;\n" +
              "      break;\n" +
              "    case 2:\n" +
              "      var a;\n" +
              "      a = 2;\n" +
              "      break;\n" +
              "  }\n" +
              "}";
          assertEquals(expectedSource, node.toSource(PrintFlags.syntaxSource));
        }
      }
    });
  }

  private Node parseTree(AbstractCompiler compiler, String source) {
    compiler.process(null, createRootNode(source));
    return compiler.getRootNode();
  }

  private Node createRootNode(String source) {
    Node root = new Node(Token.PROGRAM);
    root.setSource(source);
    return root;
  }

  private static interface NodeVisitor {
    void visit(Node node);
  }
}