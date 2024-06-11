import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SignatureStore;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MethodCompilerPassTest {

  private Compiler compiler;
  private CompilerOptions options;
  private MethodCompilerPass methodCompilerPass;
  private SignatureStore signatureStore;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(Compiler.class);
    options = mock(CompilerOptions.class);
    signatureStore = mock(SignatureStore.class);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }

      @Override
      Callback getActingCallback() {
        // no-op
        return new Callback() {};
      }
    };
  }

  @Test
  public void testProcessingExternsFile() {
    Node externs = createNode("externs.js");

    methodCompilerPass.process(externs, null);

    verify(compiler).traverseRoots(argThat(isInstanceOf(Node.class)), argThat(isNull()),
        argThat(isInstanceOf(List.class)));
    verify(compiler).traverse(argThat(isInstanceOf(Node.class)), argThat(isInstanceOf(NodeTraversal.class)),
        argThat(isInstanceOf(AbstractPostOrderCallback.class)));

    // Expected number of method definitions based on the given externs file
    verify(signatureStore, times(2)).addSignature("method1", anyNode(), anyString());
    verify(signatureStore, times(1)).addSignature("method2", anyNode(), anyString());
    verify(signatureStore, times(1)).removeSignature("method3");

    verifyNoMoreInteractions(compiler, signatureStore);
  }

  @Test
  public void testProcessingSourceFile() {
    Node source = createNode("source.js");

    methodCompilerPass.process(null, source);

    verify(compiler).traverseRoots(argThat(isInstanceOf(Node.class)), argThat(isInstanceOf(List.class)),
        argThat(isInstanceOf(AbstractPostOrderCallback.class)));

    // Expected number of method definitions based on the given source file
    verify(signatureStore, times(3)).addSignature("method1", anyNode(), anyString());
    verify(signatureStore, times(1)).removeSignature("method2");

    verifyNoMoreInteractions(compiler, signatureStore);
  }

  @Test
  public void testProcessingPrototype() {
    Node prototype = createNode("prototype.js");

    methodCompilerPass.process(null, prototype);

    verify(compiler).traverseRoots(argThat(isInstanceOf(Node.class)), argThat(isInstanceOf(List.class)),
        argThat(isInstanceOf(AbstractPostOrderCallback.class)));

    // Expected number of method definitions based on the given prototype
    verify(signatureStore, times(1)).addSignature("method1", anyNode(), anyString());

    verifyNoMoreInteractions(compiler, signatureStore);
  }

  private Node createNode(String fileName) {
    List<Node> rootNodes = Lists.newArrayList();
    NodeTraversal traversal = new NodeTraversal(compiler, options, rootNodes);
    Node node = new Node(Token.PROGRAM, null);
    node.setSourceName(fileName);
    rootNodes.add(node);
    return node;
  }
}