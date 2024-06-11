import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testProcessLabels() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        Node node = new Node(Token.LABEL);
        node.addChildToFront(new Node(Token.NAME, "a"));
        nodeTraversal.enterScope(node);

        processLabels.shouldTraverse(nodeTraversal, node, null);
        LabelNamespace labelNamespace = processLabels.namespaceStack.peek();
        Assert.assertTrue(labelNamespace.renameMap.containsKey("a"));

        processLabels.exitScope(nodeTraversal);
        labelNamespace = processLabels.namespaceStack.peek();
        Assert.assertTrue(!labelNamespace.renameMap.containsKey("a"));
    }

    @Test
    public void testVisitLabel() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

        Node node = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "a");
        node.addChildToFront(nameNode);
        processLabels.visitLabel(node, null);

        Assert.assertTrue(!processLabels.getNamespaceStack().peek().renameMap.containsKey("a"));
    }

    @Test
    public void testVisitBreakOrContinue() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();

        Node node = new Node(Token.BREAK);
        Node nameNode = new Node(Token.NAME, "a");
        node.addChildToFront(nameNode);
        processLabels.getNamespaceStack().peek().renameMap.put("a", new LabelInfo(1));
        processLabels.visitBreakOrContinue(node);

        Assert.assertEquals("a", node.getFirstChild().getString());
        Assert.assertTrue(processLabels.getLabelInfo("a").referenced);
    }

    @Test
    public void testGetNameForId() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        processLabels.names.add("c");

        Assert.assertEquals("a", processLabels.getNameForId(1));
        Assert.assertEquals("b", processLabels.getNameForId(2));
        Assert.assertEquals("c", processLabels.getNameForId(3));
    }

    @Test
    public void testGetLabelInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.getNamespaceStack().peek().renameMap.put("a", new LabelInfo(1));
        processLabels.getNamespaceStack().push(new LabelNamespace());

        Assert.assertEquals(1, processLabels.getLabelInfo("a").id);
        Assert.assertNull(processLabels.getLabelInfo("b"));
    }

}