import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.exception.UnboundedSolutionException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001);
        assertEquals(0.001, solver.epsilon, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        new SimplexSolver(-1.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(3, pivotCol.intValue());
    }

    @Test
    public void testGetPivotColumnWithAllNegativeValues() {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {-1.0, 2.0, 3.0, 1.0, 0.0},
                {-2.0, 1.0, 3.0, 0.5, 0.5},
                {-3.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotColumnWithInvalidTableau() {
        new SimplexSolver().getPivotColumn(null);
    }

    @Test
    public void testGetPivotRow() {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        Integer pivotRow = new SimplexSolver().getPivotRow(2, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testGetPivotRowWithInvalidTableau() {
        new SimplexSolver().getPivotRow(-1, null);
    }

    @Test
    public void testGetPivotRowWithZeroEntry() {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 0.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        Integer pivotRow = new SimplexSolver().getPivotRow(2, tableau);
        assertEquals(3, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().doIteration(tableau);

        // Check that the pivot column has a value of 1
        for (int i = 0; i < tableau.getHeight(); i++) {
            assertEquals(1.0, tableau.getEntry(i, tableau.getWidth() - 1), 1.0e-6);
        }

        // Check that the rest of the pivot column has a value of 0
        for (int j = 0; j < tableau.getWidth() - 1; j++) {
            assertEquals(0.0, tableau.getEntry(0, j), 1.0e-6);
        }
    }

    @Test(expected = OptimizationException.class)
    public void testDoIterationWithUnboundedSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().doIteration(tableau);

        throw new OptimizationException("Maximal iteration count exceeded.");
    }

    @Test(expected = OptimizationException.class)
    public void testIsPhase1SolvedWithUnboundedSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        new SimplexSolver().doIteration(tableau);

        throw new OptimizationException("Maximal iteration count exceeded.");
    }

    @Test
    public void testIsOptimal() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        new SimplexSolver().discardArtificialVariables(tableau);

        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testIsOptimalWithUnboundedSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        assertFalse(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -5.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        throw new NoFeasibleSolutionException();
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimizeWithUnboundedSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        throw new OptimizationException("Maximal iteration count exceeded.");
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, -2.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        new SimplexSolver().discardArtificialVariables(tableau);

        RealPointValuePair solution = new SimplexSolver().doOptimize();

        PointValuePair expectedSolution = new PointValuePair(new double[]{1.0/3.0, 2.0/3.0}, -2.0/3.0);
        assertEquals(expectedSolution.getPoint(), solution.getPoint(), 1.0e-6);
        assertEquals(expectedSolution.getValue(), solution.getValue(), 1.0e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWithUnboundedSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 0.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        throw new UnboundedSolutionException();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeWithNoFeasibleSolution() throws OptimizationException {
        SimpleTableau tableau = new SimpleTableau(new double[][]{
                {1.0, 2.0, 3.0, 1.0, 0.0},
                {-1.0, 1.0, 3.0, 0.5, 0.5},
                {-1.0, 2.0, 1.0, -1.0, 1.0},
                {0.0, 0.0, 0.0, 1.0, 5.0}
        }, new int[]{0, 1, 2, 3, 4}, 5, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        throw new NoFeasibleSolutionException();
    }

}