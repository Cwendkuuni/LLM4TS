package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Test;

public class RemoveConstantExpressionsTest {
    @Test
    public void testRemoveConstantExpressions() {
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions removeCE = new RemoveConstantExpressions(compiler);
        
        // Test process() method
        Node externs = new Node(Token.EXTERN_NAME);
        Node root = new Node(Token.ROOT);
        removeCE.process(externs, root);
        
        // Test RemoveConstantRValuesCallback
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = removeCE.new RemoveConstantRValuesCallback();
        
        // Test visit() method
        NodeTraversal traversal = new NodeTraversal(compiler, cb);
        Node parent = new Node(Token.PARENT_NODE);
        Node node = new Node(Token.NODE_TYPE);
        cb.visit(traversal, node, parent);
        
        // Test trySimplify() method
        Node exprNode = new Node(Token.EXPR_NODE);
        Node exprResult = new Node(Token.EXPR_RESULT, exprNode);
        cb.trySimplify(parent, exprResult);
        
        // Test getSideEffectNodes() method
        List<Node> sideEffectNodes = cb.getSideEffectNodes(exprNode);
        
        // Test getResult() method
        Result result = cb.getResult();
        
        // Test ReportCodeHasChangedListener
        RemoveConstantExpressions.ReportCodeHasChangedListener listener = 
        				removeCE.new ReportCodeHasChangedListener(result);
        Node removedNode = new Node(Token.REMOVED_NODE);
        listener.nodeRemoved(removedNode);
        
        // Assertions
        // TODO: Add appropriate assertions here

        assertEquals(null, result);
        assertEquals(null, listener);                
    }
}