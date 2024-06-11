import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallback;
import static com.google.javascript.rhino.Node.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CollapseVariableDeclarationsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testCollapseVariableDeclarations() {
    String code = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertCollapsed(root, 1, "var a, b = 1, c = 2, d = 3;");
  }

  @Test
  public void testCollapseVariableDeclarationsWithAssignments() {
    String code = "function f() {\n" +
        "  var a = true;\n" +
        "  var b = true;\n" +
        "  var c = true;\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertCollapsed(root, 1, "var a, b = true, c = true;");
  }

  @Test
  public void testCollapseVariableDeclarationsInFunction() {
    String code = "function f() {\n" +
        "  function g() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertCollapsed(root, 1, "function f() {\n" +
        "  function g() {\n" +
        "    var a, b = 1, c = 2;\n" +
        "  }\n" +
        "}");
  }

  @Test
  public void testCollapseVariableDeclarationsWithIfStatement() {
    String code = "function f() {\n" +
        "  var a;\n" +
        "  if (true) {\n" +
        "    var b = 1;\n" +
        "  } else {\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertNotCollapsed(root, "a", "function f() {\n" +
        "  var a;\n" +
        "  if (true) {\n" +
        "    var b = 1;\n" +
        "  } else {\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}");
  }

  @Test
  public void testCollapseVariableDeclarationsWithLoop() {
    String code = "function f() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var j = i + 1;\n" +
        "  }\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertNotCollapsed(root, "i", "function f() {\n" +
        "  for (var i = 0, j; i < 10; i++) {\n" +
        "    var j = i + 1;\n" +
        "  }\n" +
        "}");
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithDuplicateVariableName() {
    String code = "function f() {\n" +
        "  var a = 1;\n" +
        "  var a = 2;\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);
  }

  @Test
  public void testCollapseVariableDeclarationsWithMixedVariableDeclarationsAndAssignments() {
    String code = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  a = 3;\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    parser.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertCollapsed(root, 1, "function f() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "  a = 3;\n" +
        "}");
  }

  @Test
  public void testCollapseVariableDeclarationsWithMixedVariableDeclarationsAndAssignmentsWithJSDoc() {
    String code = "/**\n" +
        " * @function\n" +
        " */\n" +
        "function f() {\n" +
        "  /** @type {number} */ var a;\n" +
        "  /** @type {number} */ var b = 1;\n" +
        "  /** @type {number} */ var c = 2;\n" +
        "  a = 3;\n" +
        "}";

    Node root = new Node(Token.BLOCK);
    Parser parser = new Parser(options);
    JSDocInfo info = new JSDocInfo();
    info.parse(new StringReader(code), root, null);

    CollapseVariableDeclarations collapser = new CollapseVariableDeclarations(compiler);
    collapser.process(null, root);

    assertCollapsed(root, 1, "/**\n" +
        " * @function\n" +
        " */\n" +
        "function f() {\n" +
        "  /** @type {number} */ var a, /** @type {number} */ b = 1, /** @type {number} */ c = 2;\n" +
        "  a = 3;\n" +
        "}");
  }

  private void assertCollapsed(Node root, int numVariables, String expectedCode) {
    NodeTraversal traverser = new NodeTraversal(compiler, root) {
      int counter = 0;

      @Override
      public void visit(Node n) {
        if (n.isVar()) {
          counter++;
          assertEquals(expectedCode.substring(0, expectedCode.indexOf("var", counter)),
                      root.toString());
          assertTrue("Unexpected JSDoc info", n.getJSDocInfo() == null);
        }
        super.visit(n);
      }
    };

    traverser.traverse();
    assertEquals(numVariables, counter);
  }

  private void assertNotCollapsed(Node root, String variableName, String expectedCode) {
    NodeTraversal traverser = new NodeTraversal(compiler, root) {
      int counter = 0;

      @Override
      public void visit(Node n) {
        if (n.isVar() && n.getString().equals(variableName)) {
          fail("Variable '{}' was collapsed, expected it to remain unchanged.".format(variableName));
        }
        super.visit(n);
      }
    };

    traverser.traverse();

    String code = root.toString();
    assertEquals(expectedCode, code);
  }
}