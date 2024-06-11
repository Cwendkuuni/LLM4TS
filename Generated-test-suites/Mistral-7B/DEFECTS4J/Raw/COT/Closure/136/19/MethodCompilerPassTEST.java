import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.jscomp.traverse.AbstractCompiler;
import com.google.javascript.jscomp.traverse.AbstractNodeTraversal;
import com.google.javascript.jscomp.traverse.Callback;
import com.google.javascript.jscomp.traverse.NodeTraversal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private SignatureStore signatureStore;

  @InjectMocks
  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setup() {
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // no-op
        return null;
      }
    };
  }

  @Test
  public void testProcess_NullExternsAndRoot() {
    methodCompilerPass.process(null, null);
    // No action needed as the method is declared to be a no-op when
    // externs and root are null.
  }

  @Test
  public void testProcess_NullExterns() {
    Node root = createNode("function foo() {}");

    methodCompilerPass.process(null, root);

    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertNull(methodCompilerPass.methodDefinitions);
  }

  @Test
  public void testProcess_NullRoot() {
    Node externs = createNode("{}");

    methodCompilerPass.process(externs, null);

    assertEquals(0, methodCompilerPass.externMethods.size());
    assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertNull(methodCompilerPass.methodDefinitions);
  }

  @Test
  public void testProcess_ExternsWithoutSignatures() {
    Node root = createNode("function foo() {}");
    Node externs = createNode("{foo: function()}");

    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.contains("foo"));
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("foo"));
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertNull(methodCompilerPass.methodDefinitions);
  }

  @Test
  public void testProcess_ExternsWithSignatures() {
    Node root = createNode("function foo(x) {}");
    Node externs = createNode("{foo: function(x)}");

    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.contains("foo"));
    assertFalse(methodCompilerPass.externMethodsWithoutSignatures.contains("foo"));
    assertEquals(0, methodCompilerPass.nonMethodProperties.size());
    assertNotNull(methodCompilerPass.methodDefinitions);
    assertEquals(1, methodCompilerPass.methodDefinitions.size());
  }

  @Test
  public void testProcess_RootWithMethods() {
    Node externs = createNode("{}");

    Node root = createNode("function foo(x) {return x * 2;}");
    Node fooFunction = createNode("function foo(x) {return x + 1;}");

    methodCompilerPass.process(externs, root);

    assertTrue(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertEquals(1, methodCompilerPass.nonMethodProperties.size());
    assertEquals("prototype", methodCompilerPass.nonMethodProperties.iterator().next());
    assertNotNull(methodCompilerPass.methodDefinitions);
    assertEquals(1, methodCompilerPass.methodDefinitions.size());

    Map<String, Node> methodDefinitions = methodCompilerPass.methodDefinitions.asMap();
    assertEquals("foo", methodDefinitions.keySet().iterator().next());
    assertEquals(fooFunction, methodDefinitions.get("foo"));
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_UndefinedFunction() {
    Node externs = createNode("{}");
    Node root = createNode("x.foo = 42;");

    methodCompilerPass.process(externs, root);
  }

  @Test
  public void testAddPossibleSignature_SignatureAdded() {
    NodeTraversal traversal = createMock(NodeTraversal.class);

    methodCompilerPass.addPossibleSignature("foo", createNode("function foo() {}"), traversal);

    assertTrue(methodCompilerPass.getSignatureStore().containsKey("foo"));
  }

  @Test
  public void testAddPossibleSignature_NoSignatureAdded() {
    NodeTraversal traversal = createMock(NodeTraversal.class);

    methodCompilerPass.addPossibleSignature("foo", createNode("{}"), traversal);

    assertFalse(methodCompilerPass.getSignatureStore().containsKey("foo"));
  }

  @Test
  public void testAddPossibleSignature_NonMethodProperty() {
    Node root = createNode("x.nonMethodProperty = 42;");

    methodCompilerPass.addPossibleSignature("nonMethodProperty", root, createMock(NodeTraversal.class));

    assertTrue(methodCompilerPass.nonMethodProperties.contains("nonMethodProperty"));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddPossibleSignature_FunctionWithoutName() {
    Node root = createNode("function() {}");

    methodCompilerPass.addPossibleSignature("", root, createMock(NodeTraversal.class));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddPossibleSignature_FunctionWithMultipleNames() {
    Node root = createNode("function fooBar() {}");

    methodCompilerPass.addPossibleSignature("foo", root, createMock(NodeTraversal.class));
    methodCompilerPass.addPossibleSignature("bar", root, createMock(NodeTraversal.class));
  }

  private Node createNode(String nodeString) {
    return new Node() {
      @Override
      public String toString() {
        return nodeString;
      }
    };
  }

  private NodeTraversal createMock(Class<?> clazz) {
    return Mockito.mock(clazz);
  }
}