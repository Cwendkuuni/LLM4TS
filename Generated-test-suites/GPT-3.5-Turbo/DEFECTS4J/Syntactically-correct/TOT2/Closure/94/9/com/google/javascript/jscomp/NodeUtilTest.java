import static org.junit.Assert.*;

import org.junit.Test;
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
    public void testIsStatement() {
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        node.setParent(parent);
        
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        node.setParent(parent);
        parent.setParent(grandParent);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test(expected = NullPointerException.class)
    public void testIsStatementWithNullParent() {
        Node node = new Node(Token.EMPTY);
        
        NodeUtil.isStatement(node);
    }

}