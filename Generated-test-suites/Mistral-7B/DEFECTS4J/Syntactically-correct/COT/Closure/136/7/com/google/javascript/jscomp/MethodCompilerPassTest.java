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
      // Override this method in your test subclass
      return null;
    }

    @Override
    abstract SignatureStore getSignatureStore() {
      // Override this method in your test subclass
      return null;
    }

    @Override
    void process(Node externs, Node root) {
      // Test logic goes here
    }
  }

  // Subclass for testing getActingCallback()
  private static class TestActingCallbackMethodCompilerPass extends TestMethodCompilerPass {
    @Override
    Callback getActingCallback() {
      return new Callback() {
        @Override
        public void call(Node node, NodeTraversal traversal) {}
      };
    }
  }

  // Subclass for testing getSignatureStore()
  private static class TestSignatureStoreMethodCompilerPass extends TestMethodCompilerPass {
    private Multimap<String, Node> signatureStore = HashMultimap.create();

    @Override
    SignatureStore getSignatureStore() {
      return () -> signatureStore;
    }

    @Override
    void process(Node externs, Node root) {
      new TestActingCallbackMethodCompilerPass(null).process(externs, root);
      assertEquals(0, signatureStore.size());
    }

    @Test
    public void testGetExternMethods() {
      String source = "Foo.bar = function() {};";
      Node externs = AbstractCompiler.parseJavaScriptSource(source);

      new TestSignatureStoreMethodCompilerPass(null).process(externs, null);

      assertEquals(1, signatureStore.size());
      assertEquals("bar", signatureStore.keySet().iterator().next());
      assertSame(AbstractCompiler.parseJavaScriptSource(source).getFirstChild(),
          signatureStore.get("bar"));
    }

    @Test
    public void testGatherSignatures() {
      String source = "Foo.prototype.getBar = function() {};\n" +
          "Foo.prototype.getBaz = function() {};\n" +
          "Foo.prototype = {\n" +
          "  getBaz: function() {}\n" +
          "};";
      Node root = AbstractCompiler.parseJavaScriptSource(source);

      new TestSignatureStoreMethodCompilerPass(null).process(null, root);

      assertEquals(3, signatureStore.size());
      Set<String> names = signatureStore.keySet();
      assertTrue(names.contains("getBar"));
      assertTrue(names.contains("getBaz"));
      assertTrue(names.contains("getBaz"));
      assertSame(root.getFirstChild().getNext().getNext(), signatureStore.get("getBar"));
      assertSame(root.getFirstChild().getNext().getNext().getNext(), signatureStore.get("getBaz"));
      assertSame(root.getFirstChild().getNext(), signatureStore.get("getBaz"));
    }

    @Test
    public void testExternMethodsWithoutSignatures() {
      String source = "Foo.bar = function() {};\n" +
          "Foo.prototype.getBaz = function() {};";
      Node externs = AbstractCompiler.parseJavaScriptSource(source);

      new TestSignatureStoreMethodCompilerPass(null).process(externs, null);

      assertEquals(2, signatureStore.size());
      Set<String> names = signatureStore.keySet();
      assertFalse(names.contains("bar"));
      assertTrue(names.contains("getBaz"));
    }

    @Test(expected = IllegalStateException.class)
    public void testUndefinedFunction() {
      String source = "Foo.prototype.getBaz = undefined;";
      Node externs = AbstractCompiler.parseJavaScriptSource(source);

      new TestSignatureStoreMethodCompilerPass(null).process(externs, null);
    }
  }
}