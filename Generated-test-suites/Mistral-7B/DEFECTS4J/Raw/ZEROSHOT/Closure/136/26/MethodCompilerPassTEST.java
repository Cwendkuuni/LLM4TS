import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  private abstract static class DummyMethodCompilerPass extends MethodCompilerPass {

    @Override
    Callback getActingCallback() {
      // We don't need an acting callback for testing.
      throw new UnsupportedOperationException();
    }

    @Override
    SignatureStore getSignatureStore() {
      return new DummySignatureStore();
    }
  }

  private static class DummySignatureStore implements MethodCompilerPass.SignatureStore {

    private final Map<String, Node> signatures = new HashMap<>();

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void reset() {
      signatures.clear();
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }
  }

  private static class TestMethodCompilerPass extends DummyMethodCompilerPass {

    @Override
    abstract Callback getActingCallback();

    @Override
    SignatureStore getSignatureStore() {
      return new DummySignatureStore();
    }

    @SuppressWarnings("unchecked")
    @Override
    Callback getActingCallback() {
      // This is a placeholder for the actual acting callback.
      return new Callback() {
        @Override
        public void process(Node node, NodeTraversal nodeTraversal) {}
      };
    }
  }

  @org.junit.Test
  public void testMethodsFromExterns() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler() {};

    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);

    String externsSource =
        "function Foo() {}" + System.lineSeparator() +
        "Foo.prototype.bar = function() {};" + System.lineSeparator() +
        "function Qux() {}";

    Node externs = AbstractCompiler.parseTree(externsSource, options);

    Node root = AbstractCompiler.parseTree("function Main() {}", options);

    pass.process(externs, root);

    Set<String> expectedExternMethods = Sets.newHashSet("Foo", "bar");
    assertEquals(expectedExternMethods, pass.externMethods);
    assertEquals(1, pass.methodDefinitions.size());

    Map.Entry<String, Node> entry = pass.methodDefinitions.entries().iterator().next();
    assertEquals("bar", entry.getKey());
    Node methodNode = entry.getValue();
    assertEquals(Token.FUNCTION, methodNode.getType());
  }

  @org.junit.Test
  public void testMethodsFromSource() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler() {};

    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);

    String source =
        "function Foo() {" + System.lineSeparator() +
        "  this.bar = function() {};" + System.lineSeparator() +
        "}" + System.lineSeparator() +
        "function Main() {" + System.lineSeparator() +
        "  var f = new Foo();" + System.lineSeparator() +
        "  f.bar();" + System.lineSeparator() +
        "}" + System.lineSeparator();

    Node sourceNode = AbstractCompiler.parseTree(source, options);

    pass.process(null, sourceNode);

    Set<String> expectedExternMethods = Sets.newHashSet();
    Set<String> expectedMethods = Sets.newHashSet("bar", "Foo");
    Set<String> nonMethodProperties = Sets.newHashSet();

    assertEquals(expectedExternMethods, pass.externMethods);
    assertEquals(expectedMethods, pass.methodDefinitions.keySet());
    assertEquals(nonMethodProperties, pass.nonMethodProperties);
  }

  @org.junit.Test
  public void testIgnoredMethodsFromExterns() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler() {};

    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);

    String externsSource =
        "function Foo() {}" + System.lineSeparator() +
        "Foo.prototype.bar = function() {};" + System.lineSeparator() +
        "function Qux() {}" + System.lineSeparator() +
        "Foo.prototype.baz = function() {};"; // Should be ignored

    Node externs = AbstractCompiler.parseTree(externsSource, options);

    Node root = AbstractCompiler.parseTree("function Main() {}", options);

    pass.process(externs, root);

    Set<String> expectedExternMethods = Sets.newHashSet("Foo");
    assertEquals(expectedExternMethods, pass.externMethods);
    assertEquals(0, pass.methodDefinitions.size());
    assertTrue(pass.externMethodsWithoutSignatures.contains("baz"));
  }

  @org.junit.Test
  public void testIgnoredMethodsFromSource() {
    CompilerOptions options = new CompilerOptions(null);
    AbstractCompiler compiler = new AbstractCompiler() {};

    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);

    String source =
        "function Foo() {" + System.lineSeparator() +
        "  this.bar = function() {};" + System.lineSeparator() +
        "  this.baz = function() {};" + System.lineSeparator() + // Should be ignored
        "}" + System.lineSeparator() +
        "function Main() {" + System.lineSeparator() +
        "  var f = new Foo();" + System.lineSeparator() +
        "  f.bar();" + System.lineSeparator() +
        "}" + System.lineSeparator();

    Node sourceNode = AbstractCompiler.parseTree(source, options);

    pass.process(null, sourceNode);

    Set<String> expectedExternMethods = Sets.newHashSet();
    Set<String> expectedMethods = Sets.newHashSet("bar", "Foo");
    Set<String> nonMethodProperties = Sets.newHashSet();

    assertEquals(expectedExternMethods, pass.externMethods);
    assertEquals(expectedMethods, pass.methodDefinitions.keySet());
    assertTrue(pass.externMethodsWithoutSignatures.contains("baz"));
  }
}