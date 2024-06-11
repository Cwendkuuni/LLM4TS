import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.NodeTraversal.*;

import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {

  private AbstractCompiler mockCompiler;
  private MethodCompilerPass.SignatureStore mockSignatureStore;
  private AbstractPostOrderCallback mockPostOrderCallback;

  @Before
  public void setUp() {
    mockCompiler = mock(AbstractCompiler.class);
    mockSignatureStore = mock(MethodCompilerPass.SignatureStore.class);
    mockPostOrderCallback = mock(AbstractPostOrderCallback.class);
  }

  @Test
  public void testProcess_WithValidExternsAndRoot_CallsMethodsInCorrectOrder() {
    Node externs = new Node(Token.EXTERN_FILE);
    Node root = new Node(Token.ROOT);

    MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(mockCompiler);

    methodCompilerPass.process(externs, root);

    InOrder inOrder = inOrder(mockPostOrderCallback);
    inOrder.verify(mockPostOrderCallback).visit(any(NodeTraversal.class), eq(root), eq(null));
    inOrder.verify(mockPostOrderCallback).visit(any(NodeTraversal.class), eq(externs), eq(null));
  }

  @Test
  public void testGetActingCallback_ReturnsMockPostOrderCallback() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(mockCompiler);

    Callback callback = methodCompilerPass.getActingCallback();

    assertEquals(mockPostOrderCallback, callback);
  }

  @Test
  public void testGetSignatureStore_ReturnsMockSignatureStore() {
    MethodCompilerPass methodCompilerPass = new MethodCompilerPassImpl(mockCompiler);

    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();

    assertEquals(mockSignatureStore, signatureStore);
  }

  private class MethodCompilerPassImpl extends MethodCompilerPass {

    MethodCompilerPassImpl(AbstractCompiler compiler) {
      super(compiler);
    }

    @Override
    Callback getActingCallback() {
      return mockPostOrderCallback;
    }

    @Override
    SignatureStore getSignatureStore() {
      return mockSignatureStore;
    }
  }
}