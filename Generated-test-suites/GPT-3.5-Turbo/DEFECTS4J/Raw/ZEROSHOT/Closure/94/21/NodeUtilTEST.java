import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    private NodeUtil nodeUtil;

    @Before
    public void setUp() {
        nodeUtil = new NodeUtil();
    }

    @Test
    public void testIsForIn() {
        Node n = new Node(Token.FOR);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        assertTrue(nodeUtil.isForIn(n));
    }

    @Test
    public void testIsForIn_false() {
        Node n = new Node(Token.DO);
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));
        n.addChildToBack(new Node(Token.NAME));

        assertFalse(nodeUtil.isForIn(n));
    }

    @Test
    public void testIsStatement() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(nodeUtil.isStatement(n));
    }

    @Test
    public void testIsStatement_false() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertFalse(nodeUtil.isStatement(n));
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(nodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertFalse(nodeUtil.isFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        parent.addChildToBack(n);
        n.setParent(parent);

        assertTrue(nodeUtil.isHoistedFunctionDeclaration(n));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_false() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(new Node(Token.NAME));
        parent.addChildToBack(n);
        n.setParent(parent);

        assertFalse(nodeUtil.isHoistedFunctionDeclaration(n));
    }
}