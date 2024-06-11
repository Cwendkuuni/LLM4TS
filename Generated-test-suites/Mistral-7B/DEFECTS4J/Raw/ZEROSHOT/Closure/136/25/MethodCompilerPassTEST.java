import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodCompilerPassTest {

  private static class TestSignatureStore implements MethodCompilerPass.SignatureStore {

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

    public List<Node> getSignatures() {
      return signatures.values().asList();
    }
  }

  private static class TestMethodCompilerPass extends MethodCompilerPass {

    TestMethodCompilerPass(TestCompiler compiler) {
      super(compiler);
    }

    @Override
    abstract Callback getActingCallback();

    @Override
    abstract SignatureStore getSignatureStore();

    @Override
    Callback getActingCallback() {
      return new TestActingCallback();
    }

    @Override
    SignatureStore getSignatureStore() {
      return new TestSignatureStore();
    }
  }

  private static class TestActingCallback extends MethodCompilerPass.AbstractCallback {

    private int count = 0;

    @Override
    void call(NodeTraversal traversal, Node node, Node parent) {
      count++;
    }
  }

  @org.junit.Test
  public void testSimplePass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("function foo() {}");
    testPass.process(null, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertNull(testPass.getSignatureStore().getSignature("foo"));
  }

  @org.junit.Test
  public void testExternsPass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node externs = compiler.parse("function bar() {}");
    Node root = compiler.parse("function foo() { bar(); }");
    testPass.process(externs, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertEquals(externs, testPass.getSignatureStore().getSignatures().get(0));
  }

  @org.junit.Test
  public void testIgnoreMissingSignaturePass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node externs = compiler.parse("function bar() {}");
    Node root = compiler.parse("function foo() { bar(); }");
    testPass.process(externs, root);
    assertFalse(testPass.externMethodsWithoutSignatures.contains("bar"));
    assertFalse(testPass.externMethods.contains("bar"));
  }

  @org.junit.Test
  public void testIgnoreDuplicateSignaturePass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node externs = compiler.parse("function bar() {}");
    Node root = compiler.parse("function foo() { bar(); }");
    testPass.process(externs, root);
    testPass.process(externs, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertEquals(externs, testPass.getSignatureStore().getSignatures().get(0));
  }

  @org.junit.Test
  public void testPropertiesPass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("var foo = {getBar: function(){}}");
    testPass.process(null, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertNull(testPass.getSignatureStore().getSignature("getBar"));
  }

  @org.junit.Test
  public void testPrototypePass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("function Foo() {}" +
        "Foo.prototype.getBar = function() {}");
    testPass.process(null, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertEquals(root, testPass.getSignatureStore().getSignatures().get(0));
  }

  @org.junit.Test
  public void testPrototypeFunctionPass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("function Foo() {" +
        "Foo.prototype.getBar = function() {}" +
        "}");
    testPass.process(null, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertEquals(root, testPass.getSignatureStore().getSignatures().get(0));
  }

  @org.junit.Test
  public void testPrototypeVariablePass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("function Foo() {" +
        "Foo.prototype.getBar = null;" +
        "}");
    testPass.process(null, root);
    assertNull(testPass.getSignatureStore().getSignature("getBar"));
  }

  @org.junit.Test
  public void testPrototypeIgnoreMissingPass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("function Foo() {" +
        "Foo.prototype.getBar = null;" +
        "}");
    testPass.process(null, root);
    testPass.process(null, root);
    assertNull(testPass.getSignatureStore().getSignature("getBar"));
  }

  @org.junit.Test
  public void testPrototypeDuplicatePass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass testPass = new TestMethodCompilerPass(compiler);
    Node root = compiler.parse("function Foo() {" +
        "Foo.prototype.getBar = function() {}" +
        "Foo.prototype.getBar = function() {}" +
        "}");
    testPass.process(null, root);
    assertEquals(1, testPass.getActingCallback().count);
    assertEquals(root, testPass.getSignatureStore().getSignatures().get(0));
  }
}