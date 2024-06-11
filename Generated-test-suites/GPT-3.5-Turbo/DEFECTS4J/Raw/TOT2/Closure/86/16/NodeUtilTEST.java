import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(), new Node(), new Node());
    assertTrue(NodeUtil.isForIn(n));
    
    Node n2 = new Node(Token.FUNCTION, new Node(), new Node());
    assertFalse(NodeUtil.isForIn(n2));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.SCRIPT, new Node());
    assertTrue(NodeUtil.isStatement(n));
    
    Node n2 = new Node(Token.FUNCTION, new Node());
    assertFalse(NodeUtil.isStatement(n2));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node());
    assertTrue(NodeUtil.isFunctionDeclaration(n));
    
    Node n2 = new Node(Token.VAR, new Node());
    assertFalse(NodeUtil.isFunctionDeclaration(n2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, new Node());
    Node scriptNode = new Node(Token.SCRIPT, n);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n, scriptNode));
    
    Node n2 = new Node(Token.FUNCTION, new Node());
    Node functionNode = new Node(Token.FUNCTION, n2);
    Node parent = new Node(Token.BLOCK, functionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n2, parent));
  }
}