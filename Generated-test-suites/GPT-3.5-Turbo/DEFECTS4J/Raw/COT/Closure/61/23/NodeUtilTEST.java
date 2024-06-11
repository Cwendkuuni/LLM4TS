
import static org.junit.Assert.*;
import org.junit.Test;

public class NodeUtilTest {

    // Test isForIn() method
    @Test
    public void testIsForIn() {
        // Test for FOR loop
        Node forLoop = new Node(Token.FOR);
        forLoop.addChildToFront(new Node(Token.NAME));

        assertFalse(NodeUtil.isForIn(forLoop)); // Expected: false

        // Test for FOR-IN loop
        Node forInLoop = new Node(Token.FOR);
        forInLoop.addChildToFront(new Node(Token.NAME));
        forInLoop.addChildToBack(new Node(Token.TRUE));

        assertTrue(NodeUtil.isForIn(forInLoop)); // Expected: true
    }

    // Test isStatement() method
    @Test
    public void testIsStatement() {
        // Test for statement node
        Node statementNode = new Node(Token.SCRIPT);

        assertTrue(NodeUtil.isStatement(statementNode)); // Expected: true

        // Test for non-statement node
        Node nonStatementNode = new Node(Token.NAME);

        assertFalse(NodeUtil.isStatement(nonStatementNode)); // Expected: false
    }

    // Test isFunctionDeclaration() method
    @Test
    public void testIsFunctionDeclaration() {
        // Test for function declaration node
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        functionDeclarationNode.addChildToFront(new Node(Token.NAME));

        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode)); // Expected: true

        // Test for non-function declaration node
        Node nonFunctionDeclarationNode = new Node(Token.NAME);

        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode)); // Expected: false
    }

    // Test isHoistedFunctionDeclaration() method
    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Test for hoisted function declaration node in script
        Node hoistedFunctionDeclarationScript = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationScript.addChildToFront(new Node(Token.NAME));
        Node scriptNode = new Node(Token.SCRIPT);
        scriptNode.addChildToBack(hoistedFunctionDeclarationScript);

        assertTrue(NodeUtil.isFunctionDeclaration(hoistedFunctionDeclarationScript)); // Expected: true
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationScript)); // Expected: true

        // Test for hoisted function declaration node in function
        Node hoistedFunctionDeclarationFunction = new Node(Token.FUNCTION);
        hoistedFunctionDeclarationFunction.addChildToFront(new Node(Token.NAME));
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToFront(hoistedFunctionDeclarationFunction);

        assertTrue(NodeUtil.isFunctionDeclaration(hoistedFunctionDeclarationFunction)); // Expected: true
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationFunction)); // Expected: false

        // Test for non-hoisted function declaration node
        Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        nonHoistedFunctionDeclarationNode.addChildToFront(new Node(Token.NAME));

        assertFalse(NodeUtil.isFunctionDeclaration(nonHoistedFunctionDeclarationNode)); // Expected: false
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode)); // Expected: false
    }

}

