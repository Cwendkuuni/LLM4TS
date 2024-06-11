import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

    // Test isForIn method

    @Test
    public void testIsForIn_typicalCase() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsForIn_incorrectNodeType() {
        Node n = new Node(Token.EMPTY);

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsForIn_incorrectChildCount() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.EMPTY));
        n.addChildToBack(new Node(Token.EMPTY));

        boolean result = NodeUtil.isForIn(n);

        Assert.assertFalse(result);
    }


    // Test isStatement method

    @Test
    public void testIsStatement_typicalCase() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsStatement_incorrectParent() {
        Node parent = new Node(Token.IF);
        Node n = new Node(Token.EMPTY);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsStatement_nullParent() {
        Node n = new Node(Token.EMPTY);

        boolean result = NodeUtil.isStatement(n);

        Assert.assertFalse(result);
    }


    // Test isFunctionDeclaration method

    @Test
    public void testIsFunctionDeclaration_typicalCase() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsFunctionDeclaration_incorrectNodeType() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FOR);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration_incorrectStatement() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isFunctionDeclaration(n);

        Assert.assertFalse(result);
    }


    // Test isHoistedFunctionDeclaration method

    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.FUNCTION);
        grandParent.addChildToBack(parent);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertTrue(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node parent = new Node(Token.SCRIPT);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration_incorrectNodeType() {
        Node grandParent = new Node(Token.SCRIPT);
        Node parent = new Node(Token.IF);
        grandParent.addChildToBack(parent);
        Node n = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(n);

        Assert.assertFalse(result);
    }

}