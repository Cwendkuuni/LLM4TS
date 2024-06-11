package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

    @Test
    public void testCheckSideEffects() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = false;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        // Test empty statement
        Node emptyStatement = IR.empty();
        Node parent = IR.block();
        parent.addChildToBack(emptyStatement);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(emptyStatement, parent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test void statement
        Node voidStatement = IR.voidStatement();
        parent.addChildToBack(voidStatement);
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(voidStatement, parent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Test statement with side effect
        Node sideEffectStatement = IR.assign(IR.name("x"), IR.lit(42));
        parent.addChildToBack(sideEffectStatement);
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(sideEffectStatement, parent, checkSideEffects);
        List<Node> problemNodes = checkSideEffects.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(sideEffectStatement, problemNodes.get(0));

        // Test statement without side effect
        Node noSideEffectStatement = IR.binop("+", IR.name("s"), IR.name("s"));
        parent.addChildToBack(noSideEffectStatement);
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(noSideEffectStatement, parent, checkSideEffects);
        problemNodes = checkSideEffects.problemNodes;
        assertTrue(problemNodes.isEmpty());

        // Test statement with side effect and isResultUsed is true
        Node sideEffectWithUsedResultStatement = IR.assign(IR.name("x"), IR.name("s").invoke("toString"));
        parent.addChildToBack(sideEffectWithUsedResultStatement);
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(sideEffectWithUsedResultStatement, parent, checkSideEffects);
        problemNodes = checkSideEffects.problemNodes;
        assertTrue(problemNodes.isEmpty());

        // Test statement with simple operator
        Node simpleOpStatement = IR.binop("+", IR.name("s"), IR.name("s"));
        parent.addChildToBack(simpleOpStatement);
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(simpleOpStatement, parent, checkSideEffects);
        problemNodes = checkSideEffects.problemNodes;
        assertTrue(problemNodes.isEmpty());

        // Test statement with side effect and not used
        Node sideEffectNotUsedStatement = IR.assign(IR.name("x"), IR.name("s").invoke("toString"));
        parent.addChildToBack(IR.exprStatement(sideEffectNotUsedStatement));
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(sideEffectNotUsedStatement, parent, checkSideEffects);
        problemNodes = checkSideEffects.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(sideEffectNotUsedStatement, problemNodes.get(0));

        // Test statement with side effect and isSimpleOp is true
        Node sideEffectSimpleOpStatement = IR.binop("+", IR.name("s"), IR.name("s"));
        parent.addChildToBack(sideEffectSimpleOpStatement);
        nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(sideEffectSimpleOpStatement, parent, checkSideEffects);
        problemNodes = checkSideEffects.problemNodes;
        assertTrue(problemNodes.isEmpty());
    }

    @Test
    public void testProtectSideEffectFreeCode() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        Node problemNode = IR.assign(IR.name("x"), IR.lit(42));
        Node parent = IR.block();
        parent.addChildToBack(problemNode);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(problemNode, parent, checkSideEffects);
        assertTrue(checkSideEffects.problemNodes.isEmpty());

        // Protect side-effect free nodes
        checkSideEffects.protectSideEffects();

        // Test that problem node was not modified
        Node problemNodeAfterProtection = parent.getLastChild();
        assertEquals(problemNode, problemNodeAfterProtection);

        // Test that extern was added
        CompilerInput input = compiler.getSynthesizedExternsInput();
        Node extern = null;
        for (Node node : input.getAstRoot(compiler).getChildren()) {
            if (node.isName() && node.getString().equals(CheckSideEffects.PROTECTOR_FN)) {
                extern = node;
                break;
            }
        }
        assertNotNull(extern);
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
        StripProtection stripProtection = new StripProtection(compiler);

        Node problemNode = IR.call(IR.name(CheckSideEffects.PROTECTOR_FN), IR.name("x"));
        Node parent = IR.block();
        parent.addChildToBack(problemNode);
        NodeTraversal nodeTraversal = new NodeTraversal(compiler);
        nodeTraversal.traverse(problemNode, parent, checkSideEffects);
        List<Node> problemNodes = checkSideEffects.problemNodes;
        assertEquals(1, problemNodes.size());
        assertEquals(problemNode, problemNodes.get(0));

        stripProtection.process(null, null);

        // Test that problem node was removed
        Node problemNodeAfterStripping = parent.getLastChild();
        assertNull(problemNodeAfterStripping);
    }
}