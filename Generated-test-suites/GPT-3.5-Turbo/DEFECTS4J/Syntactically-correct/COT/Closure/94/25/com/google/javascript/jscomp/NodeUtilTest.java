
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        boolean result = NodeUtil.isForIn(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_false() {
        Node n = new Node(Token.FOR);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node scriptNode = new Node(Token.SCRIPT);
        Node n = new Node(Token.NAME);
        scriptNode.addChildToBack(n);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_false() {
        Node n = new Node(Token.NAME);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node parentNode = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parentNode.addChildToBack(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node grandParentNode = new Node(Token.SCRIPT);
        Node parentNode = new Node(Token.FUNCTION);
        Node n = new Node(Token.FUNCTION);
        grandParentNode.addChildToBack(parentNode);
        parentNode.addChildToBack(n);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

}
