package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PrepareAstTest {
    @Test
    public void testNormalizeNodeTypes() {
        // Test case 1
        Node rootNode1 = IR.root(IR.string("Hello World"));
        PrepareAst prepareAst1 = new PrepareAst(new AbstractCompiler());
        prepareAst1.normalizeNodeTypes(rootNode1);
        assertEquals(Node.Type.STRING, rootNode1.getType());
        
        // Test case 2
        Node rootNode2 = IR.root(IR.exprResult(IR.var(IR.name("x"))));
        PrepareAst prepareAst2 = new PrepareAst(new AbstractCompiler());
        prepareAst2.normalizeNodeTypes(rootNode2);
        assertEquals(Node.Type.VAR, rootNode2.getFirstChild().getType());
    }

    @Test
    public void testNormalizeBlocks() {
        // Test case 1
        Node node1 = IR.block(IR.empty());
        Node rootNode1 = IR.root(node1);
        PrepareAst prepareAst1 = new PrepareAst(new AbstractCompiler());
        prepareAst1.normalizeBlocks(rootNode1);
        assertEquals(Node.Type.EMPTY, node1.getType());
        
        // Test case 2
        Node ifNode = IR.ifNode(IR.trueNode(), IR.returnNode(IR.number(1)));
        Node node2 = IR.block(ifNode);
        Node rootNode2 = IR.root(node2);
        PrepareAst prepareAst2 = new PrepareAst(new AbstractCompiler());
        prepareAst2.normalizeBlocks(rootNode2);
        assertEquals(Node.Type.BLOCK, ifNode.getParent().getType());
    }

    @Test
    public void testPrepareAnnotations() {
        // Test case 1
        Node rootNode1 = IR.root(
            IR.exprResult(
                IR.call(
                    IR.getprop(IR.name("person"), IR.string("getName"))
                )
            )
        );
        PrepareAst prepareAst1 = new PrepareAst(new AbstractCompiler());
        PrepareAst.PrepareAnnotations prepareAnnotations1 = new PrepareAst.PrepareAnnotations();
        prepareAnnotations1.shouldTraverse(new NodeTraversal(new AbstractCompiler(), prepareAnnotations1), rootNode1, null);
        assertEquals(Node.FREE_CALL, rootNode1.getFirstChild().getPropertyKeys().iterator().next());
        
        // Test case 2
        Node rootNode2 = IR.root(
            IR.function(IR.name(""), IR.paramList(), IR.block())
        );
        PrepareAst prepareAst2 = new PrepareAst(new AbstractCompiler());
        PrepareAst.PrepareAnnotations prepareAnnotations2 = new PrepareAst.PrepareAnnotations();
        prepareAnnotations2.visit(new NodeTraversal(new AbstractCompiler(), prepareAnnotations2), rootNode2, null);
        assertNull(rootNode2.getPropertyKeys());
    }

    @Test
    public void testProcess() {
        // Test case 1
        Node rootNode1 = IR.root(
            IR.exprResult(
                IR.call(
                    IR.getprop(IR.name("person"), IR.string("getName"))
                )
            )
        );
        Node externsNode1 = null;
        PrepareAst prepareAst1 = new PrepareAst(new AbstractCompiler());
        prepareAst1.process(externsNode1, rootNode1);
        assertEquals(Node.FREE_CALL, rootNode1.getFirstChild().getPropertyKeys().iterator().next());
        
        // Test case 2
        Node externsNode2 = null;
        Node rootNode2 = IR.root(
            IR.function(IR.name(""), IR.paramList(), IR.block())
        );
        PrepareAst prepareAst2 = new PrepareAst(new AbstractCompiler());
        prepareAst2.process(externsNode2, rootNode2);
        assertNull(rootNode2.getPropertyKeys());
    }

    @Test
    public void testConstructor() {
        // Test case 1
        AbstractCompiler compiler1 = new AbstractCompiler();
        boolean checkOnly1 = false;
        PrepareAst prepareAst1 = new PrepareAst(compiler1);
        assertEquals(compiler1, prepareAst1.compiler);
        assertEquals(checkOnly1, prepareAst1.checkOnly);
        
        // Test case 2
        AbstractCompiler compiler2 = new AbstractCompiler();
        boolean checkOnly2 = true;
        PrepareAst prepareAst2 = new PrepareAst(compiler2, checkOnly2);
        assertEquals(compiler2, prepareAst2.compiler);
        assertEquals(checkOnly2, prepareAst2.checkOnly);
    }
}