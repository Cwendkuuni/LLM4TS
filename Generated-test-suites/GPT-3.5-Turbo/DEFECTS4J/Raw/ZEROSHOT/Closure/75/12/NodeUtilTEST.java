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
    
    Node n2 = new Node(Token.SCRIPT);
    n2.addChildToBack(new Node(Token.EMPTY));
    n2.addChildToBack(new Node(Token.FOR));
    n2.getLastChild().addChildToBack(new Node(Token.EMPTY));
    n2.getLastChild().addChildToBack(new Node(Token.EMPTY));
    n2.getLastChild().addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT);
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isStatement(n.getFirstChild()));
    
    Node n2 = new Node(Token.LABEL);
    n2.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isStatement(n2.getFirstChild()));
    
    Node n3 = new Node(Token.EXPR_RESULT);
    n3.addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isStatement(n3.getFirstChild()));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    Node n2 = new Node(Token.ASSIGN);
    n2.addChildToBack(new Node(Token.FUNCTION));
    n2.getFirstChild().addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isFunctionDeclaration(n2.getFirstChild()));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    n.addChildToBack(new Node(Token.EMPTY));
    
    n.getParent().addChildBefore(new Node(Token.SCRIPT), n);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));

    Node n2 = new Node(Token.FUNCTION);
    n2.addChildToBack(new Node(Token.EMPTY));
    n2.getFirstChild().getParent().addChildAfter(new Node(Token.FUNCTION), n2);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2));
  }
}