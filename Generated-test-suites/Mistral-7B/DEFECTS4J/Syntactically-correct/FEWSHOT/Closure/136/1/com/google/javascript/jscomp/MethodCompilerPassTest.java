package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
    @Override
    abstract Callback getActingCallback() {
      return new TestActingCallback();
    }

    @Override
    abstract SignatureStore getSignatureStore() {
      return new TestSignatureStore();
    }

    static class TestActingCallback extends AbstractPostOrderCallback {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.FUNCTION) {
          System.out.println("Found method: " + n.getSourcePositionStart().getLineNumber() + ":" + n.getSourcePositionStart().getCharacterOffsetInLine());
        }
      }
    }

    static class TestSignatureStore implements SignatureStore {
      Map<String, Node> signatures = HashMultimap.create().asMap();

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

      public void printSignatures() {
        for (Map.Entry<String, Node> entry : signatures.entrySet()) {
          System.out.println("Function Name: " + entry.getKey());
          System.out.println("Function Node: " + entry.getValue().getSourcePositionStart().getLineNumber() + ":" + entry.getValue().getSourcePositionStart().getCharacterOffsetInLine());
        }
      }
    }
  }

  @Test
  public void testMethodCompilerPass() {
    String testSource = "function Foo() {}" +
        "function Foo.bar() {}" +
        "function Foo.prototype.getBar() {}" +
        "Foo.prototype.getBaz = function() {};";

    Node root = JSCompiler.parseTree(testSource, null);

    TestMethodCompilerPass pass = new TestMethodCompilerPass(new Compiler());
    pass.process(null, root);
    pass.getSignatureStore().printSignatures();

    assertEquals(3, pass.methodDefinitions.size());
    assertEquals("bar", pass.methodDefinitions.get("bar").toString());
    assertEquals("getBar", pass.methodDefinitions.get("getBar").toString());
    assertEquals("getBaz", pass.methodDefinitions.get("getBaz").toString());

    assertEquals(3, pass.externMethods.size());
    assertTrue(pass.externMethods.contains("bar"));
    assertTrue(pass.externMethods.contains("getBar"));
    assertTrue(pass.externMethods.contains("getBaz"));

    assertEquals(0, pass.externMethodsWithoutSignatures.size());

    assertFalse(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.nonMethodProperties.contains("prototype"));
  }

  @Test
  public void testMethodCompilerPassExterns() {
    String testSource = "Foo.bar = function() {};" +
        "function Foo.prototype.getBar() {}";

    Node root = JSCompiler.parseTree(testSource, null);

    TestMethodCompilerPass pass = new TestMethodCompilerPass(new Compiler());
    pass.process(JSCompiler.parseTree("function Foo() {}", null), root);
    pass.getSignatureStore().printSignatures();

    assertEquals(2, pass.methodDefinitions.size());
    assertEquals("bar", pass.methodDefinitions.get("bar").toString());
    assertEquals("getBar", pass.methodDefinitions.get("getBar").toString());

    assertEquals(1, pass.externMethods.size());
    assertTrue(pass.externMethods.contains("bar"));

    assertEquals(0, pass.externMethodsWithoutSignatures.size());

    assertFalse(pass.nonMethodProperties.isEmpty());
    assertTrue(pass.nonMethodProperties.contains("prototype"));
  }

  @Test
  public void testMethodCompilerPassNoSignatures() {
    String testSource = "Foo.bar = function() {};";

    Node root = JSCompiler.parseTree(testSource, null);

    TestMethodCompilerPass pass = new TestMethodCompilerPass(new Compiler());
    pass.process(null, root);

    assertTrue(pass.externMethodsWithoutSignatures.contains("bar"));
  }
}