package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.CheckLevel;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import org.junit.Test;

public class CheckSideEffectsTest {
    @Test
    public void testProcess() {
        // Test case 1
        CheckLevel level = CheckLevel.ERROR;
        boolean protectSideEffectFreeCode = true;
        AbstractCompiler compilerMock = new AbstractCompiler() {
            public void reportCodeChange() {}
            public CompilerInput getSynthesizedExternsInput() {
                return null;
            }
        };
        CheckSideEffects cs = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
        Node externs = null;
        Node root = null;
        cs.process(externs, root);

        // Test case 2
        NodeTraversal.traverse(compilerMock, root, cs);

        // assert something
    }

    @Test
    public void testHotSwapScript() {
        // Test case 1
        CheckLevel level = CheckLevel.ERROR;
        boolean protectSideEffectFreeCode = true;
        AbstractCompiler compilerMock = new AbstractCompiler() {
            public void reportCodeChange() {}
            public CompilerInput getSynthesizedExternsInput() {
                return null;
            }
        };
        CheckSideEffects cs = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
        Node scriptRoot = null;
        Node originalRoot = null;
        cs.hotSwapScript(scriptRoot, originalRoot);

        // assert something
    }

    @Test
    public void testVisit() {
        // Test case 1
        CheckLevel level = CheckLevel.ERROR;
        boolean protectSideEffectFreeCode = true;
        AbstractCompiler compilerMock = new AbstractCompiler() {
            public void reportCodeChange() {}
            public CompilerInput getSynthesizedExternsInput() {
                return null;
            }
        };
        CheckSideEffects cs = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        cs.visit(t, n, parent);

        // assert something
    }

    @Test
    public void testProtectSideEffects() {
        // Test case 1
        CheckLevel level = CheckLevel.ERROR;
        boolean protectSideEffectFreeCode = true;
        AbstractCompiler compilerMock = new AbstractCompiler() {
            public void reportCodeChange() {}
            public CompilerInput getSynthesizedExternsInput() {
                return null;
            }
        };
        CheckSideEffects cs = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);
        cs.problemNodes = Lists.newArrayList();

        // Test case 2
        cs.protectSideEffects();
        
        // assert something
    }

    @Test
    public void testAddExtern() {
        // Test case 1
        CheckLevel level = CheckLevel.ERROR;
        boolean protectSideEffectFreeCode = true;
        AbstractCompiler compilerMock = new AbstractCompiler() {
            public void reportCodeChange() {}
            public CompilerInput getSynthesizedExternsInput() {
                return null;
            }
        };
        CheckSideEffects cs = new CheckSideEffects(compilerMock, level, protectSideEffectFreeCode);

        // Test case 2
        cs.addExtern();

        // assert something
    }

    @Test
    public void testVisitStripProtection() {
        // Test case 1
        CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(null);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        sp.visit(t, n, parent);

        // assert something
    }

    @Test
    public void testProcessStripProtection() {
        // Test case 1
        AbstractCompiler compilerMock = new AbstractCompiler() {
            public void reportCodeChange() {}
        };
        CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compilerMock);
        Node externs = null;
        Node root = null;

        // Test case 2
        sp.process(externs, root);

        // assert something
    }
}