package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

    private static class TestSignatureStore implements SignatureStore {
        private Map<String, Node> signatures = HashMultimap.create().asMap();

        @Override
        public void reset() {
            signatures.clear();
        }

        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.put(functionName, functionNode);
        }

        @Override
        public void removeSignature(String functionName) {
            signatures.remove(functionName);
        }

        public Node getSignature(String functionName) {
            return signatures.get(functionName);
        }
    }

    @Test
    public void testProcessNoExternsOrRoot() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        methodPass.process(null, null);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.isEmpty());
    }

    @Test
    public void testProcessExternsNoRoot() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        Node externs = Node.createString("foo.bar = function() {}");
        methodPass.process(externs, null);

        assertEquals(1, methodPass.externMethods.size());
        assertEquals("foo.bar", methodPass.externMethods.iterator().next());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.containsKey("foo.bar"));

        Node signature = methodPass.getSignatureStore().getSignature("foo.bar");
        assertEquals(externs, signature);
    }

    @Test
    public void testProcessExternsAndRoot() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        Node externs = Node.createString("foo.bar = function() {}",
            "foo.baz = function() {}", "foo.qux = function() {}",
            "foo.prototype.quux = function() {}", "foo.prototype.quux2 = function() {}",
            "bar.baz = function() {}", "bar.prototype.qux = function() {});");

        Node root = Node.createString("function foo() { this.bar(); this.baz(); }");
        methodPass.process(externs, root);

        assertEquals(7, methodPass.externMethods.size());
        assertEquals("foo.bar", methodPass.externMethods.iterator().next());
        assertEquals("foo.baz", methodPass.externMethods.iterator().next());
        assertEquals("foo.prototype.quux", methodPass.externMethods.iterator().next());
        assertEquals("foo.prototype.quux2", methodPass.externMethods.iterator().next());
        assertEquals("bar.baz", methodPass.externMethods.iterator().next());
        assertEquals("bar.prototype.qux", methodPass.externMethods.iterator().next());

        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.containsKey("foo.bar"));
        assertTrue(methodPass.methodDefinitions.containsKey("foo.baz"));
        assertTrue(methodPass.methodDefinitions.containsKey("foo.prototype.quux"));
        assertTrue(methodPass.methodDefinitions.containsKey("foo.prototype.quux2"));
        assertTrue(methodPass.methodDefinitions.containsKey("bar.baz"));
        assertTrue(methodPass.methodDefinitions.containsKey("bar.prototype.qux"));

        Node fooBarSignature = methodPass.getSignatureStore().getSignature("foo.bar");
        Node fooBazSignature = methodPass.getSignatureStore().getSignature("foo.baz");
        Node fooPrototypeQuuxSignature = methodPass.getSignatureStore().getSignature("foo.prototype.quux");
        Node fooPrototypeQuux2Signature = methodPass.getSignatureStore().getSignature("foo.prototype.quux2");
        Node barBazSignature = methodPass.getSignatureStore().getSignature("bar.baz");
        Node barPrototypeQuxSignature = methodPass.getSignatureStore().getSignature("bar.prototype.qux");

        assertEquals(root, fooBarSignature.getParent());
        assertEquals(root, fooBazSignature.getParent());
        assertEquals(root, fooPrototypeQuuxSignature.getParent());
        assertEquals(root, fooPrototypeQuux2Signature.getParent());
        assertEquals(root, barBazSignature.getParent());
        assertEquals(root, barPrototypeQuxSignature.getParent());
    }

    @Test
    public void testAddPossibleSignature() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        Node root = Node.createString("function foo() { this.bar(); }");
        NodeTraversal traversal = new NodeTraversal(root);

        methodPass.addPossibleSignature("bar", root.getFirstChild().getNext(), traversal);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.containsKey("bar"));

        Node barSignature = methodPass.getSignatureStore().getSignature("bar");
        assertEquals(root, barSignature.getParent());
    }

    @Test
    public void testAddPossibleSignatureFunction() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        Node root = Node.createString("function foo() { this.bar = function() {}; }");
        NodeTraversal traversal = new NodeTraversal(root);

        methodPass.addPossibleSignature("bar", traversal.getChildAtIndex(0), traversal);

        assertEquals(1, methodPass.externMethods.size());
        assertEquals("foo.bar", methodPass.externMethods.iterator().next());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.containsKey("foo.bar"));

        Node fooBarSignature = methodPass.getSignatureStore().getSignature("foo.bar");
        assertEquals(root, fooBarSignature.getParent());
    }

    @Test
    public void testAddPossibleSignatureName() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        Node root = Node.createString("function foo() { var bar = new function() {}; }");
        NodeTraversal traversal = new NodeTraversal(root);

        methodPass.addPossibleSignature("bar", traversal.getChildAtIndex(0), traversal);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.containsKey("bar"));

        Node barSignature = methodPass.getSignatureStore().getSignature("bar");
        assertEquals(root, barSignature.getParent());
    }

    @Test
    public void testAddPossibleSignatureNonMethodProperty() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler);

        Node root = Node.createString("function foo() { var qux = 42; }");
        NodeTraversal traversal = new NodeTraversal(root);

        methodPass.addPossibleSignature("qux", traversal.getChildAtIndex(0), traversal);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.contains("qux"));
        assertTrue(methodPass.methodDefinitions.isEmpty());
    }

    @Test
    public void testGetActingCallbackReturnsNull() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler) {
            @Override
            Callback getActingCallback() {
                return null;
            }
        };

        Node root = Node.createString("function foo() {}");
        methodPass.process(null, root);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.isEmpty());
    }

    @Test
    public void testGetActingCallbackThrowsIllegalStateException() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler) {
            @Override
            Callback getActingCallback() {
                throw new IllegalStateException();
            }
        };

        Node root = Node.createString("function foo() {}");
        methodPass.process(null, root);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.isEmpty());
    }

    @Test
    public void testGetSignatureStoreReturnsNull() {
        AbstractCompiler compiler = new Compiler();
        MethodCompilerPass methodPass = new MethodCompilerPass(compiler) {
            @Override
            SignatureStore getSignatureStore() {
                return null;
            }
        };

        Node root = Node.createString("function foo() {}");
        methodPass.process(null, root);

        assertTrue(methodPass.externMethods.isEmpty());
        assertTrue(methodPass.externMethodsWithoutSignatures.isEmpty());
        assertTrue(methodPass.nonMethodProperties.isEmpty());
        assertTrue(methodPass.methodDefinitions.isEmpty());
    }
}