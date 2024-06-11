import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in array) {}");
    assertTrue(NodeUtil.isForIn(node));

    node = new Node("for (var i = 0; i < 10; i++) {}");
    assertFalse(NodeUtil.isForIn(node));

    node = new Node("for (var i = 0; i < 10; i++) { return; }");
    assertFalse(NodeUtil.isForIn(node));

    node = new Node("for (var i = 0; i < 10;) { return; }");
    assertFalse(NodeUtil.isForIn(node));

    node = new Node("for (var i = 0; i < 10;) {}");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("1 + 2");
    assertFalse(NodeUtil.isStatement(node));

    node = new Node("if (true) {}", 1);
    assertTrue(NodeUtil.isStatement(node));

    node = new Node("for (var i = 0; i < 10; i++) {}", 1);
    assertTrue(NodeUtil.isStatement(node));

    node = new Node("function foo() {}", 1);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() {}", 1);
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node("function foo() {}", 1, null);
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = new Node("function foo() {}", 1, new Node("return 1;"));
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    node = new Node("function foo() {}", 1, new Node("return 1;"), new Node("return 2;"));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = new Node("function foo() { return 1; }", null);
    Node functionNode = script.getFirstChild();
    Node funcDecl = functionNode;

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(funcDecl));

    Node parent = script.getParent();
    while (parent != null) {
      if (parent.getType() == FUNCTION) {
        functionNode = parent;
        funcDecl = functionNode.getFirstChild();
      }
      parent = parent.getParent();
    }

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(funcDecl));
  }

  @Test(expected = AssertionError.class)
  public void testIsForInNullNode() {
    NodeUtil.isForIn(null);
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationNullParent() {
    Node node = new Node("function foo() {}", null);
    Preconditions.checkState(node != null);
    NodeUtil.isFunctionDeclaration(node);
  }
}