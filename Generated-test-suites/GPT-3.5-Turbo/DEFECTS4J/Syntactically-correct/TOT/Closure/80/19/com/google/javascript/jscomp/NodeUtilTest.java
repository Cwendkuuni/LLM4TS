import static org.junit.Assert.*;
import org.junit.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn_WithForInNode_ReturnsTrue() {
        Node forInNode = new Node(Token.FOR);
        forInNode.addChildToBack(new Node());
        forInNode.addChildToBack(new Node());
        forInNode.addChildToBack(new Node());
        
        assertTrue(NodeUtil.isForIn(forInNode));
    }
    
    @Test
    public void testIsForIn_WithNonForInNode_ReturnsFalse() {
        Node nonForInNode = new Node(Token.FUNCTION);
        nonForInNode.addChildToBack(new Node());
        
        assertFalse(NodeUtil.isForIn(nonForInNode));
    }
    
    @Test
    public void testIsStatement_WithStatementNode_ReturnsTrue() {
        Node statementNode = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(statementNode);
        statementNode.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(statementNode));
    }
    
    @Test
    public void testIsStatement_WithNonStatementNode_ReturnsFalse() {
        Node nonStatementNode = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isStatement(nonStatementNode));
    }
    
    @Test
    public void testIsFunctionDeclaration_WithFunctionDeclarationNode_ReturnsTrue() {
        Node functionDeclarationNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        Node grandParent = new Node(Token.SCRIPT);
        grandParent.addChildToBack(parent);
        parent.addChildToBack(functionDeclarationNode);
        functionDeclarationNode.setParent(parent);
        parent.setParent(grandParent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    }
    
    @Test
    public void testIsFunctionDeclaration_WithNonFunctionDeclarationNode_ReturnsFalse() {
        Node nonFunctionDeclarationNode = new Node(Token.NAME);
        
        assertFalse(NodeUtil.isFunctionDeclaration(nonFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WithHoistedFunctionDeclarationNode_ReturnsTrue() {
        Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.SCRIPT);
        grandParent.addChildToBack(parent);
        parent.addChildToBack(hoistedFunctionDeclarationNode);
        hoistedFunctionDeclarationNode.setParent(parent);
        parent.setParent(grandParent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_WithNonHoistedFunctionDeclarationNode_ReturnsFalse() {
        Node nonHoistedFunctionDeclarationNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        Node grandParent = new Node(Token.SCRIPT);
        grandParent.addChildToBack(parent);
        parent.addChildToBack(nonHoistedFunctionDeclarationNode);
        nonHoistedFunctionDeclarationNode.setParent(parent);
        parent.setParent(grandParent);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
    }
}