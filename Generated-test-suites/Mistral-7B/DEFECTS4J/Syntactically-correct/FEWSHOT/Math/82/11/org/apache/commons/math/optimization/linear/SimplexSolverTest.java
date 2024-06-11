package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, Precision.SAFE_EPSILON);
    }

    @Test
    public void testCustomConstructor() {
        double customEpsilon = 1.0e-7;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, solver.epsilon, Precision.SAFE_EPSILON);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        Integer columnIndex = solver.getPivotColumn(tableau);
        assertEquals(3, columnIndex.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        Integer rowIndex = solver.getPivotRow(3, tableau);
        assertEquals(1, rowIndex.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        solver.doIteration(tableau);

        assertEquals(-1.1 / 1.1, tableau.getEntry(1, 3), Precision.SAFE_EPSILON);
        assertEquals(-2.2 / 1.1, tableau.getEntry(2, 3), Precision.SAFE_EPSILON);
        assertEquals(0.0, tableau.getEntry(1, 1), Precision.SAFE_EPSILON);
        assertEquals(0.0, tableau.getEntry(2, 1), Precision.SAFE_EPSILON);
        assertEquals(1.0, tableau.getEntry(3, 3), Precision.SAFE_EPSILON);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        assertFalse(solver.isPhase1Solved(tableau));

        tableau.setEntry(1, 3, -1.1);
        tableau.setEntry(2, 3, -2.2);
        tableau.setEntry(3, 3, 1.0);

        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        assertFalse(solver.isOptimal(tableau));

        tableau.setEntry(1, 3, -1.1);
        tableau.setEntry(2, 3, -2.2);
        tableau.setEntry(3, 3, 1.0);
        tableau.setEntry(4, 4, 0.0);
        tableau.setEntry(4, 4, 0.0);
        tableau.setEntry(5, 4, 1.0);

        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        solver.solvePhase1(tableau);

        assertEquals(-1.1 / 1.1, tableau.getEntry(1, 3), Precision.SAFE_EPSILON);
        assertEquals(-2.2 / 1.1, tableau.getEntry(2, 3), Precision.SAFE_EPSILON);
        assertEquals(0.0, tableau.getEntry(1, 1), Precision.SAFE_EPSILON);
        assertEquals(0.0, tableau.getEntry(2, 1), Precision.SAFE_EPSILON);
        assertEquals(1.0, tableau.getEntry(3, 3), Precision.SAFE_EPSILON);
    }

    @Test
    public void testThrowNoFeasibleSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        tableau.setEntry(1, 5, 1.0);

        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testThrowUnboundedSolutionException() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, 1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        assertThrows(UnboundedSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new Array2DRowRealMatrix(new double[][]{
                {1.1, 2.2, 3.3, 4.4, 5.5},
                {-5.6, -2.2, 3.3, 4.4, 5.5},
                {-2.2, -2.2, -1.1, 4.4, 5.5},
                {1.0, 0.0, 0.0, 0.0, -6.0},
                {0.0, 0.0, 0.0, 1.0, -1.1}
        }), new double[]{1.0, 1.0, -1.0}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);

        RealPointValuePair solution = solver.doOptimize();

        assertEquals(5.5, solution.getPoint()[0], Precision.SAFE_EPSILON);
        assertEquals(-11.1, solution.getValue(), Precision.SAFE_EPSILON);
    }

}