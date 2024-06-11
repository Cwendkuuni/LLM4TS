import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;
import static com.google.javascript.jscomp.NodeTraversal.Callback.*;
import static com.google.javascript.jscomp.NodeTraversal.TraverseRootsCallback.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.ast.FunctionDeclaration;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {
  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        // Empty implementation, as we're just testing the setup and method discovery
        return NO_OP;
      }

      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = new SignatureStore() {
            Multimap<String, Node> signatures = HashMultimap.create();

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
          };
        }
        return signatureStore;
      }
    };
  }

  @Test
  public void testEmpty() {
    Node root = null;
    Node externs = null;

    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.isEmpty());
  }

  @Test
  public void testExterns() {
    String source = "var foo = {}; foo.bar = function() {};";
    Node externs = Node.fromSource(source);

    methodCompilerPass.process(externs, null);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    FunctionDeclaration functionDeclaration = methodCompilerPass.methodDefinitions.get("bar").asFunction();
    assertNotNull(functionDeclaration);
  }

  @Test
  public void testRootWithFunctionDeclaration() {
    String source = "function foo() {}";
    Node root = Node.fromSource(source);

    methodCompilerPass.process(null, root);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("foo", methodCompilerPass.externMethods.iterator().next());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    FunctionDeclaration functionDeclaration = methodCompilerPass.methodDefinitions.get("foo").asFunction();
    assertNotNull(functionDeclaration);
  }

  @Test
  public void testRootWithFunctionDeclarationAndExtern() {
    String sourceExterns = "var foo = {}; foo.bar = function() {};";
    Node externs = Node.fromSource(sourceExterns);

    String sourceRoot = "function foo() {}";
    Node root = Node.fromSource(sourceRoot);

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals("foo", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    FunctionDeclaration fooFunctionDeclaration = methodCompilerPass.methodDefinitions.get("foo").asFunction();
    FunctionDeclaration barFunctionDeclaration = methodCompilerPass.methodDefinitions.get("bar").asFunction();
    assertNotNull(fooFunctionDeclaration);
    assertNotNull(barFunctionDeclaration);
  }

  @Test
  public void testRootWithGetProperty() {
    String sourceRoot = "var foo = { getBar: function() {} };";
    Node root = Node.fromSource(sourceRoot);

    methodCompilerPass.process(null, root);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("getBar", methodCompilerPass.externMethods.iterator().next());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    FunctionDeclaration getBarFunctionDeclaration = methodCompilerPass.methodDefinitions.get("getBar").asFunction();
    assertNotNull(getBarFunctionDeclaration);
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    String sourceRoot = "var foo = {}; foo.getBar = undefined;";
    Node root = Node.fromSource(sourceRoot);

    methodCompilerPass.process(null, root);
  }

  @Test
  public void testPrototype() {
    String sourceExterns = "var Foo = function() {};";
    Node externs = Node.fromSource(sourceExterns);

    String sourceRoot = "Foo.prototype.getBar = function() {};";
    Node root = Node.fromSource(sourceRoot);

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("getBar", methodCompilerPass.externMethods.iterator().next());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    FunctionDeclaration getBarFunctionDeclaration = methodCompilerPass.methodDefinitions.get("getBar").asFunction();
    assertNotNull(getBarFunctionDeclaration);
  }

  @Test
  public void testPrototypeWithExtern() {
    String sourceExterns = "var Foo = function() {}; Foo.prototype.getBar = function() {};";
    Node externs = Node.fromSource(sourceExterns);

    String sourceRoot = "Foo.prototype.getBar = function() {};";
    Node root = Node.fromSource(sourceRoot);

    methodCompilerPass.process(externs, root);

    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("getBar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals("getBar", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(2, methodCompilerPass.methodDefinitions.size());
    FunctionDeclaration getBarFunctionDeclaration = methodCompilerPass.methodDefinitions.get("getBar").asFunction();
    assertNotNull(getBarFunctionDeclaration);
  }
}