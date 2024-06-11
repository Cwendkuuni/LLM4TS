import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodCompilerPassTest {

  private AbstractCompiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    signatureStore = mock(SignatureStore.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // Empty implementation, as the test will provide its own callback
        return new Callback() {
          public void call(Node node, Node parent) {}
        };
      }
    };
  }

  @Test
  public void testMethodDeclarationWithSignature() {
    Node root = createTestNode("function Foo() {}");

    methodCompilerPass.process(null, root);

    // Verify that the method was added to the methodDefinitions and signatureStore
    verifyMethodDefinitionAdded("Foo", root.getFirstChild());
    verifySignatureAdded("Foo", root.getFirstChild());
  }

  @Test
  public void testMethodDeclarationWithoutSignature() {
    Node root = createTestNode("function Foo() {}");

    methodCompilerPass.process(null, root);

    // This method should not be added to the methodDefinitions or signatureStore
    // since it was declared without a signature in the test node
    verifyNoMethodDefinitionAdded("Foo");
    verifyNoSignatureAdded("Foo");
  }

  @Test
  public void testExternMethodDeclarationWithSignature() {
    Node externs = createTestNode("function Foo(x) {}");

    methodCompilerPass.process(externs, null);

    // Verify that the method was added to the methodDefinitions and signatureStore
    verifyMethodDefinitionAdded("Foo", externs);
    verifySignatureAdded("Foo", externs);
  }

  @Test
  public void testExternMethodDeclarationWithoutSignature() {
    Node externs = createTestNode("function Foo() {}");

    methodCompilerPass.process(externs, null);

    // This method should be ignored, since it was declared without a signature in the externs
    // and was already marked as ignored in the constructor
    verifyNoMethodDefinitionAdded("Foo");
    verifyNoSignatureAdded("Foo");
  }

  @Test
  public void testNonMethodProperty() {
    Node root = createTestNode("var Foo = {}; Foo.bar = 42;");

    methodCompilerPass.process(null, root);

    // The 'bar' property should not be added to the methodDefinitions,
    // since it's not a method
    verifyNoMethodDefinitionAdded("bar");
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunction() {
    Node root = createTestNode("function Foo() { undefined.bar(); }");

    methodCompilerPass.process(null, root);
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedFunctionInExterns() {
    Node externs = createTestNode("function Foo() { undefined.bar(); }");

    methodCompilerPass.process(externs, null);
  }

  private Node createTestNode(String code) {
    // Create a mock NodeTraversal and use it to parse the test code
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(nodeTraversal);
    Node root = compiler.parseTree(code, null);
    return root;
  }

  private void verifyMethodDefinitionAdded(String methodName, Node node) {
    List<Node> methodDefs = Lists.newArrayList(methodCompilerPass.methodDefinitions.get(methodName));
    assertEquals("Expected one method definition", 1, methodDefs.size());
    assertEquals("Method definition does not match", node, methodDefs.get(0));
  }

  private void verifySignatureAdded(String methodName, Node node) {
    assertEquals("Expected one signature added", 1, signatureStore.getSignatures().size());
    Node signature = signatureStore.getSignatures().get(0);
    assertEquals("Method signature does not match", methodName, signature.getFirstChild().getString());
    assertEquals("Node that represents the function does not match", node, signature.getLastChild());
  }

  private void verifyNoMethodDefinitionAdded(String methodName) {
    assertEquals("Expected no method definition added", 0, methodCompilerPass.methodDefinitions.get(methodName).size());
  }

  private void verifyNoSignatureAdded(String methodName) {
    assertEquals("Expected no signatures added", 0, signatureStore.getSignatures().size());
  }
}