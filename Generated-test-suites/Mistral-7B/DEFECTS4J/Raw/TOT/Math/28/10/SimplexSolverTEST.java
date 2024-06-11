package org.apache.commons.math3.optimization.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Simple linear function for testing.
     */
    private static final LinearObjectiveFunction FUNCTION = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 2.0);

    /**
     * Test that the solver returns the correct solution for a simple problem.
     */
    @Test
    public void testSimpleProblem() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(FUNCTION, new LinearConstraint[0], null, true, 1e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(0.5, solution.getPoint()[0], 1e-6);
        Assert.assertEquals(1.5, solution.getPoint()[1], 1e-6);
        Assert.assertEquals(1.0, solution.getValue(), 1e-6);
    }

    /**
     * Test that the solver returns an error message for an empty constraint list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstraints() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(FUNCTION, new LinearConstraint[0], null, true, 1e-6, 10);
        solver.solve(tableau);
    }

    /**
     * Test that the solver returns an error message for a goal type that is not recognized.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnknownGoalType() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(FUNCTION, new LinearConstraint[0], 3, true, 1e-6, 10);
        solver.solve(tableau);
    }

    /**
     * Test that the solver returns an error message for a negative epsilon.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver(-1.0, 10);
        SimplexTableau tableau = new SimplexTableau(FUNCTION, new LinearConstraint[0], null, true, 1e-6, 10);
        solver.solve(tableau);
    }

    /**
     * Test that the solver returns an error message for a large ulps value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLargeUlps() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver(1e-6, 1000000);
        SimplexTableau tableau = new SimplexTableau(FUNCTION, new LinearConstraint[0], null, true, 1e-6, 10);
        solver.solve(tableau);
    }

    /**
     * Test that the solver returns an error message for a problem with an unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws NoFeasibleSolutionException, MaxCountExceededException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[0], null, true, 1e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        solver.doIteration(tableau);
    }

    /**
     * Test that the solver returns an error message for a problem with no feasible solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, -1.0}, {-1.0, 1.0}}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[]{{new LinearConstraint(1.0), Double.NEGATIVE_INFINITY}, {new LinearConstraint(1.0), Double.POSITIVE_INFINITY}}, null, true, 1e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        solver.doIteration(tableau);
    }

    /**
     * Test that the solver returns an error message for a problem with a max count exceeded.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}, {0.0, 0.0, Double.NEGATIVE_INFINITY}}, 0.0);
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[0], null, true, 1e-6, 10);
        for (int i = 0; i < 1000; i++) {
            solver.doIteration(tableau);
        }
    }

    /**
     * Test that the solver returns the correct solution for a simple problem with constraints.
     */
    @Test
    public void testConstraints() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 2.0);
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1.0), 0.0}, {new LinearConstraint(1.0), 1.0}};
        SimplexTableau tableau = new SimplexTableau(function, constraints, null, true, 1e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        PointValuePair solution = solver.doOptimize();
        Assert.assertEquals(0.5, solution.getPoint()[0], 1e-6);
        Assert.assertEquals(1.5, solution.getPoint()[1], 1e-6);
        Assert.assertEquals(1.0, solution.getValue(), 1e-6);
        Assert.assertTrue(tableau.isFeasible());
    }

    /**
     * Test that the solver throws an exception for a problem with infeasible constraints.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInfeasibleConstraints() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 2.0);
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1.0), Double.NEGATIVE_INFINITY}, {new LinearConstraint(1.0), Double.POSITIVE_INFINITY}};
        SimplexTableau tableau = new SimplexTableau(function, constraints, null, true, 1e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        solver.doIteration(tableau);
    }

    /**
     * Test that the solver throws an exception for a problem with inconsistent constraints.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInconsistentConstraints() throws NoFeasibleSolutionException, MaxCountExceededException, UnboundedSolutionException {
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 2.0);
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = new LinearConstraint[]{{new LinearConstraint(1.0), 0.0}, {new LinearConstraint(1.0), Double.POSITIVE_INFINITY}};
        SimplexTableau tableau = new SimplexTableau(function, constraints, null, true, 1e-6, 10);
        solver.solvePhase1(tableau);
        tableau.dropPhase1Objective();
        solver.doIteration(tableau);
    }
}