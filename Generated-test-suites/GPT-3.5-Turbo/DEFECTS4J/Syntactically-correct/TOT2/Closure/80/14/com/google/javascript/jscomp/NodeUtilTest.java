import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node nForIn = new Node(Token.FOR, new Node(Token.FOR_IN, new Node(Token.EMPTY), new Node(Token.EMPTY)), new Node(Token.EMPTY));
        assertTrue(NodeUtil.isForIn(nForIn));

        Node nNotForIn = new Node(Token.FOR, new Node(Token.EMPTY), new Node(Token.EMPTY));
        assertFalse(NodeUtil.isForIn(nNotForIn));
    }

    @Test
    public void testIsStatement() {
        // Test for node that is used as a statement
        Node nStatement = new Node(Token.SCRIPT);
        Node parentNode = new Node(Token.BLOCK, nStatement);
        nStatement.setParent(parentNode);
        assertTrue(NodeUtil.isStatement(nStatement));

        // Test for node that is not used as a statement
        Node nNotStatement = new Node(Token.FUNCTION);
        parentNode = new Node(Token.SCRIPT, nNotStatement);
        nNotStatement.setParent(parentNode);
        assertFalse(NodeUtil.isStatement(nNotStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test function declaration
        Node nFunctionDeclaration = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.SCRIPT, nFunctionDeclaration);
        nFunctionDeclaration.setParent(parentNode);
        assertTrue(NodeUtil.isFunctionDeclaration(nFunctionDeclaration));

        // Test for function expression
        Node nFunctionExpression = new Node(Token.ASSIGN, new Node(Token.NAME), new Node(Token.FUNCTION));
        parentNode = new Node(Token.SCRIPT, nFunctionExpression);
        nFunctionExpression.setParent(parentNode);
        assertFalse(NodeUtil.isFunctionDeclaration(nFunctionExpression));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test hoisted function declaration in script scope
        Node nHoistedFunctionDeclarationScript = new Node(Token.FUNCTION);
        Node parentNode = new Node(Token.SCRIPT, nHoistedFunctionDeclarationScript);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nHoistedFunctionDeclarationScript));

        // Test hoisted function declaration in function scope
        Node nHoistedFunctionDeclarationFunction = new Node(Token.FUNCTION);
        parentNode = new Node(Token.FUNCTION, new Node(Token.NAME), nHoistedFunctionDeclarationFunction);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(nHoistedFunctionDeclarationFunction));

        // Test non-hoisted function declaration
        Node nNonHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        parentNode = new Node(Token.BLOCK, nNonHoistedFunctionDeclaration);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nNonHoistedFunctionDeclaration));
    }
}