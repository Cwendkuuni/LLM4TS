package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

    private AbstractCompiler compiler;
    private PreprocessorSymbolTable preprocessorSymbolTable;
    private AliasTransformationHandler transformationHandler;

    @Before
    public void setUp() {
        compiler = new Compiler();
        preprocessorSymbolTable = new PreprocessorSymbolTable();
        transformationHandler = new AliasTransformationHandler();
    }

    @Test
    public void testProcess() {
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        Node externs = new Node();
        Node root = new Node();
        scopedAliases.process(externs, root);
        // assert something
    }

    @Test
    public void testHotSwapScript() {
        ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
        Node root = new Node();
        Node originalRoot = new Node();
        scopedAliases.hotSwapScript(root, originalRoot);
        // assert something
    }

    @Test
    public void testAliasedNode() {
        Node aliasReference = new Node();
        Node aliasDefinition = new Node();
        ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
        aliasedNode.applyAlias();
        // assert something
    }

    @Test
    public void testAliasedTypeNode() {
        Node aliasReference = new Node();
        String correctedType = "correctedType";
        ScopedAliases.AliasedTypeNode aliasedTypeNode = new ScopedAliases.AliasedTypeNode(aliasReference, correctedType);
        aliasedTypeNode.applyAlias();
        // assert something
    }

    @Test
    public void testTraversal() {
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, traversal);
        Node node = new Node();
        Node parent = new Node();
        traversal.enterScope(nodeTraversal);
        traversal.exitScope(nodeTraversal);
        boolean shouldTraverse = traversal.shouldTraverse(nodeTraversal, node, parent);
        traversal.report(nodeTraversal, node, ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY);
        assertEquals(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL, traversal.GOOG_SCOPE_NON_ALIAS_LOCAL);
        boolean hasErrors = traversal.hasErrors();
        traversal.fixTypeNode(node);
        // assert something
    }

    @Test
    public void testValidateScopeCall() {
        ScopedAliases.Traversal traversal = new ScopedAliases.Traversal();
        NodeTraversal nodeTraversal = new NodeTraversal(compiler, traversal);
        Node node = new Node();
        Node parent = new Node();
        traversal.validateScopeCall(nodeTraversal, node, parent);
        // assert something
    }

}