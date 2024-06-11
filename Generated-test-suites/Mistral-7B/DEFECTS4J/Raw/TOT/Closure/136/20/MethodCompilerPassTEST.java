import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MethodCompilerPassTest {

  @Test
  public void testSimpleMethodDeclaration() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {}";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.prototype.bar", methodNode.toString());
  }

  @Test
  public void testMethodDeclarationWithSignature() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.prototype.bar = function(x) {}";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.prototype.bar", methodNode.toString());

    SignatureStore signatureStore = pass.getSignatureStore();
    assertEquals(1, signatureStore.getSignatures().size());
    Node functionNode = signatureStore.getSignatures().get("Foo.prototype.bar");
    assertEquals(FUNCTION_EXPR, functionNode.getType());
    assertEquals(1, functionNode.getNumChildren());
    Node parameterNode = functionNode.getFirstChild();
    assertEquals(Token.IDENT, parameterNode.getType());
    assertEquals("x", parameterNode.getString());
  }

  @Test
  public void testMethodDeclarationWithoutSignature() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.bar = function() {}";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.bar", methodNode.toString());

    SignatureStore signatureStore = pass.getSignatureStore();
    assertEquals(0, signatureStore.getSignatures().size());
  }

  @Test
  public void testMethodDeclarationInExterns() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "extern Foo.bar = function(x) {}";

    Node externs = RhinoUtils.compileToString(sourceCode);
    Node root = null;

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.bar", methodNode.toString());

    SignatureStore signatureStore = pass.getSignatureStore();
    assertEquals(1, signatureStore.getSignatures().size());
    Node functionNode = signatureStore.getSignatures().get("Foo.bar");
    assertEquals(FUNCTION_EXPR, functionNode.getType());
    assertEquals(1, functionNode.getNumChildren());
    Node parameterNode = functionNode.getFirstChild();
    assertEquals(Token.IDENT, parameterNode.getType());
    assertEquals("x", parameterNode.getString());
  }

  @Test
  public void testMethodDeclarationWithDuplicateName() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {}; Foo.prototype.bar = function() {};";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.prototype.bar", methodNode.toString());
  }

  @Test
  public void testMethodDeclarationWithNonMethodProperty() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.bar = 42;";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(0, methodDefinitions.size());
    assertEquals(0, externMethods.size());
    assertEquals(1, nonMethodProperties.size());

    String nonMethodPropertyName = "bar";
    assertTrue(nonMethodProperties.contains(nonMethodPropertyName));
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedMethod() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.prototype.bar = undefined;";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);
  }

  @Test
  public void testStaticMethodDeclaration() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.bar = function() {}";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.bar", methodNode.toString());
  }

  @Test
  public void testStaticMethodDeclarationWithSignature() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.bar = function(x) {}";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.bar", methodNode.toString());

    SignatureStore signatureStore = pass.getSignatureStore();
    assertEquals(1, signatureStore.getSignatures().size());
    Node functionNode = signatureStore.getSignatures().get("Foo.bar");
    assertEquals(FUNCTION_EXPR, functionNode.getType());
    assertEquals(1, functionNode.getNumChildren());
    Node parameterNode = functionNode.getFirstChild();
    assertEquals(Token.IDENT, parameterNode.getType());
    assertEquals("x", parameterNode.getString());
  }

  @Test
  public void testPrototypeProperty() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.prototype.bar = 42;";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(0, methodDefinitions.size());
    assertEquals(0, externMethods.size());
    assertEquals(1, nonMethodProperties.size());

    String nonMethodPropertyName = "bar";
    assertTrue(nonMethodProperties.contains(nonMethodPropertyName));
  }

  @Test
  public void testPrototypePropertyWithSignature() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "function Foo() {} Foo.prototype.bar = function(x) {}";

    Node externs = null;
    Node root = RhinoUtils.compileToString(sourceCode);

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(0, methodDefinitions.size());
    assertEquals(0, externMethods.size());
    assertEquals(1, nonMethodProperties.size());

    String nonMethodPropertyName = "bar";
    assertTrue(nonMethodProperties.contains(nonMethodPropertyName));
  }

  @Test
  public void testMethodWithExternDeclaration() {
    AbstractCompiler compiler = new Compiler();
    CompilerPass pass = new MyMethodCompilerPass(compiler);

    String sourceCode = "extern function Foo.bar(x) {} function Foo() {} Foo.prototype.bar = function() {}";

    Node externs = RhinoUtils.compileToString(sourceCode);
    Node root = null;

    pass.process(externs, root);

    Multimap<String, Node> methodDefinitions = pass.methodDefinitions;
    Set<String> externMethods = pass.externMethods;
    Set<String> nonMethodProperties = pass.nonMethodProperties;

    assertEquals(1, methodDefinitions.size());
    assertEquals(1, externMethods.size());
    assertEquals(0, nonMethodProperties.size());

    String methodName = "bar";
    Node methodNode = methodDefinitions.get(methodName).iterator().next();
    assertEquals(CALL_EXPR, methodNode.getType());
    assertEquals("Foo.prototype.bar", methodNode.toString());

    SignatureStore signatureStore = pass.getSignatureStore();
    assertEquals(1, signatureStore.getSignatures().size());
    Node functionNode = signatureStore.getSignatures().get("Foo.prototype.bar");
    assertEquals(FUNCTION_EXPR, functionNode.getType());
    assertEquals(1, functionNode.getNumChildren());
    Node parameterNode = functionNode.getFirstChild();
    assertEquals(Token.IDENT, parameterNode.getType());
    assertEquals("x", parameterNode.getString());
  }

  private static class MyMethodCompilerPass extends MethodCompilerPass {
    MyMethodCompilerPass(AbstractCompiler compiler) {
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
  }

  private static class DoNothingCallback implements Callback {
    @Override
    public void process(NodeTraversal t, Node n, Node parent) {
      // Do nothing
    }
  }

  private static class DoNothingSignatureStore implements SignatureStore {
    Multimap<String, Node> signatures = HashMultimap.create();

    @Override
    public void reset() {
      // Do nothing
    }

    @Override
    public void addSignature(
        String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }

    @Override
    public void removeSignature(String functionName) {
      // Do nothing
    }

    public Multimap<String, Node> getSignatures() {
      return signatures;
    }
  }
}