import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {
    
    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        Node child2 = new Node(Token.TRUE);
        Node child3 = new Node(Token.FALSE);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        n.addChildToBack(child3);
        
        assertTrue(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsForIn_NotForInNode() {
        Node n = new Node(Token.IF);
        Node child1 = new Node(Token.TRUE);
        Node child2 = new Node(Token.STRING);
        n.addChildToBack(child1);
        n.addChildToBack(child2);
        
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsForIn_IncorrectChildCount() {
        Node n = new Node(Token.FOR);
        Node child1 = new Node(Token.VAR);
        n.addChildToBack(child1);
        
        assertFalse(NodeUtil.isForIn(n));
    }
    
    @Test
    public void testIsStatement_ScriptParent() {
        Node script = new Node(Token.SCRIPT);
        Node n = new Node(Token.STRING);
        script.addChildToBack(n);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsStatement_BlockParent() {
        Node block = new Node(Token.BLOCK);
        Node n = new Node(Token.STRING);
        block.addChildToBack(n);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsStatement_LabelParent() {
        Node label = new Node(Token.LABEL);
        Node n = new Node(Token.STRING);
        label.addChildToBack(n);
        
        assertTrue(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsStatement_NotStatementParent() {
        Node parent = new Node(Token.NAME);
        Node n = new Node(Token.STRING);
        parent.addChildToBack(n);
        
        assertFalse(NodeUtil.isStatement(n));
    }
    
    @Test
    public void testIsFunctionDeclaration_FunctionDeclarationNode() {
        Node function = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(function));
    }
    
    @Test
    public void testIsFunctionDeclaration_FunctionExpressionNode() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(function);
        assertFalse(NodeUtil.isFunctionDeclaration(function));
    }
    
    @Test
    public void testIsFunctionDeclaration_NotFunctionNode() {
        Node n = new Node(Token.IF);
        assertFalse(NodeUtil.isFunctionDeclaration(n));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_HoistedFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        parent1.addChildToBack(function);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
        
        Node parent2 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.SCRIPT);
        parent3.addChildToBack(parent2);
        parent2.addChildToBack(function);
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(function);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_NotFunctionDeclaration() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(function);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }
    
    @Test
    public void testIsHoistedFunctionDeclaration_NotHoistedFunctionDeclaration_NotFunctionDeclaration() {
        Node n = new Node(Token.IF);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}