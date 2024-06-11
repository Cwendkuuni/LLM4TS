import static com.google.javascript.jscomp.CollapseVariableDeclarations.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserTestingUtil;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseSingleVariableDeclarations() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  var a;"
        + "  var b = 1;"
        + "  var c = 2;"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a, b = 1, c = 2");
    Node actual = root;
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCollapseSingleAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  var a = true;"
        + "  var b = true;"
        + "  var c = true;"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "c = b = a = true");
    Node actual = root;
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsInsideFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  function bar() {"
        + "    var a;"
        + "    var b = 1;"
        + "    var c = 2;"
        + "  }"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a, b = 1, c = 2");
    Node actual = root.getFirstChild().getFirstChild();
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsInsideIfStatement() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  if (true) {"
        + "    var a;"
        + "    var b = 1;"
        + "    var c = 2;"
        + "  } else {"
        + "    var d;"
        + "    var e = 1;"
        + "    var f = 2;"
        + "  }"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expectedA = new Node(Token.VAR, "a, b = 1, c = 2");
    Node actualA = root.getChild(0);
    assertEquals(expectedA.toString(), actualA.toString());

    Node expectedB = new Node(Token.VAR, "d, e = 1, f = 2");
    Node actualB = root.getChild(1);
    assertEquals(expectedB.toString(), actualB.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithDuplicates() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  var a = 1;"
        + "  var a = 2;"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a = 2");
    Node actual = root;
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithRedeclarationAsLastNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  var a = 1;"
        + "  var a;"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a");
    Node actual = root;
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testNoCollapseIfVariablesAreDifferent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  var a;"
        + "  var b;"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a, b");
    Node actual = root;
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testNoCollapseIfVariablesAreInDifferentScopes() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  function bar() {"
        + "    var a;"
        + "  }"
        + "  var b;"
        + "}";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expectedA = new Node(Token.VAR, "a");
    Node actualA = root.getFirstChild();
    assertEquals(expectedA.toString(), actualA.toString());

    Node expectedB = new Node(Token.VAR, "b");
    Node actualB = root;
    assertEquals(expectedB.toString(), actualB.toString());
  }

  @Test
  public void testCollapseVariableDeclarationsWithFunctionExpression() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);

    String input = "function foo() {"
        + "  var a;"
        + "  function bar(x) {"
        + "    var b = 1;"
        + "    var c = 2;"
        + "  }"
        + "}"
        + "bar(1);";

    Node root = ParserTestingUtil.parseAndCompile(input, options, null);
    collarVariableDeclarations.process(null, root);

    Node expected = new Node(Token.VAR, "a");
    Node actual = root;
    assertEquals(expected.toString(), actual.toString());

    Node expectedBar = new Node(Token.FUNCTION, "bar");
    Node actualBar = root.getLastChild();
    assertEquals(expectedBar.toString(), actualBar.toString());

    Node expectedB = new Node(Token.VAR, "b, c = 1, c = 2");
    Node actualB = actualBar.getFirstChild();
    assertEquals(expectedB.toString(), actualB.toString());
  }
}