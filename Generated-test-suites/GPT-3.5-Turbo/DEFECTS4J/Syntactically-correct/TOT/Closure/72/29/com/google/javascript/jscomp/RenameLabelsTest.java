import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(new AbstractCompiler());
    }

    @Test
    public void testRenameLabels_DefaultConstructor() {
        RenameLabels.DefaultNameSupplier supplier = 
                  new RenameLabels.DefaultNameSupplier();
        assertNotNull(supplier);
        assertEquals("a", supplier.get());
    }

    @Test
    public void testRenameLabels_ParameterizedConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels.DefaultNameSupplier supplier = 
                  new RenameLabels.DefaultNameSupplier();
        RenameLabels renameLabels = 
                  new RenameLabels(compiler, supplier, true);

        assertNotNull(renameLabels);
        assertSame(compiler, renameLabels.compiler);
        assertSame(supplier, renameLabels.nameSupplier);
        assertTrue(renameLabels.removeUnused);
    }

    @Test
    public void testEnterScope() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        renameLabels.new ProcessLabels().enterScope(nodeTraversal);

        assertTrue(renameLabels.namespaceStack.size() > 0);
    }

    @Test
    public void testExitScope() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        renameLabels.new ProcessLabels().exitScope(nodeTraversal);

        assertTrue(renameLabels.namespaceStack.isEmpty());
    }

    @Test
    public void testShouldTraverse_LabelNode() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToBack(new Node(Token.STRING, "a"));
        Node parentNode = new Node(Token.BLOCK);
        boolean shouldTraverse = renameLabels.new ProcessLabels()
                  .shouldTraverse(nodeTraversal, labelNode, parentNode);

        assertTrue(shouldTraverse);
    }

    @Test
    public void testShouldTraverse_OtherNodeTypes() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node otherNode = new Node(Token.VAR);
        Node parentNode = new Node(Token.BLOCK);
        boolean shouldTraverse = renameLabels.new ProcessLabels()
                  .shouldTraverse(nodeTraversal, otherNode, parentNode);

        assertFalse(shouldTraverse);
    }

    @Test
    public void testVisit() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToBack(new Node(Token.STRING, "a"));
        Node breakNode = new Node(Token.BREAK);
        Node continueNode = new Node(Token.CONTINUE);
        Node parentNode = new Node(Token.BLOCK);
        renameLabels.new ProcessLabels()
                  .visit(nodeTraversal, labelNode, parentNode);
        renameLabels.new ProcessLabels()
                  .visit(nodeTraversal, breakNode, parentNode);
        renameLabels.new ProcessLabels()
                  .visit(nodeTraversal, continueNode, parentNode);

        // Assert that the visit methods have been called and no assertion 
        // errors have been thrown
    }

    @Test
    public void testVisitBreakOrContinue() {
        Node breakNode = new Node(Token.BREAK);
        breakNode.addChildToBack(new Node(Token.STRING, "a"));
        renameLabels.new ProcessLabels()
                  .visitBreakOrContinue(breakNode);
        assertEquals("b", breakNode.getFirstChild().getString());

        Node continueNode = new Node(Token.CONTINUE);
        continueNode.addChildToBack(new Node(Token.STRING, "b"));
        renameLabels.new ProcessLabels()
                  .visitBreakOrContinue(continueNode);
        assertEquals("b", continueNode.getFirstChild().getString());
    }

    @Test
    public void testVisitBreakOrContinue_NoMatchingLabel() {
        Node breakNode = new Node(Token.BREAK);
        breakNode.addChildToBack(new Node(Token.STRING, "c"));
        renameLabels.new ProcessLabels()
                  .visitBreakOrContinue(breakNode);
        assertEquals("c", breakNode.getFirstChild().getString());

        Node continueNode = new Node(Token.CONTINUE);
        continueNode.addChildToBack(new Node(Token.STRING, "d"));
        renameLabels.new ProcessLabels()
                  .visitBreakOrContinue(continueNode);
        assertEquals("d", continueNode.getFirstChild().getString());
    }

    @Test
    public void testVisitLabel_UnreferencedLabel() {
        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToBack(new Node(Token.STRING, "a"));
        Node parentNode = new Node(Token.BLOCK);
        renameLabels.new ProcessLabels()
                  .visitLabel(labelNode, parentNode);

        assertTrue(parentNode.getFirstChild() == null);
        assertFalse(renameLabels.namespaceStack
                  .peek().renameMap.containsKey("a"));
    }

    @Test
    public void testVisitLabel_ReferencedLabel() {
        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToBack(new Node(Token.STRING, "a"));
        Node referenceNode = new Node(Token.BREAK);
        referenceNode.addChildToBack(new Node(Token.STRING, "a"));
        Node parentNode = new Node(Token.BLOCK);
        parentNode.addChildToBack(labelNode);
        parentNode.addChildToBack(referenceNode);
        renameLabels.new ProcessLabels()
                  .visitLabel(labelNode, parentNode);

        assertTrue(parentNode.getFirstChild().equals(referenceNode));
        assertFalse(renameLabels.namespaceStack
                  .peek().renameMap.containsKey("a"));
        assertTrue(renameLabels.namespaceStack
                  .peek().renameMap.get("a").referenced);
    }

    @Test
    public void testGetNameForId() {
        renameLabels.new ProcessLabels().names.add("a");
        renameLabels.new ProcessLabels().names.add("b");

        String nameForId1 = renameLabels.new ProcessLabels()
                  .getNameForId(1);
        assertEquals("a", nameForId1);

        String nameForId2 = renameLabels.new ProcessLabels()
                  .getNameForId(2);
        assertEquals("b", nameForId2);
    }

    @Test
    public void testGetLabelInfo() {
        renameLabels.namespaceStack.peek()
                  .renameMap.put("a", new RenameLabels.LabelInfo(1));

        RenameLabels.LabelInfo labelInfo = renameLabels.new ProcessLabels()
                  .getLabelInfo("a");

        assertNotNull(labelInfo);
        assertEquals(1, labelInfo.id);
    }
}