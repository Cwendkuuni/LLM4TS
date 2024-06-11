
import static org.junit.Assert.*;

import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME, "i"));
    n.addChildToBack(new Node(Token.NAME, "obj"));
    n.addChildToBack(new Node(Token.NAME, "array"));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.NAME, "i"));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.EXPR_RESULT);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    n.setParent(parent);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.EXPR_RESULT);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToFront(new Node(Token.NAME, "myFunction"));
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME, "myFunction"));

    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToFront(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}
