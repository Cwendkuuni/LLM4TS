package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
    private MethodCompilerPass methodCompilerPass;
    private MockSignatureStore signatureStore;
    
    @Before
    public void setUp() {
        signatureStore = new MockSignatureStore();
        methodCompilerPass = new MockMethodCompilerPass(signatureStore);
    }
    
    @Test
    public void testProcess_withNullExternsAndRoot_shouldResetSignatureStoreAndMethodDefinitions() {
        methodCompilerPass.process(null, null);
        
        assertEquals(0, signatureStore.resetCalledTimes);
        assertEquals(0, signatureStore.removeSignatureCalledTimes);
        assertEquals(0, signatureStore.addSignatureCalledTimes);
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
    }
    
    @Test
    public void testProcess_withNonNullExternsAndRoot_shouldResetSignatureStoreAndMethodDefinitions() {
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        methodCompilerPass.process(externs, root);
        
        assertEquals(1, signatureStore.resetCalledTimes);
        assertEquals(0, signatureStore.removeSignatureCalledTimes);
        assertEquals(0, signatureStore.addSignatureCalledTimes);
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
    }
    
    @Test
    public void testGetActingCallback_shouldReturnNull() {
        assertTrue(methodCompilerPass.getActingCallback() == null);
    }
    
    @Test
    public void testGetSignatureStore_shouldReturnSignatureStore() {
        assertTrue(methodCompilerPass.getSignatureStore() == signatureStore);
    }
    
    @Test
    public void testAddPossibleSignature_withNonFunctionNode_shouldAddToNonMethodProperties() {
        Node node = new Node(Token.STRING, "name");
        NodeTraversal traversal = new NodeTraversal(null, null, null, null);
        
        methodCompilerPass.addPossibleSignature("name", node, traversal);
        
        assertTrue(methodCompilerPass.nonMethodProperties.contains("name"));
        assertEquals(0, signatureStore.addSignatureCalledTimes);
    }
    
    @Test
    public void testAddPossibleSignature_withFunctionNode_shouldAddToMethodDefinitionsAndCallAddSignature() {
        Node node = new Node(Token.FUNCTION);
        NodeTraversal traversal = new NodeTraversal(null, null, null, null);
        
        methodCompilerPass.addPossibleSignature("name", node, traversal);
        
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("name"));
        assertEquals(1, signatureStore.addSignatureCalledTimes);
    }
    
    
    private class MockMethodCompilerPass extends MethodCompilerPass {
        public MockMethodCompilerPass(SignatureStore signatureStore) {
            super(null);
        }
        
        @Override
        Callback getActingCallback() {
            return null;
        }
        
        @Override
        SignatureStore getSignatureStore() {
            return null;
        }
    }
    
    private class MockSignatureStore implements SignatureStore {
        public int resetCalledTimes = 0;
        public int removeSignatureCalledTimes = 0;
        public int addSignatureCalledTimes = 0;
        
        @Override
        public void reset() {
            resetCalledTimes++;
        }
        
        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
            addSignatureCalledTimes++;
        }
        
        @Override
        public void removeSignature(String functionName) {
            removeSignatureCalledTimes++;
        }
    }
}