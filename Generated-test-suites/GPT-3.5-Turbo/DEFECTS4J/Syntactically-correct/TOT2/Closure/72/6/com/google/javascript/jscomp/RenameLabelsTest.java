import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import com.google.javascript.jscomp.RenameLabels.LabelInfo;
import com.google.javascript.jscomp.RenameLabels.LabelNamespace;

public class RenameLabelsTest {

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(new AbstractCompiler());
    }

    @Test
    public void testProcessLabelsEnterScope() {
        ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope();
        assertNotNull(processLabels.namespaceStack.peek());
    }

    @Test
    public void testProcessLabelsExitScope() {
        ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope();
        processLabels.exitScope();
        assertTrue(processLabels.namespaceStack.isEmpty());
    }

    @Test
    public void testProcessLabelsShouldTraverse() {
        ProcessLabels processLabels = renameLabels.new ProcessLabels();
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.STRING);
        nameNode.setString("label");
        labelNode.addChildToBack(nameNode);
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node parent = new Node(Token.FUNCTION);
        assertTrue(processLabels.shouldTraverse(nodeTraversal, labelNode, parent));
    }

    @Test
    public void testProcessLabelsVisitBreakOrContinue() {
        ProcessLabels processLabels = renameLabels.new ProcessLabels();
        Node breakNode = new Node(Token.BREAK);
        Node nameNode = new Node(Token.STRING);
        nameNode.setString("label");
        breakNode.addChildToBack(nameNode);
        processLabels.names.add("label");
        processLabels.namespaceStack.push(new LabelNamespace());

        processLabels.visitBreakOrContinue(breakNode);
        assertEquals("label", nameNode.getString());
    }

    @Test
    public void testProcessLabelsGetNameForId() {
        ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        String result1 = processLabels.getNameForId(1);
        String result2 = processLabels.getNameForId(2);
        assertEquals("a", result1);
        assertEquals("b", result2);
    }

    @Test
    public void testProcessLabelsGetLabelInfo() {
        ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(new LabelNamespace());
        LabelInfo labelInfo = new LabelInfo(1);
        processLabels.namespaceStack.peek().renameMap.put("label", labelInfo);
        LabelInfo result = processLabels.getLabelInfo("label");
        assertEquals(labelInfo, result);
    }

}