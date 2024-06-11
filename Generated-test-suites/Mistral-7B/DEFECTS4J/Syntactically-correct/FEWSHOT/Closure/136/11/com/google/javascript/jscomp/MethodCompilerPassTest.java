package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

    static class TestMethodCompilerPass extends MethodCompilerPass {
        // Override abstract methods
        @Override
        Callback getActingCallback() {
            return null; // Not used in this test
        }

        @Override
        SignatureStore getSignatureStore() {
            return new TestSignatureStore();
        }
    }

    static class TestSignatureStore implements MethodCompilerPass.SignatureStore {
        Map<String, List<Node>> signatures = HashMultimap.create();

        @Override
        public void reset() {
            signatures.clear();
        }

        @Override
        public void addSignature(String functionName, Node functionNode, String sourceFile) {
            List<Node> functions = signatures.get(functionName);
            if (functions == null) {
                functions = Lists.newArrayList();
                signatures.put(functionName, functions);
            }
            functions.add(functionNode);
        }

        @Override
        public void removeSignature(String functionName) {
            List<Node> functions = signatures.remove(functionName);
            if (functions != null) {
                functions.clear();
            }
        }
    }

    @Test
    public void testGetExternMethods() {
        Node externs = createNode("externs: {\n" +
                "  foo: function() {}, // signature present\n" +
                "  bar: function() {}\n" + // signature absent
                "  baz: function baz() {}\n" + // name mismatch
                "  qux: function qux(x) {}\n" + // signature present
                "}");

        TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
        pass.process(externs, null);

        assertEquals(4, pass.externMethods.size());
        assertEquals("foo", pass.externMethods.iterator().next());
        assertEquals("qux", pass.externMethods.iterator().next());
        assertFalse(pass.externMethods.contains("bar"));
        assertFalse(pass.externMethods.contains("baz"));

        assertEquals(2, pass.methodDefinitions.size());
        assertSame(pass.methodDefinitions.get("foo"), getNode("function () {}"));
        assertSame(pass.methodDefinitions.get("qux"), getNode("function qux(x) {}"));

        // Check that functions without signatures are not warned about
        assertEquals(2, pass.externMethodsWithoutSignatures.size());
        assertEquals("bar", pass.externMethodsWithoutSignatures.iterator().next());
        assertEquals("baz", pass.externMethodsWithoutSignatures.iterator().next());

        // Check that non-method properties are not added to methodDefinitions
        assertEquals(0, pass.nonMethodProperties.size());
    }

    @Test
    public void testGatherSignatures() {
        Node root = createNode("function foo() {\n" +
                "  function bar() {}\n" +
                "  function baz() {}\n" +
                "  proto.qux = function() {}\n" +
                "}");

        TestMethodCompilerPass pass = new TestMethodCompilerPass(null);
        pass.process(null, root);

        assertEquals(4, pass.externMethods.size()); // empty externs are considered externs
        assertEquals("bar", pass.externMethods.iterator().next());
        assertEquals("baz", pass.externMethods.iterator().next());
        assertEquals("qux", pass.externMethods.iterator().next());
        assertEquals("foo", pass.externMethods.iterator().next());

        assertEquals(4, pass.methodDefinitions.size());
        assertSame(pass.methodDefinitions.get("bar"), getNode("function () {}"));
        assertSame(pass.methodDefinitions.get("baz"), getNode("function () {}"));
        assertSame(pass.methodDefinitions.get("qux"), getNode("function () {}"));
        assertSame(pass.methodDefinitions.get("foo"), getNode("function foo() {}"));

        assertEquals(0, pass.externMethodsWithoutSignatures.size());
        assertEquals(0, pass.nonMethodProperties.size());

        // Check that prototype methods are gathered correctly
        assertEquals(1, pass.signatures.size());
        List<Node> quxSignatures = pass.signatures.get("qux");
        assertSame(quxSignatures.get(0), getNode("function () {}"));
    }

    private Node createNode(String source) {
        RhinoRuntime runtime = new RhinoRuntime();
        Node root = runtime.eval(source);
        return root;
    }

    private Node getNode(String nodeType) {
        RhinoRuntime runtime = new RhinoRuntime();
        Node node = runtime.newNode(nodeType);
        return node;
    }
}