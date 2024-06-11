import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, null, null);
    forNode.setChild(0, new Node(Token.IDENTIFIER, "var", null));
    forNode.setChild(1, new Node(Token.ASSIGN, null, null));
    forNode.setChild(1, forNode.getChild(1).setChild(0, new Node(Token.KEYWORD, "in", null)));
    forNode.setChild(2, new Node(Token.IDENTIFIER, "obj", null));
    forNode.setChild(3, new Node(Token.ASSIGN, null, null));
    forNode.setChild(3, forNode.getChild(3).setChild(0, new Node(Token.NAME, "iterator", null)));
    Node iterNode = new Node(Token.VAR, "iterator", null);
    Node objectNode = new Node(Token.IDENTIFIER, "obj", null);

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(new Node(Token.IF, null, null)));
    assertFalse(isForIn(new Node(Token.FUNCTION, null, null)));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, null, null);
    Node ifNode = new Node(Token.IF, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null);
    Node statementNode = new Node(Token.ASSIGN, null, null);

    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(scriptNode));
    assertFalse(isStatement(ifNode));
    assertFalse(isStatement(functionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, "foo", null);
    Node statementFunction = new Node(Token.FUNCTION, null, null);
    Node functionExpression = new Node(Token.FUNCTION, null, new Node(Token.ASSIGN, null, null));

    functionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "foo", null));
    statementFunction.setChild(0, new Node(Token.IDENTIFIER, "foo", null));
    functionExpression.setChild(0, new Node(Token.IDENTIFIER, "foo", null));

    Node parentScript = new Node(Token.SCRIPT, null, null);
    Node parentFunction = new Node(Token.FUNCTION, null, null);

    parentScript.addChild(functionDeclaration);
    parentFunction.addChild(statementFunction);
    parentFunction.addChild(functionExpression);

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(statementFunction));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, null, null);
    Node functionDeclaration = new Node(Token.FUNCTION, "foo", null);
    Node functionDeclarationInFunction = new Node(Token.FUNCTION, null, null);

    functionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "foo", null));
    functionDeclarationInFunction.setChild(0, new Node(Token.IDENTIFIER, "foo", null));

    scriptNode.addChild(functionDeclaration);
    functionDeclaration.setParent(scriptNode);
    functionDeclarationInFunction.setParent(new Node(Token.FUNCTION, null, null));

    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }
}