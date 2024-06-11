import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess_emptyTree_noChanges() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        collapseVariableDeclarations.process(externs, root);

        // Test that no changes were made to the tree
        assertEquals(externs, root.getFirstChild());
    }

    @Test
    public void testProcess_singleVariableDeclaration_noChanges() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
      
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        root.addChildToBack(varNode);

        collapseVariableDeclarations.process(externs, root);

        // Test that no changes were made to the tree
        assertEquals(externs, root.getFirstChild());
        assertEquals(varNode, root.getLastChild());
    }

    @Test
    public void testProcess_multipleVariableDeclarations_collapse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
      
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        Node varNode1 = new Node(Token.VAR);
        varNode1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        root.addChildToBack(varNode1);

        Node varNode2 = new Node(Token.VAR);
        varNode2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        root.addChildToBack(varNode2);

        collapseVariableDeclarations.process(externs, root);

        // Test that the variable declarations were collapsed into one
        assertEquals(externs, root.getFirstChild());
        assertEquals(varNode1, root.getLastChild());
        assertEquals(2, varNode1.getChildCount());
    }

    @Test
    public void testProcess_variableDeclarationAndAssignment_collapse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
      
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        root.addChildToBack(varNode);

        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        assignNode.addChildToBack(new Node(Token.TRUE));
        root.addChildToBack(assignNode);

        collapseVariableDeclarations.process(externs, root);

        // Test that the variable declaration and assignment were collapsed into one
        assertEquals(externs, root.getFirstChild());
        assertEquals(varNode, root.getLastChild());
        assertEquals(2, varNode.getChildCount());
    }

    @Test
    public void testProcess_variableDeclarationAndMultipleAssignments_collapse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
      
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        root.addChildToBack(varNode);

        Node assignNode1 = new Node(Token.ASSIGN);
        assignNode1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        assignNode1.addChildToBack(new Node(Token.TRUE));
        root.addChildToBack(assignNode1);

        Node assignNode2 = new Node(Token.ASSIGN);
        assignNode2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "c")));
        assignNode2.addChildToBack(new Node(Token.TRUE));
        root.addChildToBack(assignNode2);

        collapseVariableDeclarations.process(externs, root);

        // Test that the variable declaration and multiple assignments were collapsed into one
        assertEquals(externs, root.getFirstChild());
        assertEquals(varNode, root.getLastChild());
        assertEquals(3, varNode.getChildCount());
    }

    @Test
    public void testProcess_multipleVariableDeclarationsAndAssignments_collapse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
      
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);

        Node varNode1 = new Node(Token.VAR);
        varNode1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "a")));
        root.addChildToBack(varNode1);

        Node varNode2 = new Node(Token.VAR);
        varNode2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "b")));
        root.addChildToBack(varNode2);

        Node assignNode1 = new Node(Token.ASSIGN);
        assignNode1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "c")));
        assignNode1.addChildToBack(new Node(Token.TRUE));
        root.addChildToBack(assignNode1);

        Node assignNode2 = new Node(Token.ASSIGN);
        assignNode2.addChildToBack(new Node(Token.NAME, new Node(Token.STRING, "d")));
        assignNode2.addChildToBack(new Node(Token.TRUE));
        root.addChildToBack(assignNode2);

        collapseVariableDeclarations.process(externs, root);

        // Test that the variable declarations and assignments were collapsed into one
        assertEquals(externs, root.getFirstChild());
        assertEquals(varNode1, root.getLastChild());
        assertEquals(4, varNode1.getChildCount());
    }
}