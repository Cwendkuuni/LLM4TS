import org.junit.Test;

import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typical() {
        Node node = new Node(Token.FOR).useSourceInfoIfMissingFromForTree("");
        node.addChildToBack(new Node(Token.NAME).useSourceInfoIfMissingFromForTree(""));
        node.addChildToBack(new Node(Token.EMPTY).useSourceInfoIfMissingFromForTree(""));
        node.addChildToBack(new Node(Token.EMPTY).useSourceInfoIfMissingFromForTree(""));

        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForIn_emptyStatement() {
        Node node = new Node(Token.FOR).useSourceInfoIfMissingFromForTree("");
        node.addChildToBack(new Node(Token.NAME).useSourceInfoIfMissingFromForTree(""));
        node.addChildToBack(new Node(Token.EMPTY).useSourceInfoIfMissingFromForTree(""));
        node.addChildToBack(new Node(Token.EMPTY).useSourceInfoIfMissingFromForTree(""));
        node.addChildToBack(new Node(Token.EMPTY).useSourceInfoIfMissingFromForTree(""));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_script() {
        Node node = new Node(Token.STRING);

        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_block() {
        Node node = new Node(Token.STRING);

        node.setParent(new Node(Token.BLOCK));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_label() {
        Node node = new Node(Token.STRING);

        node.setParent(new Node(Token.LABEL));
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatement_other() {
        Node node = new Node(Token.STRING);

        node.setParent(new Node(Token.NAME));
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclaration_functionDeclaration() {
        Node node = new Node(Token.FUNCTION);

        node.setParent(new Node(Token.SCRIPT));
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclaration_notDeclaration() {
        Node node = new Node(Token.FUNCTION);

        node.setParent(new Node(Token.NAME));
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_hoistedFunction() {
        Node node = new Node(Token.FUNCTION);

        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);

        function.addChildToFront(node);
        script.addChildToFront(function);

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node node = new Node(Token.FUNCTION);

        Node script = new Node(Token.SCRIPT);
        Node expression = new Node(Token.FUNCTION);

        expression.addChildToFront(node);
        script.addChildToFront(expression);

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }
}