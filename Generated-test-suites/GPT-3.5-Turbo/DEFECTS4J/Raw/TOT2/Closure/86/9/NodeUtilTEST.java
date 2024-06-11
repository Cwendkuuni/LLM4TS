import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        n.addChildToBack(new Node(Token.BLOCK));
        
        assertTrue(NodeUtil.isStatement(n.getChildAtIndex(0)));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        n.addChildToBack(new Node(Token.NAME));
        
        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChildToBack(new Node(Token.NAME));
        scriptNode.addChildToBack(functionNode);
        
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
    }

}