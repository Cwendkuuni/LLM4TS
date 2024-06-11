import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeSnippetTestHelper.newCodeSnippet;
import static com.google.javascript.jscomp.testing.CodeSnippetTestHelper.typeCheck;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSimpleVariableDeclarations() {
    String input =
        "function foo() {" +
        "  var a;" +
        "  var b = 1;" +
        "  var c = 2;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    CollapseVariableDeclarations.Collapse[] collapses = {
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.VAR, "a", null),
          Node.createTree(Token.VAR, "b", Node.createTree(Token.ASSIGN, null, Node.createIntLit(1))),
          Node.createTree(Token.BLOCK, null)
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.VAR, "c", null),
          Node.createTree(Token.ASSIGN, null, Node.createIntLit(2)),
          Node.createTree(Token.BLOCK, Node.createTree(Token.VAR, "a", null))
      )
    };

    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("a"),
        NodeFactory.createList(
            NodeFactory.createAssign(NodeFactory.createVar("a"), NodeFactory.createIntLit(1)),
            NodeFactory.createAssign(NodeFactory.createVar("a"), NodeFactory.createIntLit(2))
        )
    );

    assertEquals(expected.toSource(3), result.getSource());
  }

  @Test
  public void testCollapseAssignments() {
    String input =
        "function foo() {" +
        "  a = true;" +
        "  b = true;" +
        "  var c = true;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    CollapseVariableDeclarations.Collapse[] collapses = {
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.ASSIGN, null, Node.createVar("a")),
          Node.createTree(Token.ASSIGN, null, Node.createVar("b")),
          Node.createTree(Token.BLOCK, Node.createTree(Token.ASSIGN, null, Node.createVar("a")))
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.VAR, "c", null),
          Node.createTree(Token.ASSIGN, null, Node.createVar("b")),
          Node.createTree(Token.BLOCK, Node.createTree(Token.ASSIGN, null, Node.createVar("a")))
      )
    };

    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createAssign(NodeFactory.createVar("a"), NodeFactory.createVar("b")),
        NodeFactory.createAssign(NodeFactory.createVar("c"), NodeFactory.createVar("b"))
    );

    assertEquals(expected.toSource(3), result.getSource());
  }

  @Test
  public void testCollapseWithFunctionVariables() {
    String input =
        "function foo() {" +
        "  function bar() {}" +
        "  var a;" +
        "  var b = 1;" +
        "  var c = 2;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    CollapseVariableDeclarations.Collapse[] collapses = {
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null),
          Node.createTree(Token.VAR, "a", Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null))),
          Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null))
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.VAR, "b", null),
          Node.createTree(Token.ASSIGN, null, Node.createIntLit(1)),
          Node.createTree(Token.BLOCK, Node.createTree(Token.VAR, "a"))
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.VAR, "c", null),
          Node.createTree(Token.ASSIGN, null, Node.createIntLit(2)),
          Node.createTree(Token.BLOCK, Node.createTree(Token.VAR, "a"))
      )
    };

    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createVar("a"),
        NodeFactory.createList(
            NodeFactory.createFunction(
                NodeFactory.createName("bar"),
                NodeFactory.createList(),
                NodeFactory.createBlock()
            ),
            NodeFactory.createList(
                NodeFactory.createAssign(NodeFactory.createVar("a"), NodeFactory.createIntLit(1)),
                NodeFactory.createAssign(NodeFactory.createVar("a"), NodeFactory.createIntLit(2))
            )
        )
    );

    assertEquals(expected.toSource(3), result.getSource());
  }

  @Test
  public void testCollapseWithNestedScopes() {
    String input =
        "function foo() {" +
        "  function bar() {" +
        "    var a;" +
        "    var b = 1;" +
        "    var c = 2;" +
        "  }" +
        "  function baz() {" +
        "    var a;" +
        "    var d = 3;" +
        "  }" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    CollapseVariableDeclarations.Collapse[] collapses = {
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null),
          Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null)),
          Node.createTree(Token.BLOCK, null)
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null),
          Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null)),
          Node.createTree(Node.createTree(Token.VAR, "a", null), Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null)))
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null),
          Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null)),
          Node.createTree(Node.createTree(Token.VAR, "b", Node.createIntLit(1)), Node.createTree(Token.BLOCK, Node.createTree(Token.VAR, "a")))
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null),
          Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null)),
          Node.createTree(Node.createTree(Token.VAR, "c", null), Node.createTree(Token.BLOCK, Node.createTree(Token.VAR, "a")))
      ),
      new CollapseVariableDeclarations.Collapse(
          Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null),
          Node.createTree(Token.BLOCK, Node.createTree(Token.FUNCTION, null, null, null, null, null, null, Node.createList(), null)),
          Node.createTree(Node.createTree(Token.VAR, "d", Node.createIntLit(3)), Node.createTree(Token.BLOCK, Node.createTree(Token.VAR, "a")))
      )
    };

    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    Node expected = NodeFactory.createBlock(
        NodeFactory.createFunction("bar", NodeFactory.createList(), NodeFactory.createBlock()),
        NodeFactory.createFunction("baz", NodeFactory.createList(), NodeFactory.createBlock())
    );

    Node functionBlock = NodeFactory.createBlock();
    Node barFunction = NodeFactory.createFunction("bar", NodeFactory.createList(), NodeFactory.createBlock());

    Node varA = NodeFactory.createVar("a");
    Node assignA = NodeFactory.createAssign(varA, NodeFactory.createIntLit(1));
    Node assignA2 = NodeFactory.createAssign(varA, NodeFactory.createIntLit(2));
    Node blockA = NodeFactory.createBlock();
    blockA.addChildToBack(assignA);
    blockA.addChildToBack(assignA2);

    Node functionScope = NodeFactory.createBlock();
    functionScope.addChildToBack(barFunction);
    functionScope.addChildToBack(blockA);

    Node functionScope2 = NodeFactory.createBlock();
    functionScope2.addChildToBack(functionBlock);
    functionScope2.addChildToBack(functionScope);

    expected.addChildToBack(functionBlock);
    expected.addChildToBack(functionScope2);

    Node fooFunction = NodeFactory.createFunction("foo", NodeFactory.createList(), NodeFactory.createBlock());

    Node fooScope = NodeFactory.createBlock();
    fooScope.addChildToBack(fooFunction);

    typeCheck(newCodeSnippet(expected.toSource(3)), compiler, options);

    assertEquals(expected.toSource(3), result.getSource());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseWithNestedFunctionsAndReusedVariable() {
    String input =
        "function foo() {" +
        "  function bar() {" +
        "    var a = 1;" +
        "    function baz() {" +
        "      var a = 2;" +
        "    }" +
        "  }" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    fail("Expected an AssertionError but did not get one.");
  }

  @Test
  public void testCollapseWithEmptyBlock() {
    String input = "function foo() {}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    assertEquals("function foo() {}", result.getSource());
  }

  @Test
  public void testCollapseWithEmptyBlockAndVariable() {
    String input = "function foo() { var a; }";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    assertEquals("function foo() { var a; }", result.getSource());
  }

  @Test
  public void testCollapseWithNestedBlocks() {
    String input = "function foo() {" +
      "  if (true) {" +
      "    var a = 1;" +
      "  } else {" +
      "    var a = 2;" +
      "  }" +
      "  if (true) {" +
      "    var b = 3;" +
      "  } else {" +
      "    var b = 4;" +
      "  }" +
      "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    compiler.addPasses(collapseVariableDeclarations);
    CompilerResult result = compiler.compile(input, options);

    Node expected = NodeFactory.createBlock(
      NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(true),
        NodeFactory.createBlock(
          NodeFactory.createVar("a"),
          NodeFactory.createIntLit(1)
        ),
        NodeFactory.createBlock(
          NodeFactory.createVar("a"),
          NodeFactory.createIntLit(2)
        )
      ),
      NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(true),
        NodeFactory.createBlock(
          NodeFactory.createVar("b"),
          NodeFactory.createIntLit(3)
        ),
        NodeFactory.createBlock(
          NodeFactory.createVar("b"),
          NodeFactory.createIntLit(4)
        )
      )
    );

    assertEquals(expected.toSource(3), result.getSource());
  }
}