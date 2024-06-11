import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private static final String TEST_CASE = "TestMethodCompilerPass";
  private static final String TEST_FILE = "TestMethodCompilerPass.java";

  @org.junit.Test
  public void testMethodCompilerPass() throws Exception {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String code = "function Foo() {} " +
        "Foo.prototype.bar = function() {} " +
        "Foo.prototype.prototype = { getBaz: function() {} };";

    Node externalSourceNode = parseAndCompileString(compiler, code, options);
    Node rootNode = externalSourceNode.getFirstChild();

    MethodCompilerPass pass = new MyMethodCompilerPass(compiler);
    pass.process(externalSourceNode, rootNode);

    Set<String> expectedMethods = Sets.newHashSet("bar", "getBaz");
    Set<String> actualMethods = pass.methodDefinitions.keySet();
    assertEquals("Unexpected number of methods defined", expectedMethods.size(), actualMethods.size());
    assertEquals("Unexpected methods defined", expectedMethods, actualMethods);

    // Test for methods defined in externs
    Set<String> externMethods = pass.externMethods;
    assertTrue("Methods expected to be in externs not present", externMethods.containsAll(Sets.newHashSet("bar")));

    // Test for methods without signatures
    Set<String> externMethodsWithoutSignatures = pass.externMethodsWithoutSignatures;
    assertTrue("Methods without signatures present unexpectedly", externMethodsWithoutSignatures.isEmpty());

    // Test for non-method properties
    Set<String> nonMethodProperties = pass.nonMethodProperties;
    assertTrue("Unexpected non-method properties present", nonMethodProperties.isEmpty());
  }

  private Node parseAndCompileString(Compiler compiler, String code, CompilerOptions options) throws IOException {
    Node node = NodeFactory.compile(code, options);
    Node externalSourceNode = Traverse.deep(compiler, node, null, AbstractCallback.empty());
    return externalSourceNode;
  }

  private static class MyMethodCompilerPass extends MethodCompilerPass {

    public MyMethodCompilerPass(Compiler compiler) {
      super(compiler);
    }

    @Override
    Callback getActingCallback() {
      return new DoNothingCallback();
    }

    @Override
    SignatureStore getSignatureStore() {
      return new DoNothingSignatureStore();
    }

    private static class DoNothingCallback extends AbstractCallback {
    }

    private static class DoNothingSignatureStore implements MethodCompilerPass.SignatureStore {

      @Override
      public void addSignature(String functionName, Node functionNode, String sourceFile) {
      }

      @Override
      public void removeSignature(String functionName) {
      }

      @Override
      public void reset() {
      }
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testUnexpectedMethod() throws Exception {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String code = "function Foo() {} " +
        "Foo.prototype.bar = function() {} " +
        "Foo.prototype.qux = function() {}"; // Unexpected method

    Node externalSourceNode = parseAndCompileString(compiler, code, options);
    Node rootNode = externalSourceNode.getFirstChild();

    MethodCompilerPass pass = new MyMethodCompilerPass(compiler);
    pass.process(externalSourceNode, rootNode);

    fail("Expected AssertionError but no exception was thrown.");
  }

  @org.junit.Test
  public void testNoMethods() throws Exception {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String code = "function Foo() {}";

    Node externalSourceNode = parseAndCompileString(compiler, code, options);
    Node rootNode = externalSourceNode.getFirstChild();

    MethodCompilerPass pass = new MyMethodCompilerPass(compiler);
    pass.process(externalSourceNode, rootNode);

    Set<String> actualMethods = pass.methodDefinitions.keySet();
    assertTrue("Methods unexpectedly defined", actualMethods.isEmpty());
  }

  @org.junit.Test
  public void testPrototypeChain() throws Exception {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    String code = "function Base() {} " +
        "Base.prototype.foo = function() {} " +
        "function Derived() { Base.call(this); } " +
        "Derived.prototype.bar = function() {} " +
        "Derived.prototype.prototype.baz = function() {} ";

    Node externalSourceNode = parseAndCompileString(compiler, code, options);
    Node rootNode = externalSourceNode.getFirstChild();

    MethodCompilerPass pass = new MyMethodCompilerPass(compiler);
    pass.process(externalSourceNode, rootNode);

    Set<String> expectedMethods = Sets.newHashSet("foo", "bar", "baz");
    Set<String> actualMethods = pass.methodDefinitions.keySet();
    assertEquals("Unexpected number of methods defined", expectedMethods.size(), actualMethods.size());
    assertEquals("Unexpected methods defined", expectedMethods, actualMethods);
  }
}