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
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
  private static class TestMethodCompilerPass extends MethodCompilerPass {
    @Override
    Callback getActingCallback() {
      return new TestActingCallback();
    }

    @Override
    SignatureStore getSignatureStore() {
      return new TestSignatureStore();
    }
  }

  private static class TestActingCallback implements Callback {
    private List<String> methodNames = Lists.newArrayList();

    @Override
    public void call(Node node) {
      if (node.getType() == Token.FUNCTION) {
        methodNames.add(node.toString());
      }
    }
  }

  private static class TestSignatureStore implements SignatureStore {
    private Multimap<String, Node> signatures = HashMultimap.create();

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }

    @Override
    public void reset() {
      signatures.clear();
    }

    List<Node> getSignatures(String functionName) {
      return signatures.get(functionName);
    }
  }

  @Test
  public void testMethodCompilerPass() {
    Node externs = Node.newTextNode("function foo() {}");
    Node root = Node.newTextNode("function bar() {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(externs, root);

    TestActingCallback actingCallback = compilerPass.getActingCallback();

    assertEquals(1, actingCallback.methodNames.size());
    assertEquals("bar", actingCallback.methodNames.get(0));
  }

  @Test
  public void testExternMethods() {
    Node externs = Node.newTextNode("function foo() {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(externs, null);

    Set<String> externMethods = compilerPass.externMethods;
    assertEquals(1, externMethods.size());
    assertEquals("foo", externMethods.iterator().next());
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    Node externs = Node.newTextNode("function foo() {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(externs, null);

    Set<String> externMethodsWithoutSignatures =
        compilerPass.externMethodsWithoutSignatures;
    assertEquals(0, externMethodsWithoutSignatures.size());
  }

  @Test
  public void testNonMethodProperties() {
    Node root = Node.newTextNode("function foo() { var bar = 5; }");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(null, root);

    Set<String> nonMethodProperties = compilerPass.nonMethodProperties;
    assertEquals(1, nonMethodProperties.size());
    assertEquals("bar", nonMethodProperties.iterator().next());
  }

  @Test
  public void testMethodDefinitions() {
    Node root = Node.newTextNode("function bar(x) {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(null, root);

    Multimap<String, Node> methodDefinitions = compilerPass.methodDefinitions;
    List<Node> signatures = methodDefinitions.get("bar");
    assertEquals(1, signatures.size());
    assertSame(root, signatures.get(0));
  }

  @Test
  public void testGetExternMethods() {
    Node externs = Node.newTextNode("function foo() {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(externs, null);

    TestSignatureStore signatureStore = compilerPass.getSignatureStore();
    List<Node> signatures = signatureStore.getSignatures("foo");
    assertEquals(0, signatures.size());
  }

  @Test
  public void testGatherSignatures() {
    Node root = Node.newTextNode("function bar() {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(null, root);

    TestSignatureStore signatureStore = compilerPass.getSignatureStore();
    List<Node> signatures = signatureStore.getSignatures("bar");
    assertEquals(1, signatures.size());
    assertSame(root, signatures.get(0));
  }

  @Test
  public void testProcessPrototypeParent() {
    Node root = Node.newTextNode("function Foo() {} Foo.prototype.bar = function() {}");

    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    compilerPass.process(null, root);

    TestSignatureStore signatureStore = compilerPass.getSignatureStore();
    List<Node> signatures = signatureStore.getSignatures("Foo.prototype.bar");
    assertEquals(1, signatures.size());
    assertSame(root, signatures.get(0));
  }
}