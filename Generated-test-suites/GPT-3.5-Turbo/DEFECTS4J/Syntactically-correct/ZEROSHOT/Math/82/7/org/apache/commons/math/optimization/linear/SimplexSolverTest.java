import org.junit.Test;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.OptimizationException;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.util.MathUtils;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private static final double DEFAULT_EPSILON = 1.0e-6;
    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver(DEFAULT_EPSILON);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createSimplexTableau();
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertEquals(new Integer(1), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createSimplexTableau();
        Integer pivotRow = simplexSolver.getPivotRow(1, tableau);
        assertEquals(new Integer(1), pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = createSimplexTableau();
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = createSimplexTableau();
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(2, 1, -3.0);
        simplexSolver.doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(1, 1), DEFAULT_EPSILON);
        assertEquals(2.0, tableau.getEntry(2, 1), DEFAULT_EPSILON);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = createSimplexTableau();
        assertTrue(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = createSimplexTableau();
        assertTrue(simplexSolver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = createSimplexTableau();
        tableau.setEntry(0, 1, -1.0);
        tableau.setEntry(1, 1, -2.0);
        tableau.setEntry(2, 1, -3.0);
        simplexSolver.solvePhase1(tableau);
        assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), DEFAULT_EPSILON);
        assertTrue(simplexSolver.isOptimal(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = createSimplexTableau();
        tableau.setEntry(0, 1, -1.0);
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        double[] f = {1, 2};
        double[][] constraints = {
            {-1, -1},
            {1, 0},
            {0, 1}
        };
        double[] target = {1, 1};
        SimplexSolver simplexSolver = new SimplexSolver();
        RealPointValuePair result = simplexSolver.doOptimize(f, constraints, target, false);
        assertEquals(0.0, result.getPoint()[0], DEFAULT_EPSILON);
        assertEquals(1.0, result.getPoint()[1], DEFAULT_EPSILON);
    }

    private SimplexTableau createSimplexTableau() {
        double[] f = {1, 2};
        double[][] constraints = {
            {1, 1},
            {-1, 0},
            {0, -1}
        };
        double[] target = {3, 4};
        return new SimplexTableau(f, constraints, target, false, DEFAULT_EPSILON);
    }
}