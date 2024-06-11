import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeExpression;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // ... (previously proposed test case)
    }

    @Test
    public void testIsStatement() {
        // ... (previously proposed test case)
    }

    @Test
    public void testIsFunctionDeclaration() {
        // ... (previously proposed test case)
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // ... (previously proposed test case)
    }

    @Test
    public void testEdgeCasesIsForIn() {
        Node nodeForInWithZeroChildren = new Node("for", new int[]{Token.FOR, Token.IDENT, Token.LPAREN, Token.RPAREN});
        assertFalse(NodeUtil.isForIn(nodeForInWithZeroChildren));

        Node nodeForInWithFourChildren = new Node("for", new int[]{Token.FOR, Token.IDENT, Token.LPAREN, Token.RPAREN, Token.KEYWORD, Token.SEMICOLON, Token.LPAREN, NodeExpression.IDENT("i"), Token.RPAREN, Token.SEMICOLON, Token.KEYWORD, Token.SEMICOLON, Token.IDENT("document.getElementById"), Token.LPAREN, NodeExpression.STRING("myNode"), Token.RPAREN, Token.SEMICOLON, Token.KEYWORD, Token.SEMICOLON, Token.LPAREN, Token.RPAREN, Token.SEMICOLON, Token.RPAREN, Token.KEYWORD, Token.SEMICOLON});
        assertTrue(NodeUtil.isForIn(nodeForInWithFourChildren));
    }

    @Test
    public void testEdgeCasesIsStatement() {
        Node statementNodeWithZeroChildren = new Node("return", new int[]{});
        assertFalse(NodeUtil.isStatement(statementNodeWithZeroChildren));

        Node statementNodeWithTwoChildren = new Node("if", new int[]{Token.IF, NodeExpression.BOOL(true), Token.LPAREN, NodeExpression.BOOL(false), Token.RPAREN, Token.LPAREN, Token.RPAREN});
        assertTrue(NodeUtil.isStatement(statementNodeWithTwoChildren));
    }

    @Test
    public void testEdgeCasesIsFunctionDeclaration() {
        Node functionDeclarationNodeWithZeroChildren = new Node("function", new int[]{Token.FUNCTION, Token.IDENT("myFunction"), Token.LPAREN, Token.RPAREN});
        assertFalse(NodeUtil.isFunctionDeclaration(functionDeclarationNodeWithZeroChildren));

        Node functionDeclarationNodeWithOneChild = new Node("function", new int[]{Token.FUNCTION, Token.IDENT("myFunction"), Token.LPAREN, Token.RPAREN, null, Node.BODY, Token.RCURLY});
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNodeWithOneChild));
    }

    @Test(expected = AssertionError.class)
    public void testExceptionScenarioIsForIn() {
        Node nodeForInWithNullArgument = null;
        assertFalse(NodeUtil.isForIn(nodeForInWithNullArgument));
    }

    @Test(expected = AssertionError.class)
    public void testExceptionScenarioIsStatement() {
        Node nodeStatementWithNullArgument = null;
        assertTrue(NodeUtil.isStatement(nodeStatementWithNullArgument));
    }

    @Test(expected = AssertionError.class)
    public void testExceptionScenarioIsFunctionDeclaration() {
        Node nodeFunctionDeclarationWithNullArgument = null;
        assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithNullArgument));
    }
}