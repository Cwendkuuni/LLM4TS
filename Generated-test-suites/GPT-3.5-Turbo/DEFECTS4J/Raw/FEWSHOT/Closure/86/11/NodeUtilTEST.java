import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n1 = new Node(Token.FOR);
        n1.addChildToBack(new Node(Token.FOR_IN));
        n1.addChildToBack(new Node(Token.NAME));
        n1.addChildToBack(new Node(Token.STRING));

        Node n2 = new Node(Token.FOR);
        n2.addChildToBack(new Node(Token.NAME));
        n2.addChildToBack(new Node(Token.IN));
        n2.addChildToBack(new Node(Token.STRING));

        assertEquals(true, NodeUtil.isForIn(n1));
        assertEquals(false, NodeUtil.isForIn(n2));
    }

    @Test
    public void testIsStatement() {
        Node n1 = new Node(Token.SCRIPT);
        Node n2 = new Node(Token.BLOCK);
        Node n3 = new Node(Token.LABEL);
        Node n4 = new Node(Token.STRING);

        Node n5 = new Node(Token.FUNCTION);
        n5.addChildToBack(new Node(Token.NAME));

        assertEquals(true, NodeUtil.isStatement(n1));
        assertEquals(true, NodeUtil.isStatement(n2));
        assertEquals(true, NodeUtil.isStatement(n3));
        assertEquals(true, NodeUtil.isStatement(n4));
        assertEquals(false, NodeUtil.isStatement(n5));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.addChildToBack(new Node(Token.NAME));
        
        Node n2 = new Node(Token.FUNCTION);
        n2.addChildToBack(new Node(Token.NAME));
        n2.addChildToBack(new Node(Token.EXPR_RESULT));

        assertEquals(true, NodeUtil.isFunctionDeclaration(n1));
        assertEquals(false, NodeUtil.isFunctionDeclaration(n2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n1 = new Node(Token.FUNCTION);
        n1.addChildToBack(new Node(Token.NAME));
        n1.getParent().setType(Token.SCRIPT);

        Node n2 = new Node(Token.FUNCTION);
        n2.addChildToBack(new Node(Token.NAME));
        n2.getParent().setType(Token.BLOCK);

        Node n3 = new Node(Token.FUNCTION);
        n3.addChildToBack(new Node(Token.NAME));
        n3.getParent().setType(Token.FUNCTION);

        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(n1));
        assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(n2));
        assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(n3));
    }

}