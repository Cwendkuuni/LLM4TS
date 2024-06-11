import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR, new Node(), new Node(), new Node());
        assertTrue(NodeUtil.isForIn(node));
        
        node = new Node(Token.FUNCTION, new Node(), new Node(), new Node());
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.LABEL, parent);
        
        assertTrue(NodeUtil.isStatement(node));
        
        node = new Node(Token.FUNCTION);
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        assertTrue(NodeUtil.isFunctionDeclaration(node));
        
        node = new Node(Token.VAR);
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node functionParent = new Node(Token.FUNCTION);
        Node scriptParent = new Node(Token.SCRIPT, functionParent);
        Node node = new Node(Token.FUNCTION, scriptParent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
        
        scriptParent = new Node(Token.SCRIPT);
        node = new Node(Token.FUNCTION, scriptParent);
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}