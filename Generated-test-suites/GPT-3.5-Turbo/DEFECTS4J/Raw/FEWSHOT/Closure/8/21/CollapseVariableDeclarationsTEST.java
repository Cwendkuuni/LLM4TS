package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {
    CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations(null);
    }

    @Test
    public void testCanCollapse_shouldReturnTrueForVarNode() {
        Node node = new Node(Token.VAR);
        assertTrue(collapseVariableDeclarations.canBeRedeclared(node, null));
    }

    @Test
    public void testCanCollapse_shouldReturnTrueForAssignNode() {
        Node node = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.setString("x");
        node.addChildToBack(lhs);
        assertTrue(collapseVariableDeclarations.canBeRedeclared(node, null));
    }

    @Test
    public void testCanCollapse_shouldReturnFalseForNonVarOrAssignNode() {
        Node node = new Node(Token.STRING);
        assertFalse(collapseVariableDeclarations.canBeRedeclared(node, null));
    }

    @Test
    public void testApplyCollapses_shouldCombineVariableDeclarations() {
        Node var1 = new Node(Token.VAR);
        Node var2 = new Node(Token.VAR);
        var1.addChildToFront(new Node(Token.NAME, new Node(Token.STRING)));
        var1.addChildToBack(new Node(Token.NAME, new Node(Token.NULL)));
        var2.addChildToFront(new Node(Token.NAME, new Node(Token.NUMBER)));
        Node parent = new Node(Token.ROOT);
        parent.addChildToFront(var1);
        parent.addChildToBack(var2);

        Collapse collapse = new Collapse(var1, var2, parent);

        collapseVariableDeclarations.applyCollapses();

        assertEquals(1, parent.getChildCount());
        assertEquals(Token.VAR, parent.getFirstChild().getType());
        assertEquals(3, parent.getFirstChild().getChildCount());
    }
}