import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.base.ConfigLoader;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() throws IOException {
    CompilerOptions options = ConfigLoader.loadDefault();
    compiler = new Compiler(options);
    methodCompilerPass = new MethodCompilerPass(compiler);
    signatureStore = new SignatureStorage();
  }

  @Test
  public void testExternMethodsWithSignatures() {
    String sourceCode = "function foo() {}";
    Node externs = Node.parse(sourceCode);
    Node root = Node.parse("function bar() {}");

    methodCompilerPass.process(externs, root);

    Map<String, Node> expectedMethods = newHashMap();
    expectedMethods.put("foo", root);
    expectedMethods.put("bar", root);

    assertEquals(expectedMethods, methodCompilerPass.methodDefinitions.asMap());
    assertEquals(2, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    String sourceCode = "function Foo() {}\n" +
            "Foo.prototype.getBar = function() {}\n" +
            "Foo.prototype.getBaz = function() {}\n";
    Node externs = Node.parse(sourceCode);
    Node root = Node.parse("function Main() {}");

    methodCompilerPass.process(externs, root);

    Map<String, Node> expectedMethods = newHashMap();
    expectedMethods.put("getBar", root.getChild(0));
    expectedMethods.put("getBaz", root.getChild(0));
    expectedMethods.put("Main", root);

    assertEquals(expectedMethods, methodCompilerPass.methodDefinitions.asMap());
    assertEquals(2, methodCompilerPass.externMethods.size());
    assertEquals(2, methodCompilerPass.externMethodsWithoutSignatures.size());
  }

  @Test
  public void testNonMethodProperties() {
    String sourceCode = "var foo = 1;\n" +
            "function Foo() {}\n" +
            "Foo.prototype.getBar = function() {}\n";
    Node externs = Node.parse(sourceCode);
    Node root = Node.parse("function Main() {}");

    methodCompilerPass.process(externs, root);

    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    String sourceCode = "function Foo() {\n" +
            "  getFooBar(); // undefined getFooBar function\n" +
            "}";
    Node externs = null;
    Node root = Node.parse(sourceCode);

    methodCompilerPass.process(externs, root);
  }

  @Test
  public void testPrototypeFunction() {
    String sourceCode = "function Foo() {}\n" +
            "Foo.prototype.getBar = function() {}\n" +
            "function Main() {\n" +
            "  var obj = new Foo();\n" +
            "  obj.getBar();\n" +
            "}";
    Node externs = null;
    Node root = Node.parse(sourceCode);

    methodCompilerPass.process(externs, root);

    Node expectedMethod = root.getFirstChild();
    assertEquals("getBar", expectedMethod.getFirstChild().getString());
  }

  @Test
  public void testStaticFunction() {
    String sourceCode = "function Foo() {}\n" +
            "Foo.getBar = function() {}\n" +
            "function Main() {\n" +
            "  Foo.getBar();\n" +
            "}";
    Node externs = Node.parse(sourceCode);
    Node root = Node.parse("function Main() {}");

    methodCompilerPass.process(externs, root);

    Node expectedMethod = root.getFirstChild();
    assertEquals("getBar", expectedMethod.getFirstChild().getString());
  }

  @Test
  public void testPrototypeWithAssignment() {
    String sourceCode = "function Foo() {}\n" +
            "Foo.prototype.getBar = function() {}\n" +
            "function Main() {\n" +
            "  Foo.prototype.getBar = function() {}\n" +
            "}";
    Node externs = Node.parse(sourceCode);
    Node root = Node.parse("function Main() {}");

    methodCompilerPass.process(externs, root);

    Node expectedMethod = root.getFirstChild();
    assertEquals("getBar", expectedMethod.getFirstChild().getString());
  }

  @Test
  public void testStaticWithAssignment() {
    String sourceCode = "function Foo() {}\n" +
            "Foo.getBar = function() {}\n" +
            "function Main() {\n" +
            "  Foo.getBar = function() {}\n" +
            "}";
    Node externs = Node.parse(sourceCode);
    Node root = Node.parse("function Main() {}");

    methodCompilerPass.process(externs, root);

    Node expectedMethod = root.getFirstChild();
    assertEquals("getBar", expectedMethod.getFirstChild().getString());
  }

  private static class SignatureStorage implements MethodCompilerPass.SignatureStore {
    private Map<String, Node> signatures = newHashMap();

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
    public Node getSignature(String functionName) {
      return signatures.get(functionName);
    }
  }
}