
package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckSideEffectsTest {
    private AbstractCompiler compiler;
    private CheckSideEffects checkSideEffects;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        checkSideEffects.process(externs, root);
        
        // Assert code changes
        assertEquals(true, compiler.hasCodeChanged());
    }
    
    @Test
    public void testHotSwapScript() {
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        
        // Assert code changes
        assertEquals(true, compiler.hasCodeChanged());
    }
    
    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal(compiler, new TestCallback());
        Node n = new Node(Token.STRING);
        Node parent = new Node(Token.EXPR_RESULT);
        checkSideEffects.visit(t, n, parent);
        
        // Assert code changes
        assertEquals(true, compiler.hasCodeChanged());
    }
    
    @Test
    public void testProtectSideEffects() {
        List<Node> problemNodes = Lists.newArrayList();
        Node n = new Node(Token.STRING);
        problemNodes.add(n);
        checkSideEffects.protectSideEffects();
        
        // Assert code changes
        assertEquals(true, compiler.hasCodeChanged());
    }
    
    @Test
    public void testAddExtern() {
        checkSideEffects.addExtern();
        
        // Assert code changes
        assertEquals(true, compiler.hasCodeChanged());
    }
    
    @Test
    public void testStripProtection() {
        AbstractPostOrderCallback stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        stripProtection.process(externs, root);
        
        // Assert code changes
        assertEquals(true, compiler.hasCodeChanged());
    }
}

class TestCallback extends NodeTraversal.AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
        // Do nothing
    }
}
