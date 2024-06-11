import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = new Compiler();
    methodCompilerPass = new MyMethodCompilerPass(compiler);
    signatureStore = new SignatureStoreImpl();
    methodCompilerPass.setSignatureStore(signatureStore);
  }

  @Test
  public void testMethodFromExternsFile() {
    String source = "externs = {\n" +
            "  'myFunction': function() {}\n" +
            "};\n";

    Node externs = NodeOperations.parse(source);
    Node root = NodeOperations.parse("function main() {}");
    methodCompilerPass.process(externs, root);

    assertEquals(1, signatureStore.size());
    assertEquals("myFunction", signatureStore.keySet().iterator().next());
    assertSame(externs, signatureStore.get(signatureStore.keySet().iterator().next()));
  }

  @Test
  public void testMethodFromObjectLiteral() {
    String source = "var myObject = {\n" +
            "  method1: function() {},\n" +
            "  method2: function() {}\n" +
            "};\n";

    Node externs = null;
    Node root = NodeOperations.parse(source);
    methodCompilerPass.process(externs, root);

    assertEquals(2, signatureStore.size());
    assertTrue(signatureStore.containsKey("method1"));
    assertTrue(signatureStore.containsKey("method2"));
  }

  @Test
  public void testMethodWithPrototype() {
    String source = "var Foo = {\n" +
            "  prototype: {\n" +
            "    myMethod: function() {}\n" +
            "  }\n" +
            "};\n";

    Node externs = null;
    Node root = NodeOperations.parse(source);
    methodCompilerPass.process(externs, root);

    assertEquals(1, signatureStore.size());
    assertEquals("prototype.myMethod", signatureStore.keySet().iterator().next());
    assertSame(root, signatureStore.get(signatureStore.keySet().iterator().next()));
  }

  @Test
  public void testMethodWithNoSignatureInExterns() {
    String source = "externs = {\n" +
            "  'myFunction': function() {}\n" +
            "};\n";

    Node externs = NodeOperations.parse(source);
    Node root = NodeOperations.parse("myFunction = function() {};");
    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.size());
    assertFalse(signatureStore.containsKey("myFunction"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMethodWithUndefinedName() {
    String source = "externs = {\n" +
            "  undefinedMethod: function() {}\n" +
            "};\n";

    Node externs = NodeOperations.parse(source);
    Node root = NodeOperations.parse("myFunction = function() {};");
    methodCompilerPass.process(externs, root);
  }

  @Test
  public void testMethodWithSameNameInExternsAndObjectLiteral() {
    String sourceExterns = "externs = {\n" +
            "  'myFunction': function() {}\n" +
            "};\n";
    String sourceRoot = "var obj = {\n" +
            "  myFunction: function() {}\n" +
            "};\n";

    Node externs = NodeOperations.parse(sourceExterns);
    Node root = NodeOperations.parse(sourceRoot);
    methodCompilerPass.process(externs, root);

    assertEquals(1, signatureStore.size());
    assertEquals("myFunction", signatureStore.keySet().iterator().next());
  }

  @Test
  public void testMethodWithNoSignatureInSource() {
    String source = "function myFunction() {}";

    Node externs = null;
    Node root = NodeOperations.parse(source);
    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.size());
    assertFalse(signatureStore.containsKey("myFunction"));
  }

  @Test
  public void testMethodWithNoName() {
    String source = "function() {}";

    Node externs = null;
    Node root = NodeOperations.parse(source);
    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.size());
  }

  @Test
  public void testMethodWithErrors() {
    String source = "function myFunction() {\n" +
            "  throw new Error('Something went wrong');\n" +
            "}";

    Node externs = null;
    Node root = NodeOperations.parse(source);
    methodCompilerPass.process(externs, root);

    assertEquals(0, signatureStore.size());
  }

  private static class Compiler extends AbstractCompiler {
  }

  private static class MyMethodCompilerPass extends MethodCompilerPass {
    MyMethodCompilerPass(AbstractCompiler compiler) {
      super(compiler);
    }

    @Override
    Callback getActingCallback() {
      // Empty implementation in this test case
      return new DoNothingCallback();
    }

    @Override
    SignatureStore getSignatureStore() {
      return signatureStore;
    }
  }

  private static class DoNothingCallback implements Callback {
  }

  private static class SignatureStoreImpl implements SignatureStore {
    private Set<String> signatures = Sets.newHashSet();

    @Override
    public void reset() {
      signatures.clear();
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.add(functionName);
    }

    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }

    public int size() {
      return signatures.size();
    }

    public boolean containsKey(String key) {
      return signatures.contains(key);
    }
  }
}