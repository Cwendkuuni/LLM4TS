import org.junit.*;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    private UnreachableCodeElimination uce;

    @Before
    public void setUp() {
        uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
    }

    @After
    public void tearDown() {
        uce = null;
    }

    @Test
    public void testProcess() {
        // Write your test code here
    }

    @Test
    public void testEliminationPass() {
        // Write your test code here
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        // Write your test code here
    }

    @Test
    public void testComputeFollowing() {
        // Write your test code here
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        // Write your test code here
    }

    @Test
    public void testRemoveNode() {
        // Write your test code here
    }

}