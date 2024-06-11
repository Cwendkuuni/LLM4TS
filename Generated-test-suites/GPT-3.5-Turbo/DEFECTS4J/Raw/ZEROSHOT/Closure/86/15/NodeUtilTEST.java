import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Before
    public void setUp() {
        // add any setup code here
    }

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.SCRIPT);

        boolean result = NodeUtil.isStatement(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsStatementWithParentNull() {
        Node node = new Node(Token.EMPTY);

        boolean result = NodeUtil.isStatement(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(true, result);
    }

    @Test
    public void testIsFunctionDeclarationWithIncorrectParentType() {
        Node parent = new Node(Token.EMPTY);
        Node node = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertEquals(false, result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node functionDeclaration = new Node(Token.FUNCTION);
        Node grandparent = new Node(Token.FUNCTION);
        grandparent.addChildToBack(functionDeclaration);
        parent.addChildToBack(grandparent);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);

        assertEquals(true, result);
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithIncorrectParentType() {
        Node parent = new Node(Token.BLOCK);
        Node functionDeclaration = new Node(Token.FUNCTION);
        parent.addChildToBack(functionDeclaration);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);

        assertEquals(false, result);
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithIncorrectGrandparentType() {
        Node grandparent = new Node(Token.SCRIPT);
        Node functionDeclaration = new Node(Token.FUNCTION);
        grandparent.addChildToBack(functionDeclaration);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(functionDeclaration);

        assertEquals(false, result);
    }
}