import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[] {1, 2, 3});
    Node normalNode = new Node(Token.IDENT, "normal");

    assertTrue("Is FOR-IN loop", isForIn(forNode));
    assertFalse("Is not FOR-IN loop", isForIn(normalNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new int[] {});
    Node functionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    Node expressionNode = new Node(Token.IDENT, "expression");

    Node statementScriptNode = new Node(Token.SCRIPT, new int[] {});
    Node statementFunctionNode = new Node(Token.FUNCTION, new int[] {1, 2});
    Node statementLabelNode = new Node(Token.LABEL, new int[] {});

    Node blockNode = new Node(Token.BLOCK, new int[] {});

    // It is a statement if it is part of a script, block or label
    assertTrue("Is statement - script", isStatement(scriptNode));
    assertTrue("Is statement - function", isStatement(functionNode));
    assertTrue("Is statement - label", isStatement(labelNode));

    // It is not a statement if it is an expression or not part of an AST
    assertFalse("Is not statement - expression", isStatement(expressionNode));
    assertFalse("Is not statement - block", isStatement(blockNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[] {
        new Node(Token.IDENT, "functionName"),
        new Node(Token.LEFT_BRACE),
        new Node(Token.RIGHT_BRACE)
    });

    Node functionExpressionNode = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[] {
        new Node(Token.IDENT, "functionName"),
        new Node(Token.LEFT_PAREN),
        new Node(Token.RIGHT_PAREN),
        new Node(Token.LEFT_BRACE),
        new Node(Token.RIGHT_BRACE)
    });

    Node normalNode = new Node(Token.IDENT, "normal");

    assertTrue("Is function declaration", isFunctionDeclaration(functionDeclarationNode));
    assertFalse("Is not function declaration", isFunctionDeclaration(functionExpressionNode));
    assertFalse("Is not function declaration", isFunctionDeclaration(normalNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[] {});
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[] {
        new Node(Token.IDENT, "hoistedFunction"),
        new Node(Token.LEFT_BRACE),
        new Node(Token.RIGHT_BRACE)
    });
    Node functionDeclarationNode = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[] {
        new Node(Token.IDENT, "function"),
        new Node(Token.LEFT_BRACE),
        new Node(Token.RIGHT_BRACE)
    });

    Node hoistedFunctionDeclarationInsideFunctionNode = new Node(Token.FUNCTION, new int[] {1, 2}, new Node[] {
        new Node(Token.IDENT, "hoistedFunctionInsideFunction"),
        new Node(Token.LEFT_BRACE),
        new Node(new Node(Token.FUNCTION, new int[] {1, 2}, new Node[] {
            new Node(Token.IDENT, "functionInsideFunction"),
            new Node(Token.LEFT_PAREN),
            new Node(Token.RIGHT_PAREN),
            new Node(Token.LEFT_BRACE),
            new Node(Token.RIGHT_BRACE)
        })),
        new Node(Token.RIGHT_BRACE)
    });

    assertTrue("Is hoisted function declaration in script", isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse("Is not hoisted function declaration in script", isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse("Is not hoisted function declaration in node that is not a function declaration", isHoistedFunctionDeclaration(scriptNode));

    assertTrue("Is hoisted function declaration inside function", isHoistedFunctionDeclaration(hoistedFunctionDeclarationInsideFunctionNode));
    assertFalse("Is not hoisted function declaration inside function but not in a function declaration", isHoistedFunctionDeclaration(functionDeclarationNode));
  }
}