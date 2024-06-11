package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testContextualRenamer() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        Node root = new Node(Token.PROGRAM);
        Node scope = new Node(Token.BLOCK);
        scope.setParent(root);
        root.setFirstChild(scope);

        Node var1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, "var1");
        var1.setFirstChild(nameNode1);
        var1.setNext(new Node(Token.SEMICOLON));
        scope.setFirstChild(var1);

        Node var2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, "var1");
        var2.setFirstChild(nameNode2);
        var2.setNext(new Node(Token.SEMICOLON));
        scope.setNext(var2);

        makeDeclaredNamesUnique.traverse(new NodeTraversal(root, null));

        assertEquals("var1_0", nameNode1.getString());
        assertEquals("var1_1", nameNode2.getString());

        String originalName = "var1";
        String replacementName = makeDeclaredNamesUnique.getReplacementName(originalName);
        assertEquals("var1_0", replacementName);
    }

    @Test
    public void testInlineRenamer() {
        String idPrefix = "prefix_";
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
                new InlineRenamer(() -> idPrefix + Integer.toString(System.identityHashCode(this)),
                        idPrefix, true));
        Node root = new Node(Token.PROGRAM);
        Node scope = new Node(Token.BLOCK);
        scope.setParent(root);
        root.setFirstChild(scope);

        Node var1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, "var1");
        var1.setFirstChild(nameNode1);
        var1.setNext(new Node(Token.SEMICOLON));
        scope.setFirstChild(var1);

        Node var2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, "var1");
        var2.setFirstChild(nameNode2);
        var2.setNext(new Node(Token.SEMICOLON));
        scope.setNext(var2);

        makeDeclaredNamesUnique.traverse(new NodeTraversal(root, null));

        assertEquals("var1_prefix_170173132255925", nameNode1.getString());
        assertEquals("var1_prefix_182865512332778", nameNode2.getString());

        String originalName = "var1";
        String replacementName = makeDeclaredNamesUnique.getReplacementName(originalName);
        assertEquals("var1_prefix_170173132255925", replacementName);
    }

    @Test
    public void testContextualRenamerWithFunction() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        Node root = new Node(Token.PROGRAM);
        Node scope = new Node(Token.BLOCK);
        scope.setParent(root);
        root.setFirstChild(scope);

        Node function = new Node(Token.FUNCTION);
        Node nameNodeFunction = new Node(Token.NAME, "func");
        function.setFirstChild(nameNodeFunction);
        function.setNext(new Node(Token.FUNCTION_DECLARATION));
        function.setLastChild(new Node(Token.BLOCK));
        scope.setNext(function);

        Node var1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, "var1");
        var1.setFirstChild(nameNode1);
        var1.setNext(new Node(Token.SEMICOLON));
        function.getLastChild().setFirstChild(var1);

        Node var2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, "var1");
        var2.setFirstChild(nameNode2);
        var2.setNext(new Node(Token.SEMICOLON));
        function.getLastChild().setNext(var2);

        makeDeclaredNamesUnique.traverse(new NodeTraversal(root, null));

        assertEquals("var1_0", nameNode1.getString());
        assertEquals("var1_1", nameNode2.getString());

        String originalName = "var1";
        String replacementName = makeDeclaredNamesUnique.getReplacementName(originalName);
        assertEquals("var1_0", replacementName);

        String functionName = "func";
        replacementName = makeDeclaredNamesUnique.getReplacementName(functionName);
        assertEquals("func_0", replacementName);
    }

    @Test
    public void testInlineRenamerWithFunction() {
        String idPrefix = "prefix_";
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
                new InlineRenamer(() -> idPrefix + Integer.toString(System.identityHashCode(this)),
                        idPrefix, true));
        Node root = new Node(Token.PROGRAM);
        Node scope = new Node(Token.BLOCK);
        scope.setParent(root);
        root.setFirstChild(scope);

        Node function = new Node(Token.FUNCTION);
        Node nameNodeFunction = new Node(Token.NAME, "func");
        function.setFirstChild(nameNodeFunction);
        function.setNext(new Node(Token.FUNCTION_DECLARATION));
        function.setLastChild(new Node(Token.BLOCK));
        scope.setNext(function);

        Node var1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, "var1");
        var1.setFirstChild(nameNode1);
        var1.setNext(new Node(Token.SEMICOLON));
        function.getLastChild().setFirstChild(var1);

        Node var2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, "var1");
        var2.setFirstChild(nameNode2);
        var2.setNext(new Node(Token.SEMICOLON));
        function.getLastChild().setNext(var2);

        makeDeclaredNamesUnique.traverse(new NodeTraversal(root, null));

        assertEquals("var1_prefix_170173132255925", nameNode1.getString());
        assertEquals("var1_prefix_182865512332778", nameNode2.getString());

        String originalName = "var1";
        String replacementName = makeDeclaredNamesUnique.getReplacementName(originalName);
        assertEquals("var1_prefix_170173132255925", replacementName);

        String functionName = "func";
        replacementName = makeDeclaredNamesUnique.getReplacementName(functionName);
        assertEquals("func_prefix_170173132255925", replacementName);
    }

    @Test
    public void testRenamerForChildScope() {
        String idPrefix = "prefix_";
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
                new InlineRenamer(() -> idPrefix + Integer.toString(System.identityHashCode(this)),
                        idPrefix, true));

        Renamer childRenamer = makeDeclaredNamesUnique.forChildScope();

        String originalName = "var1";
        String childReplacementName = childRenamer.getReplacementName(originalName);
        assertEquals("var1_prefix_182865512332778", childReplacementName);
    }

    @Test
    public void testRenamerStripConst() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        Node root = new Node(Token.PROGRAM);
        Node scope = new Node(Token.BLOCK);
        scope.setParent(root);
        root.setFirstChild(scope);

        Node var1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, "constVar1");
        nameNode1.setProp(Node.IS_CONSTANT_NAME, true);
        var1.setFirstChild(nameNode1);
        var1.setNext(new Node(Token.SEMICOLON));
        scope.setFirstChild(var1);

        Node var2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, "constVar1");
        nameNode2.setProp(Node.IS_CONSTANT_NAME, true);
        var2.setFirstChild(nameNode2);
        var2.setNext(new Node(Token.SEMICOLON));
        scope.setNext(var2);

        makeDeclaredNamesUnique.traverse(new NodeTraversal(root, null));

        assertNull(nameNode1.getProp(Node.IS_CONSTANT_NAME));
        assertNull(nameNode2.getProp(Node.IS_CONSTANT_NAME));
    }

    @Test
    public void testRenamerStripConstChildScope() {
        String idPrefix = "prefix_";
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
                new InlineRenamer(() -> idPrefix + Integer.toString(System.identityHashCode(this)),
                        idPrefix, true));

        Renamer childRenamer = makeDeclaredNamesUnique.forChildScope();

        Node root = new Node(Token.PROGRAM);
        Node scope = new Node(Token.BLOCK);
        scope.setParent(root);
        root.setFirstChild(scope);

        Node var1 = new Node(Token.VAR);
        Node nameNode1 = new Node(Token.NAME, "constVar1");
        nameNode1.setProp(Node.IS_CONSTANT_NAME, true);
        var1.setFirstChild(nameNode1);
        var1.setNext(new Node(Token.SEMICOLON));
        scope.setFirstChild(var1);

        Node var2 = new Node(Token.VAR);
        Node nameNode2 = new Node(Token.NAME, "constVar1");
        nameNode2.setProp(Node.IS_CONSTANT_NAME, true);
        var2.setFirstChild(nameNode2);
        var2.setNext(new Node(Token.SEMICOLON));
        scope.setNext(var2);

        childRenamer.traverse(new NodeTraversal(root, null));

        assertNull(nameNode1.getProp(Node.IS_CONSTANT_NAME));
        assertNull(nameNode2.getProp(Node.IS_CONSTANT_NAME));
    }
}