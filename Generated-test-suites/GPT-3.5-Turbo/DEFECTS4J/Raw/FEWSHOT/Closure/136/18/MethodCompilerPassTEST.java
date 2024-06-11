package com.google.javascript.jscomp;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {
    @Test
    public void testProcess() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(new AbstractCompiler());

        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);

        methodCompilerPass.process(externs, root);

        Set<String> expectedExternMethods = Sets.newHashSet();
        Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet();
        Set<String> expectedNonMethodProperties = Sets.newHashSet();
        Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();

        assertEquals(expectedExternMethods, methodCompilerPass.externMethods);
        assertEquals(expectedExternMethodsWithoutSignatures, methodCompilerPass.externMethodsWithoutSignatures);
        assertEquals(expectedNonMethodProperties, methodCompilerPass.nonMethodProperties);
        assertEquals(expectedMethodDefinitions, methodCompilerPass.methodDefinitions);
    }

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(new AbstractCompiler());

        Callback expected = null;
        Callback actual = methodCompilerPass.getActingCallback();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(new AbstractCompiler());

        MethodCompilerPass.SignatureStore expected = null;
        MethodCompilerPass.SignatureStore actual = methodCompilerPass.getSignatureStore();

        assertEquals(expected, actual);
    }

    @Test
    public void testAddPossibleSignature_FunctionNode() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(new AbstractCompiler());
        Node nameNode = new Node(Token.NAME, "testFunction");
        Node functionNode = new Node(Token.FUNCTION);

        methodCompilerPass.addPossibleSignature("testFunction", functionNode, nameNode, null);

        MethodCompilerPass.SignatureStore.Signature expected = new MethodCompilerPass.SignatureStore.Signature(functionNode, nameNode);
        MethodCompilerPass.SignatureStore.Signature actual = methodCompilerPass.signatureStore.getSignature("testFunction");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAddPossibleSignature_NameNode() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(new AbstractCompiler());
        Node nameNode = new Node(Token.NAME, "testFunction");
        Node functionNode = new Node(Token.FUNCTION);

        methodCompilerPass.addPossibleSignature("testFunction", nameNode, null);

        MethodCompilerPass.SignatureStore.Signature expected = new MethodCompilerPass.SignatureStore.Signature(nameNode, null);
        MethodCompilerPass.SignatureStore.Signature actual = methodCompilerPass.signatureStore.getSignature("testFunction");

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testAddSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(new AbstractCompiler());
        Node nameNode = new Node(Token.NAME, "testFunction");
        Node functionNode = new Node(Token.FUNCTION);

        methodCompilerPass.addSignature("testFunction", functionNode, nameNode, null);

        MethodCompilerPass.SignatureStore.Signature expected = new MethodCompilerPass.SignatureStore.Signature(functionNode, nameNode);
        MethodCompilerPass.SignatureStore.Signature actual = methodCompilerPass.signatureStore.getSignature("testFunction");

        assertEquals(expected.toString(), actual.toString());

        assertTrue(methodCompilerPass.methodDefinitions.containsKey("testFunction"));
        assertTrue(methodCompilerPass.methodDefinitions.containsValue(functionNode));
    }

    private class MethodCompilerPassImpl extends MethodCompilerPass {
        private SignatureStore signatureStore;

        public MethodCompilerPassImpl(AbstractCompiler compiler) {
            super(compiler);

            this.signatureStore = new SignatureStoreImpl();
        }

        public Callback getActingCallback() {
            return null;
        }

        public SignatureStore getSignatureStore() {
            return this.signatureStore;
        }

        private class SignatureStoreImpl implements SignatureStore {
            private Multimap<String, Signature> signatures;

            public SignatureStoreImpl() {
                this.signatures = HashMultimap.create();
            }

            public void reset() {
                this.signatures.clear();
            }

            public void addSignature(String functionName, Node functionNode, Node nameNode, Node signatureNode) {
                Signature signature = new Signature(functionNode, nameNode, signatureNode);
                this.signatures.put(functionName, signature);
            }

            public void removeSignature(String functionName) {
                this.signatures.removeAll(functionName);
            }

            public Signature getSignature(String functionName) {
                return this.signatures.get(functionName).iterator().next();
            }

            private class Signature {
                private Node functionNode;
                private Node nameNode;
                private Node signatureNode;

                public Signature(Node functionNode, Node nameNode, Node signatureNode) {
                    this.functionNode = functionNode;
                    this.nameNode = nameNode;
                    this.signatureNode = signatureNode;
                }

                public Node getFunctionNode() {
                    return this.functionNode;
                }

                public Node getNameNode() {
                    return this.nameNode;
                }

                public Node getSignatureNode() {
                    return this.signatureNode;
                }

                public String toString() {
                    return "Function Node: " + this.functionNode.toString() + "\n" +
                           "Name Node: " + this.nameNode.toString() + "\n" +
                           "Signature Node: " + this.signatureNode.toString();
                }
            }
        }
    }
}