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
    public void testProcessLabels() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        assertNotNull(processLabels.namespaceStack);
        assertNotNull(processLabels.names);
    }

    @Test
    public void testEnterScope() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        processLabels.enterScope(nodeTraversal);
        assertEquals(2, processLabels.namespaceStack.size());
    }

    @Test
    public void testExitScope() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        processLabels.enterScope(nodeTraversal);
        processLabels.exitScope(nodeTraversal);
        assertEquals(1, processLabels.namespaceStack.size());
    }

    @Test
    public void testShouldTraverse_Label() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node();
        boolean result = processLabels.shouldTraverse(nodeTraversal, node, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_NotLabel() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.STRING);
        Node parent = new Node();
        boolean result = processLabels.shouldTraverse(nodeTraversal, node, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_Label() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node();
        processLabels.visit(nodeTraversal, node, parent);
        // Assert the changes made by visitLabel()
    }

    @Test
    public void testVisit_Break() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.BREAK);
        Node parent = new Node();
        processLabels.visit(nodeTraversal, node, parent);
        // Assert the changes made by visitBreakOrContinue()
    }

    @Test
    public void testVisit_Continue() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.CONTINUE);
        Node parent = new Node();
        processLabels.visit(nodeTraversal, node, parent);
        // Assert the changes made by visitBreakOrContinue()
    }

    @Test
    public void testGetNameForId() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        String result = processLabels.getNameForId(1);
        assertEquals("a", result);
    }

    @Test
    public void testGetLabelInfo() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());
        String name = "label1";
        processLabels.namespaceStack.peek().renameMap.put(name, new RenameLabels.LabelInfo(1));
        RenameLabels.LabelInfo result = processLabels.getLabelInfo(name);
        assertNotNull(result);
    }
}