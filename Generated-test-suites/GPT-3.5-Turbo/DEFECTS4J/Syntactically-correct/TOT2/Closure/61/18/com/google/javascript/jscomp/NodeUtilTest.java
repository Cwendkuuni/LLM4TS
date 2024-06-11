import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForInFalse() {
        Node node = new Node(Token.FOR);
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node blockNode = new Node(Token.BLOCK);
        Node scriptNode = new Node(Token.SCRIPT);
        
        assertTrue(NodeUtil.isStatement(blockNode));
        assertTrue(NodeUtil.isStatement(scriptNode));
    }

    @Test
    public void testIsStatementFalse() {
        Node labelNode = new Node(Token.LABEL);
        
        assertFalse(NodeUtil.isStatement(labelNode));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(functionNode);
        functionNode.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsFunctionDeclarationFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToFront(functionNode);
        functionNode.setParent(parent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

    @Test
    public void testIsHoistedFunctionDeclarationFalse() {
        Node functionNode = new Node(Token.FUNCTION);
        
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }
}