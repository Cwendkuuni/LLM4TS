import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenType;

/**
 * Test class for NodeUtil.
 */
public class NodeUtilTest {

  @Test
  @Category(org.junit.experimental.categories.SmallTest.class)
  public void testIsForIn() {
    Node n = NodeFactory.createTokenNode(FOR, new int[] { 0, 1, 2 });
    assertFalse("Unexpected FOR node to be FOR-IN", NodeUtil.isForIn(n));

    n = NodeFactory.createTokenNode(FOR, new int[] { FOR, IDENTIFIER, IN, IDENTIFIER, OBRACE, OBRACE, CLOSEBRACE, SEMICOLON, OBRACE, OBRACE, CLOSEBRACE, SEMICOLON, KEYWORD, COLON,VAR, OBRACE, IDENTIFIER, COMMA, IDENTIFIER, COMMA, IDENTIFIER, CLOSEBRACE, SEMICOLON, VAR, OBRACE, IDENTIFIER, COMMA, IDENTIFIER, CLOSEBRACE, SEMICOLON,Statement, OBRACE, CLOSEBRACE, SEMICOLON, OBRACE, CLOSEBRACE });
    assertTrue("Unexpected node not to be FOR-IN", NodeUtil.isForIn(n));
  }

  @Test
  @Category(org.junit.experimental.categories.SmallTest.class)
  public void testIsStatement() {
    Node script = NodeFactory.createTokenNode(SCRIPT, new int[0]);
    Node function = NodeFactory.createFunction("foo", new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);

    assertFalse("Unexpected node to be a statement when it is a script", NodeUtil.isStatement(script));
    assertFalse("Unexpected function expression to be a statement", NodeUtil.isStatement(function));

    Node statement1 = NodeFactory.createStatement(PRINT, new int[] { IDENTIFIER, OBRACE, "Hello World", CLOSEBRACE, SEMICOLON });
    Node statement2 = NodeFactory.createStatement(ASSIGN, new int[] { IDENTIFIER, OBRACE, IDENTIFIER, COMMA, INT_LITERAL, OBRACE, RPAREN, COMMA, INT_LITERAL, OBRACE, COMMA, INT_LITERAL, OBRACE, RPAREN, SEMICOLON });
    Node statement3 = NodeFactory.createStatement(IF, new int[] { IF, LPAREN, BOOLEAN_LITERAL, RPAREN, OBRACE, ELSE, LPAREN, ELSE, RPAREN, OBRACE, RETURN, INT_LITERAL, CLOSEBRACE, CLOSEBRACE, CLOSEBRACE, SEMICOLON });

    assertTrue("Unexpected node not to be a statement", NodeUtil.isStatement(statement1));
    assertTrue("Unexpected node not to be a statement", NodeUtil.isStatement(statement2));
    assertTrue("Unexpected node not to be a statement", NodeUtil.isStatement(statement3));
  }

  @Test
  @Category(org.junit.experimental.categories.SmallTest.class)
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = NodeFactory.createFunction("foo", new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);
    Node functionExpression = NodeFactory.createExpression(FUNCTION, new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);

    assertFalse("Unexpected function expression to be a function declaration", NodeUtil.isFunctionDeclaration(functionExpression));
    assertTrue("Unexpected node not to be a function declaration", NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  @Category(org.junit.experimental.categories.SmallTest.class)
  public void testIsHoistedFunctionDeclaration() {
    Node script = NodeFactory.createTokenNode(SCRIPT, new int[0]);
    Node functionDeclaration1 = NodeFactory.createFunction("foo", new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);
    Node functionDeclaration2 = NodeFactory.createFunction("bar", new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);
    Node functionExpression = NodeFactory.createExpression(FUNCTION, new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);

    assertFalse("Unexpected node not to be a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration1));
    assertFalse("Unexpected node not to be a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration2));
    assertFalse("Unexpected function expression not to be a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionExpression));

    Node scriptWithFunctionDeclaration1 = NodeFactory.createTokenNode(SCRIPT, new int[] { functionDeclaration1 });
    Node scriptWithFunctionDeclaration2 = NodeFactory.createTokenNode(SCRIPT, new int[] { functionDeclaration1, functionDeclaration2 });
    Node functionInScript = NodeFactory.createFunction("baz", new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);
    Node functionInFunction = NodeFactory.createFunction("qux", new int[] { VAR, IDENTIFIER, OBRACE, OBRACE, RETURN, NULL, CLOSEBRACE }, null);

    assertFalse("Unexpected node not to be a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionInScript));
    assertFalse("Unexpected node not to be a hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionInFunction));
    assertTrue("Unexpected function declaration not to be hoisted", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration1));
    assertTrue("Unexpected function declaration not to be hoisted", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration2));
    assertFalse("Unexpected script not to be hoisted", NodeUtil.isHoistedFunctionDeclaration(script));
    assertTrue("Unexpected script with function declaration not to be hoisted", NodeUtil.isHoistedFunctionDeclaration(scriptWithFunctionDeclaration1));
    assertTrue("Unexpected script with multiple function declarations not to be hoisted", NodeUtil.isHoistedFunctionDeclaration(scriptWithFunctionDeclaration2));
  }
}