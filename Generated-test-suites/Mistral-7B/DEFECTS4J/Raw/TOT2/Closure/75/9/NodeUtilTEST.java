import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @ org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENTIFIER, "i"));
    forNode.setChild(1, new Node(Token.EQUALS, null));
    forNode.setChild(2, new Node(Token.IDENTIFIER, "arr"));

    assertTrue(NodeUtil.isForIn(forNode));
  }

  @ org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, null);
    Node parentNode = new Node(Token.SCRIPT, null);
    parentNode.setChild(0, statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
  }

  @ org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.setChild(0, new Node(Token.IDENTIFIER, "funcName"));
    functionNode.setChild(1, new Node(Token.BLOCK, null));

    Node parentNode = new Node(Token.SCRIPT, null);
    parentNode.setChild(0, functionNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @ org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.setChild(0, new Node(Token.IDENTIFIER, "funcName"));
    functionNode.setChild(1, new Node(Token.BLOCK, null));

    Node parentFunctionNode = new Node(Token.FUNCTION, null);
    parentFunctionNode.setChild(0, functionNode);

    Node scriptNode = new Node(Token.SCRIPT, null);
    scriptNode.setChild(0, parentFunctionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @ org.junit.Test
  public void testIsForIn_False() {
    Node nonForNode = new Node(Token.IDENTIFIER, "nonForNode");

    assertFalse(NodeUtil.isForIn(nonForNode));
  }

  @ org.junit.Test
  public void testIsStatement_False() {
    Node nonStatementNode = new Node(Token.VAR, null);
    Node parentNode = new Node(Token.SCRIPT, null);
    parentNode.setChild(0, nonStatementNode);

    assertFalse(NodeUtil.isStatement(nonStatementNode));
  }

  @ org.junit.Test
  public void testIsFunctionDeclaration_False() {
    Node nonFunctionNode = new Node(Token.IF, null);

    assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionNode));
  }

  @ org.junit.Test
  public void testIsHoistedFunctionDeclaration_False() {
    Node nonHoistedFunctionNode = new Node(Token.FUNCTION, null);
    nonHoistedFunctionNode.setChild(0, new Node(Token.IDENTIFIER, "funcName"));
    nonHoistedFunctionNode.setChild(1, new Node(Token.BLOCK, null));

    Node parentNode = new Node(Token.SCRIPT, null);
    parentNode.setChild(0, nonHoistedFunctionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionNode));
  }
}