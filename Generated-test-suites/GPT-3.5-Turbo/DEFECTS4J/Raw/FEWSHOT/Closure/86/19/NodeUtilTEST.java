import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {
    @Test
    public void testIsForIn() {
        Node node1 = new Node(Token.FOR);
        Node node2 = new Node(Token.FOR);
        node2.addChildToFront(new Node(Token.ADD));
        node2.addChildToBack(new Node(Token.SUB));
        node2.addChildToBack(new Node(Token.MULT));

        Assert.assertTrue(NodeUtil.isForIn(node1));
        Assert.assertFalse(NodeUtil.isForIn(node2));
    }

    @Test
    public void testIsStatement() {
        Node node1 = new Node(Token.SCRIPT);
        Node node2 = new Node(Token.EXPR_RESULT);
        Node node3 = new Node(Token.IF);

        Assert.assertTrue(NodeUtil.isStatement(node1));
        Assert.assertTrue(NodeUtil.isStatement(node2));
        Assert.assertFalse(NodeUtil.isStatement(node3));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        Node node2 = new Node(Token.EXPR_RESULT);
        node2.addChildToFront(new Node(Token.FUNCTION));

        Assert.assertTrue(NodeUtil.isFunctionDeclaration(node1));
        Assert.assertFalse(NodeUtil.isFunctionDeclaration(node2));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node1 = new Node(Token.FUNCTION);
        node1.setParent(new Node(Token.SCRIPT));
        Node node2 = new Node(Token.EXPR_RESULT);
        node2.addChildToFront(new Node(Token.FUNCTION));
        Node node3 = new Node(Token.FUNCTION);
        node3.setParent(new Node(Token.FUNCTION));

        Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(node1));
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node2));
        Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(node3));
    }
}