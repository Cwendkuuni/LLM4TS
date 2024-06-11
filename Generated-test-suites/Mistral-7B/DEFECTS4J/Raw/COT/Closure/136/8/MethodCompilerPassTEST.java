import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test_util.AbstractCompilerForTest;
import com.google.javascript.jscomp.test_util.NodeMatcher;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    compiler = new AbstractCompilerForTest() {};
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new TestSignatureStore();
        }
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test
  public void testEmpty() {
    methodCompilerPass.process(null, null);
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testGetExternMethods() {
    String sourceName = "test.js";
    Node externs = NodeMatcher.parseJS("function foo() {}", sourceName);
    methodCompilerPass.process(externs, null);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("foo", methodCompilerPass.externMethods.iterator().next());

    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());

    assertTrue(methodCompilerPass.methodDefinitions.containsKey("foo"));
    assertSame(externs.getFirstChild(), methodCompilerPass.methodDefinitions.get("foo"));
  }

  @Test
  public void testGatherSignatures() {
    String sourceName = "test.js";
    Node root = NodeMatcher.parseJS(
        "function Foo() {}" +
        "function Foo.prototype.getBar = function() {}" +
        "function Foo.prototype.getBaz = Foo.getBaz", sourceName);
    methodCompilerPass.process(null, root);

    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals("Foo", methodCompilerPass.externMethods.iterator().next());

    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());

    assertEquals(3, methodCompilerPass.methodDefinitions.size());

    Node fooPrototype = NodeMatcher.findNode(root, "PrototypeDeclaration[name='Foo']");
    assertSame(fooPrototype, methodCompilerPass.methodDefinitions.get("Foo.prototype.getBar"));
    assertNull(methodCompilerPass.methodDefinitions.get("Foo.prototype.getBaz"));
    assertSame(root.getFirstChild(), methodCompilerPass.methodDefinitions.get("Foo"));
  }

  @Test
  public void testRemoveSignature() {
    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };

    methodCompilerPass.addSignature("foo", NodeMatcher.parseJS("function foo() {}", "test.js"), "test.js");
    methodCompilerPass.removeSignature("foo");

    assertFalse(signatureStore.containsSignature("foo"));
  }

  @Test
  public void testNonMethodProperties() {
    String sourceName = "test.js";
    Node root = NodeMatcher.parseJS("var prop = 42", sourceName);
    methodCompilerPass.process(null, root);

    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());

    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals("prop", methodCompilerPass.nonMethodProperties.iterator().next());
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

    public boolean containsSignature(String functionName) {
      return signatures.containsKey(functionName);
    }
  }

}