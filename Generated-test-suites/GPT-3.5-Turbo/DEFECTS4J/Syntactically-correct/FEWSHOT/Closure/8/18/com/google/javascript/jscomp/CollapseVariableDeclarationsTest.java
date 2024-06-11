import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess_singleVarDeclaration() {
        Node externs = new Node(Token.EXTERNALS);
        Node root = createSingleVarDeclarationNode();

        CollapseVariableDeclarations c = new CollapseVariableDeclarations(compiler);
        c.process(externs, root);

        assertFalse(c.collapses.isEmpty());
        assertEquals(1, c.collapses.size());

        CollapseVariableDeclarations.Collapse collapse = c.collapses.get(0);
        assertEquals(root.getFirstChild(), collapse.startNode);
        assertEquals(null, collapse.endNode);
        assertEquals(root, collapse.parent);
    }

    @Test
    public void testProcess_multipleVarDeclarations() {
        Node externs = new Node(Token.EXTERNALS);
        Node root = createMultipleVarDeclarationsNode();

        CollapseVariableDeclarations c = new CollapseVariableDeclarations(compiler);
        c.process(externs, root);

        assertFalse(c.collapses.isEmpty());
        assertEquals(1, c.collapses.size());

        CollapseVariableDeclarations.Collapse collapse = c.collapses.get(0);
        assertEquals(root.getFirstChild(), collapse.startNode);
        assertEquals(null, collapse.endNode);
        assertEquals(root, collapse.parent);
    }

    @Test
    public void testProcess_nestedVarDeclarations() {
        Node externs = new Node(Token.EXTERNALS);
        Node root = createNestedVarDeclarationsNode();

        CollapseVariableDeclarations c = new CollapseVariableDeclarations(compiler);
        c.process(externs, root);

        assertFalse(c.collapses.isEmpty());
        assertEquals(1, c.collapses.size());

        CollapseVariableDeclarations.Collapse collapse = c.collapses.get(0);
        assertEquals(root.getFirstChild().getFirstChild(), collapse.startNode);
        assertEquals(root.getLastChild().getFirstChild(), collapse.endNode);
        assertEquals(root.getLastChild(), collapse.parent);
    }

    @Test
    public void testProcess_noVarDeclarations() {
        Node externs = new Node(Token.EXTERNALS);
        Node root = createNoVarDeclarationsNode();

        CollapseVariableDeclarations c = new CollapseVariableDeclarations(compiler);
        c.process(externs, root);

        assertTrue(c.collapses.isEmpty());
    }

    private Node createSingleVarDeclarationNode() {
        Node varNode = new Node(Token.VAR);
        Node nameNode = new Node(Token.NAME, new Node(Token.STRING, "x"));
        Node assignNode = new Node(Token.ASSIGN, nameNode, new Node(Token.TRUE));
        varNode.addChildToBack(new Node(Token.VAR, assignNode));

        Node root = new Node(Token.ROOT, varNode);
        return root;
    }

    private Node createMultipleVarDeclarationsNode() {
        Node varNode1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
        Node assignNode1 = new Node(Token.ASSIGN, nameNode1, new Node(Token.TRUE));
        varNode1.addChildToBack(new Node(Token.VAR, assignNode1));

        Node varNode2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, new Node(Token.STRING, "y"));
        Node assignNode2 = new Node(Token.ASSIGN, nameNode2, new Node(Token.FALSE));
        varNode2.addChildToBack(new Node(Token.VAR, assignNode2));

        Node root = new Node(Token.ROOT);
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);

        return root;
    }

    private Node createNestedVarDeclarationsNode() {
        Node varNode1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, new Node(Token.STRING, "x"));
        Node assignNode1 = new Node(Token.ASSIGN, nameNode1, new Node(Token.TRUE));
        varNode1.addChildToBack(new Node(Token.VAR, assignNode1));

        Node varNode2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, new Node(Token.STRING, "y"));
        Node assignNode2 = new Node(Token.ASSIGN, nameNode2, new Node(Token.FALSE));
        varNode1.addChildToBack(new Node(Token.VAR, assignNode2));

        Node root = new Node(Token.ROOT);
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);

        return root;
    }

    private Node createNoVarDeclarationsNode() {
        Node root = new Node(Token.ROOT);
        // No VAR nodes
        return root;
    }

}