import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node(Token.FOR);
    Node nodeWithThreeChildren = new Node(Token.FOR).setChild(1, new Node(Token.IDENTIFIER, "var"));
    Node nodeWithInvalidChildCount = new Node(Token.FOR).setChild(1, new Node(Token.IDENTIFIER, "var"), new Node(Token.IDENTIFIER, "var"));

    assertTrue(isForIn(nodeWithThreeChildren));
    assertFalse(isForIn(node));
    assertFalse(isForIn(nodeWithInvalidChildCount));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeWithParentScript = new Node(Token.SCRIPT);
    Node nodeWithParentBlock = new Node(Token.BLOCK);
    Node nodeWithParentLabel = new Node(Token.LABEL, "label");
    Node nodeInsideScript = new Node(Token.IDENTIFIER, "identifier").setParent(nodeWithParentScript);
    Node nodeInsideBlock = new Node(Token.IDENTIFIER, "identifier").setParent(nodeWithParentBlock);
    Node nodeInsideLabel = new Node(Token.IDENTIFIER, "identifier").setParent(nodeWithParentLabel);

    assertTrue(isStatement(nodeInsideScript));
    assertTrue(isStatement(nodeInsideBlock));
    assertFalse(isStatement(nodeInsideLabel));
    assertFalse(isStatement(new Node(Token.IDENTIFIER, "identifier")));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(Token.IDENTIFIER, "identifier");
    node.setParent(null);
    isStatement(node);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION)
        .setChild(0, new Node(Token.IDENTIFIER, "functionName"))
        .setChild(1, new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))))
        .setParent(new Node(Token.SCRIPT));

    Node functionExpression = new Node(Token.FUNCTION)
        .setChild(0, new Node(Token.IDENTIFIER, "functionName"))
        .setChild(1, new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))))
        .setParent(new Node(Token.ASSIGNMENT, Token.ASSIGN, new Node(Token.IDENTIFIER, "variableName"), functionExpression));

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION)
        .setChild(0, new Node(Token.IDENTIFIER, "functionName"))
        .setChild(1, new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))))
        .setParent(new Node(Token.SCRIPT));

    Node notHoistedFunctionDeclaration = new Node(Token.FUNCTION)
        .setChild(0, new Node(Token.IDENTIFIER, "functionName"))
        .setChild(1, new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))))
        .setParent(new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))));

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node functionDeclaration = new Node(Token.FUNCTION)
        .setChild(0, new Node(Token.IDENTIFIER, "functionName"))
        .setChild(1, new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NULL))));
    functionDeclaration.setParent(null);
    isHoistedFunctionDeclaration(functionDeclaration);
  }
}