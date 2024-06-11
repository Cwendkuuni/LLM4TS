package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MethodCompilerPassTest {
  private static final String SOURCE_NAME = "test.js";

  @Test
  public void testGetExternMethods() {
    MethodCompilerPass pass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          private Map<String, Node> signatures = new HashMap<>();

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

          @Override
          public void printSignatures() {
            // Not used in this test
          }
        };
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // Not used in this test
          }
        };
      }
    };

    Node externs = createTestNode("externs.js");

    pass.process(externs, createTestNode("source.js"));

    Set<String> externMethods = pass.externMethods;
    Set<String> externMethodsWithoutSignatures = pass.externMethodsWithoutSignatures;
    Map<String, Node> signatures = pass.getSignatureStore().signatures;

    assertEquals(2, externMethods.size());
    assertEquals("foo", externMethods.iterator().next());
    assertEquals("bar", externMethods.iterator().next());
    assertFalse(externMethodsWithoutSignatures.isEmpty());
    assertTrue(externMethodsWithoutSignatures.contains("baz"));

    assertEquals(1, signatures.size());
    assertEquals("foo", signatures.keySet().iterator().next());
    assertSame(createTestNode("source.js", "function foo() {}"), signatures.get("foo"));
  }

  @Test
  public void testGatherSignatures() {
    MethodCompilerPass pass = new MethodCompilerPass(null) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureStore() {
          private Set<String> signatures = new HashSet<>();

          @Override
          public void reset() {
            signatures.clear();
          }

          @Override
          public void addSignature(String functionName, Node functionNode, String sourceFile) {
            signatures.add(functionName);
          }

          @Override
          public void removeSignature(String functionName) {
            signatures.remove(functionName);
          }

          @Override
          public void printSignatures() {
            // Not used in this test
          }
        };
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void call(Node node) {
            // Not used in this test
          }
        };
      }
    };

    Node root = createTestNode("source.js");

    pass.process(null, root);

    Set<String> signatures = pass.getSignatureStore().signatures;
    List<Node> methodDefinitions = Lists.newArrayList(pass.methodDefinitions.values());

    assertEquals(2, signatures.size());
    assertEquals("foo", signatures.iterator().next());
    assertEquals("bar", signatures.iterator().next());

    assertEquals(2, methodDefinitions.size());
    assertEquals("function foo() { ... }", methodDefinitions.get(0).toString());
    assertEquals("function bar() { ... }", methodDefinitions.get(1).toString());
  }

  private Node createTestNode(String fileName) {
    return createTestNode(fileName, "");
  }

  private Node createTestNode(String fileName, String nodeContent) {
    Node node = new Node();
    node.setType(Token.PROGRAM);
    node.setSourceName(fileName);
    node.setLineNumber(1);

    if (nodeContent.isEmpty()) {
      return node;
    }

    Node program = new Node();
    program.setType(Token.PROGRAM);
    program.setParent(node);

    Node code = new Node();
    code.setType(Token.LITERAL);
    code.setString(nodeContent);
    code.setParent(program);

    node.setValue(program);

    return node;
  }
}