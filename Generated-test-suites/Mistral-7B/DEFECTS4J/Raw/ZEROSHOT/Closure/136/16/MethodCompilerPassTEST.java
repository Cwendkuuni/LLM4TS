import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.BaseTestCase;

public class MethodCompilerPassTest extends BaseTestCase {

  private static class DummySignatureStore implements SignatureStore {
    private Multimap<String, Node> signatures = HashMultimap.create();

    public void reset() {
      signatures.clear();
    }

    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }

    Node getSignature(String functionName) {
      return signatures.get(functionName).get(0);
    }
  }

  @Test
  public void testSimplePass() throws Exception {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(NodeTraversal t, Node n, Node parent) {
            // do nothing
          }
        };
      }
    };

    Node externs = createNode("foo.externs.js");
    Node root = createNode("foo.js");

    pass.process(externs, root);

    assertNull(pass.getSignatureStore().getSignature("bar"));
    assertFalse(pass.externMethods.contains("bar"));

    Node externMethod = createNode("foo.externs.js", "bar", Token.GETPROP);
    externMethod.setNext(createNode("foo.externs.js", "function", Token.FUNCTION));
    Node externFunction = externMethod.getNext();
    externFunction.setNext(createNode("foo.externs.js", "{}"));

    externs = updateNode(externs, "foo.externs.js", 0, externMethod);

    pass.process(externs, root);

    assertNull(pass.getSignatureStore().getSignature("bar"));
    assertFalse(pass.externMethods.contains("bar"));

    Node method = createNode("foo.js", "bar", Token.FUNCTION);
    root = updateNode(root, "foo.js", 0, method);

    pass.process(externs, root);

    Node signature = pass.getSignatureStore().getSignature("bar");
    assertNotNull(signature);
    assertEquals(method, signature);
    assertTrue(pass.externMethods.contains("bar"));
  }

  @Test
  public void testPrototypePass() throws Exception {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(NodeTraversal t, Node n, Node parent) {
            if (n.getType() == Token.STRING && parent.getType() == Token.ASSIGN) {
              String prototypeName = n.getString();
              if (prototypeName.equals("prototype")) {
                Node prototypeNode = parent.getFirstChild();
                Node prototypeValue = prototypeNode.getNext();
                if (prototypeValue.getType() == Token.OBJECTLIT) {
                  for (Node key = prototypeValue.getFirstChild();
                      key != null; key = key.getNext().getNext()) {
                    if (key.getType() == Token.STRING) {
                      Node value = key.getNext();
                      if (value.getType() == Token.FUNCTION) {
                        String methodName = key.getString();
                        pass.addSignature(methodName, value, t.getSourceName());
                      }
                    }
                  }
                }
              }
            }
          }
        };
      }
    };

    Node externs = createNode("foo.externs.js");
    Node root = createNode("foo.js");

    pass.process(externs, root);

    Node prototype = createNode("foo.js", "prototype", Token.GETPROP);
    Node prototypeObject = createNode("foo.js", "{}");
    Node prototypeFunction = createNode("foo.js", "function bar() {}", Token.FUNCTION);
    Node prototypeProperty = createNode("foo.js", "bar", Token.PROPSPEC);
    prototype.setNext(prototypeObject);
    prototypeObject.setNext(prototypeFunction);
    prototypeFunction.setNext(prototypeProperty);
    root = updateNode(root, "foo.js", 0, prototype);

    pass.process(externs, root);

    Node signature = pass.getSignatureStore().getSignature("bar");
    assertNotNull(signature);
    assertEquals(prototypeFunction, signature);
    assertTrue(pass.externMethods.contains("bar"));
  }

  @Test
  public void testIgnoredMethods() throws Exception {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    MethodCompilerPass pass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }

      @Override
      Callback getActingCallback() {
        return new Callback() {
          @Override
          public void process(NodeTraversal t, Node n, Node parent) {
            if (n.getType() == Token.STRING && parent.getType() == Token.ASSIGN) {
              String prototypeName = n.getString();
              if (prototypeName.equals("prototype")) {
                Node prototypeNode = parent.getFirstChild();
                Node prototypeValue = prototypeNode.getNext();
                if (prototypeValue.getType() == Token.OBJECTLIT) {
                  for (Node key = prototypeValue.getFirstChild();
                      key != null; key = key.getNext().getNext()) {
                    if (key.getType() == Token.STRING) {
                      Node value = key.getNext();
                      if (value.getType() == Token.FUNCTION) {
                        String methodName = key.getString();
                        pass.addSignature(methodName, value, t.getSourceName());
                      } else if (value.getType() == Token.STRING) {
                        pass.externMethodsWithoutSignatures.add(methodName);
                      }
                    }
                  }
                }
              }
            }
          }
        };
      }
    };

    Node externs = createNode("foo.externs.js");
    Node root = createNode("foo.js");

    pass.process(externs, root);

    Node prototype = createNode("foo.js", "prototype", Token.GETPROP);
    Node prototypeObject = createNode("foo.js", "{}");
    Node ignoredMethod = createNode("foo.js", "bar", Token.STRING);
    Node prototypeFunction = createNode("foo.js", "function foo() {}", Token.FUNCTION);
    Node prototypeProperty = createNode("foo.js", "bar", Token.PROPSPEC);
    prototype.setNext(prototypeObject);
    prototypeObject.setNext(prototypeFunction);
    prototypeFunction.setNext(prototypeProperty);
    prototypeProperty.setNext(ignoredMethod);
    root = updateNode(root, "foo.js", 0, prototype);

    pass.process(externs, root);

    assertNull(pass.getSignatureStore().getSignature("bar"));
    assertTrue(pass.externMethodsWithoutSignatures.contains("bar"));
  }
}