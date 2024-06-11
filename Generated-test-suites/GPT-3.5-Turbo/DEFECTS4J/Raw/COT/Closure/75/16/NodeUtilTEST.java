import org.junit.Test;
import static org.junit.Assert.*;

public class NodeUtilTest {

    @Test
    public void testIsForInWithForInLoop() {
        Node node = createForInNode();
        assertTrue(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsForInWithoutForInLoop() {
        Node node = createNonForInNode();
        assertFalse(NodeUtil.isForIn(node));
    }

    @Test
    public void testIsStatementWithStatementNode() {
        Node node = createStatementNode();
        assertTrue(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsStatementWithoutStatementNode() {
        Node node = createNonStatementNode();
        assertFalse(NodeUtil.isStatement(node));
    }

    @Test
    public void testIsFunctionDeclarationWithFunctionDeclarationNode() {
        Node node = createFunctionDeclarationNode();
        assertTrue(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsFunctionDeclarationWithoutFunctionDeclarationNode() {
        Node node = createNonFunctionDeclarationNode();
        assertFalse(NodeUtil.isFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithHoistedFunctionDeclarationNode() {
        Node node = createHoistedFunctionDeclarationNode();
        assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    @Test
    public void testIsHoistedFunctionDeclarationWithoutHoistedFunctionDeclarationNode() {
        Node node = createNonHoistedFunctionDeclarationNode();
        assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
    }

    private Node createForInNode() {
        // TODO: Create a node that represents a FOR-IN loop
        return null;
    }

    private Node createNonForInNode() {
        // TODO: Create a node that does not represent a FOR-IN loop
        return null;
    }

    private Node createStatementNode() {
        // TODO: Create a node that is used as a statement
        return null;
    }

    private Node createNonStatementNode() {
        // TODO: Create a node that is not used as a statement
        return null;
    }

    private Node createFunctionDeclarationNode() {
        // TODO: Create a node that represents a function declaration
        return null;
    }

    private Node createNonFunctionDeclarationNode() {
        // TODO: Create a node that does not represent a function declaration
        return null;
    }

    private Node createHoistedFunctionDeclarationNode() {
        // TODO: Create a node that represents a hoisted function declaration
        return null;
    }

    private Node createNonHoistedFunctionDeclarationNode() {
        // TODO: Create a node that does not represent a hoisted function declaration
        return null;
    }
}