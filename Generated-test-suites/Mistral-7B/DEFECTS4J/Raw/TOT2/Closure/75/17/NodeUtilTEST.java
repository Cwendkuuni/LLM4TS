import static org.junit.Assert.assertEquals;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(createToken(Token.FOR, null, null));
    Node notForNode = createNode(createToken(Token.IF, null, null));
    assertEquals(true, NodeUtil.isForIn(forNode));
    assertEquals(false, NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(createToken(Token.IF, null, null));
    Node notStatementNode = createNode(createToken(Token.IDENT, "x", null));
    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(createToken(Token.FUNCTION, "myFunction", null), createNode(createToken(Token.BLOCK, null, null)));
    Node notFunctionDeclaration = createNode(createToken(Token.RETURN, null, null));
    assertEquals(true, NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertEquals(false, NodeUtil.isFunctionDeclaration(notFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunction = createNode(createToken(Token.FUNCTION, "myFunction", null), createNode(createToken(Token.BLOCK, null, null)), createNode(createToken(Token.SCRIPT, null, null)));
    Node notHoistedFunction = createNode(createToken(Token.FUNCTION, "myFunction", null), createNode(createToken(Token.BLOCK, null, null)), createNode(createToken(Token.IF, null, null), createNode(createToken(Token.IDENT, "x", null), null, null), createNode(createToken(Token.BLOCK, null, null))));
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(hoistedFunction));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(notHoistedFunction));
  }
}