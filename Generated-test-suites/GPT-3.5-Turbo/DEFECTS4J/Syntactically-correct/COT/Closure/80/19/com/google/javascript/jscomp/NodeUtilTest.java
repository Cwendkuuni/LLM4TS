
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test for a valid FOR-IN loop
        Node forInNode = new Node(Token.FOR, new Node(Token.FOR_IN, new Node(Token.NAME),
                        new Node(Token.EMPTY, new Node(Token.EMPTY)))) ;
        assertTrue(NodeUtil.isForIn(forInNode));

        // Test for a non-FOR-IN loop
        Node notForInNode = new Node(Token.FOR, new Node(Token.FOR_OF, new Node(Token.NAME),
                        new Node(Token.EMPTY, new Node(Token.EMPTY)))) ;
        assertFalse(NodeUtil.isForIn(notForInNode));
    }

    @Test
    public void testIsStatement() {
        // Test for a node used as a statement
        Node parentNode = new Node(Token.SCRIPT, new Node(Token.NAME)) ;
        Node statementNode = new Node(Token.NAME);
        parentNode.addChildToFront(statementNode);
        assertTrue(NodeUtil.isStatement(statementNode));

        // Test for a node not used as a statement
        Node notStatementNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isStatement(notStatementNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test for a function declaration
        Node functionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

        // Test for a non-function declaration
        Node notFunctionDeclarationNode = new Node(Token.NAME);
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for a hoisted function declaration
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.NAME));
        hoistedFunctionDeclarationNode.getParent().setType(Token.SCRIPT);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

        // Test for a non-hoisted function declaration
        Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node(Token.NAME));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}
