import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[]{}, new Node[]{}, null);
    Node invalidNode1 = Node.newNode(Token.IF, new int[]{}, new Node[]{}, null);
    Node invalidNode2 = Node.newNode(Token.RETURN, new int[]{}, new Node[]{}, null);

    assertTrue("Nodes representing FOR-IN loops should return true", NodeUtil.isForIn(forNode));
    assertFalse("Nodes not representing FOR-IN loops should return false", NodeUtil.isForIn(invalidNode1));
    assertFalse("Nodes not representing FOR-IN loops should return false", NodeUtil.isForIn(invalidNode2));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    Node blockNode = Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}, null);
    Node labelNode = Node.newNode(Token.LABEL, new int[]{}, new Node[]{}, null);
    Node functionNode = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node expressionNode = Node.newNode(Token.IDENTIFIER, new int[]{}, new Node[]{}, null);

    assertTrue("Nodes representing statements (SCRIPT, BLOCK, LABEL) should return true",
        NodeUtil.isStatement(scriptNode) && NodeUtil.isStatement(blockNode) && NodeUtil.isStatement(labelNode));
    assertFalse("Nodes not representing statements should return false", NodeUtil.isStatement(functionNode));
    assertFalse("Nodes not representing statements should return false", NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, null);
    Node functionExpression = Node.newNode(Token.FUNCTION, new int[]{Token.NAME}, new Node[]{}, Node.newNode(Token.IDENTIFIER, new int[]{}, new Node[]{}, null));

    assertTrue("Nodes representing function declarations should return true", NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse("Nodes not representing function declarations should return false", NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[]{}, new Node[]{}, null);
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[]{Token.NAME}, new Node[]{}, Node.newNode(Token.IDENTIFIER, new int[]{}, new Node[]{}, null));
    Node functionNodeInExpression = Node.newNode(Token.FUNCTION, new int[]{Token.NAME}, new Node[]{}, Node.newNode(Token.IDENTIFIER, new int[]{}, new Node[]{}, null));
    Node functionNodeInBlock = Node.newNode(Token.FUNCTION, new int[]{Token.NAME}, new Node[]{}, Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}, null));

    assertFalse("Nodes not representing hoisted function declarations should return false", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse("Nodes not representing hoisted function declarations should return false", NodeUtil.isHoistedFunctionDeclaration(functionNodeInExpression));
    assertTrue("Nodes representing hoisted function declarations should return true", NodeUtil.isHoistedFunctionDeclaration(functionNodeInBlock));
  }

  @Test
  public void testNullInputs() {
    assertNull("isForIn method should return null when passed null", NodeUtil.isForIn(null));
    assertNull("isStatement method should return null when passed null", NodeUtil.isStatement(null));
    assertNull("isFunctionDeclaration method should return null when passed null", NodeUtil.isFunctionDeclaration(null));
    assertNull("isHoistedFunctionDeclaration method should return null when passed null", NodeUtil.isHoistedFunctionDeclaration(null));
  }
}