package com.google.javascript.jscomp.test;

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
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.testtype.AbstractCompilerForTest;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
  private AbstractCompilerForTest compiler;
  private MethodCompilerPass pass;
  private Multimap<String, Node> methodDefinitions;
  private SignatureStore signatureStore;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompilerForTest();
    pass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new SignatureStore() {
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
          };
        }
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node, int position, Node parent) {
            // No-op
          }
        };
      }
    };
    methodDefinitions = HashMultimap.create();
  }

  @org.junit.Test
  public void testGetExternMethods() {
    Node externs = Node.externalCall("Object", "Object");
    pass.process(externs, Node.nullNode());

    Set<String> expectedExternMethods = Sets.newHashSet("toString", "valueOf");
    assertEquals(expectedExternMethods, pass.externMethods);

    Set<String> expectedExternMethodsWithoutSignatures =
        Sets.newHashSet("hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable", "constructor");
    assertEquals(expectedExternMethodsWithoutSignatures, pass.externMethodsWithoutSignatures);

    assertFalse(pass.externMethods.containsAll(Sets.newHashSet("toString2", "valueOf2")));
  }

  @org.junit.Test
  public void testGatherSignatures() {
    Node root = Node.script("function foo() {}", "test.js");

    pass.process(Node.externalCall("Object", "Object"), root);

    Set<String> expectedNonMethodProperties = Sets.newHashSet();
    assertEquals(expectedNonMethodProperties, pass.nonMethodProperties);

    List<Node> signatures = Lists.newArrayList(
        Node.function("toString", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null),
        Node.function("valueOf", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null)
    );
    assertEquals(signatures, pass.getSignatureStore().getSignatures());

    assertEquals(2, pass.methodDefinitions.size());
    assertEquals(Node.function("foo", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null),
        pass.methodDefinitions.get("foo").get(0));
  }

  @org.junit.Test
  public void testAddPossibleSignature() {
    Node root = Node.script(
        "function foo() {}",
        "function bar() {}",
        "function foo.prototype.getBar = function() { return this.bar(); }",
        "test.js"
    );

    pass.process(Node.externalCall("Object", "Object"), root);

    Set<String> expectedNonMethodProperties = Sets.newHashSet();
    assertEquals(expectedNonMethodProperties, pass.nonMethodProperties);

    List<Node> signatures = Lists.newArrayList(
        Node.function("foo", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null),
        Node.function("bar", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null),
        Node.function("foo.prototype.getBar", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null)
    );
    assertEquals(signatures, pass.getSignatureStore().getSignatures());

    assertEquals(3, pass.methodDefinitions.size());
    Node foo = Node.function("foo", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null);
    assertEquals(foo, pass.methodDefinitions.get("foo").get(0));
    Node bar = Node.function("bar", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null);
    assertEquals(bar, pass.methodDefinitions.get("bar").get(0));
    Node getBar = Node.function("foo.prototype.getBar", Node.nullNode(), Node.nullNode(), null, Node.nullNode(), null, null);
    assertEquals(getBar, pass.methodDefinitions.get("foo.prototype.getBar").get(0));
  }
}

class SignatureStoreForTest implements SignatureStore {
  private Multimap<String, Node> signatures;

  SignatureStoreForTest() {
    signatures = HashMultimap.create();
  }

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

  public List<Node> getSignatures() {
    return Lists.newArrayList(signatures.values());
  }
}