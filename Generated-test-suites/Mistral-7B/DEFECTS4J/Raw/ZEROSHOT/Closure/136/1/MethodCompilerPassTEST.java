import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class DummySignatureStore implements SignatureStore {

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
  }

  @org.junit.Test
  public void testMethodCompilerPass() {
    String sourceCode = "function Foo() {}"
        + "Foo.prototype.bar = function() {}"
        + "function Foo.baz() {}"
        + "Foo.prototype.qux = 42";

    Node root = AbstractCompiler.parseMain(sourceCode, null);
    MethodCompilerPass pass = new MethodCompilerPass(null) {
      @Override
      abstract Callback getActingCallback() {
        // noop
        return new Callback() {
          @Override
          public void process(NodeTraversal t, Node n, Node parent) {}
        };
      }

      @Override
      abstract SignatureStore getSignatureStore() {
        return new DummySignatureStore();
      }
    };

    pass.process(null, root);

    assertFalse(pass.externMethods.isEmpty());
    assertEquals(3, pass.externMethods.size());
    assertEquals("Foo", pass.externMethods.iterator().next());
    assertEquals("Foo.baz", pass.externMethods.iterator().next());
    assertEquals("Foo", pass.externMethods.iterator().next());

    assertFalse(pass.externMethodsWithoutSignatures.isEmpty());
    assertEquals(1, pass.externMethodsWithoutSignatures.size());
    assertEquals("Foo.qux", pass.externMethodsWithoutSignatures.iterator().next());

    assertFalse(pass.nonMethodProperties.isEmpty());
    assertEquals(1, pass.nonMethodProperties.size());
    assertEquals("prototype", pass.nonMethodProperties.iterator().next());

    assertNotNull(pass.methodDefinitions.get("bar"));
    assertNotNull(pass.methodDefinitions.get("baz"));
    assertNull(pass.methodDefinitions.get("qux"));

    assertEquals(2, pass.methodDefinitions.size());

    Node functionNode = pass.methodDefinitions.get("bar").getValue();
    Node functionNodeBaz = pass.methodDefinitions.get("baz").getValue();
    assertEquals(Token.FUNCTION, functionNode.getType());
    assertEquals(Token.FUNCTION, functionNodeBaz.getType());
  }
}