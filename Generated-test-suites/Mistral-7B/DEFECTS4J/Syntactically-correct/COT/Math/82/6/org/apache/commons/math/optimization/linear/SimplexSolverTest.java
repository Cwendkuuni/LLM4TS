package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for SimplexSolver.
 */
public class SimplexSolverTest {

    @Test
    public void testSimplexSolverDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(DEFAULT_EPSILON, solver.epsilon, 0.0);
    }

    @Test
    public void testSimplexSolverCustomConstructor() {
        double customEpsilon = 1.0e-9;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, solver.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        Integer expectedPivotColumn = 0;
        Integer actualPivotColumn = solver.getPivotColumn(tableau);
        assertEquals(expectedPivotColumn, actualPivotColumn);
    }

    @Test
    public void testGetPivotColumnWhenAllEntriesAreZero() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[0]), new LinearConstraint[0],
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);
        Integer actualPivotColumn = solver.getPivotColumn(tableau);
        assertNull(actualPivotColumn);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotRowWhenNoEntryIsGreaterOrEqualThanZero() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        Integer pivotColumn = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(pivotColumn, tableau);

        assertNull(pivotRow);
        throw new UnboundedSolutionException();
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        Integer pivotCol = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(pivotCol, tableau);

        solver.doIteration(tableau);

        double pivotVal = tableau.getEntry(pivotRow, pivotCol);
        assertEquals(1.0, pivotVal, Precision.SAFE_EPSILON);

        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                double multiplier = tableau.getEntry(i, pivotCol);
                double result = tableau.getEntry(i, 0);
                assertEquals(-multiplier, result, Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWhenNoEntryIsGreaterOrEqualThanZero() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        Integer pivotCol = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(pivotCol, tableau);

        solver.doIteration(tableau);

        throw new UnboundedSolutionException();
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        assertFalse(solver.isPhase1Solved(tableau));

        solver.doIteration(tableau);

        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();

        assertFalse(solver.isOptimal(tableau));

        solver.doIteration(tableau);

        assertTrue(solver.isOptimal(tableau));
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolvePhase1WhenMaxIterationsExceeded() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        solver.setMaxIterations(0);

        solver.solvePhase1(tableau);

        throw new MaxIterationsExceededException("maximum number of iterations exceeded");
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WhenNoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 0.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        solver.solvePhase1(tableau);

        throw new NoFeasibleSolutionException();
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, 3.0f)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        RealPointValuePair solution = solver.doOptimize();

        PointValuePair expectedSolution = new PointValuePair(new double[]{0.0, 2.0, 1.0}, -3.0);
        assertEquals(expectedSolution.getPoint(), solution.getPoint(), Precision.SAFE_EPSILON);
        assertEquals(expectedSolution.getValue(), solution.getValue(), Precision.SAFE_EPSILON);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWhenUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}),
                new LinearConstraint[]{new LinearConstraint(new double[]{-1.0, 1.0}, 1.0f),
                        new LinearConstraint(new double[]{1.0, 1.0}, 2.0f),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Minimize, true, DEFAULT_EPSILON);

        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();

        throw new UnboundedSolutionException();
    }
}