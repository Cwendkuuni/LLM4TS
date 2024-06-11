import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(new AbstractCompiler());
    }

    @Test
    public void testEnterScope() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        renameLabels.enterScope(nodeTraversal);
        assertTrue(renameLabels.namespaceStack.size() > 0);
    }

    @Test
    public void testExitScope() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        renameLabels.exitScope(nodeTraversal);
        assertTrue(renameLabels.namespaceStack.size() == 0);
    }

    @Test
    public void testShouldTraverse() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.BLOCK);
        assertTrue(renameLabels.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void testVisitLabel() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.BLOCK);
        renameLabels.visitLabel(node, parent);
        assertTrue(renameLabels.namespaceStack.peek().renameMap.size() == 0);
    }

    @Test
    public void testVisitBreakOrContinue() {
        Node node = new Node(Token.BREAK);
        renameLabels.visitBreakOrContinue(node);
        assertTrue(renameLabels.namespaceStack.peek().renameMap.size() == 0);
    }

    @Test
    public void testGetNameForId() {
        int id = 1;
        String name = renameLabels.getNameForId(id);
        assertNotNull(name);
    }

    @Test
    public void testGetLabelInfo() {
        String name = "test";
        RenameLabels.LabelInfo labelInfo = renameLabels.getLabelInfo(name);
        assertNull(labelInfo);
    }
}