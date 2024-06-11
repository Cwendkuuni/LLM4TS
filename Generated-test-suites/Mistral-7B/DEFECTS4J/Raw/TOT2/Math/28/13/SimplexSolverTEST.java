package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

/**
 * Test cases for the SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * A simple test for the constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        new SimplexSolver();
        new SimplexSolver(0.001, 10);
    }

    /**
     * A test for the solve method with a simple objective function and no constraints.
     */
    @org.junit.Test
    public void testSolveSimple() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f);
        assertEquals(0, solution.getPoint()[0], 1e-6);
        assertEquals(-1, solution.getValue(), 1e-6);
    }

    /**
     * A test for the solve method with a simple objective function and one constraint.
     */
    @org.junit.Test
    public void testSolveSimpleConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        LinearObjectiveFunction g = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {-2.0, -1.0}}, 0);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, g);
        assertEquals(0.5, solution.getPoint()[0], 1e-6);
        assertEquals(-1.5, solution.getValue(), 1e-6);
    }

    /**
     * A test for the solve method with a simple objective function and two constraints.
     */
    @org.junit.Test
    public void testSolveSimpleConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        LinearObjectiveFunction g1 = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {-2.0, -1.0}}, 0);
        LinearObjectiveFunction g2 = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {-1.0, -1.0}}, 1);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, g1, g2);
        assertEquals(1.0 / 3.0, solution.getPoint()[0], 1e-6);
        assertEquals(-4.0 / 3.0, solution.getValue(), 1e-6);
    }

    /**
     * A test for the solve method with an unbounded solution.
     */
    @org.junit.Test
    public void testSolveUnbounded() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        SimplexSolver solver = new SimplexSolver();
        assertThrows(UnboundedSolutionException.class, () -> solver.optimize(f));
    }

    /**
     * A test for the solve method with a no feasible solution.
     */
    @org.junit.Test
    public void testSolveNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        LinearObjectiveFunction g = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {-2.0, -1.0}}, 0);
        g.setAdditionally(new SimpleBounds(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        SimplexSolver solver = new SimplexSolver();
        assertThrows(NoFeasibleSolutionException.class, () -> solver.optimize(f, g));
    }

    /**
     * A test for the solve method with a maximal iteration count exceeded.
     */
    @org.junit.Test
    public void testSolveMaxIterations() throws MaxCountExceededException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        SimplexSolver solver = new SimplexSolver(1, 1);
        assertThrows(MaxCountExceededException.class, () -> solver.optimize(f));
    }

    /**
     * A test for the solve method with an objective function and constraints in reverse order.
     */
    @org.junit.Test
    public void testSolveWithConstraintsFirst() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        LinearObjectiveFunction g1 = new LinearObjectiveFunction(new double[][]{{1.0, 1.0}, {-2.0, -1.0}}, 0);
        LinearObjectiveFunction g2 = new LinearObjectiveFunction(new double[][]{{2.0, 1.0}, {-1.0, -1.0}}, 1);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(g1, g2, f);
        assertEquals(-1.0 / 3.0, solution.getPoint()[0], 1e-6);
        assertEquals(2.0 / 3.0, solution.getValue(), 1e-6);
    }

    /**
     * A test for the solve method with a goal type.
     */
    @org.junit.Test
    public void testSolveWithGoalType() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(f, GoalType.MINIMIZE);
        assertEquals(0, solution.getPoint()[0], 1e-6);
        assertEquals(-1, solution.getValue(), 1e-6);
    }

    /**
     * A test for the solve method with a limited number of iterations and a goal type.
     */
    @org.junit.Test
    public void testSolveWithGoalTypeAndMaxIterations() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{-1.0, 1.0}, {-3.0, -1.0}}, 2);
        SimplexSolver solver = new SimplexSolver(1, 1);
        PointValuePair solution = solver.optimize(f, GoalType.MINIMIZE);
        assertEquals(Double.NaN, solution.getPoint()[0], 0);
        assertEquals(Double.NaN, solution.getValue(), 0);
    }
}