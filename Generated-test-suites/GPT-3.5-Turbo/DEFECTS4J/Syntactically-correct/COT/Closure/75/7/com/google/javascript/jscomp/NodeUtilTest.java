
import org.junit.Assert;
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
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsStatement() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.EMPTY);
    n.setParent(parent);
    
    boolean result = NodeUtil.isStatement(n);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    Assert.assertTrue(result);
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node grandParent = new Node(Token.SCRIPT);
    Node parent = new Node(Token.FUNCTION);
    parent.setParent(grandParent);
    Node n = new Node(Token.FUNCTION);
    n.setParent(parent);
    
    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    Assert.assertTrue(result);
  }
}

