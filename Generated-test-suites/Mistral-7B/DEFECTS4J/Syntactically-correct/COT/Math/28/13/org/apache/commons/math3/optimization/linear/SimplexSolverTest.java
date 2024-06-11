import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SimplexSolver.
 */
public class SimplexSolverTest {

    /**
     * Test constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.getEpsilon(), 0);
        Assert.assertEquals(10, solver.getMaxUlps());
    }

    /**
     * Test constructor with specified settings.
     */
    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001, 5);
        Assert.assertEquals(0.001, solver.getEpsilon(), 0);
        Assert.assertEquals(5, solver.getMaxUlps());
    }

    /**
     * Test getPivotColumn method.
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     */
    @Test
    public void testGetPivotColumn() throws UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau(new SimpleFunction(), new SimpleConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6, 10);
        Integer expected = 0;
        Integer actual = SimplexSolver.getInstance().getPivotColumn(tableau);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test getPivotRow method.
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     */
    @Test
    public void testGetPivotRow() throws UnboundedSolutionException, MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(new SimpleFunction(), new SimpleConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6, 10);
        Integer expected = 0;
        Integer actual = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test doIteration method.
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     */
    @Test
    public void testDoIteration() throws UnboundedSolutionException, MaxCountExceededException {
        SimplexTableau tableau = new SimplexTableau(new SimpleFunction(), new SimpleConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        // The implementation does not provide enough information to test the internal state of the tableau after an iteration.
    }

    /**
     * Test solvePhase1 method.
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     * @throws NoFeasibleSolutionException if there is no feasible solution
     */
    @Test
    public void testSolvePhase1() throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new SimpleFunction(), new SimpleConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6, 10);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        // The implementation does not provide enough information to test the internal state of the tableau after Phase 1.
    }

    /**
     * Test doOptimize method.
     * @throws UnboundedSolutionException if the model is found not to have a bounded solution
     * @throws MaxCountExceededException if the maximal iteration count has been exceeded
     * @throws NoFeasibleSolutionException if there is no feasible solution
     */
    @Test
    public void testDoOptimize()
        throws UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(new SimpleFunction(), new SimpleConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6, 10);
        PointValuePair expected = new PointValuePair(2.0, new double[]{1.0, 1.0});
        PointValuePair actual = SimplexSolver.getInstance().doOptimize();
        Assert.assertArrayEquals(expected.getPoint(), actual.getPoint(), 0);
        Assert.assertEquals(expected.getValue(), actual.getValue(), 0);
    }
}