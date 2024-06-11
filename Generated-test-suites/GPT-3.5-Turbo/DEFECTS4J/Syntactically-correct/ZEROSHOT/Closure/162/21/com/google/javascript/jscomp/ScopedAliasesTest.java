
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ScopedAliasesTest {

    private AbstractCompiler compiler;
    private PreprocessorSymbolTable preprocessorSymbolTable;
    private AliasTransformationHandler transformationHandler;
    private ScopedAliases scopedAliases;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
        transformationHandler = mock(AliasTransformationHandler.class);
        scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        scopedAliases.process(externs, root);

        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testHotSwapScript() {
        Node root = mock(Node.class);
        Node originalRoot = mock(Node.class);

        scopedAliases.hotSwapScript(root, originalRoot);

        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testAliasedNodeApplyAlias() {
        Node aliasReference = mock(Node.class);
        Node aliasDefinition = mock(Node.class);

        ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
        aliasedNode.applyAlias();

        verify(aliasReference, times(1)).getParent();
        verify(aliasReference, times(1)).cloneTree();
        verify(aliasReference.getParent(), times(1)).replaceChild(aliasReference, aliasDefinition.cloneTree());
    }

    @Test
    public void testAliasedTypeNodeApplyAlias() {
        Node aliasReference = mock(Node.class);
        String correctedType = "correctedType";

        ScopedAliases.AliasedTypeNode aliasedTypeNode = new ScopedAliases.AliasedTypeNode(aliasReference, correctedType);
        aliasedTypeNode.applyAlias();

        verify(aliasReference, times(1)).setString(correctedType);
    }

    @Test
    public void testTraversalEnterScope() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);

        when(n.getParent()).thenReturn(n);
        when(n.getFirstChild()).thenReturn(n);
        when(n.getQualifiedName()).thenReturn("goog.scope");

        scopedAliases.new Traversal().enterScope(t);

        verify(transformationHandler, times(1)).logAliasTransformation(anyString(), anyObject());
    }

    @Test
    public void testTraversalExitScope() {
        NodeTraversal t = mock(NodeTraversal.class);

        scopedAliases.new Traversal().exitScope(t);

        verify(compiler, never()).report(anyObject());
    }

    @Test
    public void testTraversalShouldTraverse() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.CALL);
        when(parent.getFirstChild()).thenReturn(n);
        when(n.getParent()).thenReturn(parent);
        when(n.getQualifiedName()).thenReturn("goog.scope");

        boolean result = scopedAliases.new Traversal().shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testTraversalShouldTraverse2() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        when(n.getType()).thenReturn(Token.FUNCTION);
        when(parent.getType()).thenReturn(Token.CALL);
        when(parent.getFirstChild()).thenReturn(n);
        when(n.getParent()).thenReturn(parent);
        when(n.getQualifiedName()).thenReturn("not.goog.scope");

        boolean result = scopedAliases.new Traversal().shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testTraversalVisit() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);

        scopedAliases.new Traversal().visit(t, n, parent);

        verify(compiler, never()).report(anyObject());
    }
}
