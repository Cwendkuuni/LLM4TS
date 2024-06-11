
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.Node;

public class NodeUtilTest {

    @Test
    public void testIsForIn() {
        Node node = new Node(Token.FOR);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        boolean result = NodeUtil.isForIn(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotForIn() {
        Node node = new Node(Token.IF);
        Node child1 = new Node();
        Node child2 = new Node();
        Node child3 = new Node();
        node.addChildToBack(child1);
        node.addChildToBack(child2);
        node.addChildToBack(child3);

        boolean result = NodeUtil.isForIn(node);

        assertFalse(result);
    }

    @Test
    public void testIsStatement() {
        Node node = new Node(Token.BLOCK);

        boolean result = NodeUtil.isStatement(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotStatement() {
        Node node = new Node(Token.IF);

        boolean result = NodeUtil.isStatement(node);

        assertFalse(result);
    }

    @Test
    public void testIsFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(NodeUtil.IS_FUNCTION_DECLARATION, true);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        node.putBooleanProp(NodeUtil.IS_FUNCTION_DECLARATION, false);

        boolean result = NodeUtil.isFunctionDeclaration(node);

        assertFalse(result);
    }

    @Test
    public void testIsHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.SCRIPT);
        node.putBooleanProp(NodeUtil.IS_FUNCTION_DECLARATION, true);
        node.addChildToBack(parent1);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertTrue(result);
    }

    @Test
    public void testIsNotHoistedFunctionDeclaration() {
        Node node = new Node(Token.FUNCTION);
        Node parent1 = new Node(Token.VAR);
        Node parent2 = new Node(Token.FUNCTION);
        node.putBooleanProp(NodeUtil.IS_FUNCTION_DECLARATION, true);
        node.addChildToBack(parent1);
        node.addChildToBack(parent2);

        boolean result = NodeUtil.isHoistedFunctionDeclaration(node);

        assertFalse(result);
    }
}
