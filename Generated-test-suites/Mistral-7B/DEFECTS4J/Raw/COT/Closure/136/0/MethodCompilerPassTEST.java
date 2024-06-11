import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {

  private Compiler compiler;
  private MethodCompilerPass methodCompilerPass;
  private Node node;
  private NodeTraversal nodeTraversal;
  private Multimap<String, Node> methodDefinitions;

  @Before
  public void setup() {
    compiler = mock(Compiler.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return new SignatureMock();
      }

      @Override
      Callback getActingCallback() {
        return new TestCallback();
      }
    };

    node = new Node();
    nodeTraversal = new NodeTraversal(compiler, new CompilerOptions(), node);
    methodDefinitions = HashMultimap.create();
  }

  @Test
  public void testConstructor() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPass(mock(Compiler.class));
    assert null != methodCompilerPass;
  }

  @Test
  public void testProcessExterns() {
    // Given
    String externFileName = "externs.js";
    String rootFileName = "main.js";
    Node externs = new Node(Token.PROGRAM, null);
    when(compiler.getSourceFile(externFileName)).thenReturn(externs);
    when(compiler.getSourceFile(rootFileName)).thenReturn(node);

    // When
    methodCompilerPass.process(externs, node);

    // Then
    assertThat(methodCompilerPass.externMethods).containsExactly("foo", "bar");
    assertThat(methodCompilerPass.externMethodsWithoutSignatures).containsExactly("baz");
    assertThat(methodCompilerPass.methodDefinitions).isEmpty();
  }

  @Test
  public void testAddPossibleSignature() {
    // Given
    String name = "foo";
    Node functionNode = new Node(Token.FUNCTION, null);

    // When
    methodCompilerPass.addPossibleSignature(name, functionNode, nodeTraversal);

    // Then
    assertThat(methodCompilerPass.methodDefinitions).containsEntry(name, functionNode);
  }

  @Test
  public void testAddSignature() {
    // Given
    String name = "foo";
    Node functionNode = new Node(Token.FUNCTION, null);
    String functionSourceName = "main.js";

    // When
    methodCompilerPass.addSignature(name, functionNode, functionSourceName);

    // Then
    assertThat(methodCompilerPass.methodDefinitions).containsEntry(name, functionNode);
  }

  @Test
  public void testGetActingCallback() {
    // Given
    Callback testCallback = methodCompilerPass.getActingCallback();

    // Then
    assert null != testCallback;
  }

  @Test(expected = IllegalStateException.class)
  public void testUnreachableCodePath() {
    // Given
    Node unreachableNode = new Node(Token.ASSIGN, null);

    // When
    methodCompilerPass.addPossibleSignature("foo", unreachableNode, nodeTraversal);

    // Then
    // Expected exception
  }

  private static class SignatureMock implements MethodCompilerPass.SignatureStore {

    @Override
    public void reset() {
      // Empty
    }

    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      // Empty
    }

    @Override
    public void removeSignature(String functionName) {
      // Empty
    }
  }

  private static class TestCallback implements MethodCompilerPass.Callback {

    @Override
    public void processMethodSignatures(
        Set<String> externMethods, Set<String> externMethodsWithoutSignatures,
        Multimap<String, Node> methodDefinitions) {
      // Empty
    }
  }
}