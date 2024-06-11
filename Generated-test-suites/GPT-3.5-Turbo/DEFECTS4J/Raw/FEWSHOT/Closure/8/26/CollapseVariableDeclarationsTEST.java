package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    private CollapseVariableDeclarations collapse;

    @Before
    public void setUp() {
        collapse = new CollapseVariableDeclarations(new AbstractCompiler());
    }

    @Test
    public void testProcess_emptyRoot() {
        Node externs = new Node();
        Node root = new Node();
        collapse.process(externs, root);
        // Assert that no code change has been reported
        assertFalse(collapse.compiler.hasCodeChanged());
    }

    @Test
    public void testProcess_noCollapses() {
        Node externs = new Node();
        Node root = createRootNodeWithoutCollapses();
        collapse.process(externs, root);
        // Assert that no code change has been reported
        assertFalse(collapse.compiler.hasCodeChanged());
    }

    @Test
    public void testProcess_singleCollapse() {
        Node externs = new Node();
        Node root = createRootNodeWithSingleCollapse();
        collapse.process(externs, root);
        // Assert that the code has been changed
        assertTrue(collapse.compiler.hasCodeChanged());
    }

    @Test
    public void testProcess_multipleCollapses() {
        Node externs = new Node();
        Node root = createRootNodeWithMultipleCollapses();
        collapse.process(externs, root);
        // Assert that the code has been changed
        assertTrue(collapse.compiler.hasCodeChanged());
    }

    @Test
    public void testGatherCollapses_varNode() {
        NodeTraversal t = new NodeTraversal(collapse.compiler, new DummyScopeCreator());
        Node n = createVarNode();
        Node parent = new Node();
        collapse.gatherCollapses(t, n, parent);
        // Assert that the collapse list is empty since there are no adjacent var nodes
        assertNotNull(collapse.collapses);
        assertEquals(0, collapse.collapses.size());
    }

    @Test
    public void testGatherCollapses_noCollapse() {
        NodeTraversal t = new NodeTraversal(collapse.compiler, new DummyScopeCreator());
        Node n = createNodeWithoutCollapses();
        Node parent = new Node();
        collapse.gatherCollapses(t, n, parent);
        // Assert that the collapse list is empty since there are no adjacent var or redeclared nodes
        assertNotNull(collapse.collapses);
        assertEquals(0, collapse.collapses.size());
    }

    @Test
    public void testGatherCollapses_singleCollapse() {
        NodeTraversal t = new NodeTraversal(collapse.compiler, new DummyScopeCreator());
        Node n = createNodeWithSingleCollapse();
        Node parent = new Node();
        collapse.gatherCollapses(t, n, parent);
        // Assert that there is a single collapse in the collapse list
        assertNotNull(collapse.collapses);
        assertEquals(1, collapse.collapses.size());
        CollapseVariableDeclarations.Collapse collapseObj = collapse.collapses.get(0);
        assertNotNull(collapseObj);
        assertEquals(n, collapseObj.startNode);
        assertEquals(n.getNext().getNext(), collapseObj.endNode);
        assertEquals(parent, collapseObj.parent);
    }

    @Test
    public void testGatherCollapses_multipleCollapses() {
        NodeTraversal t = new NodeTraversal(collapse.compiler, new DummyScopeCreator());
        Node n = createNodeWithMultipleCollapses();
        Node parent = new Node();
        collapse.gatherCollapses(t, n, parent);
        // Assert that there are multiple collapses in the collapse list
        assertNotNull(collapse.collapses);
        assertEquals(2, collapse.collapses.size());
    }

    @Test
    public void testApplyCollapses_singleCollapse() {
        CollapseVariableDeclarations.Collapse collapseObj = createSingleCollapseObject();
        collapse.collapses.add(collapseObj);
        Node parent = collapseObj.parent;
        collapse.applyCollapses();
        // Assert that the collapse has been applied correctly
        assertEquals(1, parent.getChildCount());
        Node var = parent.getFirstChild();
        assertNotNull(var);
        assertEquals(Token.VAR, var.getToken());
        assertEquals(collapseObj.startNode.getSourceFileName(), var.getSourceFileName());
        assertEquals(collapseObj.startNode.getLineno(), var.getLineno());
        assertEquals(collapseObj.startNode.getCharno(), var.getCharno());
        assertEquals(3, var.getChildCount());
        Node first = var.getFirstChild();
        assertNotNull(first);
        Node second = first.getNext();
        assertNotNull(second);
        Node third = second.getNext();
        assertNotNull(third);
        assertEquals(NodeUtil.toSourceNode(first), collapseObj.startNode.getFirstChild());
        assertEquals(NodeUtil.toSourceNode(second), collapseObj.startNode.getNext().getFirstChild());
        assertEquals(NodeUtil.toSourceNode(third), collapseObj.startNode.getNext().getNext().getFirstChild());
    }

    @Test
    public void testApplyCollapses_multipleCollapses() {
        CollapseVariableDeclarations.Collapse collapseObj1 = createSingleCollapseObject();
        CollapseVariableDeclarations.Collapse collapseObj2 = createSingleCollapseObject();
        collapse.collapses.add(collapseObj1);
        collapse.collapses.add(collapseObj2);
        Node parent = collapseObj1.parent;
        collapse.applyCollapses();
        // Assert that the collapses have been applied correctly
        assertEquals(2, parent.getChildCount());
        Node var1 = parent.getFirstChild();
        assertNotNull(var1);
        assertEquals(Token.VAR, var1.getToken());
        assertEquals(collapseObj1.startNode.getSourceFileName(), var1.getSourceFileName());
        assertEquals(collapseObj1.startNode.getLineno(), var1.getLineno());
        assertEquals(collapseObj1.startNode.getCharno(), var1.getCharno());
        assertEquals(3, var1.getChildCount());
        Node first1 = var1.getFirstChild();
        assertNotNull(first1);
        Node second1 = first1.getNext();
        assertNotNull(second1);
        Node third1 = second1.getNext();
        assertNotNull(third1);
        assertEquals(NodeUtil.toSourceNode(first1), collapseObj1.startNode.getFirstChild());
        assertEquals(NodeUtil.toSourceNode(second1), collapseObj1.startNode.getNext().getFirstChild());
        assertEquals(NodeUtil.toSourceNode(third1), collapseObj1.startNode.getNext().getNext().getFirstChild());

        Node var2 = parent.getChildAtIndex(1);
        assertNotNull(var2);
        assertEquals(Token.VAR, var2.getToken());
        assertEquals(collapseObj2.startNode.getNext().getNext().getSourceFileName(), var2.getSourceFileName());
        assertEquals(collapseObj2.startNode.getNext().getNext().getLineno(), var2.getLineno());
        assertEquals(collapseObj2.startNode.getNext().getNext().getCharno(), var2.getCharno());
        assertEquals(3, var2.getChildCount());
        Node first2 = var2.getFirstChild();
        assertNotNull(first2);
        Node second2 = first2.getNext();
        assertNotNull(second2);
        Node third2 = second2.getNext();
        assertNotNull(third2);
        assertEquals(NodeUtil.toSourceNode(first2), collapseObj2.startNode.getNext().getNext().getFirstChild());
        assertEquals(NodeUtil.toSourceNode(second2), collapseObj2.startNode.getNext().getNext().getNext().getFirstChild());
        assertEquals(NodeUtil.toSourceNode(third2), collapseObj2.startNode.getNext().getNext().getNext().getNext().getFirstChild());
    }

    private Node createRootNodeWithoutCollapses() {
        Node root = new Node();
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        return root;
    }

    private Node createRootNodeWithSingleCollapse() {
        Node root = new Node();
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        return root;
    }

    private Node createRootNodeWithMultipleCollapses() {
        Node root = new Node();
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.VAR));
        return root;
    }

    private Node createVarNode() {
        Node var = new Node(Token.VAR);
        Node name1 = new Node(Token.NAME, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node name2 = new Node(Token.NAME, new Node(Token.EMPTY), new Node(Token.EMPTY));
        Node name3 = new Node(Token.NAME, new Node(Token.EMPTY), new Node(Token.EMPTY));
        var.addChildToBack(name1);
        var.addChildToBack(name2);
        var.addChildToBack(name3);
        return var;
    }

    private Node createNodeWithoutCollapses() {
        Node rootNode = createRootNodeWithoutCollapses();
        return rootNode.getFirstChild();
    }

    private Node createNodeWithSingleCollapse() {
        Node rootNode = createRootNodeWithSingleCollapse();
        return rootNode.getFirstChild();
    }

    private Node createNodeWithMultipleCollapses() {
        Node rootNode = createRootNodeWithMultipleCollapses();
        return rootNode.getFirstChild();
    }

    private CollapseVariableDeclarations.Collapse createSingleCollapseObject() {
        Node startNode = new Node(Token.VAR);
        Node endNode = new Node(Token.VAR);
        Node parent = new Node();
        return collapse.new Collapse(startNode, endNode, parent);
    }
}

class DummyScopeCreator implements ScopeCreator {
    @Override
    public Scope createScope(Node n, Scope parentScope) {
        return new Scope(n, parentScope, null);
    }
}