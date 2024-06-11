import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
    
    Node n2 = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    assertTrue(NodeUtil.isStatement(n));
    
    Node n2 = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    Node n2 = new Node(Token.FUNCTION);
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    
    Node n2 = new Node(Token.FUNCTION);
    Node parent2 = new Node(Token.BLOCK);
    parent2.addChildToBack(n2);
    parent.addChildToBack(parent2);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
}