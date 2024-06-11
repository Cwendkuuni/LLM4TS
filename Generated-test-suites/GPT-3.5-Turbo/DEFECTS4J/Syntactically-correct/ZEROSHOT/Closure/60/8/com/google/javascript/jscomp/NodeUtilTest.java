import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, 
                      new Node(Token.NAME, "i"),
                      new Node(Token.LP, 
                               new Node(Token.INIT_VAR), 
                               new Node(Token.NAME, "arr")),
                      new Node(Token.NAME, "empty"));
    assertTrue(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR, 
                      new Node(Token.NAME, "i"),
                      new Node(Token.LP, 
                               new Node(Token.INIT_VAR), 
                               new Node(Token.NAME, "arr")));
    assertFalse(NodeUtil.isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.BLOCK, 
                      new Node(Token.NAME, "x"),
                      new Node(Token.EXPR_RESULT, 
                               new Node(Token.NAME, "y")));
    assertTrue(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.NAME, "x");
    assertFalse(NodeUtil.isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, 
                      new Node(Token.NAME, "foo"),
                      new Node(Token.EMPTY),
                      new Node(Token.BLOCK, 
                               new Node(Token.EXPR_RESULT,
                                        new Node(Token.NUMBER, "1"))));
    assertTrue(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, 
                      new Node(Token.NAME, "foo"),
                      new Node(Token.EMPTY),
                      new Node(Token.BLOCK, 
                               new Node(Token.RETURN, 
                                        new Node(Token.NUMBER, "1"))));
    assertFalse(NodeUtil.isFunctionDeclaration(n));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, 
                      new Node(Token.NAME, "foo"),
                      new Node(Token.EMPTY),
                      new Node(Token.BLOCK, 
                               new Node(Token.EXPR_RESULT,
                                        new Node(Token.NUMBER, "1"))));
    n.getParent().setType(Token.SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION, 
                      new Node(Token.NAME, "foo"),
                      new Node(Token.EMPTY),
                      new Node(Token.BLOCK, 
                               new Node(Token.RETURN, 
                                        new Node(Token.NUMBER, "1"))));
    n.getParent().getParent().setType(Token.FUNCTION);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
  }
}