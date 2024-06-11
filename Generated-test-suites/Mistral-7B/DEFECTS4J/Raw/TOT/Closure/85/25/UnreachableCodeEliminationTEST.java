import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.graph.*;
import com.google.javascript.jscomp.parser.*;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UnreachableCodeEliminationTest {

  private Compiler compiler;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
  }

  @Test
  public void testSimpleReturn() {
    String jsCode = "function f() {" +
        "if (true) {" +
        "  return; " +
        "} " +
        "alert('unreachable'); " +
        "}";

    Node root =Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    assertEquals(0, root.getChildCount());
    assertEquals(TOKEN_NULL, root.getType());
  }

  @Test
  public void testEmptyReturn() {
    String jsCode = "function f() {" +
        "if (true) {" +
        "  return;" +
        "} " +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    assertEquals(0, root.getChildCount());
    assertEquals(TOKEN_NULL, root.getType());
  }

  @Test
  public void testNoOpStatements() {
    String jsCode = "function f() {" +
        "a.b.MyClass.prototype.propertyName;" +
        "true;" +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, true);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testSimpleIf() {
    String jsCode = "function f() {" +
        "if (true) {" +
        "  alert('true'); " +
        "} else {" +
        "  alert('false'); " +
        "} " +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeIfStatement(true, null, null),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeIdentifier("true"),
        new NodeRightBrace(),
        new NodeLeftBrace(),
        new NodeElseKeyword(),
        new NodeLeftBrace(),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeIdentifier("false"),
        new NodeRightBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testComplexIf() {
    String jsCode = "function f() {" +
        "if (true) {" +
        "  if (false) {" +
        "    alert('false'); " +
        "  } else {" +
        "    alert('true'); " +
        "  } " +
        "} else {" +
        "  alert('false'); " +
        "} " +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeIfStatement(true, null, null),
        new NodeIfStatement(false, null, null),
        new NodeToken(Token.KEYWORD, "else"),
        new NodeLeftBrace(),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeIdentifier("false"),
        new NodeRightBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testNestedIf() {
    String jsCode = "function f() {" +
        "if (true) {" +
        "  if (false) {" +
        "    if (true) {" +
        "      alert('true'); " +
        "    } " +
        "  } " +
        "} else {" +
        "  alert('false'); " +
        "} " +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeIfStatement(true, null, null),
        new NodeIfStatement(false, null, null),
        new NodeLeftBrace(),
        new NodeIfStatement(true, null, null),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeIdentifier("true"),
        new NodeRightBrace(),
        new NodeRightBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testSimpleSwitch() {
    String jsCode = "function f() {" +
        "switch (1) {" +
        "  case 1:" +
        "    alert('one'); " +
        "    break;" +
        "  default:" +
        "    break;" +
        "  case 2:" +
        "    alert('two'); " +
        "    break;" +
        "}" +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeSwitchStatement(1),
        new NodeSwitchCase(1),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeStringLiteral("one"),
        new NodeToken(Token.KEYWORD, "break"),
        new NodeDefaultClause(),
        new NodeToken(Token.KEYWORD, "break"),
        new NodeSwitchCase(2),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeStringLiteral("two"),
        new NodeToken(Token.KEYWORD, "break"),
        new NodeRightBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testComplexSwitch() {
    String jsCode = "function f() {" +
        "switch (1) {" +
        "  case 1:" +
        "    if (true) {" +
        "      alert('one'); " +
        "    } " +
        "    break;" +
        "  case 2:" +
        "    if (false) {" +
        "      alert('two'); " +
        "    } " +
        "    break;" +
        "  default:" +
        "    break;" +
        "}" +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeSwitchStatement(1),
        new NodeSwitchCase(1),
        new NodeIfStatement(true, null, null),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeStringLiteral("one"),
        new NodeToken(Token.KEYWORD, "break"),
        new NodeSwitchCase(2),
        new NodeIfStatement(false, null, null),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeStringLiteral("two"),
        new NodeToken(Token.KEYWORD, "break"),
        new NodeRightBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testUnconditionalBranching() {
    String jsCode = "function f() {" +
        "for (var i = 0; i < 1; i++) {" +
        "  break;" +
        "}" +
        "alert('unreachable'); " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeForStatement(true, null, null, null, null),
        new NodeToken(Token.KEYWORD, "break"),
        new NodeRightBrace(),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testUnreachableFunctionReturn() {
    String jsCode = "function f() {" +
        "function g() {" +
        "  return; " +
        "}" +
        "alert('unreachable'); " +
        "g(); " +
        "return; " +
        "}";

    Node root = Parser.parseCompilationUnit(jsCode);

    UnreachableCodeElimination eliminator =
        new UnreachableCodeElimination(compiler, false);

    eliminator.process(null, root);

    List<Node> expectedChildren = Arrays.asList(
        new NodeToken(Token.KEYWORD, "function"),
        new NodeKeyword(Token.FUNCTION),
        new NodeIdentifier("f"),
        new NodeLeftParen(),
        new NodeRightParen(),
        new NodeLeftBrace(),
        new NodeFunctionDeclaration("g"),
        new NodeLeftBrace(),
        new NodeToken(Token.KEYWORD, "return"),
        new NodeRightBrace(),
        new NodeRightBrace(),
        new NodeToken(Token.KEYWORD, "alert"),
        new NodeStringLiteral("unreachable"),
        new NodeToken(Token.KEYWORD, "return"),
        new NodeRightBrace()
    );

    List<Node> actualChildren = NodeUtil.getChildrenList(root);

    assertEquals(expectedChildren, actualChildren);
  }

  @Test
  public void testErrorOnEmptyBlock() {
    String jsCode = "function f() {";

    Node root = Parser.parseCompilationUnit(jsCode);

    try {
      new UnreachableCodeElimination(compiler, false).process(null, root);
    } catch (IllegalArgumentException e) {
      //Expected
      logger.log(Level.INFO, "Test for error on empty block passed.");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorOnNullNode() {
    Node root = null;

    new UnreachableCodeElimination(compiler, false).process(null, root);
  }
}