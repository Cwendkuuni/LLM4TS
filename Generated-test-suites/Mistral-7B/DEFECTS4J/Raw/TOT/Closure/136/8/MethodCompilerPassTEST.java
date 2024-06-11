import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class MethodCompilerPassTest {

  @Test
  public void testMethodCompilerPass() {
    Compiler compiler = new Compiler();
    CompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.getBaz = function() { return 42; }";

    Node root = compiler.parse(sourceCode, null, null);
    methodCompilerPass.process(null, root);

    Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;

    // Check that the correct methods were identified
    assertEquals(2, methodDefinitions.size());
    assertEquals("bar", methodDefinitions.get("Foo.prototype.bar").get(0).toString());
    assertEquals("getBaz", methodDefinitions.get("Foo.prototype.getBaz").get(0).toString());

    // Check that extern methods were not added
    assertTrue(methodCompilerPass.externMethods.isEmpty());

    // Check that non-method properties were not added
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
  }

  @Test
  public void testMethodCompilerPassWithExterns() {
    Compiler compiler = new Compiler();
    CompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

    String externs = "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.getBaz = function() { return 42; }";

    String sourceCode = "function Foo() {}\n";

    Node externsNode = compiler.parse(externs, null, null);
    Node root = compiler.parse(sourceCode, null, null);

    methodCompilerPass.process(externsNode, root);

    Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;

    // Check that the correct methods were identified from externs
    assertEquals(2, methodDefinitions.size());
    assertEquals("bar", methodDefinitions.get("Foo.prototype.bar").get(0).toString());
    assertEquals("getBaz", methodDefinitions.get("Foo.prototype.getBaz").get(0).toString());

    // Check that the methods were not added again from the source code
    assertEquals(2, methodDefinitions.size());

    // Check that extern methods without signatures were ignored
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
  }

  @Test
  public void testMethodCompilerPassWithInvalidProperty() {
    Compiler compiler = new Compiler();
    CompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "Foo.prototype.baz = 42;";

    Node root = compiler.parse(sourceCode, null, null);
    methodCompilerPass.process(null, root);

    // Check that the invalid property was not added as a method
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());

    // Check that it was added to the non-method properties list
    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals("baz", methodCompilerPass.nonMethodProperties.get(0));
  }

  @Test
  public void testMethodCompilerPassWithErrorScenario() {
    Compiler compiler = new Compiler();
    CompilerPass methodCompilerPass = new MethodCompilerPass(compiler);

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() { throw new Error('Boom'); }";

    Node root = compiler.parse(sourceCode, null, null);
    methodCompilerPass.process(null, root);

    // Check that the method was identified
    Multimap<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions;
    assertEquals(1, methodDefinitions.size());
    assertEquals("bar", methodDefinitions.get("Foo.prototype.bar").get(0).toString());

    // Check that no errors were thrown during processing
    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
  }
}