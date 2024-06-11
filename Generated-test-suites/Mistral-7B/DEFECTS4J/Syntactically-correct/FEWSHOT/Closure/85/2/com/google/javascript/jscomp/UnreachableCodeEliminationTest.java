package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
    @Test
    public void testUnreachableCodeElimination() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node root = new Node(Token.SCRIPT, 0, null);
        Scope scope = new Scope(compiler, root);

        root.addChild(new Node(Token.VAR, 0, "x"));
        root.addChild(new Node(Token.RETURN, 1, null));
        root.addChild(new Node(Token.IF, 2, null, null, null, null, null, null, null));
        root.addChild(new Node(Token.BLOCK, 3, null));
        root.addChild(new Node(Token.RETURN, 4, null));
        root.addChild(new Node(Token.IF, 5, null, null, null, null, null, null, null));

        root.getChild(2).addChild(new Node(Token.VAR, 6, "y"));
        root.getChild(2).addChild(new Node(Token.IF, 7, null, null, null, null, null, null, null));
        root.getChild(2).getChild(7).addChild(new Node(Token.RETURN, 8, null));

        root.getChild(3).addChild(new Node(Token.VAR, 9, "z"));
        root.getChild(3).addChild(new Node(Token.IF, 10, null, null, null, null, null, null, null));
        root.getChild(3).getChild(10).addChild(new Node(Token.RETURN, 11, null));

        NodeTraversal traversal = new NodeTraversal(compiler, root);
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
        elimination.enterScope(traversal);
        traversal.traverse(elimination);
        elimination.exitScope(traversal);

        // Check that all dead statements were removed.
        assertTrue(NodeUtil.hasType(root.getChild(3), Token.BLOCK) && root.getChild(3).getChildCount() == 0);
        assertTrue(NodeUtil.hasType(root.getChild(5), Token.BLOCK) && root.getChild(5).getChildCount() == 0);

        // Check that no dead variables remain.
        assertFalse(NodeUtil.hasType(root, Token.VAR) && root.getChildCount() > 1);

        // Check that return statements were not removed if they have side effects.
        assertTrue(NodeUtil.hasType(root.getChild(0), Token.RETURN));
        assertTrue(NodeUtil.hasType(root.getChild(1), Token.RETURN));

        // Check that dead return statements were removed.
        assertNull(root.getChild(4));
        assertNull(root.getChild(8));
        assertNull(root.getChild(11));
    }
}