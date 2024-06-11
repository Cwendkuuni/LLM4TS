import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackType.*;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    methodCompilerPass = new MyMethodCompilerPass(compiler);
    signatureStore = new SignatureStore() {
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
    };
    methodCompilerPass.setSignatureStore(signatureStore);
  }

  @Test
  public void testExternMethodWithSignature() {
    String sourceCode = "var foo = {\n" +
            "  bar: function() {\n" +
            "  },\n" +
            "  baz: function(x, y) {\n" +
            "  }\n" +
            "};\n";

    Node externs = Node.jsSrc(sourceCode);
    Node root = Node.objectLiteral();

    methodCompilerPass.process(externs, root);

    Node foo = methodCompilerPass.methodDefinitions.get("foo").get(0);
    assertEquals(METHOD_DEF, foo.getType());
    assertEquals("baz", methodCompilerPass.externMethods.toString());
    Node baz = getSignature("foo.baz");
    assertNotNull(baz);
    assertEquals(3, baz.getChildCount());
    assertEquals(Token.FUNCTION, baz.getChild(0).getType());
  }

  @Test
  public void testExternMethodWithoutSignature() {
    String sourceCode = "var foo = {\n" +
            "  bar: function() {\n" +
            "  },\n" +
            "  baz:\n" +
            "};\n";

    Node externs = Node.jsSrc(sourceCode);
    Node root = Node.objectLiteral();

    methodCompilerPass.process(externs, root);

    Node foo = methodCompilerPass.methodDefinitions.get("foo").get(0);
    assertEquals(METHOD_DEF, foo.getType());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("baz"));
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("baz"));
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    String sourceCode = "var foo = {\n" +
            "  bar: function() {\n" +
            "  },\n" +
            "  undefinedMethod:\n" +
            "};\n";

    Node externs = Node.jsSrc(sourceCode);
    Node root = Node.objectLiteral();

    methodCompilerPass.process(externs, root);
  }

  @Test
  public void testMethodWithPrototype() {
    String sourceCode = "function Foo() {}\n" +
            "Foo.prototype.bar = function() {\n" +
            "};\n";

    Node externs = null;
    Node root = Node.script(sourceCode);

    methodCompilerPass.process(externs, root);

    Node fooPrototype = methodCompilerPass.methodDefinitions.get("Foo.prototype").get(0);
    assertEquals(METHOD_DEF, fooPrototype.getType());
    Node bar = getSignature("Foo.prototype.bar");
    assertNotNull(bar);
    assertEquals(3, bar.getChildCount());
    assertEquals(Token.FUNCTION, bar.getChild(0).getType());
  }

  @Test
  public void testMethodWithPrototypeAndExtern() {
    String sourceCodeExtern = "var Foo = {\n" +
            "  bar: function() {\n" +
            "  },\n" +
            "  prototype: {\n" +
            "    bar: function() {\n" +
            "    },\n" +
            "  }\n" +
            "};\n";

    String sourceCodeRoot = "function Foo() {}\n" +
            "Foo.prototype.bar = function() {\n" +
            "};\n";

    Node externs = Node.jsSrc(sourceCodeExtern);
    Node root = Node.script(sourceCodeRoot);

    methodCompilerPass.process(externs, root);

    Node fooPrototype = methodCompilerPass.methodDefinitions.get("Foo.prototype").get(0);
    assertEquals(METHOD_DEF, fooPrototype.getType());
    Node externBar = methodCompilerPass.externMethods.isEmpty() ? null : getSignature("Foo.bar");
    Node prototypeBar = getSignature("Foo.prototype.bar");
    assertNotNull(prototypeBar);
    if (externBar != null) {
      assertEquals(prototypeBar, externBar);
    }
  }

  private Node getSignature(String functionName) {
    return methodCompilerPass.signatureStore.getSignature(functionName);
  }
}

class MyMethodCompilerPass extends MethodCompilerPass {
  MyMethodCompilerPass(AbstractCompiler compiler) {
    super(compiler);
  }

  @Override
  Callback getActingCallback() {
    // No-op
    return new Callback() {
    };
  }

  @Override
  SignatureStore getSignatureStore() {
    return signatureStore;
  }
}