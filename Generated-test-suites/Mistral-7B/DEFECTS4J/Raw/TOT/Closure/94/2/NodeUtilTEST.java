import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().newNode("for (var i in array) {}");
    assertTrue(isForIn(node));

    node = Context.getCurrentContext().newNode("for (var i = 0; i < 10; i++) {}", 0);
    assertFalse(isForIn(node));

    node = Context.getCurrentContext().newNode("for (; ;) {}", 0);
    assertFalse(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = Context.getCurrentContext().newNode("{}");
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().newNode("function foo() {}");
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().newNode("1 + 1");
    assertFalse(isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = Context.getCurrentContext().newNode("function foo() {}", 0);
    assertTrue(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("function foo() {}", 1);
    assertFalse(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("function foo() {}", 10);
    assertFalse(isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().newNode("function foo() {}", 0);
    Node functionNode = Context.getCurrentContext().newNode("function foo() {}", 1);

    Node functionDeclaration = new Node(Token.FUNCTION, "foo", new Node[]{});
    Node script = new Node(Token.SCRIPT, null, new Node[]{functionDeclaration, scriptNode});

    Node functionDeclarationInScript = Context.getCurrentContext().newNodeFromString(script.toSource());
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInScript));

    Node functionDeclarationOutsideScript = new Node(Token.FUNCTION, "foo", new Node[]{});
    Node parent = Context.getCurrentContext().newNode("function bar() {}", 0);
    Node node = new Node(Token.ASSIGN, null, new Node[]{functionDeclarationOutsideScript, new Node(Token.IDENT, "foo")});
    Node grandParent = new Node(Token.ASSIGN, null, new Node[]{parent, node});

    Node functionDeclarationOutsideScriptInParent = Context.getCurrentContext().newNodeFromString(grandParent.toSource());
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationOutsideScriptInParent));
  }
}