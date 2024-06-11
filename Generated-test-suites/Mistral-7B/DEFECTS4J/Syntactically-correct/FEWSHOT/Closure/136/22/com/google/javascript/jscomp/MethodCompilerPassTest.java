package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.base.JSCompTestWithExternalResources;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest extends JSCompTestWithExternalResources {
  private static class TestMethodCompilerPass extends MethodCompilerPass {
    private SignatureStore signatureStore = new SignatureStore() {
      private Map<String, Node> signatures = new java.util.HashMap<>();

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
      public SignatureStore getSignatureStore() {
        return this;
      }
    };

    public TestMethodCompilerPass(AbstractCompiler compiler) {
      super(compiler);
    }

    @Override
    abstract Callback getActingCallback();

    @Override
    SignatureStore getSignatureStore() {
      return signatureStore;
    }
  };

  private static class TestActingCallback extends AbstractPostOrderCallback {
    private int methodCount = 0;

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      methodCount++;
    }

    public int getMethodCount() {
      return methodCount;
    }
  }

  @Test
  public void testProcess_NoMethods() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    Node root = createSourceFile("test.js", "");
    Node externs = null;
    pass.process(externs, root);
    assertThat(pass.externMethods).isEmpty();
    assertThat(pass.methodDefinitions).isEmpty();
    assertThat(pass.getSignatureStore().signatures).isEmpty();
  }

  @Test
  public void testProcess_ExternMethods() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    String source = "foo.bar = function() {}";
    Node root = createSourceFile("test.js", source);
    Node externs = createSourceFile("foo.js", "foo.bar = function() {}");
    pass.process(externs, root);
    Set<String> expectedExternMethods = Sets.newHashSet("foo.bar");
    assertThat(pass.externMethods).containsExactlyElementsIn(expectedExternMethods);
    Set<String> expectedNonMethodProperties = Sets.newHashSet();
    assertThat(pass.nonMethodProperties).containsExactlyElementsIn(expectedNonMethodProperties);
    Map<String, Node> expectedMethodDefinitions = new java.util.HashMap<>();
    expectedMethodDefinitions.put("foo.bar", createNodeFromSource("function() {}", "test.js"));
    assertThat(pass.methodDefinitions).containsExactlyEntriesIn(expectedMethodDefinitions.entrySet());
    Map<String, Node> expectedSignatures = new java.util.HashMap<>();
    expectedSignatures.put("foo.bar", createNodeFromSource("function() {}", "test.js"));
    assertThat(pass.getSignatureStore().signatures).containsExactlyEntriesIn(expectedSignatures.entrySet());
  }

  @Test
  public void testProcess_MethodsWithFunctionSignatures() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    String source =
        "foo.bar = function(a, b) {" +
        "  return a + b;" +
        "}";
    Node root = createSourceFile("test.js", source);
    Node externs = null;
    pass.process(externs, root);
    Set<String> expectedExternMethods = Sets.newHashSet("foo.bar");
    assertThat(pass.externMethods).containsExactlyElementsIn(expectedExternMethods);
    Set<String> expectedNonMethodProperties = Sets.newHashSet();
    assertThat(pass.nonMethodProperties).containsExactlyElementsIn(expectedNonMethodProperties);
    Map<String, Node> expectedMethodDefinitions = new java.util.HashMap<>();
    expectedMethodDefinitions.put("foo.bar", createNodeFromSource("function(a, b) {return a + b;}", "test.js"));
    assertThat(pass.methodDefinitions).containsExactlyEntriesIn(expectedMethodDefinitions.entrySet());
    Map<String, Node> expectedSignatures = new java.util.HashMap<>();
    expectedSignatures.put("foo.bar", createNodeFromSource("function(a, b) {return a + b;}", "test.js"));
    assertThat(pass.getSignatureStore().signatures).containsExactlyEntriesIn(expectedSignatures.entrySet());
  }

  @Test
  public void testProcess_MethodsWithoutFunctionSignatures() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    String source = "foo.bar = function() {}";
    Node root = createSourceFile("test.js", source);
    Node externs = null;
    pass.process(externs, root);
    Set<String> expectedExternMethods = Sets.newHashSet("foo.bar");
    Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet();
    assertThat(pass.externMethods).containsExactlyElementsIn(expectedExternMethods);
    assertThat(pass.externMethodsWithoutSignatures).containsExactlyElementsIn(expectedExternMethodsWithoutSignatures);
    Map<String, Node> expectedMethodDefinitions = new java.util.HashMap<>();
    expectedMethodDefinitions.put("foo.bar", createNodeFromSource("function() {}", "test.js"));
    Map<String, Node> expectedSignatures = new java.util.HashMap<>();
    assertThat(pass.methodDefinitions).containsExactlyEntriesIn(expectedMethodDefinitions.entrySet());
    assertThat(pass.getSignatureStore().signatures).isEmpty();
  }

  @Test
  public void testProcess_MethodsWithPrototypeProperties() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    String source =
        "Foo.prototype.bar = function() {" +
        "  return this.baz();" +
        "}" +
        "Foo.prototype.baz = function() {" +
        "  return 42;" +
        "}";
    Node root = createSourceFile("test.js", source);
    Node externs = null;
    pass.process(externs, root);
    Set<String> expectedExternMethods = Sets.newHashSet();
    Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet();
    Map<String, Node> expectedMethodDefinitions = new java.util.HashMap<>();
    expectedMethodDefinitions.put("Foo.prototype.bar", createNodeFromSource("function() {return this.baz();}", "test.js"));
    expectedMethodDefinitions.put("Foo.prototype.baz", createNodeFromSource("function() {return 42;}", "test.js"));
    Map<String, Node> expectedSignatures = new java.util.HashMap<>();
    expectedSignatures.put("Foo.prototype.bar", createNodeFromSource("function() {return this.baz();}", "test.js"));
    expectedSignatures.put("Foo.prototype.baz", createNodeFromSource("function() {return 42;}", "test.js"));
    assertThat(pass.externMethods).containsExactlyElementsIn(expectedExternMethods);
    assertThat(pass.externMethodsWithoutSignatures).containsExactlyElementsIn(expectedExternMethodsWithoutSignatures);
    assertThat(pass.methodDefinitions).containsExactlyEntriesIn(expectedMethodDefinitions.entrySet());
    assertThat(pass.getSignatureStore().signatures).containsExactlyEntriesIn(expectedSignatures.entrySet());
  }

  @Test
  public void testProcess_MethodsWithMultipleSignatures() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    String source =
        "Foo.prototype.bar = function(a, b) {" +
        "  if (a) {" +
        "    return a;" +
        "  } else {" +
        "    return b;" +
        "  }" +
        "}" +
        "Foo.prototype.bar = function(c) {" +
        "  return c.toUpperCase();" +
        "}";
    Node root = createSourceFile("test.js", source);
    Node externs = null;
    pass.process(externs, root);
    Set<String> expectedExternMethods = Sets.newHashSet();
    Set<String> expectedExternMethodsWithoutSignatures = Sets.newHashSet();
    Map<String, Node> expectedMethodDefinitions = new java.util.HashMap<>();
    Map<String, List<Node>> expectedSignatures = new java.util.HashMap<>();
    expectedSignatures.put("Foo.prototype.bar", Lists.newArrayList(
        createNodeFromSource("function(a, b) {if (a) {return a; } else {return b;}}", "test.js"),
        createNodeFromSource("function(c) {return c.toUpperCase();}", "test.js")
    ));
    assertThat(pass.externMethods).containsExactlyElementsIn(expectedExternMethods);
    assertThat(pass.externMethodsWithoutSignatures).containsExactlyElementsIn(expectedExternMethodsWithoutSignatures);
    assertThat(pass.methodDefinitions).containsExactlyEntriesIn(expectedMethodDefinitions.entrySet());
    assertThat(pass.getSignatureStore().signatures).containsExactlyEntriesIn(expectedSignatures.entrySet());
  }

  @Test
  public void testActingCallback_CountsMethods() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    TestActingCallback actingCallback = new TestActingCallback();
    pass.getActingCallback() = actingCallback;
    String source = "function foo() {}";
    Node root = createSourceFile("test.js", source);
    Node externs = null;
    pass.process(externs, root);
    int expectedMethodCount = 1;
    assertThat(actingCallback.getMethodCount()).isEqualTo(expectedMethodCount);
  }

  @Test(expected = IllegalStateException.class)
  public void testActingCallback_ThrowsExceptionIfUndefinedFunction() {
    AbstractCompiler compiler = getNewCompiler();
    TestMethodCompilerPass pass = new TestMethodCompilerPass(compiler);
    TestActingCallback actingCallback = new TestActingCallback();
    pass.getActingCallback() = actingCallback;
    String source = "foo.bar = undefined";
    Node root = createSourceFile("test.js", source);
    Node externs = null;
    pass.process(externs, root);
  }
}