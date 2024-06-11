
package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CheckSideEffectsTest {

    @Test
    public void testProcess_EmptyScriptRoot_NoWarningsReported() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode("");
        checkSideEffects.process(externs, root);
        assertFalse(compiler.getWarnings().hasNext());
    }

    @Test
    public void testProcess_ScriptRootWithSideEffects_WarningsReported() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode("var x = 5;");
        checkSideEffects.process(externs, root);
        assertTrue(compiler.getWarnings().hasNext());
    }

    @Test
    public void testHotSwapScript_ScriptRootWithSideEffects_WarningsReported() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode("var x = 5;");
        checkSideEffects.hotSwapScript(root, null);
        assertTrue(compiler.getWarnings().hasNext());
    }

    @Test
    public void testVisit_EmptyNode_NoWarningsReported() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node n = new Node(Token.EMPTY);
        checkSideEffects.visit(traversal, n, null);
        assertFalse(compiler.getWarnings().hasNext());
    }

    @Test
    public void testVisit_NodeWithSideEffects_WarningsReported() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node n = new Node(Token.SCRIPT);
        n.addChildToFront(new Node(Token.VAR).addChildToFront(new Node(Token.NAME, "x")));
        checkSideEffects.visit(traversal, n, null);
        assertTrue(compiler.getWarnings().hasNext());
    }

    @Test
    public void testStripProtection_Process_NoProtectionNodesLeft() {
        AbstractCompiler compiler = new Compiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = compiler.parseTestCode("");
        Node root = compiler.parseTestCode("var x = JSCOMPILER_PRESERVE();");
        stripProtection.process(externs, root);
        assertNull(NodeUtil.findQualifiedName(root, "JSCOMPILER_PRESERVE"));
    }
    
}
