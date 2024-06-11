import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForIn_typicalCase() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isForIn(node));
    }
    
    @Test
    public void testIsForIn_missingChild() {
        Node node = new Node(Token.FOR);
        node.addChildToFront(new Node(Token.EMPTY));
        node.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatement_scriptParent() {
        Node script = new Node(Token.SCRIPT);
        Node statement = new Node(Token.EMPTY);
        script.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(statement));
    }

    @Test
    public void testIsStatement_blockParent() {
        Node block = new Node(Token.BLOCK);
        Node statement = new Node(Token.EMPTY);
        block.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(statement));
    }

    @Test
    public void testIsStatement_labelParent() {
        Node label = new Node(Token.LABEL);
        Node statement = new Node(Token.EMPTY);
        label.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isStatement(statement));
    }

    @Test
    public void testIsStatement_otherParent() {
        Node parent = new Node(Token.TYPEOF);
        Node statement = new Node(Token.EMPTY);
        parent.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isStatement(statement));
    }

    @Test
    public void testIsFunctionDeclaration_typicalCase() {
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);
        function.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isFunctionDeclaration(function));
    }

    @Test
    public void testIsFunctionDeclaration_notFunction() {
        Node statement = new Node(Token.EMPTY);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isFunctionDeclaration(statement));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_typicalCase() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);

        script.addChildToBack(function);
        function.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertTrue(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notHoisted() {
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);

        function.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(function));
    }

    @Test
    public void testIsHoistedFunctionDeclaration_notFunctionDeclaration() {
        Node script = new Node(Token.SCRIPT);
        Node function = new Node(Token.FUNCTION);
        Node statement = new Node(Token.EMPTY);

        script.addChildToBack(statement);
        statement.addChildToBack(new Node(Token.EMPTY));

        assertFalse(NodeUtil.isHoistedFunctionDeclaration(statement));
    }

}