import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {
  
  private Node n;

  @Before
  public void setUp() {
    n = new Node(Token.FOR);
  }

  @Test
  public void testIsForIn_True() {
    // Typical use case
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);
    
    assertTrue(result);
  }

  @Test
  public void testIsForIn_False() {
    // Edge case: empty FOR node
    n.addChildToBack(new Node(Token.EMPTY));

    boolean result = NodeUtil.isForIn(n);
    
    assertFalse(result);
  }

  @Test
  public void testIsStatement_True() {
    // Typical use case
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);

    boolean result = NodeUtil.isStatement(n);
    
    assertTrue(result);
  }

  @Test
  public void testIsStatement_False() {
    // Edge case: FOR node not part of the AST
    boolean result = NodeUtil.isStatement(n);
    
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration_True() {
    // Typical use case
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.addChildToFront(new Node(Token.NAME));

    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assertTrue(result);
  }

  @Test
  public void testIsFunctionDeclaration_False() {
    // Edge case: FUNCTION node not a statement
    boolean result = NodeUtil.isFunctionDeclaration(n);
    
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_True() {
    // Typical use case
    Node grandParent = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    grandParent.addChildToBack(parent);
    parent.addChildToBack(n);
    n.addChildToFront(new Node(Token.NAME));

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assertTrue(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration_False() {
    // Edge case: FUNCTION node not hoisted
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(n);
    n.addChildToFront(new Node(Token.NAME));

    boolean result = NodeUtil.isHoistedFunctionDeclaration(n);
    
    assertFalse(result);
  }
}