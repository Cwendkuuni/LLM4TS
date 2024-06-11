import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node(); // Valid Node
    node.setType(Token.FOR);
    node.setChildCount(3);
    assertTrue(isForIn(node));

    node.setType(Token.IF); // Invalid Node, not a FOR node
    assertFalse(isForIn(node));

    node.setType(Token.FOR); // Valid Node, but without required children
    node.setChildCount(2);
    assertFalse(isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node(); // Valid Node, part of a script
    node.setParent(new Node() {
      {
        setType(Token.SCRIPT);
      }
    });
    assertTrue(isStatement(node));

    node.setParent(new Node() {
      {
        setType(Token.BLOCK); // Valid Node, part of a block statement
      }
    });
    assertTrue(isStatement(node));

    node.setParent(new Node() {
      {
        setType(Token.LABEL); // Valid Node, part of a label statement
      }
    });
    assertTrue(isStatement(node));

    node.setParent(new Node() {
      {
        setType(Token.ASSIGN); // Invalid Node, not a statement
      }
    });
    assertFalse(isStatement(node));

    node.setParent(null); // Invalid Node, not part of the AST
    assertFalse(isStatement(node));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node();
    node.setParent(null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node(); // Valid Node, function declaration
    node.setType(Token.FUNCTION);
    assertTrue(isStatement(node));
    assertTrue(isFunctionDeclaration(node));

    Node functionExpressionNode = new Node(); // Invalid Node, function expression
    functionExpressionNode.setType(Token.FUNCTION);
    assertFalse(isFunctionDeclaration(functionExpressionNode));

    Node functionDeclarationInExpressionNode = new Node(); // Invalid Node, function declaration in an expression
    functionDeclarationInExpressionNode.setType(Token.FUNCTION);
    node.setParent(functionDeclarationInExpressionNode);
    assertFalse(isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(); // Valid Node, hoisted function declaration
    hoistedFunctionDeclarationNode.setType(Token.FUNCTION);
    hoistedFunctionDeclarationNode.setParent(new Node() {
      {
        setType(Token.SCRIPT);
      }
    });
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node hoistedFunctionDeclarationInFunctionNode = new Node(); // Invalid Node, hoisted function declaration inside a function
    hoistedFunctionDeclarationInFunctionNode.setType(Token.FUNCTION);
    node.setParent(new Node() {
      {
        setType(Token.FUNCTION);
      }
    });
    hoistedFunctionDeclarationInFunctionNode.setParent(node);
    assertFalse(isHoistedFunctionDeclaration(hoistedFunctionDeclarationInFunctionNode));
  }
}