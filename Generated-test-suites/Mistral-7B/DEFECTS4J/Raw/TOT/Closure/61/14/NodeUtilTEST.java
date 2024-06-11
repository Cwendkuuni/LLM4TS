import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().newNode("for (var i in arr) {}", Token.FOR);
    assertTrue(isForIn(node));

    node = Context.getCurrentContext().newNode("for (var i = 0; i < arr.length; i++) {}", Token.FOR);
    assertFalse(isForIn(node));

    node = Context.getCurrentContext().newNode("if (true) for (var i in arr) {}", Token.IF);
    assertFalse(isForIn(node));

    node = Context.getCurrentContext().newNode("for (var i = 0; i < arr.length; i++) {}", Token.FOR);
    assertFalse(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = Context.getCurrentContext().newNode("{}");
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().newNode("1 + 1");
    assertFalse(isStatement(node));

    node = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION);
    assertTrue(isStatement(node));

    node = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION, Context.getCurrentContext().newNode("{}"));
    assertFalse(isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION);
    assertTrue(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION, Context.getCurrentContext().newNode("{}"));
    assertFalse(isFunctionDeclaration(node));

    node = Context.getCurrentContext().newNode("1 + 1", Token.EXPR);
    assertFalse(isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION);
    Node scriptParent = scriptNode.getParent();
    Node scriptParentParent = scriptParent.getParent();

    Node blockNode = Context.getCurrentContext().newNode("function foo() {}", Token.FUNCTION, Context.getCurrentContext().newNode("{}"));
    Node blockParent = blockNode.getParent();
    Node blockParentParent = blockParent.getParent();

    assertTrue(isHoistedFunctionDeclaration(scriptNode));
    assertFalse(isHoistedFunctionDeclaration(blockNode));
  }
}