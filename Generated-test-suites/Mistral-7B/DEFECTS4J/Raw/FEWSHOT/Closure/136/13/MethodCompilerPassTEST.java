package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitors;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {
    @Test
    public void testExternMethods() {
        MethodCompilerPass pass = new MethodCompilerPass(null) {
            @Override
            AbstractCompiler getCompiler() {
                throw new UnsupportedOperationException();
            }

            @Override
            SignatureStore getSignatureStore() {
                return new SignatureStore() {
                    Multimap<String, Node> signatures = HashMultimap.create();

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

                    public Map<String, Node> getSignatures() {
                        return signatures;
                    }
                };
            }

            @Override
            Callback getActingCallback() {
                throw new UnsupportedOperationException();
            }
        };

        Node externs = NodeVisitors.parseJs("function foo() {}", null);
        Node root = NodeVisitors.parseJs("function bar() {}", null);

        pass.process(externs, root);

        Set<String> expectedExternMethods = Sets.newHashSet("foo");
        Set<String> actualExternMethods = Sets.newHashSet();
        for (String functionName : pass.externMethods) {
            actualExternMethods.add(functionName);
        }
        assertThat(actualExternMethods).containsExactlyElementsIn(expectedExternMethods);

        Map<String, Node> signatures = pass.getSignatureStore().getSignatures();
        assertEquals("Should not have any signatures in signatures map", 0, signatures.size());
    }

    @Test
    public void testNonMethodProperties() {
        MethodCompilerPass pass = new MethodCompilerPass(null) {
            @Override
            AbstractCompiler getCompiler() {
                throw new UnsupportedOperationException();
            }

            @Override
            SignatureStore getSignatureStore() {
                throw new UnsupportedOperationException();
            }

            @Override
            Callback getActingCallback() {
                throw new UnsupportedOperationException();
            }

            @Override
            Set<String> getNonMethodProperties() {
                return Sets.newHashSet("foo");
            }
        };

        Node externs = NodeVisitors.parseJs("function foo() {}", null);
        Node root = NodeVisitors.parseJs("var x = foo;", null);

        pass.process(externs, root);

        Set<String> expectedNonMethodProperties = Sets.newHashSet("foo");
        Set<String> actualNonMethodProperties = Sets.newHashSet();
        for (String propertyName : pass.nonMethodProperties) {
            actualNonMethodProperties.add(propertyName);
        }
        assertThat(actualNonMethodProperties).containsExactlyElementsIn(expectedNonMethodProperties);
    }

    @Test
    public void testMethodDefinitions() {
        MethodCompilerPass pass = new MethodCompilerPass(null) {
            @Override
            AbstractCompiler getCompiler() {
                throw new UnsupportedOperationException();
            }

            @Override
            SignatureStore getSignatureStore() {
                throw new UnsupportedOperationException();
            }

            @Override
            Callback getActingCallback() {
                throw new UnsupportedOperationException();
            }

            @Override
            Multimap<String, Node> getMethodDefinitions() {
                return HashMultimap.create();
            }
        };

        Node externs = NodeVisitors.parseJs("function foo() {}", null);
        Node root = NodeVisitors.parseJs("function bar() {} function baz() {}", null);

        pass.process(externs, root);

        Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
        expectedMethodDefinitions.put("bar", root.getFirstChild());
        expectedMethodDefinitions.put("baz", root.getLastChild());

        Multimap<String, Node> actualMethodDefinitions = pass.getMethodDefinitions();
        assertThat(actualMethodDefinitions).isEqualTo(expectedMethodDefinitions);
    }

    @Test
    public void testExternMethodsWithoutSignatures() {
        MethodCompilerPass pass = new MethodCompilerPass(null) {
            @Override
            AbstractCompiler getCompiler() {
                throw new UnsupportedOperationException();
            }

            @Override
            SignatureStore getSignatureStore() {
                throw new UnsupportedOperationException();
            }

            @Override
            Callback getActingCallback() {
                throw new UnsupportedOperationException();
            }

            @Override
            Set<String> getExternMethodsWithoutSignatures() {
                return Sets.newHashSet("foo");
            }
        };

        Node externs = NodeVisitors.parseJs("function foo() {}", null);
        Node root = NodeVisitors.parseJs("function bar() {}", null);

        pass.process(externs, root);

        Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet("foo");
        Set<String> actualExternMethodsWithoutSignatures = Sets.newHashSet();
        for (String methodName : pass.externMethodsWithoutSignatures) {
            actualExternMethodsWithoutSignatures.add(methodName);
        }
        assertThat(actualExternMethodsWithoutSignatures).containsExactlyElementsIn(expectedExternMethodsWithoutSignatures);

        Map<String, Node> signatures = pass.getSignatureStore().getSignatures();
        assertEquals("Should not have any signatures in signatures map", 0, signatures.size());
    }

    @Test
    public void testEmptyProcess() {
        MethodCompilerPass pass = new MethodCompilerPass(null) {
            @Override
            AbstractCompiler getCompiler() {
                throw new UnsupportedOperationException();
            }

            @Override
            SignatureStore getSignatureStore() {
                throw new UnsupportedOperationException();
            }

            @Override
            Callback getActingCallback() {
                throw new UnsupportedOperationException();
            }
        };

        pass.process(null, null);

        // No assertions here as there's nothing to test in an empty process
    }

    @Test
    public void testNullParent() {
        MethodCompilerPass pass = new MethodCompilerPass(null) {
            @Override
            AbstractCompiler getCompiler() {
                throw new UnsupportedOperationException();
            }

            @Override
            SignatureStore getSignatureStore() {
                throw new UnsupportedOperationException();
            }

            @Override
            Callback getActingCallback() {
                throw new UnsupportedOperationException();
            }

            @Override
            void addPossibleSignature(String name, Node node, NodeTraversal t) {
                throw new UnsupportedOperationException();
            }
        };

        NodeTraversal traversal = NodeTraversal.create();
        Node node = Node.newNode(Token.NAME, "foo");

        pass.addPossibleSignature(node, null, traversal);

        // Should not reach this point
        assertNull("addPossibleSignature() should not be called with a null parent");
    }
}