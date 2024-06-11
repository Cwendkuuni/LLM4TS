import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeSnippetTestHelper.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapsingSimpleVariableDeclarations() {
    String jsCode = "function foo() {"
        + "var a;"
        + "var b = 1;"
        + "var c = 2;"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("a"),
        NodeFactory.createBlock(
            NodeFactory.createVar("b"),
            NodeFactory.createAssign(NodeFactory.createName("b"), NodeFactory.createNum(1)),
            NodeFactory.createAssign(NodeFactory.createName("c"), NodeFactory.createNum(2))
        )
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testCollapsingAssignments() {
    String jsCode = "function foo() {"
        + "var a = true;"
        + "var b = true;"
        + "var c = true;"
        + "a = false;"
        + "b = false;"
        + "c = false;"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("a, b, c"),
        NodeFactory.createAssign(NodeFactory.createName("a, b, c"), NodeFactory.createBool(false))
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testCollapsingWithFunction() {
    String jsCode = "function foo() {"
        + "function bar() {"
        + "var a;"
        + "var b = 1;"
        + "var c = 2;"
        + "}"
        + "}"
        + "foo.bar();";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createFunction(
        NodeFactory.createName("foo"),
        NodeFactory.createBlock(
            NodeFactory.createFunction(
                NodeFactory.createName("bar"),
                NodeFactory.createBlock(
                    NodeFactory.createVar("a"),
                    NodeFactory.createBlock(
                        NodeFactory.createVar("b"),
                        NodeFactory.createAssign(NodeFactory.createName("b"), NodeFactory.createNum(1)),
                        NodeFactory.createAssign(NodeFactory.createName("c"), NodeFactory.createNum(2))
                    )
                )
            ),
            null
        )
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testCollapsingVariablesWithSameName() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "var a = 2;"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("a"),
        NodeFactory.createAssign(NodeFactory.createName("a"), NodeFactory.createNum(2))
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testCollapsingVariablesWithDifferentScopes() {
    String jsCode = "function foo() {"
        + "function bar() {"
        + "var a;"
        + "}"
        + "var a = 1;"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createFunction(
            NodeFactory.createName("bar"),
            NodeFactory.createBlock(NodeFactory.createVar("a"))
        ),
        NodeFactory.createVar("a")
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testCollapsingVariablesWithSameNameDifferentScopes() {
    String jsCode = "function foo() {"
        + "function bar() {"
        + "var a;"
        + "}"
        + "function baz() {"
        + "var a;"
        + "}"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createFunction(NodeFactory.createName("bar"), NodeFactory.createBlock(NodeFactory.createVar("a"))),
        NodeFactory.createFunction(NodeFactory.createName("baz"), NodeFactory.createBlock(NodeFactory.createVar("a")))
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testEmptyCollapses() {
    String jsCode = "function foo() {}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock();

    compareTrees(expected, rootNode);
  }

  @Test
  public void testNoCollapses() {
    String jsCode = "function foo() {"
        + "var a = 1;"
        + "}"
        + "function bar() {}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createFunction(NodeFactory.createName("foo"), NodeFactory.createBlock(NodeFactory.createVar("a"))),
        NodeFactory.createFunction(NodeFactory.createName("bar"))
    );

    compareTrees(expected, rootNode);
  }

  @Test
  public void testCollapsesWithComments() {
    String jsCode = "function foo() {"
        + "// comment before first var"
        + "var a;"
        + "// comment after first var"
        + "var b = 1;"
        + "// comment before second var"
        + "var c = 2;"
        + "// comment after second var"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("a"),
        NodeFactory.createBlock(
            NodeFactory.createVar("b"),
            NodeFactory.createAssign(NodeFactory.createName("b"), NodeFactory.createNum(1)),
            NodeFactory.createAssign(NodeFactory.createName("c"), NodeFactory.createNum(2))
        )
    );

    compareTreesWithComments(expected, rootNode);
  }

  @Test
  public void testCollapsesWithJSDoc() {
    String jsCode = "/** @constructor */\n"
        + "function foo() {"
        + "/** @type {!Array.<number>} */\n"
        + "var arr;"
        + "/** @type {!Array.<number>} */\n"
        + "var arr2 = [1, 2, 3];"
        + "}"
        + "foo.prototype.push = function(num) {"
        + "this.push(num);"
        + "}";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPasses(new CollapseVariableDeclarations(compiler));

    Node rootNode = parsejs(jsCode);
    compiler.compile(rootNode, null, null);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("arr, arr2"),
        NodeFactory.createBlock(
            NodeFactory.createAssign(NodeFactory.createName("arr"), NodeFactory.createArrayLiteral(NodeFactory.createNum(1), NodeFactory.createNum(2), NodeFactory.createNum(3))),
            NodeFactory.createFunction(
                NodeFactory.createName("push"),
                NodeFactory.createBlock(
                    NodeFactory.createAssign(NodeFactory.createName("this"), NodeFactory.createThis()),
                    NodeFactory.createAssign(
                        NodeFactory.createName("this.length"),
                        NodeFactory.createBinop(NodeFactory.createName("this.length"), NodeFactory. operator("+"), NodeFactory.createNum(1))
                    ),
                    NodeFactory.createAssign(
                        NodeFactory.createName("this[" + NodeFactory.createName("this.length") + "]"),
                        NodeFactory.createArg(NodeFactory.createName("num"))
                    )
                )
            )
        )
    );

    compareTreesWithJSDoc(expected, rootNode);
  }

  private void compareTrees(Node expected, Node actual) {
    assertTreeEquals(expected, actual);
  }

  private void compareTreesWithComments(Node expected, Node actual) {
    assertTreeEqualsWithComments(expected, actual);
  }

  private void compareTreesWithJSDoc(Node expected, Node actual) {
    assertTreeEqualsWithJSDoc(expected, actual);
  }
}