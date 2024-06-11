package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.testtypes.ExternMethodsAndJs;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private SignatureStore signatureStore;
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    signatureStore = new SignatureStoreImpl();
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // No-op
        return new Callback() {};
      }
    };
  }

  @Test
  public void testGetExternMethods() {
    Node externs = createNode("var foo = function() {};\n" +
                               "var bar = function(x) {};\n" +
                               "var baz = function(x, y) {};");

    Node root = createNode("(function() {})();");

    methodCompilerPass.process(externs, root);

    // Extern methods should be added to the set
    assertThat(methodCompilerPass.externMethods).containsExactly("foo", "bar", "baz");
  }

  @Test
  public void testGatherSignatures() {
    String sourceName = "source.js";

    Node externs = createNode("var Foo = {\n" +
                               "  prototype: {\n" +
                               "    bar: function() {}\n" +
                               "  },\n" +
                               "  getBaz: function() {}\n" +
                               "};");

    Node root = createNode("(function() {\n" +
                           "  Foo.prototype.qux = function() {}\n" +
                           "})();");

    methodCompilerPass.process(externs, root);

    // Signatures should be added to the signature store
    assertThat(signatureStore.getSignatures()).containsExactlyEntriesIn(
        ImmutableList.of(
            MapEntry.of("Foo.bar", createNode("function() {}")),
            MapEntry.of("Foo.prototype.bar", createNode("function() {}")),
            MapEntry.of("Foo.getBaz", createNode("function() {}")),
            MapEntry.of("Foo.prototype.qux", createNode("function() {}"))));

    // Non-method properties should not be added
    assertNull(methodCompilerPass.methodDefinitions.get("qux"));
  }

  @Test
  public void testIgnoreMethodsWithoutSignatures() {
    String sourceName = "source.js";

    Node externs = createNode("var Foo = {\n" +
                               "  bar: function() {}\n" +
                               "};");

    Node root = createNode("(function() {\n" +
                           "  Foo.qux = function() {}\n" +
                           "})();");

    methodCompilerPass.process(externs, root);

    // Ignored methods should not be added to methodDefinitions
    assertNull(methodCompilerPass.methodDefinitions.get("qux"));
  }

  private Node createNode(String code) {
    RhinoJsCompiler compiler = new RhinoJsCompiler();
    return compiler.parse(code, sourceName, null);
  }

  private static class SignatureStoreImpl implements SignatureStore {
    private final Multimap<String, Node> signatures = HashMultimap.create();

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

    public Multimap<String, Node> getSignatures() {
      return signatures;
    }
  }
}