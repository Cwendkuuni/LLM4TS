import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        // Testing for FOR-IN loop
        Node nodeForIn = new Node(Token.FOR);
        Node child1 = new Node(Token.NAME);
        Node child2 = new Node(Token.EMPTY);
        Node child3 = new Node(Token.EMPTY);
        nodeForIn.addChildToBack(child1);
        nodeForIn.addChildToBack(child2);
        nodeForIn.addChildToBack(child3);
        assertEquals(true, NodeUtil.isForIn(nodeForIn));

        // Testing for non FOR-IN loop
        Node nodeNotForIn = new Node(Token.FUNCTION);
        assertEquals(false, NodeUtil.isForIn(nodeNotForIn));
    }

    @Test
    public void testIsStatement() {
        // Testing for statement node
        Node nodeStatement = new Node(Token.BLOCK);
        assertEquals(true, NodeUtil.isStatement(nodeStatement));

        // Testing for non-statement node
        Node nodeNotStatement = new Node(Token.ADD);
        assertEquals(false, NodeUtil.isStatement(nodeNotStatement));
    }

    @Test
    public void testIsFunctionDeclaration() {
        // Testing for function declaration node
        Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        nodeFunctionDeclaration.setParent(parent);
        assertEquals(true, NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

        // Testing for non-function declaration node
        Node nodeNotFunctionDeclaration = new Node(Token.VAR);
        assertEquals(false, NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        // Testing for hoisted function declaration node
        Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION);

        Node parent1 = new Node(Token.SCRIPT);
        nodeHoistedFunctionDeclaration.setParent(parent1);
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

        Node parent2 = new Node(Token.FUNCTION);
        Node grandparent2 = new Node(Token.SCRIPT);
        parent2.setParent(grandparent2);
        nodeHoistedFunctionDeclaration.setParent(parent2);
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

        // Testing for non-hoisted function declaration node
        Node nodeNotHoistedFunctionDeclaration = new Node(Token.VAR);
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
    }
}