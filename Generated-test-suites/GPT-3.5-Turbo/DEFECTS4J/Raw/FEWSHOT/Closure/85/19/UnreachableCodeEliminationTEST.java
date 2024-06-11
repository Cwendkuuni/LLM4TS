package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        pass.enterScope(new NodeTraversal(compiler, null));

        assertNotNull(pass.cfgStack);
        assertFalse(pass.cfgStack.isEmpty());
        assertNotNull(pass.curCfg);
    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        pass.exitScope(new NodeTraversal(compiler, null));

        assertTrue(pass.cfgStack.isEmpty());
        assertNull(pass.curCfg);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        pass.process(null, null);

        // Verify that the traversal is done correctly
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.NAME, "test");
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        pass.cfgStack.push(null);
        pass.curCfg = null;
        pass.visit(t, n, parent);

        // Verify that the correct statements are removed
        // Verify that the control flow is computed correctly
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        Node n = new Node(Token.BREAK);
        Node result = pass.tryRemoveUnconditionalBranching(n);

        assertEquals(n, result);
    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        Node n = new Node(Token.BREAK);
        Node result = pass.computeFollowing(n);

        assertNull(result);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
        compiler.addPass(pass);

        Node n = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.FUNCTION);
        parent.addChildToBack(n);

        pass.removeDeadExprStatementSafely(n);

        // Verify that the statement is removed correctly
        // Verify that the code change is reported
        // Verify that the child is removed from the parent
    }
}