import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testEnterScope() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        NodeTraversal nodeTraversal = null;
        processLabels.enterScope(nodeTraversal);
    }

    @Test
    public void testExitScope() {
        RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
        NodeTraversal nodeTraversal = null;
        processLabels.exitScope(nodeTraversal);
    }

    @Test
    public void testShouldTraverse() {
        RenameLabels renameLabels = new RenameLabels(null);
        NodeTraversal nodeTraversal = null;
        Node node = null;
        Node parent = null;
        renameLabels.new ProcessLabels().shouldTraverse(nodeTraversal, node, parent);
    }

    @Test
    public void testVisit() {
        RenameLabels renameLabels = new RenameLabels(null);
        NodeTraversal nodeTraversal = null;
        Node node = null;
        Node parent = null;
        renameLabels.new ProcessLabels().visit(nodeTraversal, node, parent);
    }

    @Test
    public void testVisitBreakOrContinue() {
        RenameLabels renameLabels = new RenameLabels(null);
        Node node = null;
        renameLabels.new ProcessLabels().visitBreakOrContinue(node);
    }

    @Test
    public void testVisitLabel() {
        RenameLabels renameLabels = new RenameLabels(null);
        Node node = null;
        Node parent = null;
        renameLabels.new ProcessLabels().visitLabel(node, parent);
    }

    @Test
    public void testGetNameForId() {
        RenameLabels renameLabels = new RenameLabels(null);
        int id = 0;
        renameLabels.new ProcessLabels().getNameForId(id);
    }

    @Test
    public void testGetLabelInfo() {
        RenameLabels renameLabels = new RenameLabels(null);
        String name = "";
        renameLabels.new ProcessLabels().getLabelInfo(name);
    }

    @Test
    public void testProcess() {
        RenameLabels renameLabels = new RenameLabels(null);
        Node externs = null;
        Node root = null;
        renameLabels.process(externs, root);
    }

}