import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import static org.junit.Assert.*;

public class NodeUtilTest {
  
  private NodeUtil nodeUtil;

  @Before
  public void setUp() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertTrue(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.WHILE);
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    
    assertFalse(NodeUtil.isForIn(n));
  }
  
  @Test
  public void testIsStatement() {
    Node n = new Node(Token.RETURN);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    assertFalse(NodeUtil.isStatement(n));
  }
  
  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToBack(n);
    n.setParent(parent);
    
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }
  
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node scriptParent = new Node(Token.SCRIPT);
    scriptParent.addChildToBack(n);
    n.setParent(scriptParent);
    
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }
  
  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    Node functionParent = new Node(Token.FUNCTION);
    functionParent.addChildToBack(new Node(Token.EMPTY));
    functionParent.addChildToBack(new Node(Token.EMPTY));
    functionParent.addChildToBack(n);
    n.setParent(functionParent);
    
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }

}