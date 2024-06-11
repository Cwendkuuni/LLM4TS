package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimpleLinearConstraint;
import org.apache.commons.math3.optimization.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test case for {@link SimplexSolver#SimplexSolver()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.getEpsilon(), 0);
        assertEquals(10, solver.getMaxUlps(), 0);
    }

    /**
     * Test case for {@link SimplexSolver#SimplexSolver(double, int)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCustomConstructor() throws Exception {
        SimplexSolver solver = new SimplexSolver(0.001, 5);
        assertEquals(0.001, solver.getEpsilon(), 0);
        assertEquals(5, solver.getMaxUlps(), 0);
    }

    /**
     * Test case for {@link SimplexSolver#getPivotColumn(SimplexTableau)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetPivotColumn() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, -3, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, -1}, 3),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 2}, 4),
                                                        new SimpleLinearConstraint(new double[]{0, 1, -1}, 2)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 10);

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    /**
     * Test case for {@link SimplexSolver#getPivotRow(SimplexTableau, int)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetPivotRow() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, -3, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, -1}, 3),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 2}, 4),
                                                        new SimpleLinearConstraint(new double[]{0, 1, -1}, 2)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 10);

        Integer pivotRow = new SimplexSolver().getPivotRow(tableau, 0);
        assertEquals(2, pivotRow.intValue());
    }

    /**
     * Test case for {@link SimplexSolver#doIteration(SimplexTableau)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, -3, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, -1}, 3),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 2}, 4),
                                                        new SimpleLinearConstraint(new double[]{0, 1, -1}, 2)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();

        solver.doIteration(tableau);

        assertEquals(2, tableau.getNumArtificialVariables());
        assertArrayEquals(new double[]{-0.25, -1.5, 0}, tableau.getBasicVariables(), 0);
        assertArrayEquals(new double[]{0, 3, 0}, tableau.getBasicConstants(), 0);
        assertArrayEquals(new double[]{0.5, 0, 0}, tableau.getSlack(), 0);
        assertArrayEquals(new double[]{-1.75, -4.5, 0}, tableau.getReducedCosts(), 0);
        assertEquals(-2, tableau.getObjValue(), 0);
    }

    /**
     * Test case for {@link SimplexSolver#solvePhase1(SimplexTableau)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, -3, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, -1}, 3),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 2}, 4),
                                                        new SimpleLinearConstraint(new double[]{0, 1, -1}, 2)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        assertEquals(0, tableau.getNumArtificialVariables());
        assertArrayEquals(new double[]{0.25, 0.5, 0}, tableau.getBasicVariables(), 0);
        assertArrayEquals(new double[]{3, 1, 2}, tableau.getBasicConstants(), 0);
        assertArrayEquals(new double[]{0, 0, 0}, tableau.getSlack(), 0);
        assertArrayEquals(new double[]{1.75, 1.5, 0}, tableau.getReducedCosts(), 0);
        assertEquals(0, tableau.getObjValue(), 0);
    }

    /**
     * Test case for {@link SimplexSolver#doOptimize()}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testDoOptimize() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, -3, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, -1}, 3),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 2}, 4),
                                                        new SimpleLinearConstraint(new double[]{0, 1, -1}, 2)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();

        assertEquals(0.25, solution.getPoint()[0], 0);
        assertEquals(0.5, solution.getPoint()[1], 0);
        assertEquals(0, solution.getValue(), 0);
    }

    /**
     * Test case for {@link SimplexSolver#doOptimize()} with a non-bounded solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{0, 0, -1}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, 0}, 0),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 0}, 0),
                                                        new SimpleLinearConstraint(new double[]{0, 1, 0}, 0)
                                                    },
                                                    LinearConstraintType.EQ, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    /**
     * Test case for {@link SimplexSolver#doOptimize()} with an infeasible solution.
     * @throws Exception if an error occurs
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, 0, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, -1, 0}, 0),
                                                        new SimpleLinearConstraint(new double[]{1, 1, 0}, 0),
                                                        new SimpleLinearConstraint(new double[]{0, 1, 0}, 0)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    /**
     * Test case for {@link SimplexSolver#doIteration(SimplexTableau)} with a maximal iteration count.
     * @throws Exception if an error occurs
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationCount() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new SimpleLinearObjectiveFunction(new double[]{-1, -3, 0}),
                                                    new LinearConstraint[]{
                                                        new SimpleLinearConstraint(new double[]{1, 1, -1}, 3),
                                                        new SimpleLinearConstraint(new double[]{1, 0, 2}, 4),
                                                        new SimpleLinearConstraint(new double[]{0, 1, -1}, 2)
                                                    },
                                                    LinearConstraintType.LEQ, null, 1.0e-6, 0);

        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
    }

}