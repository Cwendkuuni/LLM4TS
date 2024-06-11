import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

/**
 * Tests for the NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    node.setChildCount(3);
    Node child1 = new Node(Token.IDENT, "label");
    Node child2 = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "array"));
    Node child3 = new Node(Token.KEYWORD, "in");
    child1.setParent(node);
    child2.setParent(node);
    child3.setParent(node);

    assertTrue(isForIn(node));
  }

  @Test
  public void testIsForInWithInvalidNode() {
    Node node = new Node(Token.IF);
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(Token.SCRIPT);
    node.setChild(new Node(Token.PRINT, null, new Node(Token.STRING, "Hello World!")));

    assertTrue(isStatement(node.getChild()));
  }

  @Test
  public void testIsStatementWithInvalidNode() {
    Node node = new Node(Token.ASSIGN);
    assertFalse(isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "functionName"));
    node.setChild(new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, 42))));

    assertTrue(isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationWithInvalidNode() {
    Node node = new Node(Token.FUNCTION_CALL, null, new Node(Token.IDENT, "functionName"));
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "functionName"));
    functionNode.setChild(new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, 42))));
    scriptNode.setChild(functionNode);

    assertTrue(isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithInvalidNode() {
    Node functionNode = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "functionName"));
    functionNode.setChild(new Node(Token.BLOCK, null, new Node(Token.RETURN, null, new Node(Token.NUMBER, 42))));
    Node parentNode = new Node(Token.IF);
    functionNode.setParent(parentNode);

    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }
}