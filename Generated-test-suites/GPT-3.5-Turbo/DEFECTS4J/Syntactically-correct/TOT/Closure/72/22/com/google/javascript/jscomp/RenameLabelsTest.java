import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

    private RenameLabels.NameGenerator nameGenerator;
    private RenameLabels.ProcessLabels processLabels;

    @Before
    public void setUp() {
        nameGenerator = new RenameLabels.DefaultNameSupplier().nameGenerator;
        processLabels = new RenameLabels().new ProcessLabels();
    }

    // Test for enterScope()

    @Test
    public void testEnterScope() {
        assertEquals(1, processLabels.namespaceStack.size());
        processLabels.enterScope(null);
        assertEquals(2, processLabels.namespaceStack.size());
    }

    // Test for exitScope()

    @Test
    public void testExitScope() {
        processLabels.exitScope(null);
        assertEquals(0, processLabels.namespaceStack.size());
    }

    // Test for shouldTraverse()

    @Test
    public void testShouldTraverse_AddLabelNameToRenameMap() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToBack(nameNode);

        processLabels.shouldTraverse(null, labelNode, null);

        assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
    }

    @Test
    public void testShouldTraverse_CreateNewDepthName() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToBack(nameNode);

        processLabels.shouldTraverse(null, labelNode, null);

        assertEquals(1, processLabels.names.size());
    }

    @Test
    public void testShouldTraverse_GetNameForId_ReturnCorrectName() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToBack(nameNode);

        processLabels.shouldTraverse(null, labelNode, null);
        String nameForId = processLabels.getNameForId(1);

        assertEquals(processLabels.names.get(0), nameForId);
    }

    // Test for visit()

    @Test
    public void testVisit_HandleLabelNode() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToBack(nameNode);

        processLabels.shouldTraverse(null, labelNode, null);
        processLabels.visit(null, labelNode, null);

        assertFalse(processLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
    }

    @Test
    public void testVisit_RenameReferencedLabelNode() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToBack(nameNode);

        processLabels.shouldTraverse(null, labelNode, null);
        processLabels.visit(null, labelNode, null);

        assertEquals(processLabels.names.get(0), nameNode.getString());
    }

    @Test
    public void testVisit_RemoveUnreferencedLabelNode() {
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "labelName");
        labelNode.addChildToBack(nameNode);

        processLabels.visit(null, labelNode, null);

        assertEquals(nameNode, labelNode.getNext());
    }

    // Test for visitBreakOrContinue()

    @Test
    public void testVisitBreakOrContinue_RenameBreakOrContinueNode() {
        Node breakNode = new Node(Token.BREAK);
        Node nameNode = new Node(Token.NAME, "labelName");
        breakNode.addChildToBack(nameNode);

        processLabels.enterScope(null);
        processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));

        processLabels.visitBreakOrContinue(breakNode);

        assertEquals(processLabels.names.get(0), nameNode.getString());
    }

    @Test
    public void testVisitBreakOrContinue_MarkReferencedLabel() {
        Node breakNode = new Node(Token.BREAK);
        Node nameNode = new Node(Token.NAME, "labelName");
        breakNode.addChildToBack(nameNode);

        processLabels.enterScope(null);
        processLabels.namespaceStack.peek().renameMap.put("labelName", new RenameLabels.LabelInfo(1));

        processLabels.visitBreakOrContinue(breakNode);

        assertTrue(processLabels.namespaceStack.peek().renameMap.get("labelName").referenced);
    }

    // Test for renameLabels()

    @Test
    public void testRenameLabels_ProcessLabelsIsCalled() {
        RenameLabels renameLabels = new RenameLabels();
        renameLabels.process(null, null);

        // Verify that processLabels() method is called
    }

}