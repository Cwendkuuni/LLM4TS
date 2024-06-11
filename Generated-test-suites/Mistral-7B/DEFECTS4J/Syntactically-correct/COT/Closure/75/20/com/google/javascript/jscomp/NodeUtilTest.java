import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node nodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.ASSIGN), new Node(Token.IDENTIFIER)}, null);

    assertTrue("Wrong result for isForIn method with invalid node", !isForIn(node));
    assertTrue("Wrong result for isForIn method with valid node", isForIn(nodeWithThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.ASSIGN, new Node(Token.NUMBER, 5), new Node(Token.IDENTIFIER))}, new Node(Token.BLOCK, null, null));
    Node expressionNode = new Node(Token.IDENTIFIER, null, null);

    assertTrue("Wrong result for isStatement method with statement node", isStatement(statementNode));
    assertFalse("Wrong result for isStatement method with expression node", isStatement(expressionNode));
  }

  @Test(expected = RuntimeException.class)
  public void testIsStatementWithNullParentNode() {
    Node node = new Node(Token.IF, null, null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, null, null)}, new Node(Token.BLOCK, null, null));

    assertFalse("Wrong result for isFunctionDeclaration method with non function node", isFunctionDeclaration(new Node(Token.IDENTIFIER, null, null)));
    assertTrue("Wrong result for isFunctionDeclaration method with function node", isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, null, null);
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, null, null)}, new Node(Token.BLOCK, null, null));
    Node functionDeclarationInsideFunctionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, null, null)}, new Node(Token.BLOCK, null, null));

    assertFalse("Wrong result for isHoistedFunctionDeclaration method with non hoisted function declaration node", isHoistedFunctionDeclaration(functionDeclarationInsideFunctionNode));
    assertTrue("Wrong result for isHoistedFunctionDeclaration method with hoisted function declaration node", isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertTrue("Wrong result for isHoistedFunctionDeclaration method with script node", isHoistedFunctionDeclaration(scriptNode));
  }
}