import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  private NodeUtil nodeUtil;

  @Before
  public void setup() {
    nodeUtil = new NodeUtil();
  }

  @Test
  public void testIsForIn() {
    Node n = new Node(Token.FOR, new Node(Token.IN), new Node(Token.BLOCK));
    boolean result = nodeUtil.isForIn(n);
    assertEquals(true, result);
  }

  @Test
  public void testIsNotForIn() {
    Node n = new Node(Token.FOR);
    boolean result = nodeUtil.isForIn(n);
    assertEquals(false, result);
  }

  @Test
  public void testIsStatement() {
    Node n = new Node(Token.EXPR_RESULT);
    boolean result = nodeUtil.isStatement(n);
    assertEquals(true, result);
  }

  @Test
  public void testIsNotStatement() {
    Node n = new Node(Token.LITERAL);
    boolean result = nodeUtil.isStatement(n);
    assertEquals(false, result);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = new Node(Token.FUNCTION);
    boolean result = nodeUtil.isFunctionDeclaration(n);
    assertEquals(true, result);
  }

  @Test
  public void testIsNotFunctionDeclaration() {
    Node n = new Node(Token.VAR);
    boolean result = nodeUtil.isFunctionDeclaration(n);
    assertEquals(false, result);
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node parent = new Node(Token.SCRIPT);
    Node n = new Node(Token.FUNCTION, parent);
    boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
    assertEquals(true, result);
  }

  @Test
  public void testIsNotHoistedFunctionDeclaration() {
    Node parent = new Node(Token.IF);
    Node n = new Node(Token.FUNCTION, parent);
    boolean result = nodeUtil.isHoistedFunctionDeclaration(n);
    assertEquals(false, result);
  }
}