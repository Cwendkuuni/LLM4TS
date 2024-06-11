import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsForIn_notForIn() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsStatement_notStatement() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsFunctionDeclaration_notFunctionDeclaration() {
    Node n = new Node(Token.EMPTY);
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.EMPTY));
    parent.addChildToFront(n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration_notHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(n);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}