import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        Node node3 = new Node(Token.WHILE);
        
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.NAME));
        node1.addChildToBack(new Node(Token.NAME));
        
        assertFalse(NodeUtil.isForIn(node3));
        assertTrue(NodeUtil.isForIn(node1));
        assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node labelNode = new Node(Token.LABEL);
        Node functionNode = new Node(Token.FUNCTION);
        
        assertTrue(NodeUtil.isStatement(scriptNode));
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(labelNode));
        assertTrue(NodeUtil.isStatement(functionNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node notFunctionDeclarationNode = new Node(Token.NAME);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
        assertFalse(NodeUtil.isFunctionDeclaration(notFunctionDeclarationNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node hoistedFunctionDeclarationNode1 = new Node(Token.FUNCTION);
        Node hoistedFunctionDeclarationNode2 = new Node(Token.FUNCTION);
        Node notHoistedFunctionDeclarationNode = new Node(Token.NAME);
        
        hoistedFunctionDeclarationNode1.addChildToBack(new Node(Token.SCRIPT));
        hoistedFunctionDeclarationNode2.getParent().addChildToBack(new Node(Token.FUNCTION));
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode1));
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode2));
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    }
}