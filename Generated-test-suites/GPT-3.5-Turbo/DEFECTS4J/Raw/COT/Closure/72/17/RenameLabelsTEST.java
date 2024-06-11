import org.junit.Assert;
import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testProcessLabels() {
        // Test case for ProcessLabels class
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();

        // Test enterScope method
        // Add a new namespace to the stack
        processLabels.enterScope(null);
        assertEquals(1, processLabels.namespaceStack.size());

        // Test exitScope method
        // Remove the current namespace from the stack
        processLabels.exitScope(null);
        assertEquals(0, processLabels.namespaceStack.size());

        // Test shouldTraverse method
        // Enter a new scope and add a label to the renameMap
        processLabels.enterScope(null);
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToFront(nameNode);
        NodeTraversal nodeTraversalCopy = nodeTraversal.clone();
        boolean shouldTraverse = processLabels.shouldTraverse(nodeTraversalCopy, labelNode, null);
        assertTrue(shouldTraverse);
        assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());

        // Test getNameForId method
        // Add a new name to the list and get the name for id=1 (the first name)
        processLabels.names.add("name1");
        String nameForId = processLabels.getNameForId(1);
        assertEquals("name1", nameForId);

        // Test getLabelInfo method
        // Add a label to the renameMap and get its info
        processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));
        RenameLabels.LabelInfo labelInfo = processLabels.getLabelInfo("labelName");
        assertNotNull(labelInfo);

        // Test visit method for LABEL node
        // Rename a referenced label
        Node parent = new Node(Token.SCRIPT);
        NodeVisitor parentVisitor = new NodeVisitor(null, null) {
            @Override
            public void visit(Node n) {
                if (n.isLabel()) {
                    processLabels.visitLabel(n, parent);
                }
                traverseChildren(n);
            }
        };
        Node labelNodeCopy = labelNode.cloneTree();
        parentVisitor.visit(labelNodeCopy);
        assertEquals("name1", labelNodeCopy.getFirstChild().getString());

        // Test visit method for BREAK node
        // Rename a referenced label in a break statement
        Node breakNode = new Node(Token.BREAK, new Node(Token.NAME, "labelName"));
        NodeVisitor breakVisitor = new NodeVisitor(null, null) {
            @Override
            public void visit(Node n) {
                if (n.isBreak() || n.isContinue()) {
                    processLabels.visitBreakOrContinue(n);
                }
                traverseChildren(n);
            }
        };
        Node breakNodeCopy = breakNode.cloneTree();
        breakVisitor.visit(breakNodeCopy);
        assertEquals("name1", breakNodeCopy.getFirstChild().getString());
        assertTrue(processLabels.getLabelInfo("labelName").referenced);

        // Test visit method for CONTINUE node
        // Rename a referenced label in a continue statement
        Node continueNode = new Node(Token.CONTINUE, new Node(Token.NAME, "labelName"));
        NodeVisitor continueVisitor = new NodeVisitor(null, null) {
            @Override
            public void visit(Node n) {
                if (n.isBreak() || n.isContinue()) {
                    processLabels.visitBreakOrContinue(n);
                }
                traverseChildren(n);
            }
        };
        Node continueNodeCopy = continueNode.cloneTree();
        continueVisitor.visit(continueNodeCopy);
        assertEquals("name1", continueNodeCopy.getFirstChild().getString());
        assertTrue(processLabels.getLabelInfo("labelName").referenced);
    }
}