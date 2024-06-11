import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class TestMethodCompilerPass extends MethodCompilerPass {
    @Override
    abstract Callback getActingCallback() {
      return new DoNothingCallback();
    }

    @Override
    abstract SignatureStore getSignatureStore() {
      return new TestSignatureStore();
    }

    private static class DoNothingCallback implements Callback {
      @Override
      public void call(NodeTraversal traversal, Node node, Node parent) {}
    }

    private static class TestSignatureStore implements SignatureStore {
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
  }

  @org.junit.Test
  public void testMethodCompilerPass_EmptyExterns() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.createObject();
    Node externs = Node.NULL;
    compilerPass.process(externs, root);

    assertEquals(0, compilerPass.externMethods.size());
    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());
    assertFalse(compilerPass.methodDefinitions.containsKey("methodName"));
  }

  @org.junit.Test
  public void testMethodCompilerPass_OneMethodInExterns() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.createObject();
    Node externs = Node.createObject();
    Node methodNameNode = Node.createString("methodName");
    Node methodNode = Node.createFunction();

    NodeTraversal.traverse(null, externs, new NodeTraversal.Callback() {
      @Override
      public void call(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.GETPROP &&
            node.getFirstChild().getString().equals("methodName")) {
          Node value = Node.createObject();
          Node functionNode = Node.createFunction();
          node.setFirstChild(value);
          value.setFirstChild(functionNode);
          functionNode.setFirstChild(methodNode);
          return;
        }
      }
    });

    compilerPass.process(externs, root);

    assertEquals(1, compilerPass.externMethods.size());
    assertEquals("methodName", compilerPass.externMethods.iterator().next());
    assertFalse(compilerPass.externMethodsWithoutSignatures.contains("methodName"));
    assertNull(compilerPass.getSignatureStore().getSignature("methodName"));

    assertEquals(0, compilerPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testMethodCompilerPass_OneMethodInSource() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.createObject();
    Node externs = Node.NULL;
    Node functionNode = Node.createFunction();

    NodeTraversal.traverse(null, root, new NodeTraversal.Callback() {
      @Override
      public void call(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.FUNCTION) {
          node.setParent(parent);
          node.setFirstChild(functionNode);
          return;
        }
      }
    });

    String functionName = "methodName";
    Node methodNameNode = Node.createString(functionName);
    Node getPropNode = Node.createGetProp();
    getPropNode.setParent(root);
    getPropNode.setFirstChild(methodNameNode);
    methodNameNode.setFirstChild(functionNode);

    compilerPass.process(externs, root);

    assertEquals(0, compilerPass.externMethods.size());
    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());
    assertFalse(compilerPass.methodDefinitions.containsKey(functionName));

    Node signature = compilerPass.getSignatureStore().getSignature(functionName);
    assertSame(node, signature);
  }

  @org.junit.Test
  public void testMethodCompilerPass_OneMethodInSourceWithExterns() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.createObject();
    Node externs = Node.createObject();
    Node functionNode = Node.createFunction();

    NodeTraversal.traverse(null, root, new NodeTraversal.Callback() {
      @Override
      public void call(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.FUNCTION) {
          node.setParent(parent);
          node.setFirstChild(functionNode);
          return;
        }
      }
    });

    String functionName = "methodName";
    Node methodNameNode = Node.createString(functionName);
    Node getPropNode = Node.createGetProp();
    getPropNode.setParent(root);
    getPropNode.setFirstChild(methodNameNode);
    methodNameNode.setFirstChild(functionNode);

    Node externMethodNode = Node.createObject();
    Node externMethodNameNode = Node.createString(functionName);
    Node externMethodFunctionNode = Node.createFunction();
    Node externMethodGetPropNode = Node.createGetProp();
    externMethodGetPropNode.setParent(externMethodNode);
    externMethodGetPropNode.setFirstChild(externMethodNameNode);
    externMethodNameNode.setFirstChild(externMethodFunctionNode);
    externMethodFunctionNode.setFirstChild(functionNode);

    NodeTraversal.traverse(null, externs, new NodeTraversal.Callback() {
      @Override
      public void call(NodeTraversal traversal, Node node, Node parent) {
        if (node.getType() == Token.GETPROP &&
            node.getFirstChild().getString().equals(functionName)) {
          node.setParent(parent);
          node.setFirstChild(externMethodNode);
          return;
        }
      }
    });

    compilerPass.process(externs, root);

    assertEquals(1, compilerPass.externMethods.size());
    assertEquals(functionName, compilerPass.externMethods.iterator().next());
    assertTrue(compilerPass.externMethodsWithoutSignatures.isEmpty());
    assertSame(externMethodNode, compilerPass.getSignatureStore().getSignature(functionName));

    assertEquals(0, compilerPass.methodDefinitions.size());
  }

  @org.junit.Test
  public void testMethodCompilerPass_MultipleMethodsInSource() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);
    Node root = Node.createObject();
    Node externs = Node.NULL;

    Node method1FunctionNode = Node.createFunction();
    Node method1GetPropNode = Node.createGetProp();
    method1GetPropNode.setParent(root);
    method1GetPropNode.setFirstChild(Node.createString("method1"));
    method1GetPropNode.getFirstChild().setFirstChild(method1FunctionNode);
    method1FunctionNode.setFirstChild(method1FunctionNode); // Function declaration

    Node method2FunctionNode = Node.createFunction();
    Node method2GetPropNode = Node.createGetProp();
    method2GetPropNode.setParent(root);
    method2GetPropNode.setFirstChild(Node.createString("method2"));
    method2GetPropNode.getFirstChild().setFirstChild(method2FunctionNode);
    method2FunctionNode.setFirstChild(method2FunctionNode); // Function declaration

    compilerPass.process(externs, root);

    assertEquals(0, compilerPass.externMethods.size());
    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());

    Node method1Signature = compilerPass.getSignatureStore().getSignature("method1");
    assertSame(method1FunctionNode, method1Signature);

    Node method2Signature = compilerPass.getSignatureStore().getSignature("method2");
    assertSame(method2FunctionNode, method2Signature);
  }
}