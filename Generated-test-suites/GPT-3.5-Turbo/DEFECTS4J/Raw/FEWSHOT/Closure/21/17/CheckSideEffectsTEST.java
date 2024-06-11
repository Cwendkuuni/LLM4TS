package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CheckSideEffectsTest {
    private AbstractCompiler compiler;
    private CheckLevel level;
    private boolean protectSideEffectFreeCode;
    private CheckSideEffects checkSideEffects;

    @Before
    public void setup() {
        compiler = mock(AbstractCompiler.class);
        level = mock(CheckLevel.class);
        protectSideEffectFreeCode = true;
        checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    }

    @Test
    public void testProcess() {
        Node externs = IR.root();
        Node root = IR.root();
        checkSideEffects.process(externs, root);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testHotSwapScript() {
        Node scriptRoot = IR.root();
        Node originalRoot = IR.root();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testVisit_exprResult_notRemovable() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = IR.exprResult(IR.name("x"));
        Node parent = IR.block();
        checkSideEffects.visit(t, n, parent);
        verify(compiler, times(0)).report(any(DiagnosticType.class), any(Node.class));
    }

    @Test
    public void testVisit_xxx_notRemovable() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = IR.block(IR.exprResult(IR.name("x")));
        Node parent = IR.block();
        checkSideEffects.visit(t, n, parent);
        verify(compiler, times(0)).report(any(DiagnosticType.class), any(Node.class));
    }

    @Test
    public void testVisit_exprResult_removable() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = IR.exprResult(IR.nullNode());
        Node parent = IR.block();
        checkSideEffects.visit(t, n, parent);
        verify(compiler, times(1)).report(any(DiagnosticType.class), any(Node.class));
        verify(compiler, times(1)).reportCodeChange();
        List<Node> problemNodes = Whitebox.getInternalState(checkSideEffects, "problemNodes");
        assertEquals(n, problemNodes.get(0));
    }

    @Test
    public void testVisit_qualifiedName_removable() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = IR.name("x").setJSDocInfo(mock(JSDocInfo.class));
        Node parent = IR.block();
        checkSideEffects.visit(t, n, parent);
        verify(compiler, times(0)).report(any(DiagnosticType.class), any(Node.class));
    }

    @Test
    public void testProtectSideEffects() {
        Node n = IR.nullNode();
        List<Node> problemNodes = Lists.newArrayList(n);
        Whitebox.setInternalState(checkSideEffects, "problemNodes", problemNodes);
        checkSideEffects.protectSideEffects();
        verify(compiler, times(1)).reportCodeChange();
        Node name = IR.name("JSCOMPILER_PRESERVE");
        Node replacement = IR.call(name).srcref(n);
        replacement.putBooleanProp(Node.FREE_CALL, true);
        n.getParent().replaceChild(n, replacement);
        replacement.addChildToBack(n);
        List<Node> protectedNodes = Whitebox.getInternalState(problemNodes.get(0).getParent(), "children");
        assertEquals(replacement, protectedNodes.get(protectedNodes.size() - 1));
        problemNodes = Lists.newArrayList();
        Whitebox.setInternalState(checkSideEffects, "problemNodes", problemNodes);
        checkSideEffects.protectSideEffects();
        verify(compiler, times(0)).reportCodeChange();
    }

    @Test
    public void testAddExtern() {
        Node name = IR.name("JSCOMPILER_PRESERVE");
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        Node var = IR.var(name);
        compiler.getSynthesizedExternsInput().getAstRoot(compiler).addChildrenToBack(var);
        verify(compiler, times(1)).reportCodeChange();
    }
}