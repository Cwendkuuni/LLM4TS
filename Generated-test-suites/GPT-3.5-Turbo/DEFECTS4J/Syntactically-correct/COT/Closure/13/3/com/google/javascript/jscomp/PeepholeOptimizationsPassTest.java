
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {

    private PeepholeOptimizationsPass peepholeOptimizationsPass;

    @Before
    public void setUp() {
        peepholeOptimizationsPass = new PeepholeOptimizationsPass(new AbstractCompiler(), new AbstractPeepholeOptimization());
    }

    @Test
    public void testProcess() {
        // TODO: write test case
    }

    @Test
    public void testGetCompiler() {
        assertEquals(new AbstractCompiler(), peepholeOptimizationsPass.getCompiler());
    }

    @Test
    public void testTraverse() {
        // TODO: write test case
    }

    @Test
    public void testShouldRetraverse() {
        // TODO: write test case
    }

    @Test
    public void testShouldVisit() {
        // TODO: write test case
    }

    @Test
    public void testExitNode() {
        // TODO: write test case
    }

    @Test
    public void testVisit() {
        // TODO: write test case
    }

    @Test
    public void testBeginTraversal() {
        // TODO: write test case
    }

    @Test
    public void testEndTraversal() {
        // TODO: write test case
    }

}
