package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testMakeDeclaredNamesUnique() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = new NodeTraversal(null, null, null);

        // Test empty code
        nodeTraversal.traverse(makeDeclaredNamesUnique, testNode(Token.SKIP_PAST_ELSE, null), null);
        assertEquals(0, makeDeclaredNamesUnique.nameStack.size());

        // Test simple variable declaration
        Node varDeclarationNode = testNode(Token.VAR, "x");
        Node varDeclarationParentNode = testNode(Token.BLOCK, null);
        Node varDeclarationScope = new Scope(varDeclarationParentNode, null, null, null);
        nodeTraversal.setScope(varDeclarationScope);
        nodeTraversal.traverse(makeDeclaredNamesUnique, varDeclarationNode, varDeclarationParentNode);
        assertEquals("x", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        // Test multiple variable declaration with conflicts
        Node conflictingVarDeclarationNode1 = testNode(Token.VAR, "x");
        Node conflictingVarDeclarationNode2 = testNode(Token.VAR, "x");
        Node conflictingVarDeclarationParentNode = testNode(Token.BLOCK, null);
        nodeTraversal.setScope(new Scope(conflictingVarDeclarationParentNode, null, null, null));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingVarDeclarationNode1, conflictingVarDeclarationParentNode);
        assertEquals("x_1", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingVarDeclarationNode2, conflictingVarDeclarationParentNode);
        assertEquals("x_2", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        // Test function declaration
        Node functionDeclarationNode = testNode(Token.FUNCTION, "foo");
        Node functionDeclarationParentNode = testNode(Token.BLOCK, null);
        nodeTraversal.setScope(new Scope(functionDeclarationParentNode, null, null, null));
        nodeTraversal.traverse(makeDeclaredNamesUnique, functionDeclarationNode, functionDeclarationParentNode);
        assertEquals("foo", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("foo"));

        // Test function with conflicting parameter name
        Node conflictingFunctionDeclarationNode = testNode(
            Token.FUNCTION,
            "function bar(x)",
            testNode(Token.IDENTIFIER, "x"));
        nodeTraversal.setScope(new Scope(conflictingFunctionDeclarationNode, null, null, null));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingFunctionDeclarationNode, null);
        assertEquals("x_1", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        // Test block with conflicting variable declaration
        Node conflictingBlockNode = testNode(Token.BLOCK, null);
        Node conflictingVarDeclarationNode1 = testNode(Token.VAR, "x");
        Node conflictingVarDeclarationNode2 = testNode(Token.VAR, "x");
        nodeTraversal.setScope(new Scope(conflictingBlockNode, null, null, null));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingBlockNode, null);
        nodeTraversal.setScope(makeDeclaredNamesUnique.nameStack.peek());
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingVarDeclarationNode1, conflictingBlockNode);
        assertEquals("x_1", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingVarDeclarationNode2, conflictingBlockNode);
        assertEquals("x_2", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        // Test block with conflicting function declaration
        Node conflictingFunctionDeclarationNode1 = testNode(
            Token.FUNCTION,
            "function foo(x)",
            testNode(Token.IDENTIFIER, "x"));
        Node conflictingBlockNode1 = testNode(Token.BLOCK, null);
        nodeTraversal.setScope(new Scope(conflictingBlockNode1, null, null, null));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingBlockNode1, null);
        nodeTraversal.setScope(makeDeclaredNamesUnique.nameStack.peek());
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingFunctionDeclarationNode1, conflictingBlockNode1);
        assertEquals("x_1", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        Node conflictingFunctionDeclarationNode2 = testNode(
            Token.FUNCTION,
            "function foo(x)",
            testNode(Token.IDENTIFIER, "x"));
        nodeTraversal.setScope(makeDeclaredNamesUnique.nameStack.peek());
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingFunctionDeclarationNode2, conflictingBlockNode1);
        assertEquals("x_2", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        // Test block with conflicting variable and function declaration
        Node conflictingVariableDeclarationNode = testNode(Token.VAR, "x");
        Node conflictingFunctionDeclarationNode3 = testNode(
            Token.FUNCTION,
            "function foo(x)",
            testNode(Token.IDENTIFIER, "x"));
        Node conflictingBlockNode2 = testNode(Token.BLOCK, null);
        nodeTraversal.setScope(new Scope(conflictingBlockNode2, null, null, null));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingBlockNode2, null);
        nodeTraversal.setScope(makeDeclaredNamesUnique.nameStack.peek());
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingVariableDeclarationNode, conflictingBlockNode2);
        assertEquals("x_1", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));
        nodeTraversal.traverse(makeDeclaredNamesUnique, conflictingFunctionDeclarationNode3, conflictingBlockNode2);
        assertEquals("x_2", makeDeclaredNamesUnique.nameStack.peek().getReplacementName("x"));

        // Test global scope
        nodeTraversal.traverse(makeDeclaredNamesUnique, testNode(Token.SKIP_PAST_ELSE, null), null);
        assertNull(makeDeclaredNamesUnique.nameStack.peek());
    }

    private Node testNode(int... types) {
        Node node = new Node();
        TokenStream tokenStream = new TokenStream(new StringBuilder(), null);
        tokenStream.putNext(new Token(Token.SKIP_PAST_ELSE, 0, ""));
        for (int type : types) {
            tokenStream.putNext(new Token(type, 0, ""));
        }
        node.init(tokenStream, 0, tokenStream.getCurrentPosition());
        return node;
    }
}