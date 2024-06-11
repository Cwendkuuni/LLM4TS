import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn_typicalUseCase() {
    // Create a node representing a FOR-IN loop
    Node forInLoop = new Node(Token.FOR);
    forInLoop.addChildToFront(new Node(Token.EMPTY));
    forInLoop.addChildToFront(new Node(Token.EMPTY));
    forInLoop.addChildToFront(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isForIn(forInLoop));
  }

  @Test
  public void testIsForIn_edgeCase() {
    // Create a node representing a FOR loop with only 2 children
    Node forLoop = new Node(Token.FOR);
    forLoop.addChildToFront(new Node(Token.EMPTY));
    forLoop.addChildToFront(new Node(Token.EMPTY));

    assertFalse(NodeUtil.isForIn(forLoop));
  }

  @Test
  public void testIsStatement_typicalUseCase() {
    // Create a node that is part of a SCRIPT
    Node script = new Node(Token.SCRIPT);
    Node statement = new Node(Token.EMPTY);
    script.addChildToFront(statement);

    assertTrue(NodeUtil.isStatement(statement));
  }

  @Test
  public void testIsStatement_edgeCase() {
    // Create a node that is not part of a SCRIPT, BLOCK, or LABEL
    Node parent = new Node(Token.EMPTY);
    Node statement = new Node(Token.EMPTY);
    parent.addChildToFront(statement);

    assertFalse(NodeUtil.isStatement(statement));
  }

  @Test
  public void testIsFunctionDeclaration_typicalUseCase() {
    // Create a node representing a function declaration
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.addChildToFront(new Node(Token.EMPTY));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsFunctionDeclaration_edgeCase() {
    // Create a node representing a function expression
    Node functionExpression = new Node(Token.FUNCTION);
    functionExpression.addChildToFront(new Node(Token.EMPTY));
    Node parent = new Node(Token.EMPTY);
    parent.addChildToFront(functionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_typicalUseCase() {
    // Create a node representing a hoisted function declaration
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.addChildToFront(new Node(Token.EMPTY));
    Node parentScript = new Node(Token.SCRIPT);
    parentScript.addChildToFront(functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_edgeCase() {
    // Create a node representing a function declaration not in the root scope
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.addChildToFront(new Node(Token.EMPTY));
    Node parentFunction = new Node(Token.FUNCTION);
    parentFunction.addChildToFront(functionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }

}