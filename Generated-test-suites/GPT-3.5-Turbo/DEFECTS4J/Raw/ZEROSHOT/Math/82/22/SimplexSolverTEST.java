import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetPivotRow() {
        fail("Not yet implemented");
    }

    @Test
    public void testDoIteration() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsPhase1Solved() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsOptimal() {
        fail("Not yet implemented");
    }

    @Test
    public void testSolvePhase1() {
        fail("Not yet implemented");
    }

    @Test
    public void testDoOptimize() {
        fail("Not yet implemented");
    }

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);

        SimplexSolver solver2 = new SimplexSolver(0.1);
        assertEquals(0.1, solver2.epsilon, 0.0);
    }

    @Test
    public void testIsOptimalWithArtificialVariables() {
        fail("Not yet implemented");
    }

    @Test
    public void testNoFeasibleSolutionException() {
        try {
            throw new NoFeasibleSolutionException();
        } catch (NoFeasibleSolutionException e) {
            // do nothing
        }
    }

    @Test
    public void testUnboundedSolutionException() {
        try {
            throw new UnboundedSolutionException();
        } catch (UnboundedSolutionException e) {
            // do nothing
        }
    }

}