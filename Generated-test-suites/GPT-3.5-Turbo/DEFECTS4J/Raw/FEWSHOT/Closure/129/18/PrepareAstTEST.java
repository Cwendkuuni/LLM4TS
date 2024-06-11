package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
    
    private PrepareAst prepareAst;
    
    @Before
    public void setUp() {
        prepareAst = new PrepareAst(new AbstractCompiler());
    }
    
    @Test
    public void testProcess_withNullExternsAndRoot() {
        try {
            prepareAst.process(null, null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testProcess_withNonNullExternsAndRoot() {
        try {
            prepareAst.process(IR.root(), IR.root());
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testNormalizeNodeTypes_withNullNode() {
        try {
            prepareAst.normalizeNodeTypes(null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testNormalizeNodeTypes_withNonNullNode() {
        try {
            prepareAst.normalizeNodeTypes(IR.root());
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testNormalizeBlocks_withNullNode() {
        try {
            prepareAst.normalizeBlocks(null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testNormalizeBlocks_withNonNullNode() {
        try {
            prepareAst.normalizeBlocks(IR.block());
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testPrepareAnnotations_withNullNodeTraversal() {
        try {
            PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
            prepareAnnotations.shouldTraverse(null, null, null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testPrepareAnnotations_withNullNode() {
        try {
            PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
            prepareAnnotations.shouldTraverse(new NodeTraversal(), null, null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testPrepareAnnotations_withNonNullNode() {
        try {
            PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
            prepareAnnotations.shouldTraverse(new NodeTraversal(), IR.empty(), null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testVisit_withNullNodeTraversal() {
        try {
            PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
            prepareAnnotations.visit(null, null, null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testVisit_withNullNode() {
        try {
            PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
            prepareAnnotations.visit(new NodeTraversal(), null, null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

    @Test
    public void testVisit_withNonNullNode() {
        try {
            PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
            prepareAnnotations.visit(new NodeTraversal(), IR.empty(), null);
        } catch (Exception e) {
            assertFalse(e.getMessage(), true);
        }
    }

}