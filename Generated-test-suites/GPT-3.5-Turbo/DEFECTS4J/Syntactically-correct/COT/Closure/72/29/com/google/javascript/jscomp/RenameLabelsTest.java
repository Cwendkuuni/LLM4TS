

import static org.junit.Assert.*;
import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testProcessLabels() {
        // Test the processLabels() method
        RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

        // Set up test data
        String[] names = { "a", "b", "c" };

        // Add labels to the namespace stack
        processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());
        processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());
        processLabels.namespaceStack.push(new RenameLabels.LabelNamespace());

        // Add names to the labels
        for (String name : names) {
            RenameLabels.LabelInfo li = new RenameLabels.LabelInfo(processLabels.namespaceStack.size() + 1);
            processLabels.namespaceStack.peek().renameMap.put(name, li);
        }

        // Test getNameForId() method
        assertEquals("a", processLabels.getNameForId(1));
        assertEquals("b", processLabels.getNameForId(2));
        assertEquals("c", processLabels.getNameForId(3));

        // Test getLabelInfo() method
        RenameLabels.LabelInfo liA = processLabels.getLabelInfo("a");
        RenameLabels.LabelInfo liB = processLabels.getLabelInfo("b");
        RenameLabels.LabelInfo liC = processLabels.getLabelInfo("c");
        assertNotNull(liA);
        assertNotNull(liB);
        assertNotNull(liC);
        assertFalse(liA.referenced);
        assertFalse(liB.referenced);
        assertFalse(liC.referenced);
        assertEquals(1, liA.id);
        assertEquals(2, liB.id);
        assertEquals(3, liC.id);
    }

    @Test
    public void testVisitBreakOrContinue() {
        // Test the visitBreakOrContinue() method
        RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

        // Set up test data
        RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();
        processLabels.namespaceStack.push(namespace);

        // Add labels to the namespace
        namespace.renameMap.put("a", new RenameLabels.LabelInfo(1));
        namespace.renameMap.put("b", new RenameLabels.LabelInfo(2));

        // Create break and continue nodes
        Node breakNodeA = new Node(Token.BREAK);
        Node breakNodeB = new Node(Token.BREAK);
        Node continueNodeA = new Node(Token.CONTINUE);
        Node continueNodeB = new Node(Token.CONTINUE);

        // Set the label names for break and continue nodes
        breakNodeA.addChildToFront(new Node(Token.STRING, "a"));
        breakNodeB.addChildToFront(new Node(Token.STRING, "b"));
        continueNodeA.addChildToFront(new Node(Token.STRING, "a"));
        continueNodeB.addChildToFront(new Node(Token.STRING, "b"));

        // Perform the visitBreakOrContinue() method
        processLabels.visitBreakOrContinue(breakNodeA);
        processLabels.visitBreakOrContinue(breakNodeB);
        processLabels.visitBreakOrContinue(continueNodeA);
        processLabels.visitBreakOrContinue(continueNodeB);

        // Check if the names have been replaced with the short names
        assertEquals("a", breakNodeA.getFirstChild().getString());
        assertEquals("b", breakNodeB.getFirstChild().getString());
        assertEquals("a", continueNodeA.getFirstChild().getString());
        assertEquals("b", continueNodeB.getFirstChild().getString());
    }

    @Test
    public void testVisitLabel() {
        // Test the visitLabel() method
        RenameLabels.ProcessLabels processLabels = new RenameLabels().new ProcessLabels();

        // Set up test data
        RenameLabels.LabelNamespace namespace = new RenameLabels.LabelNamespace();
        processLabels.namespaceStack.push(namespace);

        // Add labels to the namespace
        namespace.renameMap.put("a", new RenameLabels.LabelInfo(1));
        namespace.renameMap.put("b", new RenameLabels.LabelInfo(2));

        // Create label nodes
        Node labelNodeA = new Node(Token.LABEL);
        Node labelNodeB = new Node(Token.LABEL);

        // Set the label names for label nodes
        labelNodeA.addChildToFront(new Node(Token.STRING, "a"));
        labelNodeB.addChildToFront(new Node(Token.STRING, "b"));

        // Perform the visitLabel() method
        processLabels.visitLabel(labelNodeA, null);
        processLabels.visitLabel(labelNodeB, null);

        // Check if the names have been replaced with the short names
        assertEquals("a", labelNodeA.getFirstChild().getString());
        assertEquals("b", labelNodeB.getFirstChild().getString());
        assertFalse(namespace.renameMap.containsKey("a"));
        assertFalse(namespace.renameMap.containsKey("b"));
    }
}
