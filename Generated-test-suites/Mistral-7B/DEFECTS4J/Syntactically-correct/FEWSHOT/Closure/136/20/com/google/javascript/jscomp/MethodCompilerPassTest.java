package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class DummySignatureStore implements MethodCompilerPass.SignatureStore {
    private Multimap<String, Node> signatures = HashMultimap.create();

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
      return signatures.get(functionName).get(0);
    }
  }

  @Test
  public void testProcess_EmptyCode() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            // no-op
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node root = Node.NULL;
    Node externs = Node.NULL;

    pass.process(externs, root);

    assertTrue(pass.externMethods.isEmpty());
    assertTrue(pass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testProcess_ExternMethods() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            // no-op
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node externs = Node.objectLiteral(
        Node.PROPERTY(
            Token.getIdent("foo"),
            Node.ASSIGN(
                Node.CALL(
                    Node.IDENT("Function"),
                    Lists.newArrayList(
                        Node.STRING("bar"),
                        Node.LIST(Node.NULL)
                    )
                )
            )
        )
    );

    Node root = Node.NULL;

    pass.process(externs, root);

    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());

    Node signature = pass.getSignatureStore().getSignature("foo");
    Node function = signature.getFirstChild();
    assertEquals(Token.FUNCTION, function.getType());
    assertEquals("bar", function.getLastChild().getString());

    assertFalse(pass.externMethodsWithoutSignatures.contains("foo"));
    assertTrue(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testProcess_ExternMethodsWithSignatures() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            // no-op
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node externs = Node.objectLiteral(
        Node.PROPERTIES(
            Node.PROPERTY(
                Token.getIdent("foo"),
                Node.ASSIGN(
                    Node.CALL(
                        Node.IDENT("Function"),
                        Lists.newArrayList(
                            Node.STRING("bar"),
                            Node.LIST(
                                Node.PROPERTIES(
                                    Node.PROPERTY(
                                        Token.getIdent("a"),
                                        Node.ASSIGN(Node.NULL)
                                    ),
                                    Node.PROPERTY(
                                        Token.getIdent("b"),
                                        Node.ASSIGN(Node.NULL)
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
    );

    Node root = Node.NULL;

    pass.process(externs, root);

    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());

    Node signature = pass.getSignatureStore().getSignature("foo");
    Node function = signature.getFirstChild();
    assertEquals(Token.FUNCTION, function.getType());

    Node functionPrototype = function.getFirstChild();
    assertEquals(Token.OBJECTLIT, functionPrototype.getType());
    List<Node> functionArguments = Lists.newArrayList(functionPrototype.getLastChild().getChildren());

    assertEquals(2, functionArguments.size());
    assertEquals("a", functionArguments.get(0).getLastChild().getString());
    assertEquals("b", functionArguments.get(1).getLastChild().getString());

    assertFalse(pass.externMethodsWithoutSignatures.contains("foo"));
    assertTrue(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testProcess_ExternMethodsWithoutSignatures() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            // no-op
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node externs = Node.objectLiteral(
        Node.PROPERTIES(
            Node.PROPERTY(
                Token.getIdent("foo"),
                Node.ASSIGN(Node.NULL)
            )
        )
    );

    Node root = Node.NULL;

    pass.process(externs, root);

    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());

    assertFalse(pass.getSignatureStore().getSignature("foo") != null);
    assertTrue(pass.externMethodsWithoutSignatures.contains("foo"));
    assertTrue(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.methodDefinitions.isEmpty());
  }

  @Test
  public void testProcess_MethodsFromCode() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            if (node.getType() == Token.FUNCTION) {
              String name = node.getFirstChild().getString();
              pass.addSignature(name, node, NodeTraversal.getSourceName(node));
            }
            next.process(node, next);
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node root = Node.function(
        Node.IDENT("foo"),
        Lists.newArrayList(),
        Node.LIST(
            Node.VARDECL(
                Node.getIdent("a"),
                Token.ASSIGN,
                Node.NULL
            ),
            Node.VARDECL(
                Node.getIdent("b"),
                Token.ASSIGN,
                Node.NUMBER(1)
            ),
            Node.RETURN(Node.IDENT("a"))
        )
    );

    Node externs = Node.NULL;

    pass.process(externs, root);

    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());

    Node signature = pass.getSignatureStore().getSignature("foo");
    Node function = signature.getFirstChild();
    assertEquals(Token.FUNCTION, function.getType());

    Node functionPrototype = function.getFirstChild();
    assertEquals(Token.OBJECTLIT, functionPrototype.getType());
    List<Node> functionArguments = Lists.newArrayList(functionPrototype.getLastChild().getChildren());

    assertEquals(0, functionArguments.size());

    assertFalse(pass.externMethodsWithoutSignatures.contains("foo"));
    assertTrue(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.methodDefinitions.containsKey("foo"));
    assertSame(function, pass.methodDefinitions.get("foo"));
  }

  @Test
  public void testProcess_MethodsWithPrototype() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            if (node.getType() == Token.FUNCTION) {
              String name = node.getFirstChild().getString();
              pass.addSignature(name, node, NodeTraversal.getSourceName(node));
            }
            next.process(node, next);
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node root = Node.function(
        Node.IDENT("foo"),
        Lists.newArrayList(),
        Node.BLOCK(
            Node.VARDECL(
                Node.getIdent("prototype"),
                Token.ASSIGN,
                Node.IDENT("Object")
            ),
            Node.VARDECL(
                Node.getIdent("a"),
                Token.ASSIGN,
                Node.NUMBER(1)
            ),
            Node.RETURN(Node.IDENT("prototype"))
        )
    );

    Node externs = Node.NULL;

    pass.process(externs, root);

    assertEquals(1, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());

    Node prototype = pass.getSignature("foo").getFirstChild().getLastChild();
    assertEquals(Token.OBJECTLIT, prototype.getType());

    assertTrue(pass.methodDefinitions.containsKey("foo"));
    assertSame(prototype, pass.methodDefinitions.get("foo"));
  }

  @Test
  public void testProcess_MethodsWithPrototypeAndArguments() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            if (node.getType() == Token.FUNCTION) {
              String name = node.getFirstChild().getString();
              pass.addSignature(name, node, NodeTraversal.getSourceName(node));
            }
            next.process(node, next);
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node root = Node.function(
        Node.IDENT("foo"),
        Lists.newArrayList(),
        Node.BLOCK(
            Node.VARDECL(
                Node.getIdent("prototype"),
                Token.ASSIGN,
                Node.IDENT("Object")
            ),
            Node.VARDECL(
                Node.getIdent("a"),
                Token.ASSIGN,
                Node.NUMBER(1)
            ),
            Node.RETURN(Node.IDENT("prototype"))
        ),
        Node.VARDECL(
            Node.getIdent("bar"),
            Token.ASSIGN,
            Node.FUNCTION(
                Node.IDENT("baz"),
                Lists.newArrayList(),
                Node.BLOCK(
                    Node.VARDECL(
                        Node.getIdent("x"),
                        Token.ASSIGN,
                        Node.NUMBER(2)
                    ),
                    Node.RETURN(Node.NUMBER(3))
                )
            )
        )
    );

    Node externs = Node.NULL;

    pass.process(externs, root);

    assertEquals(2, pass.externMethods.size());
    assertEquals("foo", pass.externMethods.iterator().next());
    assertEquals("bar", pass.externMethods.iterator().next());

    Node fooPrototype = pass.getSignature("foo").getFirstChild().getLastChild();
    assertEquals(Token.OBJECTLIT, fooPrototype.getType());

    Node barPrototype = pass.getSignature("bar").getFirstChild().getLastChild();
    assertEquals(Token.OBJECTLIT, barPrototype.getType());

    List<Node> barArguments = Lists.newArrayList(barPrototype.getLastChild().getChildren());

    assertEquals(0, barArguments.size());

    assertTrue(pass.methodDefinitions.containsKey("foo"));
    assertSame(fooPrototype, pass.methodDefinitions.get("foo"));
    assertTrue(pass.methodDefinitions.containsKey("bar"));
    assertSame(barPrototype, pass.methodDefinitions.get("bar"));
  }

  @Test
  public void testProcess_NonMethodProperties() {
    AbstractCompiler compiler = new CompilerOptions();
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(Node node, Callback next) {
            if (node.getType() == Token.GETPROP) {
              String name = node.getFirstChild().getString();
              pass.nonMethodProperties.add(name);
            }
            next.process(node, next);
          }
        };
      }

      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    Node root = Node.function(
        Node.IDENT("foo"),
        Lists.newArrayList(),
        Node.BLOCK(
            Node.VARDECL(
                Node.getIdent("a"),
                Token.ASSIGN,
                Node.NUMBER(1)
            ),
            Node.VARDECL(
                Node.getIdent("prototype"),
                Token.ASSIGN,
                Node.IDENT("Object")
            ),
            Node.RETURN(Node.IDENT("a"))
        )
    );

    Node externs = Node.NULL;

    pass.process(externs, root);

    assertEquals(1, pass.nonMethodProperties.size());
    assertEquals("prototype", pass.nonMethodProperties.iterator().next());

    assertTrue(pass.externMethods.isEmpty());
    assertTrue(pass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(pass.methodDefinitions.isEmpty());
  }
}