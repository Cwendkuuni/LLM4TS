import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.MethodCompilerPass.SignatureStore;
import com.google.javascript.jscomp.MethodCompilerPassStub;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class MethodCompilerPassTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        Node externs = new Node(Token.EXTERNALLS);
        Node root = new Node(Token.ROOT);
        MethodCompilerPassStub methodCompilerPass = new MethodCompilerPassStub(compiler);

        methodCompilerPass.process(externs, root);

        Set<String> expectedExternMethods = new HashSet<>();
        Set<String> expectedExternMethodsWithoutSignatures = new HashSet<>();
        Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
        SignatureStore signatureStore = methodCompilerPass.getSignatureStore();

        assertEquals(expectedExternMethods, methodCompilerPass.externMethods);
        assertEquals(expectedExternMethodsWithoutSignatures, methodCompilerPass.externMethodsWithoutSignatures);
        assertEquals(expectedMethodDefinitions, methodCompilerPass.methodDefinitions);
        verify(signatureStore).reset();
        verify(compiler, times(3)).isIdeMode();
        verifyNoMoreInteractions(compiler);
        verifyNoMoreInteractions(signatureStore);
    }

    @Test
    public void testGetActingCallback() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        MethodCompilerPassStub methodCompilerPass = new MethodCompilerPassStub(compiler);
        Callback expectedCallback = mock(Callback.class);

        Callback actualCallback = methodCompilerPass.getActingCallback();

        assertEquals(expectedCallback, actualCallback);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void testGetSignatureStore() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        MethodCompilerPassStub methodCompilerPass = new MethodCompilerPassStub(compiler);
        SignatureStore expectedSignatureStore = mock(SignatureStore.class);

        SignatureStore actualSignatureStore = methodCompilerPass.getSignatureStore();

        assertEquals(expectedSignatureStore, actualSignatureStore);
        verifyZeroInteractions(compiler);
    }

    @Test
    public void testAddPossibleSignature() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.STRING);
        n.setString("test");
        Node parent = new Node(Token.FUNCTION);
        MethodCompilerPassStub methodCompilerPass = new MethodCompilerPassStub(compiler);

        methodCompilerPass.addPossibleSignature("test", n, t);

        assertEquals(1, methodCompilerPass.nonMethodProperties.size());
        assertTrue(methodCompilerPass.nonMethodProperties.contains("test"));
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
        verifyZeroInteractions(compiler);
        verifyZeroInteractions(t);
    }

    @Test
    public void testAddSignature() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        Node function = new Node(Token.FUNCTION);
        SignatureStore signatureStore = mock(SignatureStore.class);
        String functionName = "test";
        String fnSourceName = "test.js";
        MethodCompilerPassStub methodCompilerPass = new MethodCompilerPassStub(compiler);
        methodCompilerPass.setSignatureStore(signatureStore);

        methodCompilerPass.addSignature(functionName, function, fnSourceName);

        verify(signatureStore).addSignature(functionName, function, fnSourceName);
        assertEquals(1, methodCompilerPass.methodDefinitions.size());
        assertTrue(methodCompilerPass.methodDefinitions.containsEntry(functionName, function));
        verifyZeroInteractions(compiler);
        verifyNoMoreInteractions(signatureStore);
    }

    @Test
    public void testProcessPrototypeParent() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.STRING);
        n.setString("prototype");
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.NAME, "Foo"));
        parent.addChildToBack(n);
        MethodCompilerPassStub methodCompilerPass = new MethodCompilerPassStub(compiler);

        methodCompilerPass.processPrototypeParent(t, parent);

        assertEquals(0, methodCompilerPass.nonMethodProperties.size());
        assertEquals(0, methodCompilerPass.methodDefinitions.size());
        verifyZeroInteractions(compiler);
        verifyZeroInteractions(t);
    }
}