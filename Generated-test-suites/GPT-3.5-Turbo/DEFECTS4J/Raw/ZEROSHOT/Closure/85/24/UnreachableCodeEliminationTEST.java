import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
    private UnreachableCodeElimination unreachableCodeElimination;

    @Before
    public void setUp() {
        unreachableCodeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
    }

    @Test
    public void testEnterScope() {
        // TODO: Create test case for enterScope() method
    }

    @Test
    public void testExitScope() {
        // TODO: Create test case for exitScope() method
    }

    @Test
    public void testProcess() {
        // TODO: Create test case for process() method
    }

    @Test
    public void testVisit() {
        // TODO: Create test case for visit() method
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        // TODO: Create test case for tryRemoveUnconditionalBranching() method
    }

    @Test
    public void testComputeFollowing() {
         // TODO: Create test case for computeFollowing() method
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        // TODO: Create test case for removeDeadExprStatementSafely() method
    }
}