import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.NodeOperations;

public class MethodCompilerPassTest extends BaseCompilerTest {

  private static final String EXTERN_FILE = "TestExterns.js";
  private static final String SOURCE_FILE = "TestSource.js";

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    compiler = new Compiler(new CompilerOptions());
    methodCompilerPass = new MethodCompilerPass(compiler);
    signatureStore = new TestSignatureStore();
    methodCompilerPass.setSignatureStore(signatureStore);
  }

  @org.junit.Test
  public void testEmptyExternsFile() throws Exception {
    Node externs = null;
    Node root = getTestNode("/TestSource.js");
    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @org.junit.Test
  public void testGetExternMethodsFromObjectLiterals() throws Exception {
    Node externs = createNodeFromString("{" +
        "  method1: function() {}, " +
        "  method2: function() {}, " +
        "  method3: function() {}" +
        "}");
    Node root = getTestNode("/TestSource.js");

    methodCompilerPass.process(externs, root);

    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals("method1", methodCompilerPass.externMethods.iterator().next());
    assertEquals("method2", methodCompilerPass.externMethods.iterator().next());
    assertEquals("method3", methodCompilerPass.externMethods.iterator().next());

    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @org.junit.Test
  public void testGetExternMethodsFromGetProperties() throws Exception {
    Node externs = createNodeFromString("Foo = {" +
        "  prototype: {" +
        "    method1: function() {}, " +
        "    method2: function() {}" +
        "  }" +
        "};");
    Node root = getTestNode("/TestSource.js");

    methodCompilerPass.process(externs, root);

    assertEquals(2, methodCompilerPass.externMethods.size());
    assertEquals("prototype.method1", methodCompilerPass.externMethods.iterator().next());
    assertEquals("prototype.method2", methodCompilerPass.externMethods.iterator().next());

    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @org.junit.Test
  public void testGetExternMethodsWithNoSignatures() throws Exception {
    Node externs = createNodeFromString("Foo = {" +
        "  method1: function() {}, " +
        "  method2: function() {}" +
        "};");
    Node root = createNodeFromString("Foo.method3 = function() {}");

    methodCompilerPass.process(externs, root);

    assertEquals(2, methodCompilerPass.externMethods.size());
    assertEquals("method1", methodCompilerPass.externMethods.iterator().next());
    assertEquals("method2", methodCompilerPass.externMethods.iterator().next());

    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("method3"));
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @org.junit.Test
  public void testGetSignaturesFromGetProperties() throws Exception {
    Node externs = null;
    Node root = createNodeFromString("Foo = {" +
        "  prototype: {" +
        "    method1: function() {}, " +
        "    method2: function(a, b) {}" +
        "  }" +
        "};");

    methodCompilerPass.process(externs, root);

    assertEquals(2, methodCompilerPass.methodDefinitions.size());

    Node method1 = methodCompilerPass.methodDefinitions.get("prototype.method1");
    assertNull(method1.getFirstChild());

    Node method2 = methodCompilerPass.methodDefinitions.get("prototype.method2");
    assertSame(createNodeFromString("function(a, b) {}"), method2);
  }

  @org.junit.Test
  public void testGetSignaturesFromFunctionDeclarations() throws Exception {
    Node externs = null;
    Node root = createNodeFromString("function Foo() {" +
        "  this.method1 = function(a, b) {};" +
        "  this.method2 = function() {};" +
        "}");

    methodCompilerPass.process(externs, root);

    assertEquals(2, methodCompilerPass.methodDefinitions.size());

    Node method1 = methodCompilerPass.methodDefinitions.get("Foo.method1");
    assertSame(createNodeFromString("function(a, b) {}"), method1);

    Node method2 = methodCompilerPass.methodDefinitions.get("Foo.method2");
    assertNull(method2.getFirstChild());
  }

  @org.junit.Test
  public void testGetSignaturesFromFunctionExpressions() throws Exception {
    Node externs = null;
    Node root = createNodeFromString("function Foo() {" +
        "  this.method1 = function() {};" +
        "  var method2 = function(a, b) {};" +
        "}");

    methodCompilerPass.process(externs, root);

    assertEquals(2, methodCompilerPass.methodDefinitions.size());

    Node method1 = methodCompilerPass.methodDefinitions.get("Foo.method1");
    assertNull(method1.getFirstChild());

    Node method2 = methodCompilerPass.methodDefinitions.get("Foo.method2");
    assertSame(createNodeFromString("function(a, b) {}"), method2);
  }

  @org.junit.Test
  public void testGetSignaturesFromPrototypeAssignments() throws Exception {
    Node externs = null;
    Node root = createNodeFromString("Foo = {}");
    root = NodeOperations.insertBefore(root, NodeOperations.createAssignment(
        root, "prototype", createNodeFromString("{method1: function() {})")), 0);

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());

    Node method1 = methodCompilerPass.methodDefinitions.get("Foo.prototype.method1");
    assertNull(method1.getFirstChild());
  }

  @org.junit.Test
  public void testGetSignaturesFromPrototypeAssignmentsWithFunction() throws Exception {
    Node externs = null;
    Node root = createNodeFromString("Foo = {}");
    root = NodeOperations.insertBefore(root, NodeOperations.createAssignment(
        root, "prototype", createNodeFromString("{method1: function(a, b) {})}")), 0);

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());

    Node method1 = methodCompilerPass.methodDefinitions.get("Foo.prototype.method1");
    assertSame(createNodeFromString("function(a, b) {}"), method1);
  }

  @org.junit.Test
  public void testRemoveExternSignature() throws Exception {
    Node externs = createNodeFromString("Foo = {" +
        "  prototype: {" +
        "    method1: function() {}, " +
        "    method2: function(a, b) {}" +
        "  }" +
        "};");
    Node root = createNodeFromString("delete Foo.prototype.method1;");

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertFalse(methodCompilerPass.methodDefinitions.containsKey("prototype.method1"));
  }

  private Node getTestNode(String fileName) throws Exception {
    return getTestFile(fileName).getParseTree();
  }

  private class TestSignatureStore implements SignatureStore {

    @Override
    public void reset() {
      // no-op
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      // no-op
    }

    @Override
    public void removeSignature(String functionName) {
      // no-op
    }
  }
}