
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        // Test 1: For Loop
        Node forLoop = new Node(Token.FOR);
        // Add child nodes
        forLoop.addChildToFront(new Node(Token.NAME));
        forLoop.addChildToFront(new Node(Token.NAME));
        forLoop.addChildToFront(new Node(Token.NAME));
        assertTrue(NodeUtil.isForIn(forLoop));

        // Test 2: Other Node
        Node otherNode = new Node(Token.MUL);
        assertFalse(NodeUtil.isForIn(otherNode));
    }

    @Test
    public void testIsStatement() {
        // Test 1: SCRIPT node
        Node scriptNode = new Node(Token.SCRIPT);
        assertTrue(NodeUtil.isStatement(scriptNode));

        // Test 2: BLOCK node
        Node blockNode = new Node(Token.BLOCK);
        assertTrue(NodeUtil.isStatement(blockNode));

        // Test 3: LABEL node
        Node labelNode = new Node(Token.LABEL);
        assertTrue(NodeUtil.isStatement(labelNode));

        // Test 4: Other node
        Node otherNode = new Node(Token.MUL);
        assertFalse(NodeUtil.isStatement(otherNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Test 1: FUNCTION declaration
        Node functionDeclaration = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        functionDeclaration.setParent(parent);
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

        // Test 2: Other node
        Node otherNode = new Node(Token.MUL);
        assertFalse(NodeUtil.isFunctionDeclaration(otherNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test 1: Hoisted FUNCTION declaration
        Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        hoistedFunctionDeclaration.setParent(parent);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

        // Test 2: Non-hoisted FUNCTION declaration
        Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION);
        Node grandParent = new Node(Token.FUNCTION);
        nonHoistedFunctionDeclaration.setParent(grandParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));

        // Test 3: Other node
        Node otherNode = new Node(Token.MUL);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(otherNode));
    }
}
