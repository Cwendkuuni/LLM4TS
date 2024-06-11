package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MethodCompilerPassTest {

    @Test
    public void testGetActingCallback() {
        MethodCompilerPassTest methodCompilerPassTest = new MethodCompilerPassTest();
        Callback callback = methodCompilerPassTest.getActingCallback();
        assertTrue(callback instanceof Callback);
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPassTest methodCompilerPassTest = new MethodCompilerPassTest();
        SignatureStore signatureStore = methodCompilerPassTest.getSignatureStore();
        assertTrue(signatureStore instanceof SignatureStore);
    }

    @Test
    public void testAddSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());
        Node functionNode = new Node(Token.FUNCTION);
        String functionName = "testFunction";
        methodCompilerPass.addSignature(functionName, functionNode, "testFile.js");
        assertTrue(methodCompilerPass.methodDefinitions.containsKey(functionName));
    }

    @Test
    public void testAddPossibleSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());
        Node functionNode = new Node(Token.FUNCTION);
        String functionName = "testFunction";
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new Callback());
        methodCompilerPass.addPossibleSignature(functionName, functionNode, t);
        assertTrue(methodCompilerPass.methodDefinitions.containsKey(functionName));
    }

    @Test
    public void testProcess() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());
        Node externs = new Node(Token.SCRIPT);
        Node root = new Node(Token.SCRIPT);
        methodCompilerPass.process(externs, root);

        assertEquals(methodCompilerPass.externMethods.size(), 0);
        assertEquals(methodCompilerPass.externMethodsWithoutSignatures.size(), 0);
        assertEquals(methodCompilerPass.nonMethodProperties.size(), 0);
        assertEquals(methodCompilerPass.methodDefinitions.size(), 0);
    }
}