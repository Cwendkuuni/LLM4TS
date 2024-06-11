import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test class for CollapseVariableDeclarations.
 */
public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "  var e;\n" +
        "  var f = 4;\n" +
        "  var g = 5;\n" +
        "  var h;\n" +
        "  var i = 6;\n" +
        "  var j = 7;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    collapseVariableDeclarations.process(null, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "a")
    );
    variables.setLastChild(
        new Node(Token.VAR, "h")
    );
    expectedRoot.getLastChild().setFirstChild(variables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testCollapseVariableAssignments() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  var a = true;\n" +
        "  var b = true;\n" +
        "  var c = true;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    collapseVariableDeclarations.process(null, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "c", new Node(Token.ASSIGN, null, new Node(Token.TRUE)))
    );
    variables.setLastChild(
        new Node(Token.VAR, "a", new Node(Token.ASSIGN, null, new Node(Token.TRUE)))
    );
    expectedRoot.getLastChild().setFirstChild(variables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithinFunction() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var a = 1;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    collapseVariableDeclarations.process(null, root);

    fail("Expected AssertionError but got: " + compiler.getErrors().toString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsWithinIfStatement() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    collapseVariableDeclarations.process(null, root);

    fail("Expected AssertionError but got: " + compiler.getErrors().toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithComments() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  // This is a comment\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    compile(collapseVariableDeclarations, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "a")
    );
    variables.setLastChild(
        new Node(Token.VAR, "c")
    );
    expectedRoot.getLastChild().setFirstChild(variables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithJSDocs() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  /** @type {number} */\n" +
        "  var a;\n" +
        "  /** @type {boolean} */\n" +
        "  var b = true;\n" +
        "  var c = 2;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    compile(collapseVariableDeclarations, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "a")
            .setJSDocInfo(new JSDocInfo().setType("number"))
    );
    variables.setLastChild(
        new Node(Token.VAR, "c")
    );
    expectedRoot.getLastChild().setFirstChild(variables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithAssignments() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = a;\n" +
        "  var c = 2;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    compile(collapseVariableDeclarations, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "a")
            .setInitializer(new Node(Token.ASSIGN, null, new Node(Token.NUM, 1)))
    );
    variables.setLastChild(
        new Node(Token.VAR, "c")
    );
    expectedRoot.getLastChild().setFirstChild(variables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithFunctionDeclaration() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    compile(collapseVariableDeclarations, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node barFunction = new Node(Token.FUNCTION_DECLARATION);
    barFunction.setName("bar");
    barFunction.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "a")
    );
    variables.setLastChild(
        new Node(Token.VAR, "a") // This should not be collapsed with the previous one
    );
    barFunction.getLastChild().setFirstChild(variables);

    Node fooVariables = new Node(Token.VAR_DECLARATIONS);
    fooVariables.setFirstChild(variables.cloneTree());
    fooVariables.setLastChild(
        new Node(Token.VAR, "c")
    );
    expectedRoot.getLastChild().setFirstChild(fooVariables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithFunctionExpression() {
    AbstractCompiler compiler = new TestCompiler();
    CompilerPass collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

    String sourceCode = "function foo() {\n" +
        "  var bar = function() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  };\n" +
        "  var c = 2;\n" +
        "}";

    Node root = ParserUtil.parseCompilationUnit(sourceCode);
    compile(collapseVariableDeclarations, root);

    Node expectedRoot = new Node(Token.FUNCTION_DECLARATION);
    expectedRoot.setName("foo");
    expectedRoot.setBody(new Node(Token.BLOCK));

    Node barFunction = new Node(Token.FUNCTION_EXPRESSION);
    barFunction.setName("bar");
    barFunction.setBody(new Node(Token.BLOCK));

    Node variables = new Node(Token.VAR_DECLARATIONS);
    variables.setFirstChild(
        new Node(Token.VAR, "a")
    );
    variables.setLastChild(
        new Node(Token.VAR, "a") // This should not be collapsed with the previous one
    );
    barFunction.getBody().setFirstChild(variables);

    Node fooVariables = new Node(Token.VAR_DECLARATIONS);
    fooVariables.setFirstChild(variables.cloneTree());
    fooVariables.setLastChild(
        new Node(Token.VAR, "c")
    );
    expectedRoot.getLastChild().setFirstChild(fooVariables);

    Node actualRoot = compiler.getRootNode();
    assertEquals(expectedRoot.toString(), actualRoot.toString());
  }

  private void compile(CompilerPass collapseVariableDeclarations, Node root) {
    traverse(new TestCompiler(), root, new CollapseVariableDeclarations(new TestCompiler()), null);
    collapseVariableDeclarations.process(null, root);
  }
}