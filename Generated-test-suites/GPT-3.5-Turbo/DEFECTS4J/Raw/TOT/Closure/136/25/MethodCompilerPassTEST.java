import com.google.javascript.jscomp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MethodCompilerPassTest {
    private AbstractCompiler compiler;
    private MethodCompilerPass.SignatureStore signatureStore;
    private MethodCompilerPass methodCompilerPass;

    @BeforeEach
    void setUp() {
        compiler = mock(AbstractCompiler.class);
        signatureStore = mock(MethodCompilerPass.SignatureStore.class);
        methodCompilerPass = new MethodCompilerPassImpl(compiler);
    }

    @Test
    void process_ShouldResetSignatureStore() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        methodCompilerPass.process(externs, root);

        verify(signatureStore).reset();
    }

    @Test
    void process_ShouldClearSetsAndMultimap() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        methodCompilerPass.process(externs, root);

        assertEquals(0, methodCompilerPass.externMethods.size());
        assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
        assertEquals(0, methodCompilerPass.nonMethodProperties.size());
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
    }

    @Test
    void process_WithExterns_ShouldTraverseExternsAndCollectMethods() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        methodCompilerPass.process(externs, root);

        // Verify traversal of externs
        verify(compiler).traverse(eq(externs), any(MethodCompilerPass.GetExternMethods.class));
        // Verify traversal of externs and root
        verify(compiler).traverseRoots(eq(externsAndJs), any(MethodCompilerPass.GetActingCallback.class));
    }

    @Test
    void process_WithoutExterns_ShouldOnlyTraverseRootAndCollectMethods() {
        Node root = mock(Node.class);

        methodCompilerPass.process(null, root);

        // Verify traversal of root
        verify(compiler).traverseRoots(eq(externsAndJs), any(MethodCompilerPass.GetActingCallback.class));
    }

    @Test
    void getActingCallback_ShouldReturnNonAbstractCallbackImplementation() {
        NodeTraversal.Callback callback = methodCompilerPass.getActingCallback();

        assertFalse(callback instanceof MethodCompilerPass.AbstractPostOrderCallback);
    }

    @Test
    void getSignatureStore_ShouldReturnSignatureStoreImplementation() {
        MethodCompilerPass.SignatureStore store = methodCompilerPass.getSignatureStore();

        assertTrue(store instanceof MethodCompilerPassImpl);
    }
}