package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ScopedAliasesTest {

    @Test
    public void testScopedAliases() {
        // Test empty case
        ScopedAliases scopedAliases = new ScopedAliases(null, null, null);
        scopedAliases.process(null, null);
        assertFalse(scopedAliases.hasErrors());

        // Test with aliases
        ScopedAliases scopedAliasesWithAliases = new ScopedAliases(null, null, null);
        Node externs = Node.newScope();
        Node root = Node.newBlock("test");
        externs.addChild(Node.newVariable("goog", Node.newObject(Node.newName("goog"))));
        externs.addChild(Node.newVariable("dom", Node.newObject(Node.newName("goog.dom"))));
        root.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        root.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.dom"), "createElement",
                        Node.newName("DIV")))));
        scopedAliasesWithAliases.process(externs, root);
        assertFalse(scopedAliasesWithAliases.hasErrors());
        assertNotNull(scopedAliasesWithAliases.getScopeCalls().get(0).getLastChild());
        assertEquals("goog.dom.createElement",
                scopedAliasesWithAliases.getScopeCalls().get(0)
                        .getLastChild().getText());

        // Test with errors
        ScopedAliases scopedAliasesWithErrors = new ScopedAliases(null, null, null);
        Node rootWithErrors = Node.newBlock("testWithErrors");
        rootWithErrors.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithErrors.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithErrors.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(Node.newName("this")),
                        Node.newBlock(1, new Node[0])))));
        scopedAliasesWithErrors.process(null, rootWithErrors);
        assertTrue(scopedAliasesWithErrors.hasErrors());
        List<Node> aliasDefinitionsInOrder = scopedAliasesWithErrors.getAliasDefinitionsInOrder();
        assertEquals(0, aliasDefinitionsInOrder.size());
        List<Node> scopeCalls = scopedAliasesWithErrors.getScopeCalls();
        assertEquals(3, scopeCalls.size());

        // Test with different number of arguments in goog.scope call
        ScopedAliases scopedAliasesWithDifferentArguments = new ScopedAliases(null, null, null);
        Node rootWithDifferentArguments = Node.newBlock("testWithDifferentArguments");
        rootWithDifferentArguments.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(Node.newName("a")),
                        Node.newBlock(1, new Node[0])))));
        scopedAliasesWithDifferentArguments.process(null, rootWithDifferentArguments);
        assertTrue(scopedAliasesWithDifferentArguments.hasErrors());
        assertEquals(1, scopedAliasesWithDifferentArguments.getAliasDefinitionsInOrder().size());
        assertEquals(1, scopedAliasesWithDifferentArguments.getScopeCalls().size());

        // Test with return statement in goog.scope
        ScopedAliases scopedAliasesWithReturn = new ScopedAliases(null, null, null);
        Node rootWithReturn = Node.newBlock("testWithReturn");
        rootWithReturn.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithReturn.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newReturnStatement(Node.newNumberLiteral(42)))));
        scopedAliasesWithReturn.process(null, rootWithReturn);
        assertTrue(scopedAliasesWithReturn.hasErrors());

        // Test with throw statement in goog.scope
        ScopedAliases scopedAliasesWithThrow = new ScopedAliases(null, null, null);
        Node rootWithThrow = Node.newBlock("testWithThrow");
        rootWithThrow.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithThrow.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newThrowStatement(Node.newIdentifier("e")))));
        scopedAliasesWithThrow.process(null, rootWithThrow);
        assertTrue(scopedAliasesWithThrow.hasErrors());

        // Test with alias redefined
        ScopedAliases scopedAliasesWithAliasRedefined = new ScopedAliases(null, null, null);
        Node rootWithAliasRedefined = Node.newBlock("testWithAliasRedefined");
        Node aliasDefinition1 = Node.newVariable("x", Node.newName("goog.x"));
        Node aliasDefinition2 = Node.newVariable("x", Node.newName("goog.y"));
        rootWithAliasRedefined.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithAliasRedefined.addChild(Node.newStatement(Node.newDeclarationStatement(aliasDefinition1)));
        rootWithAliasRedefined.addChild(Node.newStatement(Node.newDeclarationStatement(aliasDefinition2)));
        scopedAliasesWithAliasRedefined.process(null, rootWithAliasRedefined);
        assertTrue(scopedAliasesWithAliasRedefined.hasErrors());
        assertEquals(1, scopedAliasesWithAliasRedefined.getAliasDefinitionsInOrder().size());
        assertEquals("goog.x", scopedAliasesWithAliasRedefined.getAliasDefinitionsInOrder().get(0).getName());

        // Test with non-alias local in goog.scope
        ScopedAliases scopedAliasesWithNonAliasLocal = new ScopedAliases(null, null, null);
        Node rootWithNonAliasLocal = Node.newBlock("testWithNonAliasLocal");
        rootWithNonAliasLocal.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithNonAliasLocal.addChild(Node.newStatement(Node.newDeclarationStatement(
                Node.newVariable("x", Node.newNumberLiteral(42)))));
        scopedAliasesWithNonAliasLocal.process(null, rootWithNonAliasLocal);
        assertTrue(scopedAliasesWithNonAliasLocal.hasErrors());
        assertEquals(1, scopedAliasesWithNonAliasLocal.getAliasDefinitionsInOrder().size());
        assertEquals("x", scopedAliasesWithNonAliasLocal.getAliasDefinitionsInOrder().get(0).getName());

        // Test with this keyword in goog.scope
        ScopedAliases scopedAliasesWithThisKeyword = new ScopedAliases(null, null, null);
        Node rootWithThisKeyword = Node.newBlock("testWithThisKeyword");
        rootWithThisKeyword.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), Node.newFunction(null),
                        Node.newBlock(1, new Node[0])))));
        rootWithThisKeyword.addChild(Node.newStatement(Node.newExpressionStatement(
                Node.newCall(Node.newName("goog.scope"), null,
                        Node.newThis()))));
        scopedAliasesWithThisKeyword.process(null, rootWithThisKeyword);
        assertTrue(scopedAliasesWithThisKeyword.hasErrors());
    }
}