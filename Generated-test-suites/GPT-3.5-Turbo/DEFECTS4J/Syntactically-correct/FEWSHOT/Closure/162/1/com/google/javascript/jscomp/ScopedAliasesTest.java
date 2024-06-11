package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
    
    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        scopedAliases = new ScopedAliases(new AbstractCompiler(), null, new AliasTransformationHandler());
    }

    @Test
    public void testProcess() {
        scopedAliases.process(new Node(), new Node());
    }

    @Test
    public void testHotSwapScript() {
        scopedAliases.hotSwapScript(new Node(), new Node());
    }
    
    @Test
    public void testAliasedNode() {
        Node aliasReference = new Node(Token.NAME);
        Node aliasDefinition = new Node(Token.STRING);
        AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);
        
        aliasedNode.applyAlias();
        
        assertEquals(aliasDefinition, aliasReference.getParent());
    }
    
    @Test
    public void testAliasedTypeNode() {
        Node aliasReference = new Node(Token.STRING);
        String correctedType = "newType";
        AliasedTypeNode aliasedTypeNode = new AliasedTypeNode(aliasReference, correctedType);
        
        aliasedTypeNode.applyAlias();
        
        assertEquals(correctedType, aliasReference.getString());
    }
    
    @Test
    public void testInvalidScopeCall() {
        Node callNode = new Node(Token.CALL);
        callNode.addChildToFront(new Node(Token.NAME, new Node(Token.STRING, "invalidScopeCall")));
        callNode.addChildToFront(new Node(Token.STRING, "var"));
        Traversal traversal = new ScopedAliases(null, null, null).new Traversal();
        
        traversal.validateScopeCall(null, callNode, null);
        
        assertEquals(0, traversal.getScopeCalls().size());
        assertEquals(true, traversal.hasErrors());
    }

    @Test
    public void testValidScopeCall() {
        Node callNode = new Node(Token.CALL);
        callNode.addChildToFront(new Node(Token.NAME, new Node(Token.STRING, "validScopeCall")));
        callNode.addChildToFront(new Node(Token.FUNCTION));
        Traversal traversal = new ScopedAliases(null, null, null).new Traversal();
        
        traversal.validateScopeCall(null, callNode, null);
        
        assertEquals(1, traversal.getScopeCalls().size());
        assertEquals(false, traversal.hasErrors());
    }
    
    @Test
    public void testShouldTraverse() {
        Node node = new Node(Token.AND);
        Node parent = new Node(Token.OR);
        NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), null);
        
        boolean shouldTraverse = new ScopedAliases(null, null, null).new Traversal().shouldTraverse(traversal, node, parent);
        
        assertEquals(true, shouldTraverse);
    }
    
    @Test
    public void testShouldNotTraverse() {
        Node node = new Node(Token.AND);
        Node parent = new Node(Token.FUNCTION);
        NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), null);
        
        boolean shouldTraverse = new ScopedAliases(null, null, null).new Traversal().shouldTraverse(traversal, node, parent);
        
        assertEquals(false, shouldTraverse);
    }
    
    @Test
    public void testVisit() {
        NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(), null);
        Node node = new Node(Token.CALL);
        Node parent = new Node(Token.FUNCTION);
        scopedAliases.new Traversal().visit(traversal, node, parent);
    }
}