import static org.junit.Assert.*;

import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * Test for MethodCompilerPass class
 */
public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

    // Test with empty externs and root
    Node externs = NodeOperations.createNull();
    Node root = NodeOperations.createNull();
    methodCompilerPass.process(externs, root);
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());

    // Test with externs file
    String externsSource = "Foo.prototype.getBar = function() {};";
    externs = NodeOperations.createFromString(externsSource, "externs.js");
    root = NodeOperations.createFromString("", "main.js");
    methodCompilerPass.process(externs, root);
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("getBar", methodCompilerPass.externMethods.iterator().next());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());

    // Test with source file containing methods
    String source = "function foo(){} function bar(){} Foo.prototype.baz = function(){}";
    root = NodeOperations.createFromString(source, "main.js");
    methodCompilerPass.process(externs, root);
    assertEquals(3, methodCompilerPass.externMethods.size());
    assertEquals("foo", methodCompilerPass.externMethods.iterator().next());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals("baz", methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals("foo", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());
    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals("prototype", methodCompilerPass.nonMethodProperties.iterator().next());
    assertEquals(3, methodCompilerPass.methodDefinitions.size());
    assertEquals("foo", methodCompilerPass.methodDefinitions.get("foo").toString());
    assertEquals("bar", methodCompilerPass.methodDefinitions.get("bar").toString());
    assertEquals("baz", methodCompilerPass.methodDefinitions.get("baz").toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalStateException() {
    TestCompiler compiler = new TestCompiler();
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

    String source = "var Foo = {}; Foo.prototype.getBar = function() {};";
    Node externs = NodeOperations.createFromString("", "externs.js");
    Node root = NodeOperations.createFromString(source, "main.js");
    methodCompilerPass.process(externs, root);

    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
    signatureStore.addSignature("getBar", NodeOperations.createFromString("function() {}", ""), "main.js");
    methodCompilerPass.process(externs, root);

    try {
      methodCompilerPass.addSignature("getBar", NodeOperations.createFromString("function() {}", ""), root.getFirstChild());
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}