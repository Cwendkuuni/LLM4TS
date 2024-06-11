import static org.junit.Assert.*;
import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testRenameLabels_constructor() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        assertNotNull(renameLabels);
    }

    @Test
    public void testProcessLabels_enterScope() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        processLabels.enterScope(new NodeTraversal(new Compiler()));
        assertFalse(processLabels.namespaceStack.isEmpty());
    }

    @Test
    public void testProcessLabels_exitScope() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        processLabels.enterScope(new NodeTraversal(new Compiler()));
        processLabels.exitScope(new NodeTraversal(new Compiler()));
        assertTrue(processLabels.namespaceStack.isEmpty());
    }

    @Test
    public void testProcessLabels_shouldTraverse() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        Node node = new Node(Token.LABEL);
        NodeTraversal nodeTraversal = new NodeTraversal(new Compiler());
        Node parent = new Node(Token.BLOCK);
        assertTrue(processLabels.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void testProcessLabels_visit_Break() {
        Node node = new Node(Token.BREAK);
        NodeTraversal nodeTraversal = new NodeTraversal(new Compiler());
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        processLabels.namespaceStack.push(new LabelNamespace());
        processLabels.names.add("a");
        Node nameNode = new Node(Token.NAME, "a");
        node.addChildToBack(nameNode);
        processLabels.visit(nodeTraversal, node, null);
        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testProcessLabels_visit_Label() {
        Node node = new Node(Token.LABEL);
        NodeTraversal nodeTraversal = new NodeTraversal(new Compiler());
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        processLabels.namespaceStack.push(new LabelNamespace());
        processLabels.names.add("a");
        Node nameNode = new Node(Token.NAME, "label");
        node.addChildToBack(nameNode);
        processLabels.namespaceStack.peek().renameMap.put("label", new LabelInfo(1));
        processLabels.visit(nodeTraversal, node, null);
        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testGetLabelInfo() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        processLabels.namespaceStack.push(new LabelNamespace());
        processLabels.namespaceStack.peek().renameMap.put("test", new LabelInfo(1));
        LabelInfo info = processLabels.getLabelInfo("test");
        assertNotNull(info);
        assertEquals(1, info.id);
    }

    @Test
    public void testGetNameForId() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        processLabels.names.add("c");
        assertEquals("a", processLabels.getNameForId(1));
        assertEquals("b", processLabels.getNameForId(2));
        assertEquals("c", processLabels.getNameForId(3));
    }
}