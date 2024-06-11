package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
    @Test
    public void testUnreachableCodeElimination() {
        Node root = new Node(Token.ROOT, "test");

        // Unreachable code after return statement
        root.addChild(new Node(Token.RETURN, null));
        root.addChild(new Node(Token.PRINT, "unreachable"));

        // Test UnreachableCodeElimination
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
        NodeTraversal traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertTrue(root.getLastChild() == null);

        // Unreachable code with no side effects
        root = new Node(Token.ROOT, "test2");
        Node a = new Node(Token.IDENTIFIER, "a");
        Node b = NodeUtil.createQualifiedName("MyClass", "prototype", "propertyName");
        root.addChild(a);
        root.addChild(b);

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertTrue(root.getLastChild() == a);

        // Unreachable code in a function
        Node function = new Node(Token.FUNCTION, "test3", new Node[0], null);
        root.addChild(function);
        Node unreachable = new Node(Token.PRINT, "unreachable");
        function.addChild(new Node(Token.RETURN, null));
        function.addChild(unreachable);

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertTrue(function.getLastChild() == null);

        // Unreachable code in a script
        root = new Node(Token.SCRIPT, null);
        root.addChild(new Node(Token.PRINT, "unreachable"));
        root.addChild(new Node(Token.RETURN, null));

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertTrue(root.getLastChild() == null);

        // Unreachable code in a try-catch block
        root = new Node(Token.ROOT, "test4");
        try {
            root.addChild(new Node(Token.TRY, null, new Node[0], null));
            root.addChild(new Node(Token.THROW, null));
            root.addChild(new Node(Token.CATCH, null, new Node[0], null));
            root.addChild(new Node(Token.PRINT, "unreachable"));
        } finally {
            root.addChild(new Node(Token.RETURN, null));
        }

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertTrue(root.getLastChild() == null);

        // Unreachable code in a for-in statement
        root = new Node(Token.ROOT, "test5");
        Node forNode = new Node(Token.FOR_IN, null, new Node[0], null);
        root.addChild(forNode);
        root.addChild(new Node(Token.IDENTIFIER, "x"));
        root.addChild(new Node(Token.RETURN, null));

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertTrue(root.getLastChild() == forNode);

        // Unreachable code with side effects
        root = new Node(Token.ROOT, "test6");
        Node a2 = new Node(Token.VAR, "a", new Node[0], null);
        root.addChild(a2);
        root.addChild(new Node(Token.RETURN, null));
        root.addChild(a2);

        elimination = new UnreachableCodeElimination(null, false);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);
        assertFalse(a2.getLastChild() == null);
    }

    @Test
    public void testUnconditionalBranching() {
        Node root = new Node(Token.ROOT, "test7");

        // Test unconditional branching to the same node
        Node ifNode = new Node(Token.IF, null, new Node[0], new Node[0]);
        root.addChild(ifNode);
        root.addChild(new Node(Token.GOTO, "label1", null));
        root.addChild(new Node(Token.LABEL, "label1"));
        root.addChild(new Node(Token.RETURN, null));

        UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
        NodeTraversal traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        Node ifNode2 = root.getChildAt(0);
        assertTrue(ifNode2.getLastChild() == null);

        // Test unconditional branching to another unconditional branching node
        root = new Node(Token.ROOT, "test8");
        ifNode = new Node(Token.IF, null, new Node[0], new Node[0]);
        root.addChild(ifNode);
        root.addChild(new Node(Token.GOTO, "label1", null));
        root.addChild(new Node(Token.LABEL, "label1"));
        root.addChild(new Node(Token.BREAK, null));
        root.addChild(new Node(Token.LABEL, "label2"));
        root.addChild(new Node(Token.RETURN, null));

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        Node ifNode3 = root.getChildAt(0);
        assertTrue(ifNode3.getLastChild() == null);
    }

    @Test
    public void testRemoveNoOpStatements() {
        Node root = new Node(Token.ROOT, "test9");

        // Test remove no-op statements in a function
        Node function = new Node(Token.FUNCTION, "testFunction", new Node[0], null);
        root.addChild(function);
        function.addChild(new Node(Token.RETURN, null));
        function.addChild(new Node(Token.TRUE, null));

        UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
        NodeTraversal traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        assertTrue(function.getLastChild() == null);

        // Test remove no-op statements in a script
        root = new Node(Token.SCRIPT, null);
        root.addChild(new Node(Token.TRUE, null));
        root.addChild(new Node(Token.RETURN, null));

        elimination = new UnreachableCodeElimination(null, true);
        traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        assertTrue(root.getLastChild() == null);
    }

    @Test
    public void testRemoveNoOpStatementsInForInStatement() {
        Node root = new Node(Token.ROOT, "test10");
        Node forNode = new Node(Token.FOR_IN, null, new Node[0], null);
        root.addChild(forNode);
        root.addChild(new Node(Token.IDENTIFIER, "x"));
        root.addChild(new Node(Token.TRUE, null));
        root.addChild(new Node(Token.RETURN, null));

        UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
        NodeTraversal traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        assertTrue(root.getLastChild() == forNode);
    }

    @Test
    public void testRemoveUnreachableBreak() {
        Node root = new Node(Token.ROOT, "test11");

        Node ifNode = new Node(Token.IF, null, new Node[0], new Node[0]);
        root.addChild(ifNode);
        root.addChild(new Node(Token.LABEL, "label1"));
        root.addChild(new Node(Token.BREAK, "label2"));
        root.addChild(new Node(Token.LABEL, "label2"));
        root.addChild(new Node(Token.RETURN, null));

        UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
        NodeTraversal traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        Node ifNode2 = root.getChildAt(0);
        assertTrue(ifNode2.getLastChild() != null);
        Node breakNode = ifNode2.getLastChild().getLastChild();
        assertNull(breakNode.getNext());
    }

    @Test
    public void testRemoveUnreachableContinue() {
        Node root = new Node(Token.ROOT, "test12");

        Node whileNode = new Node(Token.WHILE, null, new Node[0], new Node[0]);
        root.addChild(whileNode);
        root.addChild(new Node(Token.LABEL, "label1"));
        root.addChild(new Node(Token.CONTINUE, null));
        root.addChild(new Node(Token.LABEL, "label2"));
        root.addChild(new Node(Token.RETURN, null));

        UnreachableCodeElimination elimination = new UnreachableCodeElimination(null, true);
        NodeTraversal traversal = new NodeTraversal(root);
        elimination.process(null, root);
        traversal.traverse(null, root, elimination);

        Node whileNode2 = root.getChildAt(0);
        assertTrue(whileNode2.getLastChild() != null);
        Node continueNode = whileNode2.getLastChild().getLastChild();
        assertNull(continueNode.getNext());
    }
}