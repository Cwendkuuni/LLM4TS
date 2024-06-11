import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.STRING);
    Node child3 = new Node(Token.BOOLEAN);
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node n = new Node(Token.WHILE);
    Node child1 = new Node(Token.VAR);
    Node child2 = new Node(Token.STRING);
    Node child3 = new Node(Token.BOOLEAN);
    n.addChildToBack(child1);
    n.addChildToBack(child2);
    n.addChildToBack(child3);
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.OBJECTLIT);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(parent);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node n = new Node(Token.FOR);
    Node parent = new Node(Token.BLOCK);
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.addChildToBack(parent);
    parent.addChildToBack(n);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.FUNCTION);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(n);
    parent1.addChildToBack(parent2);
    parent2.addChildToBack(blockNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent1 = new Node(Token.SCRIPT);
    Node parent2 = new Node(Token.IF);
    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(n);
    parent1.addChildToBack(parent2);
    parent2.addChildToBack(blockNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}