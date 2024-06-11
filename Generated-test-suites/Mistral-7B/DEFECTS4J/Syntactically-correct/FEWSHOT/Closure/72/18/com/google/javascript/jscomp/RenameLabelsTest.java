package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class RenameLabelsTest {

    @Test
    public void testProcess() {
        String input = "function bar() {" +
                "  a: {" +
                "    b: {" +
                "      foo();" +
                "    }" +
                "  }" +
                "  a: {" +
                "    b: break a;" +
                "  }" +
                "}";

        AbstractCompiler compiler = new AbstractCompiler() {
            @Override
            public Node parse(Node externs, Node root, Node sourceFile) {
                return root;
            }
        };

        RenameLabels renameLabels = new RenameLabels(compiler);
        Node ast = compiler.parse(null, input, null);
        renameLabels.process(null, ast);

        Node expected = new Node() {
            @Override
            public int getType() {
                return Token.PROGRAM;
            }
        };

        expected.addChild(new Node() {
            @Override
            public int getType() {
                return Token.FUNCTION;
            }

            @Override
            public String getIdentifier() {
                return "bar";
            }

            @Override
            public Node getBody() {
                Node body = new Node() {
                    @Override
                    public int getType() {
                        return Token.BLOCK;
                    }
                };

                body.addChild(new Node() {
                    @Override
                    public int getType() {
                        return Token.LABEL;
                    }

                    @Override
                    public String getIdentifier() {
                        return "c";
                    }

                    @Override
                    public Node getBody() {
                        return new Node() {
                            @Override
                            public int getType() {
                                return Token.BLOCK;
                            }

                            @Override
                            public Node getFirstChild() {
                                return new Node() {
                                    @Override
                                    public int getType() {
                                        return Token.CALL;
                                    }

                                    @Override
                                    public Node getChildNodes() {
                                        Node args = new Node() {
                                            @Override
                                            public int getType() {
                                                return Token.ARRAY;
                                            }
                                        };

                                        args.addChild(new Node() {
                                            @Override
                                            public int getType() {
                                                return Token.STRING_LITERAL;
                                            }

                                            @Override
                                            public String getString() {
                                                return "foo";
                                            }
                                        });

                                        return args;
                                    }
                                };
                            }
                        };
                    }
                });

                body.addChild(new Node() {
                    @Override
                    public int getType() {
                        return Token.LABEL;
                    }

                    @Override
                    public String getIdentifier() {
                        return "d";
                    }
                });

                body.addChild(new Node() {
                    @Override
                    public int getType() {
                        return Token.BREAK;
                    }

                    @Override
                    public Node getFirstChild() {
                        Node label = new Node() {
                            @Override
                            public int getType() {
                                return Token.IDENTIFIER;
                            }

                            @Override
                            public String getIdentifier() {
                                return "d";
                            }
                        };

                        return label;
                    }
                });

                return body;
            }
        });

        assertEquals(expected.toString(), ast.toString());
    }

    @Test
    public void testProcessWithRemoveUnused() {
        String input = "function bar() {" +
                "  a: {" +
                "    b: {" +
                "      foo();" +
                "    }" +
                "  }" +
                "  a: {" +
                "    b: break a;" +
                "  }" +
                "}";

        AbstractCompiler compiler = new AbstractCompiler() {
            @Override
            public Node parse(Node externs, Node root, Node sourceFile) {
                return root;
            }
        };

        RenameLabels renameLabels = new RenameLabels(compiler, () -> "x", false);
        Node ast = compiler.parse(null, input, null);
        renameLabels.process(null, ast);

        Node expected = new Node() {
            @Override
            public int getType() {
                return Token.PROGRAM;
            }
        };

        expected.addChild(new Node() {
            @Override
            public int getType() {
                return Token.FUNCTION;
            }

            @Override
            public String getIdentifier() {
                return "bar";
            }

            @Override
            public Node getBody() {
                Node body = new Node() {
                    @Override
                    public int getType() {
                        return Token.BLOCK;
                    }
                };

                body.addChild(new Node() {
                    @Override
                    public int getType() {
                        return Token.LABEL;
                    }

                    @Override
                    public String getIdentifier() {
                        return "d";
                    }
                });

                body.addChild(new Node() {
                    @Override
                    public int getType() {
                        return Token.BREAK;
                    }

                    @Override
                    public Node getFirstChild() {
                        Node label = new Node() {
                            @Override
                            public int getType() {
                                return Token.IDENTIFIER;
                            }

                            @Override
                            public String getIdentifier() {
                                return "d";
                            }
                        };

                        return label;
                    }
                });

                return body;
            }
        });

        Map<String, LabelInfo> renamedLabels = Maps.newHashMap();
        NodeTraversal traversal = new NodeTraversal(compiler) {
            @Override
            public void visitLabel(NodeTraversal nodeTraversal, Node node, Node parent) {
                String name = node.getFirstChild().getString();
                LabelInfo li = new LabelInfo(renamedLabels.size() + 1);
                renamedLabels.put(name, li);
                node.getFirstChild().setString(li.id.toString());
                super.visitLabel(nodeTraversal, node, parent);
            }
        };
        Node processedNode = new Node();
        traversal.traverse(compiler, ast, processedNode);
        assertEquals(expected.toString(), processedNode.toString());

        assertEquals(1, renamedLabels.size());
        LabelInfo li = renamedLabels.get("a");
        assertEquals(2, li.id);

        assertNull(renameLabels.get("b"));
    }
}

class LabelInfo {
    int id;
}