package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MethodCompilerPassTest {

    @Test
    public void testGetActingCallback() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        assertNotNull(methodCompilerPass.getActingCallback());
    }

    @Test
    public void testGetSignatureStore() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        assertNotNull(methodCompilerPass.getSignatureStore());
    }

    @Test
    public void testProcess() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        // pass null values as arguments just for test coverage
        methodCompilerPass.process(null, null);
    }

    @Test
    public void testAddPossibleSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return null;
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        methodCompilerPass.addPossibleSignature("name", new Node(Token.NAME), new NodeTraversal(new Compiler()));
        assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("name"));

        methodCompilerPass.addPossibleSignature("functionName", new Node(Token.FUNCTION), new NodeTraversal(new Compiler()));
        assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("functionName"));
    }

    @Test
    public void testAddSignature() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return new MethodCompilerPass.SignatureStore() {
                    @Override
                    public void reset() {

                    }

                    @Override
                    public void addSignature(String functionName, Node functionNode, String sourceFile) {

                    }

                    @Override
                    public void removeSignature(String functionName) {

                    }
                };
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        methodCompilerPass.addSignature("name", new Node(Token.NAME), "sourceFile");
        assertTrue(methodCompilerPass.methodDefinitions.containsKey("name"));
    }

    @Test
    public void testGetExternMethods() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return new MethodCompilerPass.SignatureStore() {
                    @Override
                    public void reset() {

                    }

                    @Override
                    public void addSignature(String functionName, Node functionNode, String sourceFile) {

                    }

                    @Override
                    public void removeSignature(String functionName) {

                    }
                };
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        methodCompilerPass.externMethodsWithoutSignatures.add("name");
        methodCompilerPass.externMethods.add("name");

        NodeTraversal nodeTraversal = new NodeTraversal(new Compiler());
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);

        methodCompilerPass.new GetExternMethods().visit(nodeTraversal, node, parent);

        assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("name"));
    }

    @Test
    public void testGatherSignatures() {
        MethodCompilerPass methodCompilerPass = new MethodCompilerPass() {
            @Override
            MethodCompilerPass.SignatureStore getSignatureStore() {
                return new MethodCompilerPass.SignatureStore() {
                    @Override
                    public void reset() {

                    }

                    @Override
                    public void addSignature(String functionName, Node functionNode, String sourceFile) {

                    }

                    @Override
                    public void removeSignature(String functionName) {

                    }
                };
            }

            @Override
            NodeTraversal.Callback getActingCallback() {
                return null;
            }
        };

        NodeTraversal nodeTraversal = new NodeTraversal(new Compiler());
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);

        methodCompilerPass.new GatherSignatures().visit(nodeTraversal, node, parent);

        assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    }

}