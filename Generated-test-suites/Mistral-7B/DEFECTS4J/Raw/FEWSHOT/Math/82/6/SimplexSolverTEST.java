package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleProblem() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        RealPointValuePair solution = solver.doOptimize();
        assertArrayEquals(new double[]{-3.0 / 2.0, -1.5}, solution.getPoint(), 0.001);
        assertEquals(1.0 / 2.0, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveProblemWithFeasibleSolutionButNoOptimal() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 0.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testSolveProblemWithUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, Double.POSITIVE_INFINITY}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testSolveProblemWhereAllVariablesAreFree() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{Double.NEGATIVE_INFINITY, 0.0}, {0.0, Double.NEGATIVE_INFINITY}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        RealPointValuePair solution = solver.doOptimize();
        assertArrayEquals(new double[]{-Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, solution.getPoint(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, solution.getValue(), 0.0);
    }

    @Test
    public void testSolveProblemWithRestrictions() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 0.0}, {1.0, 0.0}};
        Constraint[] constraints = {
            new LinearConstraint(new Variable[]{new Variable("x1"), new Variable("x2")}, 2.0),
            new LinearConstraint(new Variable[]{new Variable("x1"), new Variable("x2")}, 0.0)};

        SimplexTableau tableau = new SimplexTableau(f, constraints, null, null, 0.0);

        RealPointValuePair solution = solver.doOptimize();
        assertArrayEquals(new double[]{1.0 / 2.0, 0.0}, solution.getPoint(), 0.001);
        assertEquals(1.0 / 2.0, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveProblemWithDifferentGoalTypes() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 0.0}, {1.0, 0.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, GoalType.MINIMIZE, null, 0.0);

        RealPointValuePair solution = solver.doOptimize();
        assertArrayEquals(new double[]{-3.0 / 2.0, -1.5}, solution.getPoint(), 0.001);
        assertEquals(-1.5, solution.getValue(), 0.001);

        tableau = new SimplexTableau(f, null, GoalType.MAXIMIZE, null, 0.0);
        solution = solver.doOptimize();
        assertArrayEquals(new double[]{3.0 / 2.0, 1.5}, solution.getPoint(), 0.001);
        assertEquals(1.5, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveProblemWithCustomEpsilon() {
        SimplexSolver solver = new SimplexSolver(1.0e-8);
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 0.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        RealPointValuePair solution = solver.doOptimize();
        assertArrayEquals(new double[]{-3.0 / 2.0, -1.5}, solution.getPoint(), 1.0e-8);
        assertEquals(1.0 / 2.0, solution.getValue(), 1.0e-8);
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 0.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        assertTrue(solver.isOptimal(tableau));

        tableau.setEntry(1, 3, -1.0);
        assertFalse(solver.isOptimal(tableau));
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        assertFalse(solver.isPhase1Solved(tableau));

        tableau.setEntry(0, 3, 0.0);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}, {1.0, 1.0}, {0.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.0);

        solver.solvePhase1(tableau);
        assertTrue(MathUtils.equals(tableau.getEntry(0, 3), 0.0, 1.0e-8));

        tableau.setEntry(3, 3, -1.0);
        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(tableau));
    }
}