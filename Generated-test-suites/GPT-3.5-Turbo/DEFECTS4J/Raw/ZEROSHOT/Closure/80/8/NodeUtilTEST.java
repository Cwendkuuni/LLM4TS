import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR, new Node(Token.EXPR_RESULT),
                         new Node(Token.CALL, new Node(Token.NAME, "f")));
    boolean result = NodeUtil.isForIn(node);
    assertFalse(result);
    
    node = new Node(Token.FOR, new Node(Token.VAR, new Node(Token.NAME, "i")),
                    new Node(Token.CALL, new Node(Token.NAME, "f")));
    result = NodeUtil.isForIn(node);
    assertFalse(result);
    
    node = new Node(Token.FOR, new Node(Token.VAR, new Node(Token.NAME, "i")),
                    new Node(Token.CALL, new Node(Token.NAME, "f")),
                    new Node(Token.TRUE));
    result = NodeUtil.isForIn(node);
    assertTrue(result);
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.NAME, "x");
    node.addChildToFront(new Node(Token.SCRIPT));
    boolean result = NodeUtil.isStatement(node);
    assertTrue(result);
    
    node = new Node(Token.SCRIPT);
    node.addChildToFront(new Node(Token.NAME, "x"));
    result = NodeUtil.isStatement(node);
    assertFalse(result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.NAME, "f"),
                         new Node(Token.EMPTY));
    boolean result = NodeUtil.isFunctionDeclaration(node);
    assertTrue(result);
    
    node = new Node(Token.FUNCTION, new Node(Token.EXPR_RESULT),
                    new Node(Token.NAME, "f"));
    result = NodeUtil.isFunctionDeclaration(node);
    assertFalse(result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, new Node(Token.NAME, "f"),
                         new Node(Token.EMPTY));
    node.getParent().setType(Token.SCRIPT);
    boolean result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
    
    node = new Node(Token.FUNCTION, new Node(Token.NAME, "f"),
                    new Node(Token.EMPTY));
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(node);
    node.setParent(parent);
    parent.setParent(new Node(Token.SCRIPT));
    result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertTrue(result);
    
    node = new Node(Token.FUNCTION, new Node(Token.EXPR_RESULT),
                             new Node(Token.NAME, "f"));
    node.getParent().setType(Token.SCRIPT);
    result = NodeUtil.isHoistedFunctionDeclaration(node);
    assertFalse(result);
  }
}