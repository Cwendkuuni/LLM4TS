import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private static final double EPSILON = 1e-6;
    private static final int MAX_ULPS = 10;

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = createSimplexSolver();

        Integer pivotColumn = solver.getPivotColumn(tableau);

        assertEquals(3, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = createSimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);

        Integer pivotRow = solver.getPivotRow(tableau, pivotColumn);

        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexTableau tableau = createSimplexTableau();
        SimplexSolver solver = createSimplexSolver();

        try {
            solver.doIteration(tableau);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertEquals(1.0, tableau.getEntry(1, 3), EPSILON);
        assertEquals(0.0, tableau.getEntry(0, 3), EPSILON);
        assertEquals(0.0, tableau.getEntry(2, 3), EPSILON);
        assertEquals(0.0, tableau.getEntry(1, 0), EPSILON);
        assertEquals(0.0, tableau.getEntry(1, 1), EPSILON);
        assertEquals(0.0, tableau.getEntry(1, 2), EPSILON);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() throws UnboundedSolutionException {
        SimplexTableau tableau = createUnboundedSimplexTableau();
        SimplexSolver solver = createSimplexSolver();

        solver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = createPhase1SimplexTableau();
        SimplexSolver solver = createSimplexSolver();

        try {
            solver.solvePhase1(tableau);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws NoFeasibleSolutionException {
        SimplexTableau tableau = createNoFeasibleSolutionSimplexTableau();
        SimplexSolver solver = createSimplexSolver();

        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = createSimplexSolver();
        PointValuePair solution = null;

        try {
            solution = solver.doOptimize();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        assertNotNull(solution);
        assertEquals(15.0, solution.getValue(), EPSILON);
        assertEquals(2.0, solution.getPoint()[0], EPSILON);
        assertEquals(3.0, solution.getPoint()[1], EPSILON);
    }

    private SimplexTableau createSimplexTableau() {
        double[][] coefficients = {{2, 3, 1, 0, 0}, {1, -1, 0, 1, 0}, {-2, 1, 0, 0, 1}};
        double[] constantTerms = {8, 1, -1};

        return new SimplexTableau(coefficients, constantTerms, GoalType.MAXIMIZE, false, EPSILON, MAX_ULPS);
    }

    private SimplexTableau createUnboundedSimplexTableau() {
        double[][] coefficients = {{-1, -2, -3, 0, 0}, {-1, 1, 0, -1, 0}, {-2, -1, 0, 0, -1}};
        double[] constantTerms = {-10, 0, 0};

        return new SimplexTableau(coefficients, constantTerms, GoalType.MAXIMIZE, false, EPSILON, MAX_ULPS);
    }

    private SimplexTableau createPhase1SimplexTableau() {
        double[][] coefficients = {{0, 2, 1, 0, 0}, {-1, 1, 0, 1, 0}, {0, -3, 0, 0, 1}, {1, -1, 0, 0, 0}};
        double[] constantTerms = {0, 1, -1, 0};

        return new SimplexTableau(coefficients, constantTerms, GoalType.MAXIMIZE, false, EPSILON, MAX_ULPS);
    }

    private SimplexTableau createNoFeasibleSolutionSimplexTableau() {
        double[][] coefficients = {{1, -1, 1}, {-1, 1, -1}};
        double[] constantTerms = {1, -1};

        return new SimplexTableau(coefficients, constantTerms, GoalType.MAXIMIZE, false, EPSILON, MAX_ULPS);
    }

    private SimplexSolver createSimplexSolver() {
        return new SimplexSolver(EPSILON, MAX_ULPS);
    }

}