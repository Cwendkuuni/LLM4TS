package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

    private MethodCompilerPass methodCompilerPass;

    @Before
    public void setUp() {
        methodCompilerPass = new MethodCompilerPass(new AbstractCompiler());
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        methodCompilerPass.process(externs, root);
        assertEmpty(methodCompilerPass.externMethods);
        assertEmpty(methodCompilerPass.externMethodsWithoutSignatures);
        assertEmpty(methodCompilerPass.nonMethodProperties);
        assertEmpty(methodCompilerPass.methodDefinitions);
    }

    @Test
    public void testGetActingCallback() {
        Callback callback = methodCompilerPass.getActingCallback();
        assertNotNull(callback);
    }

    @Test
    public void testGetSignatureStore() {
        SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
        assertNotNull(signatureStore);
    }

    @Test
    public void testAddPossibleSignatureFunction() {
        Node node = new Node(Token.FUNCTION);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler());
        methodCompilerPass.addPossibleSignature("name", node, t);
        SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
        Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;
        assertTrue(signatureStore instanceof SignatureStore);
        assertEquals(node, methodDefinitions.get("name").get(0));
    }

    @Test
    public void testAddPossibleSignatureNameNotFound() {
        Node node = new Node(Token.NAME);
        NodeTraversal t = new NodeTraversal(new AbstractCompiler());
        methodCompilerPass.addPossibleSignature("name", node, t);
        SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
        Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;
        assertTrue(signatureStore instanceof SignatureStore);
        assertEmpty(methodDefinitions.get("name"));
        assertTrue(methodCompilerPass.nonMethodProperties.contains("name"));
    }

    @Test
    public void testAddSignature() {
        Node function = new Node(Token.FUNCTION);
        String fnSourceName = "source";
        methodCompilerPass.addSignature("name", function, fnSourceName);
        SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
        Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;
        assertTrue(signatureStore instanceof SignatureStore);
        assertEquals(function, methodDefinitions.get("name").get(0));
    }

    private void assertEmpty(Set set) {
        assertTrue(set.isEmpty());
    }

    private void assertEmpty(Multimap multimap) {
        assertTrue(multimap.isEmpty());
    }
}