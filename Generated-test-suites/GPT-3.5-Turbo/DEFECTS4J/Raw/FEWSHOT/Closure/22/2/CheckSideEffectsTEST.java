import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {
    private AbstractCompiler compiler;
    private NodeTraversal traversal;
    private Node node;
    private Node parent;

    @Before
    public void setup() {
        compiler = mock(AbstractCompiler.class);
        traversal = mock(NodeTraversal.class);
        node = mock(Node.class);
        parent = mock(Node.class);
    }

    @Test
    public void testVisitWithEmptyNode() {
        // Setup
        when(node.isEmpty()).thenReturn(true);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).getChildCount();
        verifyZeroInteractions(compiler);
    }

    @Test
    public void testVisitWithCommaNode() {
        // Setup
        when(node.getType()).thenReturn(Token.COMMA);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).getChildCount();
        verifyZeroInteractions(compiler);
    }

    @Test
    public void testVisitWithQualifiedNameNodeAndJSDocInfo() {
        // Setup
        when(node.isQualifiedName()).thenReturn(true);
        when(node.getJSDocInfo()).thenReturn(mock(JSDocInfo.class));

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).getParent();
        verify(node, never()).getJSDocInfo();
        verifyZeroInteractions(compiler);
    }

    @Test
    public void testVisitWithQualifiedNameNodeAndNoJSDocInfo() {
        // Setup
        when(node.isQualifiedName()).thenReturn(true);
        when(node.getJSDocInfo()).thenReturn(null);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node).getParent();
        verify(node, never()).getJSDocInfo();
        verify(compiler).report(any(DiagnosticType.class));
    }

    @Test
    public void testVisitWithExprResultNode() {
        // Setup
        when(node.isExprResult()).thenReturn(true);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).getParent();
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void testVisitWithSimpleOpAndNoSideEffects() {
        // Setup
        when(node.getType()).thenReturn(Token.ADD);
        when(NodeUtil.isSimpleOperatorType(node.getType())).thenReturn(true);
        when(NodeUtil.mayHaveSideEffects(node, compiler)).thenReturn(false);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).isQualifiedName();
        verify(compiler).report(any(DiagnosticType.class));
    }

    @Test
    public void testVisitWithIdentifierOpAndNoSideEffects() {
        // Setup
        when(node.getType()).thenReturn(Token.NAME);
        when(NodeUtil.isSimpleOperatorType(node.getType())).thenReturn(false);
        when(NodeUtil.mayHaveSideEffects(node, compiler)).thenReturn(false);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node).isQualifiedName();
        verify(compiler, never()).report(any(DiagnosticType.class));
        verify(node).getParent(); // Check if statement
        verify(compiler).report(any(DiagnosticType.class));
    }

    @Test
    public void testVisitWithIdentifierOpAndSideEffects() {
        // Setup
        when(node.getType()).thenReturn(Token.NAME);
        when(NodeUtil.isSimpleOperatorType(node.getType())).thenReturn(false);
        when(NodeUtil.mayHaveSideEffects(node, compiler)).thenReturn(true);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node).isQualifiedName();
        verify(compiler, never()).report(any(DiagnosticType.class));
        verify(node).getParent(); // Check if statement
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void testVisitWithParentIsNull() {
        // Setup
        parent = null;

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).getType();
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void testVisitWithParentIsNotExprResultOrBlock() {
        // Setup
        when(parent.getType()).thenReturn(Token.NAME);

        // Run
        CheckSideEffects.check(compiler, traversal, node, parent);

        // Verify
        verify(node, never()).getType();
        verify(compiler, never()).report(any(DiagnosticType.class));
    }

    @Test
    public void testHotSwapScript() {
        // Setup
        Node scriptRoot = mock(Node.class);
        Node originalRoot = mock(Node.class);

        // Run
        CheckSideEffects.hotSwapScript(compiler, scriptRoot, originalRoot);

        // Verify
        verify(scriptRoot).addChildrenToBack(any(Node.class));
    }

    @Test
    public void testStripProtection() {
        // Setup
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        // Run
        CheckSideEffects.StripProtection.process(externs, root);

        // Verify
        verify(root).addChildToBack(any(Node.class));
    }
}