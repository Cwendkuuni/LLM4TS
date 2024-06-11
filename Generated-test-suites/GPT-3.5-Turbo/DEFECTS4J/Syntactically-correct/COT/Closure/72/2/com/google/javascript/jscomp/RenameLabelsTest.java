
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.base.Supplier;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

    private RenameLabels.DefaultNameSupplier nameSupplier;
    private RenameLabels.ProcessLabels processLabels;
    private RenameLabels.LabelNamespace labelNamespace;

    @Before
    public void setup() {
        nameSupplier = new RenameLabels.DefaultNameSupplier();
        processLabels = new RenameLabels().new ProcessLabels();
        labelNamespace = new RenameLabels().new LabelNamespace();
    }

    @Test
    public void testEnterScope() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);

        processLabels.enterScope(nodeTraversal);

        assertEquals(2, processLabels.namespaceStack.size());
    }

    @Test
    public void testExitScope() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);

        processLabels.exitScope(nodeTraversal);

        assertEquals(0, processLabels.namespaceStack.size());
    }

    @Test
    public void testShouldTraverse_LabelNode() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.BLOCK);

        boolean result = processLabels.shouldTraverse(nodeTraversal, node, parent);

        assertEquals(true, result);
        assertEquals(1, processLabels.namespaceStack.size());
        assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
    }

    @Test
    public void testVisitBreakOrContinue_NamedBreakOrContinue() {
        Node node = new Node(Token.BREAK);
        node.addChildToFront(new Node(Token.STRING, "label"));

        processLabels.visitBreakOrContinue(node);

        assertEquals("a", node.getFirstChild().getString());
    }

    @Test
    public void testVisitBreakOrContinue_UnnamedBreakOrContinue() {
        Node node = new Node(Token.BREAK);

        processLabels.visitBreakOrContinue(node);

        assertEquals(null, node.getFirstChild());
    }

    @Test
    public void testVisitLabel_ReferencedLabel() {
        Node node = new Node(Token.LABEL);
        Node nameNode = new Node(Token.STRING, "label");
        node.addChildToFront(nameNode);
        processLabels.namespaceStack.push(labelNamespace);

        labelNamespace.renameMap.put("label", new RenameLabels.LabelInfo(1));

        processLabels.visitLabel(node, null);

        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testVisitLabel_UnreferencedLabel() {
        Node node = new Node(Token.LABEL);
        Node nameNode = new Node(Token.STRING, "label");
        node.addChildToFront(nameNode);
        processLabels.namespaceStack.push(labelNamespace);

        labelNamespace.renameMap.put("label", new RenameLabels.LabelInfo(1));
        
        processLabels.visitLabel(node, null);

        assertEquals(null, nameNode.getNext());
    }

    @Test
    public void testGetNameForId() {
        processLabels.names.add("a");
        processLabels.names.add("b");

        String result = processLabels.getNameForId(2);

        assertEquals("b", result);
    }

    @Test
    public void testGetLabelInfo() {
        processLabels.namespaceStack.push(labelNamespace);
        labelNamespace.renameMap.put("label", new RenameLabels.LabelInfo(1));

        RenameLabels.LabelInfo result = processLabels.getLabelInfo("label");

        assertEquals(1, result.id);
    }
}
