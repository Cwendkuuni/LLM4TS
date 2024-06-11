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
    
    boolean result = NodeUtil.isForIn(n);
    
    assertFalse(result);
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    n.addChildToBack(new Node(Token.EMPTY));
    
    boolean result = NodeUtil.isStatement(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.setParent(new Node(Token.SCRIPT));
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToFront(new Node(Token.EMPTY));
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assertTrue(result);
  }
}