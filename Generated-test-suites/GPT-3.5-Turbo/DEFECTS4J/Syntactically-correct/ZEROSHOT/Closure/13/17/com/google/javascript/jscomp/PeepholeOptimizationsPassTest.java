import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class PeepholeOptimizationsPassTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private AbstractPeepholeOptimization optimization1;

    @Mock
    private AbstractPeepholeOptimization optimization2;

    private PeepholeOptimizationsPass peepholeOptimizationsPass;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();

        PeepholeOptimizationsPass.PeepholeChangeHandler handler = peepholeOptimizationsPass.new PeepholeChangeHandler();
        compiler.addChangeHandler(handler);
        peepholeOptimizationsPass.process(externs, root);
        compiler.removeChangeHandler(handler);

        // Verify that addChangeHandler and removeChangeHandler methods are called
        verify(compiler, times(1)).addChangeHandler(handler);
        verify(compiler, times(1)).removeChangeHandler(handler);
    }

    @Test
    public void testTraverse() {
        Node node = new Node();
        peepholeOptimizationsPass.traverse(node);

        // Verify that shouldVisit, visit, shouldRetraverse, exitNode methods are called
        verify(peepholeOptimizationsPass, times(1)).shouldVisit(node);
        verify(peepholeOptimizationsPass, times(1)).visit(node);
        verify(peepholeOptimizationsPass, times(1)).shouldRetraverse(node);
        verify(peepholeOptimizationsPass, times(1)).exitNode(node);
    }

    @Test
    public void testShouldRetraverse() {
        Node node = new Node();
        peepholeOptimizationsPass.shouldRetraverse(node);

        // Verify that shouldRetraverse method returns false
        assertFalse(peepholeOptimizationsPass.shouldRetraverse(node));
    }

    @Test
    public void testShouldVisit() {
        Node node = new Node();
        peepholeOptimizationsPass.shouldVisit(node);

        // Verify that shouldVisit method returns true
        assertTrue(peepholeOptimizationsPass.shouldVisit(node));
    }

    @Test
    public void testExitNode() {
        Node node = new Node();
        peepholeOptimizationsPass.exitNode(node);

        // Verify that exitNode method is called
        verify(peepholeOptimizationsPass, times(1)).exitNode(node);
    }

    @Test
    public void testVisit() {
        Node node = new Node();
        peepholeOptimizationsPass.visit(node);

        // Verify that optimizeSubtree method of each peephole optimization is called
        verify(optimization1, times(1)).optimizeSubtree(node);
        verify(optimization2, times(1)).optimizeSubtree(node);
    }

    @Test
    public void testBeginTraversal() {
        peepholeOptimizationsPass.beginTraversal();

        // Verify that beginTraversal method of each peephole optimization is called
        verify(optimization1, times(1)).beginTraversal(compiler);
        verify(optimization2, times(1)).beginTraversal(compiler);
    }

    @Test
    public void testEndTraversal() {
        peepholeOptimizationsPass.endTraversal();

        // Verify that endTraversal method of each peephole optimization is called
        verify(optimization1, times(1)).endTraversal(compiler);
        verify(optimization2, times(1)).endTraversal(compiler);
    }
}