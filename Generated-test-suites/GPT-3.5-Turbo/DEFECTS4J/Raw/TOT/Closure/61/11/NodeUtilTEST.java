import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalUseCase() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.BLOCK));
        
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_emptyForLoop() {
        Node node = new Node(Token.FOR);
        
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_incompleteForLoop() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_inScript_true() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_inScript_false() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsStatement_inBlock_true() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_inBlock_false() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsStatement_inLabel_true() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(node);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_inLabel_false() {
        Node node = new Node(Token.VAR);
        Node parent = new Node(Token.LABEL);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isStatement(parent));
    }

    @Test
    public void testIsFunctionDeclaration_typicalUseCase() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        node.setParent(new Node(Token.BLOCK));
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notStatement() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        node.setParent(new Node(Token.SCRIPT));

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notFunction() {
        Node node = new Node(Token.NAME);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        node.setParent(new Node(Token.BLOCK));

        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_inScript_true() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_inScript_false() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_inFunction_true() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);
        node.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_inFunction_false() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(Node.IS_LENGTHLESS, true);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(node);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}