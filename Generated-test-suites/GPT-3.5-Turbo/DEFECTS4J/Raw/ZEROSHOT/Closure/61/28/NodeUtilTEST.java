import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.BLOCK));
        n.addChildToBack(new Node(Token.EXPR_RESULT));
        n.addChildToBack(new Node(Token.NAME));
        org.junit.Assert.assertTrue(NodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.SCRIPT);
        org.junit.Assert.assertTrue(NodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        org.junit.Assert.assertTrue(NodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node parent = new Node(Token.SCRIPT);
        Node grandParent = new Node(Token.FUNCTION);
        Node n = new Node(Token.FUNCTION);
        n.setParent(parent);
        parent.addChildToBack(n);
        grandParent.addChildToBack(parent);
        org.junit.Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(n));
    }
}