import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcessEmptyRoot() {
        // Create an empty root node
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.SCRIPT);

        // Initialize the collapse variable declarations pass
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Process the empty root node
        collapseVariableDeclarations.process(externs, root);

        // Assert that no collapses are found
        assertTrue(collapseVariableDeclarations.collapses.isEmpty());
    }

    @Test
    public void testProcessSingleVarDeclaration() {
        // Create a root node with a single variable declaration
        Node externs = IR.root();
        Node varNode = IR.var(IR.name("x"));
        Node root = IR.root(varNode);

        // Initialize the collapse variable declarations pass
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Process the root node
        collapseVariableDeclarations.process(externs, root);

        // Assert that no collapses are found
        assertTrue(collapseVariableDeclarations.collapses.isEmpty());
    }

    @Test
    public void testProcessMultipleVarDeclarations() {
        // Create a root node with multiple variable declarations
        Node externs = IR.root();
        Node varNode1 = IR.var(IR.name("x"));
        Node varNode2 = IR.var(IR.name("y"));
        Node varNode3 = IR.var(IR.name("z"));
        Node root = IR.root(varNode1, varNode2, varNode3);

        // Initialize the collapse variable declarations pass
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();

        // Process the root node
        collapseVariableDeclarations.process(externs, root);

        // Assert that all variable declarations are collapsed into a single one
        assertEquals(1, collapseVariableDeclarations.collapses.size());
    }

    @Test
    public void testApplyCollapses() {
        // Create a collapse instance with multiple variable declarations
        Node startNode = IR.var(IR.name("x"));
        Node middleNode = IR.var(IR.name("y"));
        Node endNode = IR.var(IR.name("z"));
        Node parent = new Node(Token.SCRIPT);
        parent.addChildBefore(middleNode, endNode);
        parent.addChildBefore(startNode, middleNode);

        Collapse collapse = new Collapse(startNode, endNode, parent);

        // Initialize the collapse variable declarations pass
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();
        collapseVariableDeclarations.collapses.add(collapse);

        // Apply the collapses
        collapseVariableDeclarations.applyCollapses();

        // Assert that the variable declarations are collapsed correctly
        assertEquals(1, parent.getChildCount());
        Node varNode = parent.getFirstChild();
        assertEquals("var x, y, z", varNode.toString());
    }

}