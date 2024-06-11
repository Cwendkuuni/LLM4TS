import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_WhenNodeIsForIn_ReturnsTrue() {
        Node node = createForInNode();
        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsForIn_WhenNodeIsNotForIn_ReturnsFalse() {
        Node node = createNonForInNode();
        assertFalse(NodeUtil.isForIn(node));
    }
    
    private Node createForInNode() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME, new Node(Token.FOR_IN)));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        return node;
    }
    
    private Node createNonForInNode() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        node.addChildToBack(new Node(Token.NAME));
        return node;
    }
    
    @Test
    public void testIsStatement_WhenNodeIsUsedAsStatement_ReturnsTrue() {
        Node node = createStatementNode();
        assertTrue(NodeUtil.isStatement(node));
    }
    
    @Test
    public void testIsStatement_WhenNodeIsNotUsedAsStatement_ReturnsFalse() {
        Node node = createNonStatementNode();
        assertFalse(NodeUtil.isStatement(node));
    }

    private Node createStatementNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node blockNode = new Node(Token.BLOCK);
        Node statementNode = new Node(Token.NAME);
        blockNode.addChildToBack(statementNode);
        scriptNode.addChildToBack(blockNode);
        return statementNode;
    }

    private Node createNonStatementNode() {
        return new Node(Token.NAME);
    }
    
    @Test
    public void testIsFunctionDeclaration_WhenNodeIsFunctionDeclaration_ReturnsTrue() {
        Node node = createFunctionDeclarationNode();
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }
    
    @Test
    public void testIsFunctionDeclaration_WhenNodeIsNotFunctionDeclaration_ReturnsFalse() {
        Node node = createNonFunctionDeclarationNode();
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }
    
    private Node createFunctionDeclarationNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node nameNode = new Node(Token.NAME);
        scriptNode.addChildToBack(functionNode);
        functionNode.addChildToBack(nameNode);
        return functionNode;
    }
    
    private Node createNonFunctionDeclarationNode() {
        return new Node(Token.FUNCTION);
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsHoistedFunctionDeclaration_ReturnsTrue() {
        Node node = createHoistedFunctionDeclarationNode();
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WhenNodeIsNotHoistedFunctionDeclaration_ReturnsFalse() {
        Node node = createNonHoistedFunctionDeclarationNode();
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
    
    private Node createHoistedFunctionDeclarationNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        Node scriptParentNode = new Node(Token.SCRIPT);
        scriptParentNode.addChildToBack(functionNode);
        scriptNode.addChildToBack(scriptParentNode);
        return functionNode;
    }
    
    private Node createNonHoistedFunctionDeclarationNode() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        scriptNode.addChildToBack(functionNode);
        return functionNode;
    }
}