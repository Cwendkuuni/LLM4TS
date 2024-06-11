import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.NodeUtil.isStatement;
import static com.google.javascript.jscomp.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.NodeUtil.isHoistedFunctionDeclaration;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node();
    node.setType(Token.FOR);
    node.setChildCount(3);

    assertTrue(isForIn(node));

    node.setType(Token.IF);
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node();
    Node parent = new Node();
    Node grandParent = new Node();

    Preconditions.checkState(parent != null);
    Preconditions.checkState(grandParent != null);

    parent.setType(Token.SCRIPT);
    parent.setParent(grandParent);

    node.setParent(parent);

    // It is not possible to determine definitely if a node is a statement
    // or not if it is not part of the AST.  A FUNCTION node can be
    // either part of an expression or a statement.
    assertTrue(isStatement(node));

    node.setType(Token.FUNCTION);
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node();
    Node parent = new Node();

    Preconditions.checkState(parent != null);

    parent.setType(Token.FUNCTION);
    parent.setParent(new Node());

    node.setParent(parent);
    node.setType(Token.FUNCTION);

    assertTrue(isFunctionDeclaration(node));

    node.setType(Token.EXPR);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node();
    Node parent = new Node();
    Node grandParent = new Node();

    Preconditions.checkState(grandParent != null);
    Preconditions.checkState(parent != null);

    grandParent.setType(Token.SCRIPT);
    grandParent.setParent(null);

    parent.setType(Token.FUNCTION);
    parent.setParent(grandParent);

    node.setParent(parent);
    node.setType(Token.FUNCTION);

    assertTrue(isHoistedFunctionDeclaration(node));

    parent.setType(Token.BLOCK);
    assertFalse(isHoistedFunctionDeclaration(node));
  }
}