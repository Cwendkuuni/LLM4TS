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
        Node n = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.setParent(parent);

        assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclarationUnderFunction() {
        Node n = new Node(Token.FUNCTION);
        Node parentFunc = new Node(Token.FUNCTION);
        Node parentScript = new Node(Token.SCRIPT);
        parentFunc.addChildToFront(n);
        parentScript.addChildToFront(parentFunc);
        n.setParent(parentFunc);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }

}