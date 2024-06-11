import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackType;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.testing.TestCompiler;
import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class TestSignatureStore implements MethodCompilerPass.SignatureStore {
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

    public List<Node> getSignatures() {
      return signatures.values();
    }
  }

  @Test
  public void testBasicFunction() {
    String testCode =
        "function foo() {}"; // A simple function declaration

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse(testCode);
    methodPass.process(null, root);

    List<Node> signatures = signatureStore.getSignatures();
    assertEquals(1, signatures.size());
    Node signature = signatures.get(0);
    assertEquals(Token.FUNCTION, signature.getType());
  }

  @Test
  public void testExternFunction() {
    String testCode =
        "// Externs file.\n"
        + "exports.foo = function() {};"; // A function declared in externs

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node externs = compiler.parse(testCode);
    Node root = compiler.parse("function bar() {}"); // A function in the source code

    methodPass.process(externs, root);

    List<Node> signatures = signatureStore.getSignatures();
    assertEquals(1, signatures.size());
    Node signature = signatures.get(0);
    assertEquals(Token.FUNCTION, signature.getType());
    assertEquals("foo", signature.getFirstChild().getString());
  }

  @Test
  public void testNoFunction() {
    String testCode = "function foo() {}"; // A function declaration

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse("var a = 5;"); // No function in the source code

    methodPass.process(null, root);

    List<Node> signatures = signatureStore.getSignatures();
    assertEquals(0, signatures.size());
  }

  @Test
  public void testNonMethodProperty() {
    String testCode = "function foo() {} var a = 5;"; // A non-method property

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse(testCode);

    methodPass.process(null, root);

    Set<String> nonMethodProperties = methodPass.externMethods;
    assertTrue(nonMethodProperties.contains("a"));
  }

  @Test
  public void testPrototype() {
    String testCode = "function Foo() {} Foo.prototype.bar = function() {};";

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse(testCode);

    methodPass.process(null, root);

    List<Node> signatures = signatureStore.getSignatures();
    assertEquals(1, signatures.size());
    Node signature = signatures.get(0);
    assertEquals(Token.FUNCTION, signature.getType());
    assertEquals("bar", signature.getFirstChild().getNext().getString());
  }

  @Test
  public void testPrototypeWithExtern() {
    String testCode =
        "// Externs file.\n"
        + "exports.Foo = function() {};\n"
        + "exports.Foo.prototype.bar = function() {};"; // A prototype function declared in externs

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node externs = compiler.parse(testCode);
    Node root = compiler.parse("function Foo() {}"); // A constructor in the source code

    methodPass.process(externs, root);

    List<Node> signatures = signatureStore.getSignatures();
    assertEquals(1, signatures.size());
    Node signature = signatures.get(0);
    assertEquals(Token.FUNCTION, signature.getType());
    assertTrue(signature.getFirstChild().getNext().getNext().getType() == Token.FUNCTION);
  }

  @Test
  public void testMissingPrototypeFunction() {
    String testCode = "function Foo() {} Foo.prototype.bar = 5;"; // A prototype without a function

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse(testCode);

    methodPass.process(null, root);

    Set<String> nonMethodProperties = methodPass.externMethods;
    assertTrue(nonMethodProperties.contains("bar"));
  }

  @Test
  public void testMissingPrototypeProperty() {
    String testCode = "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype.baz = 5;"; // A prototype with a missing property

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse(testCode);

    methodPass.process(null, root);

    Set<String> nonMethodProperties = methodPass.externMethods;
    assertFalse(nonMethodProperties.contains("baz"));
  }

  @Test
  public void testMissingFunction() {
    String testCode = "function Foo() {} function bar() {}"; // A missing function

    TestCompiler compiler = new TestCompiler();
    CompilerPass pass = new MethodCompilerPass(compiler);

    SignatureStore signatureStore = new TestSignatureStore();
    MethodCompilerPass methodPass = new MyMethodCompilerPass(pass, signatureStore);

    Node root = compiler.parse(testCode);

    methodPass.process(null, root);

    Set<String> nonMethodProperties = methodPass.externMethods;
    assertFalse(nonMethodProperties.contains("bar"));
  }

  private static class MyMethodCompilerPass extends MethodCompilerPass {
    public MyMethodCompilerPass(CompilerPass pass, SignatureStore store) {
      super(pass, store);
    }

    @Override
    abstract Callback getActingCallback();

    @Override
    abstract SignatureStore getSignatureStore();

    @Override
    Callback getActingCallback() {
      // Do nothing in this test
      return new Callback() {};
    }

    @Override
    SignatureStore getSignatureStore() {
      return (SignatureStore) signatureStore;
    }
  }
}