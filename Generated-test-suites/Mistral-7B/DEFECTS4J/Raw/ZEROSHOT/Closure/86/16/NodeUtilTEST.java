import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(isForIn(forNode));

    Node notForNode = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    Node parentScriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node parentBlockNode = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    Node labelNode = new Node(Token.LABEL, new int[]{}, new int[]{0, 0, 0});

    statementNode.setParent(parentScriptNode);
    assertTrue(isStatement(statementNode));

    statementNode.setParent(parentBlockNode);
    assertTrue(isStatement(statementNode));

    statementNode.setParent(labelNode);
    assertFalse(isStatement(statementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node statementNode = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "anonymous");

    statementNode.setParent(new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0}));
    assertFalse(isFunctionDeclaration(functionDeclarationNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
    assertFalse(isFunctionDeclaration(statementNode));

    functionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    functionDeclarationNode.setParent(new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "outerFunction"));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "hoistedFunction");
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "functionDeclaration");
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "functionExpression");

    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    functionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));

    functionExpressionNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));

    hoistedFunctionDeclarationNode.setParent(new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "outerFunction"));
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    functionDeclarationNode.setParent(new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "outerFunction"));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));

    functionExpressionNode.setParent(new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "outerFunction"));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));

    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}, "outerScript"));
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    functionDeclarationNode.setParent(new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "outerFunction"));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));

    functionExpressionNode.setParent(new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, "outerFunction"));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));

    hoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}, "outerScript"));
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    functionDeclarationNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode));

    functionExpressionNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0}));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));
  }
}