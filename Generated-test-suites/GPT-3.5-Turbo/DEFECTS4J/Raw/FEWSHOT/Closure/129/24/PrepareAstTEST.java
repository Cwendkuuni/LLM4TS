package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrepareAstTest {
    @Test
    public void testNormalizeNodeTypes() {
        // Test case 1: Normalizing EXPR_VOID to EXPR_RESULT
        Node node1 = IR.exprResult(IR.string("test")).srcref(0);
        PrepareAst.prepareNodeTypes(node1);
        assertEquals(Node.Token.EXPR_RESULT, node1.getToken());
        
        // Test case 2: Normalizing blocks for IF, WHILE, DO
        Node node2 = IR.ifNode(IR.trueNode(), IR.block(IR.string("test"))).srcref(1);
        PrepareAst.normalizeBlocks(node2);
        assertEquals(Node.Token.BLOCK, node2.getChildAtIndex(1).getToken());
        
        Node node3 = IR.whileNode(IR.trueNode(), IR.string("test")).srcref(2);
        PrepareAst.normalizeBlocks(node3);
        assertEquals(Node.Token.BLOCK, node3.getChildAtIndex(1).getToken());
        
        Node node4 = IR.doNode(IR.string("test"), IR.trueNode()).srcref(3);
        PrepareAst.normalizeBlocks(node4);
        assertEquals(Node.Token.BLOCK, node4.getChildAtIndex(0).getToken());
    }

    @Test
    public void testPrepareAnnotations() {
        // Test case 1: Checking if dispatchers are annotated correctly
        Node parent1 = IR.assign(IR.name("foo"), IR.function(IR.name(""), IR.paramList(), IR.string("test"))).srcref(4);
        Node node1 = parent1.getLastChild();
        PrepareAst.prepareAnnotations(parent1, node1);
        assertNull(node1.getBooleanProp(Node.IS_DISPATCHER));
        
        Node parent2 = IR.assign(IR.name("bar"), IR.function(IR.name(""), IR.paramList(), IR.string("test"))).srcref(5);
        Node node2 = parent2.getLastChild();
        parent2.setJSDocInfo(new JSDocInfoBuilder(true).build());
        PrepareAst.prepareAnnotations(parent2, node2);
        assertEquals(true, node2.getBooleanProp(Node.IS_DISPATCHER));
        
        // Test case 2: Checking if free calls and direct evals are annotated correctly
        Node node3 = IR.call(IR.name("func"), IR.nullNode()).srcref(6);
        PrepareAst.prepareAnnotations(node3);
        assertEquals(true, node3.getBooleanProp(Node.FREE_CALL));
        assertNull(node3.getBooleanProp(Node.DIRECT_EVAL));
        
        Node node4 = IR.call(IR.name("eval"), IR.nullNode()).srcref(7);
        PrepareAst.prepareAnnotations(node4);
        assertNull(node4.getBooleanProp(Node.FREE_CALL));
        assertEquals(true, node4.getBooleanProp(Node.DIRECT_EVAL));
    }

    @Test
    public void testProcess() {
        // Test case 1: Checking if PrepareAnnotations is called for externs
        PrepareAst prepareAst1 = new PrepareAst(new AbstractCompiler());
        Node externs1 = IR.externs(IR.string("test"));
        Node root1 = null;
        PrepareAst.process(externs1, root1, prepareAst1);
        assertEquals(Node.Token.BLOCK, externs1.getFirstChild().getToken());
        
        // Test case 2: Checking if PrepareAnnotations is called for root
        PrepareAst prepareAst2 = new PrepareAst(new AbstractCompiler());
        Node externs2 = null;
        Node root2 = IR.root(IR.string("test"));
        PrepareAst.process(externs2, root2, prepareAst2);
        assertEquals(Node.Token.BLOCK, root2.getFirstChild().getToken());
    }
}