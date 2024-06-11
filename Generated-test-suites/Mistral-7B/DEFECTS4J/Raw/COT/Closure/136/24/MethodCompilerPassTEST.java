import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {

  private static class TestMethodCompilerPass extends MethodCompilerPass {
    @Override
    Callback getActingCallback() {
      // Override this method with a test callback in the subclass
      return null;
    }

    @Override
    SignatureStore getSignatureStore() {
      // Use a dummy signature store for testing
      return new SignatureStore() {
        private Multimap<String, Node> signatures = HashMultimap.create();

        @Override
        public void reset() {
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
    }
  }

  @Test
  public void testProcessExterns() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);

    // Arrange
    String sourceCode = "var foo = {\n" +
        "  bar: function() {},\n" +
        "  baz: function() {}\n" +
        "};";
    Node root = AbstractCompiler.parseJs(sourceCode, "test.js");

    // Act
    compilerPass.process(null, root);

    // Assert
    assertEquals(2, compilerPass.externMethods.size());
    assertEquals("bar", compilerPass.externMethods.iterator().next());
    assertEquals("baz", compilerPass.externMethods.iterator().next());

    assertEquals(0, compilerPass.externMethodsWithoutSignatures.size());
  }

  @Test
  public void testProcessMethods() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);

    // Arrange
    String sourceCode = "function Foo() {\n" +
        "  this.bar = function() {}\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.getBar = function() {}\n" +
        "\n" +
        "Foo.prototype = {\n" +
        "  getBaz: function() {}\n" +
        "};";
    Node root = AbstractCompiler.parseJs(sourceCode, "test.js");

    // Act
    compilerPass.process(null, root);

    // Assert
    assertEquals(4, compilerPass.methodDefinitions.size());

    Node fooConstructor = compilerPass.getSignatureStore().getSignature("Foo");
    Node fooBar = compilerPass.getSignatureStore().getSignature("Foo.bar");
    Node fooGetBar = compilerPass.getSignatureStore().getSignature("Foo.prototype.getBar");
    Node fooGetBaz = compilerPass.getSignatureStore().getSignature("Foo.prototype.getBaz");

    assertNotNull(fooConstructor);
    assertNotNull(fooBar);
    assertNotNull(fooGetBar);
    assertNotNull(fooGetBaz);
  }

  @Test
  public void testNonMethodProperties() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);

    // Arrange
    String sourceCode = "function Foo() {\n" +
        "  this.bar = 5;\n" +
        "  this.baz = function() {}\n" +
        "}\n" +
        "\n" +
        "Foo.prototype.getBar = function() {}\n" +
        "\n" +
        "Foo.prototype = {\n" +
        "  getBaz: function() {}\n" +
        "};";
    Node root = AbstractCompiler.parseJs(sourceCode, "test.js");

    // Act
    compilerPass.process(null, root);

    // Assert
    assertEquals(3, compilerPass.nonMethodProperties.size());
    assertTrue(compilerPass.nonMethodProperties.contains("bar"));
    assertTrue(compilerPass.nonMethodProperties.contains("baz"));
    assertTrue(compilerPass.nonMethodProperties.contains("getBaz"));
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);

    // Arrange
    String sourceCode = "var foo = {\n" +
        "  bar: function() {},\n" +
        "  baz: function() {}\n" +
        "};\n" +
        "\n" +
        "// This method has no signature\n" +
        "function qux() {}\n";
    Node root = AbstractCompiler.parseJs(sourceCode, "test.js");

    // Act
    compilerPass.process(null, root);

    // Assert
    assertEquals(2, compilerPass.externMethods.size());
    assertEquals("bar", compilerPass.externMethods.iterator().next());
    assertEquals("baz", compilerPass.externMethods.iterator().next());

    assertEquals(1, compilerPass.externMethodsWithoutSignatures.size());
    assertEquals("qux", compilerPass.externMethodsWithoutSignatures.iterator().next());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    TestMethodCompilerPass compilerPass = new TestMethodCompilerPass(null);

    // Arrange
    String sourceCode = "function Foo() {\n" +
        "  this.bar = function() {}\n" +
        "}\n" +
        "\n" +
        "// This function is undefined\n" +
        "Foo.prototype.qux = undefined;\n";
    Node root = AbstractCompiler.parseJs(sourceCode, "test.js");

    // Act and Assert (expecting an IllegalStateException)
    compilerPass.process(null, root);
  }
}