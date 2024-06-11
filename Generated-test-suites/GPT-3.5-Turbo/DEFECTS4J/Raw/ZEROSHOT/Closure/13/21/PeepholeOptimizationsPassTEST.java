
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass peepholeOptimizationsPass;
    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization[] peepholeOptimizations;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations);
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        CodeChangeHandler handler = mock(CodeChangeHandler.class);
        when(compiler.addChangeHandler(any(CodeChangeHandler.class))).thenReturn(true);
        when(compiler.removeChangeHandler(any(CodeChangeHandler.class))).thenReturn(true);

        peepholeOptimizationsPass.process(externs, root);

        verify(compiler, times(1)).addChangeHandler(handler);
        verify(compiler, times(1)).removeChangeHandler(handler);
    }

    @Test
    public void testTraverse() {
        Node node = mock(Node.class);
        peepholeOptimizationsPass.traverse(node);

        verify(peepholeOptimizationsPass, times(1)).shouldVisit(node);
        verify(peepholeOptimizationsPass, times(1)).traverse(node);
        verify(peepholeOptimizationsPass, times(1)).visit(node);
        verify(peepholeOptimizationsPass, times(1)).shouldRetraverse(node);
        verify(peepholeOptimizationsPass, times(1)).exitNode(node);

    }

    @Test
    public void testShouldRetraverse() {
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(mock(Node.class));
        when(node.isFunction()).thenReturn(true);
        when(node.isScript()).thenReturn(false);
        peepholeOptimizationsPass.shouldRetraverse(node);

        verify(peepholeOptimizationsPass, times(1)).shouldRetraverse(node);
        verify(peepholeOptimizationsPass, never()).shouldVisit(node);
        verify(peepholeOptimizationsPass, never()).traverse(node);
        verify(peepholeOptimizationsPass, never()).visit(node);
        verify(peepholeOptimizationsPass, never()).exitNode(node);
    }

    @Test
    public void testShouldVisit() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);
        when(node.isScript()).thenReturn(false);
        peepholeOptimizationsPass.shouldVisit(node);

        verify(peepholeOptimizationsPass, times(1)).shouldVisit(node);
        verify(peepholeOptimizationsPass, times(1)).traversalState.push();
        verify(peepholeOptimizationsPass, never()).shouldRetraverse(node);
        verify(peepholeOptimizationsPass, never()).traverse(node);
        verify(peepholeOptimizationsPass, never()).visit(node);
    }

    @Test
    public void testExitNode() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);
        when(node.isScript()).thenReturn(false);
        peepholeOptimizationsPass.exitNode(node);

        verify(peepholeOptimizationsPass, times(1)).exitNode(node);
        verify(peepholeOptimizationsPass, times(1)).traversalState.pop();
        verify(peepholeOptimizationsPass, never()).shouldVisit(node);
        verify(peepholeOptimizationsPass, never()).shouldRetraverse(node);
        verify(peepholeOptimizationsPass, never()).traverse(node);
        verify(peepholeOptimizationsPass, never()).visit(node);
    }

    @Test
    public void testVisit() {
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        Node n = mock(Node.class);
        Node currentVersionOfNode = mock(Node.class);
        when(optimization.optimizeSubtree(currentVersionOfNode)).thenReturn(mock(Node.class));
        when(optimization.optimizeSubtree(any(Node.class))).thenReturn(null);

        peepholeOptimizationsPass.visit(n);

        verify(optimization, times(2)).optimizeSubtree(any(Node.class));
    }

    @Test
    public void testBeginTraversal() {
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        peepholeOptimizationsPass.beginTraversal();

        verify(optimization, times(1)).beginTraversal(compiler);
    }

    @Test
    public void testEndTraversal() {
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        peepholeOptimizationsPass.endTraversal();

        verify(optimization, times(1)).endTraversal(compiler);
    }
}

