import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MethodCompilerPass.class})
public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    CompilerOptions options = new CompilerOptions();
    compiler = Mockito.mock(AbstractCompiler.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        if (signatureStore == null) {
          signatureStore = Mockito.mock(SignatureStore.class);
        }
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test
  public void testNoMethods() {
    Node root = Node.nullLiteral();
    Node externs = Node.nullLiteral();
    methodCompilerPass.process(externs, root);
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
  }

  @Test
  public void testGetExternMethods() {
    String externFileName = "externs.js";
    Node externs = Node.objectLiteral();
    externs = externs.addProperty("Foo", Node.name("foo"));
    externs = externs.addProperty("Foo.prototype", Node.objectLiteral());
    externs = externs.getLastChild().addProperty("bar", Node.name("bar"));

    methodCompilerPass.process(externs, Node.nullLiteral());

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertEquals(externs, methodCompilerPass.methodDefinitions.get("Foo.bar"));
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("Foo.bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());

    // Check that we can't find non-existent methods
    assertNull(methodCompilerPass.methodDefinitions.get("Foo.baz"));
    assertFalse(methodCompilerPass.externMethods.contains("Foo.baz"));

    // Check that we can find methods with signatures
    externs = Node.objectLiteral();
    externs = externs.addProperty("Foo", Node.name("foo"));
    externs = externs.addProperty("Foo.prototype", Node.objectLiteral());
    externs = externs.getLastChild().addProperty("bar", Node.name("bar").add("(int a)"));

    methodCompilerPass.process(externs, Node.nullLiteral());

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertEquals(externs, methodCompilerPass.methodDefinitions.get("Foo.bar"));
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("Foo.bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());

    // Check that we can find methods without signatures
    externs = Node.objectLiteral();
    externs = externs.addProperty("Foo", Node.name("foo"));
    externs = externs.addProperty("Foo.prototype", Node.objectLiteral());
    externs = externs.getLastChild().addProperty("bar", Node.name("bar"));

    methodCompilerPass.process(externs, Node.nullLiteral());

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertEquals(externs, methodCompilerPass.methodDefinitions.get("Foo.bar"));
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("Foo.bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals("Foo.bar", methodCompilerPass.externMethodsWithoutSignatures.iterator().next());

    // Check that we don't add the same method twice
    externs = Node.objectLiteral();
    externs = externs.addProperty("Foo", Node.name("foo"));
    externs = externs.addProperty("Foo.prototype", Node.objectLiteral());
    externs = externs.getLastChild().addProperty("bar", Node.name("bar"));

    methodCompilerPass.process(externs, Node.nullLiteral());

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo.bar", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertEquals(externs, methodCompilerPass.methodDefinitions.get("Foo.bar"));
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("Foo.bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());

    // Check that we find the method in the source code
    Node root = Node.script("function Foo() { }", externFileName);
    methodCompilerPass.process(Node.nullLiteral(), root);

    assertEquals(1, methodCompilerPass.methodDefinitions.size());
    assertEquals("Foo", methodCompilerPass.methodDefinitions.keys().iterator().next());
    assertEquals(root, methodCompilerPass.methodDefinitions.get("Foo"));
    assertEquals(1, methodCompilerPass.externMethods.size());
    assertEquals("Foo.bar", methodCompilerPass.externMethods.iterator().next());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
  }

  @Test
  public void testGatherSignatures() {
    String sourceFileName = "source.js";
    Node root = Node.script("function Foo() { \n" +
        "  this.bar = function(a) { \n" +
        "    return a; \n" +
        "  }; \n" +
        "  this.baz = function() {}; \n" +
        "}", sourceFileName);

    methodCompilerPass.process(Node.nullLiteral(), root);

    Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
    expectedMethodDefinitions.put("Foo.bar", root.getChild(0));
    expectedMethodDefinitions.put("Foo.baz", root.getChild(2));

    assertEquals(expectedMethodDefinitions.size(), methodCompilerPass.methodDefinitions.size());
    for (String name : methodCompilerPass.methodDefinitions.keySet()) {
      assertEquals(expectedMethodDefinitions.get(name).iterator().next(), methodCompilerPass.methodDefinitions.get(name));
    }

    // Check that we find prototype methods
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void processPrototypeParent(NodeTraversal t, Node n) {
        // Do nothing
      }
    };

    methodCompilerPass.process(Node.nullLiteral(), root);

    assertEquals(expectedMethodDefinitions.size(), methodCompilerPass.methodDefinitions.size());
    for (String name : methodCompilerPass.methodDefinitions.keySet()) {
      assertEquals(expectedMethodDefinitions.get(name).iterator().next(), methodCompilerPass.methodDefinitions.get(name));
    }

    // Check that we don't find non-method properties
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        throw new UnsupportedOperationException();
      }

      @Override
      Callback getActingCallback() {
        throw new UnsupportedOperationException();
      }

      @Override
      void addPossibleSignature(String name, Node node, NodeTraversal t) {
        if (name.equals("nonMethodProperty")) {
          // Fail
          throw new IllegalStateException();
        }
        super.addPossibleSignature(name, node, t);
      }
    };

    Node externs = Node.objectLiteral();
    externs = externs.addProperty("Foo", Node.name("Foo"));
    externs = externs.addProperty("Foo.prototype", Node.objectLiteral());
    externs = externs.getLastChild().addProperty("nonMethodProperty", Node.name("nonMethodProperty"));

    methodCompilerPass.process(externs, Node.nullLiteral());

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.nonMethodProperties.isEmpty());
    assertEquals(0, methodCompilerPass.methodDefinitions.size());
  }
}