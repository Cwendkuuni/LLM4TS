import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.math.optimization.linear.*;
import org.apache.commons.math.optim.PointsToTest;
import org.apache.commons.math.optim.SimplePointsToTest;
import org.apache.commons.math.optim.linear.SimplexSolver;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

/**
 * Tests for the SimplexSolver class.
 */
public class SimplexSolverTest {

    private SimplexSolver solver;
    private RealMatrix f;
    private RealVector constraints;
    private LinearObjectiveFunction goalType;
    private boolean restrictToNonNegative;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        int numVars = 4;
        int numConstr = 3;
        double[][] coefficients = {{1.0, -2.0, 0.0, 0.0, 1.0, -1.0},
                                    {-2.0, 1.0, 1.0, 0.0, 0.0, -1.0},
                                    {0.0, 0.0, 0.0, -1.0, 1.0, 1.0}};
        RealMatrix constraintMatrix = new Array2DRowRealMatrix(coefficients);
        constraints = constraintMatrix.getColumnVector(0);
        f = new Array2DRowRealMatrix(new double[][]{{-1.0, 5.0, 3.0, 2.0},
                                                   {-2.0, 2.0, 1.0, 1.0},
                                                   {-1.5, 4.5, 2.5, 3.5}});
        goalType = new LinearObjectiveFunction(new SimplePointsToTest(f.getColumnVector(0)));
        restrictToNonNegative = false;
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver);
    }

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    /**
     * Tests the constructor with a specified epsilon.
     */
    @Test
    public void testConstructorWithEpsilon() {
        SimplexSolver solver = new SimplexSolver(0.0001);
        assertEquals(0.0001, solver.epsilon, 0.0);
    }

    /**
     * Tests the solve method with the default epsilon.
     * @throws OptimizationException
     */
    @Test
    public void testSolveWithDefaultEpsilon() throws OptimizationException {
        PointValuePair solution = solver.doOptimize();
        RealVector x = solution.getPoint();
        RealVector fx = solver.evaluateObjectiveFunction(x);
        double[] expectedX = {-0.357142857142857, -0.428571428571429, 1.942857142857143, 1.142857142857143};
        double[] expectedFx = {-2.142857142857143, -1.571428571428571, 5.714285714285714, 4.285714285714286};
        assertArrayEquals(expectedX, x.toArray(), 0.001);
        assertArrayEquals(expectedFx, fx.toArray(), 0.001);
    }

    /**
     * Tests the solve method with a specified epsilon.
     * @throws OptimizationException
     */
    @Test
    public void testSolveWithSpecifiedEpsilon() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver(0.001);
        PointValuePair solution = solver.doOptimize();
        RealVector x = solution.getPoint();
        RealVector fx = solver.evaluateObjectiveFunction(x);
        double[] expectedX = {-0.357142857142857, -0.428571428571429, 1.942857142857143, 1.142857142857143};
        double[] expectedFx = {-2.142857142857143, -1.571428571428571, 5.714285714285714, 4.285714285714286};
        assertArrayEquals(expectedX, x.toArray(), 0.001);
        assertArrayEquals(expectedFx, fx.toArray(), 0.001);
    }

    /**
     * Test the case when the problem has no feasible solution.
     * @throws OptimizationException
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimpleLinearProgram lp = new SimpleLinearProgram(new RealMatrix(new double[0][0]),
                                                         new RealVector(new double[0]),
                                                         LinearObjectiveFunction.MINIMIZE,
                                                         10,
                                                         new LinearConstraint[0],
                                                         null,
                                                         Double.POSITIVE_INFINITY,
                                                         Double.POSITIVE_INFINITY);
        solver.doOptimize();
    }

    /**
     * Tests the case when the problem has an unbounded solution.
     * @throws OptimizationException
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, solver.epsilon);
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
    }

    /**
     * Tests the case when the problem has a bounded solution.
     * @throws OptimizationException
     */
    @Test
    public void testBoundedSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        RealVector x = solution.getPoint();
        RealVector fx = solver.evaluateObjectiveFunction(x);
        double[] expectedX = {-0.357142857142857, -0.428571428571429, 1.942857142857143, 1.142857142857143};
        double[] expectedFx = {-2.142857142857143, -1.571428571428571, 5.714285714285714, 4.285714285714286};
        assertArrayEquals(expectedX, x.toArray(), 0.001);
        assertArrayEquals(expectedFx, fx.toArray(), 0.001);
    }

    /**
     * Tests the case when the problem has a solution with negative values.
     * @throws OptimizationException
     */
    @Test
    public void testNegativeSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        restrictToNonNegative = true;
        PointValuePair solution = solver.doOptimize();
        RealVector x = solution.getPoint();
        RealVector fx = solver.evaluateObjectiveFunction(x);
        double[] expectedX = {0.0, 0.0, 2.0, 1.0};
        double[] expectedFx = {0.0, 0.0, 5.0, 4.0};
        assertArrayEquals(expectedX, x.toArray(), 0.001);
        assertArrayEquals(expectedFx, fx.toArray(), 0.001);
    }

    /**
     * Tests the case when the problem has a solution with all zeros.
     * @throws OptimizationException
     */
    @Test
    public void testZeroSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, solver.epsilon);
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        PointValuePair solution = solver.doOptimize();
        RealVector x = solution.getPoint();
        RealVector fx = solver.evaluateObjectiveFunction(x);
        double[] expectedX = {0.0, 0.0, 0.0, 0.0};
        double[] expectedFx = {0.0, 0.0, 0.0, 0.0};
        assertArrayEquals(expectedX, x.toArray(), 0.001);
        assertArrayEquals(expectedFx, fx.toArray(), 0.001);
    }

    /**
     * Tests the case when the problem has a solution with all zeros and
     * a specified epsilon.
     * @throws OptimizationException
     */
    @Test
    public void testZeroSolutionWithEpsilon() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver(0.0001);
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, solver.epsilon);
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        PointValuePair solution = solver.doOptimize();
        RealVector x = solution.getPoint();
        RealVector fx = solver.evaluateObjectiveFunction(x);
        double[] expectedX = {0.0, 0.0, 0.0, 0.0};
        double[] expectedFx = {0.0, 0.0, 0.0, 0.0};
        assertArrayEquals(expectedX, x.toArray(), 0.0001);
        assertArrayEquals(expectedFx, fx.toArray(), 0.0001);
    }

    /**
     * Tests the case when the problem has a solution with all zeros and
     * a specified maximum number of iterations.
     * @throws OptimizationException
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testZeroSolutionWithMaxIterations() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, solver.epsilon);
        solver.solvePhase1(tableau);
        solver.setMaxIterations(1);
        solver.doIteration(tableau);
        solver.doOptimize();
    }
}