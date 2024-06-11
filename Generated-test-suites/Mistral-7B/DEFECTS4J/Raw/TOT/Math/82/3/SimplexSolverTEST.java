package org.apache.commons.math.optimization.linear;

import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.optim.*;
import org.apache.commons.math.optim.linear.*;
import org.apache.commons.math.optim.linear.LinearConstraint.Relation;
import org.apache.commons.math.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math.optim.linear.LinearConstraint;
import org.apache.commons.math.optim.linear.SimplexSolver;

/**
 * Test cases for SimplexSolver class.
 */
public class SimplexSolverTest {

    /**
     * Test optimization with a simple problem.
     */
    @Test
    public void testSimpleProblem() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 1.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints);
        assertEquals("Expected solution: [0.5, 1.5]", 0.5, Precision.round(solution.getPoint()[0], 3), 1e-9);
        assertEquals("Expected objective value: 1.5", 1.5, Precision.round(solution.getValue(), 3), 1e-9);
    }

    /**
     * Test optimization with a problem with no solution.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 0.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints);
    }

    /**
     * Test optimization with a problem with an unbounded solution.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints);
    }

    /**
     * Test optimization with a problem with no objective function.
     */
    @Test(expected = OptimizationException.class)
    public void testNoObjectiveFunction() {
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(null, constraints);
    }

    /**
     * Test optimization with a problem with no constraints.
     */
    @Test
    public void testNoConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, new LinearConstraint[0]);
        assertEquals("Expected solution: [1.0, -1.0]", 1.0, Precision.round(solution.getPoint()[0], 3), 1e-9);
        assertEquals("Expected objective value: -5.0", -5.0, Precision.round(solution.getValue(), 3), 1e-9);
    }

    /**
     * Test optimization with a problem with a single constraint.
     */
    @Test
    public void testSingleConstraint() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint constraint = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 1.0);
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, new LinearConstraint[] {constraint});
        assertEquals("Expected solution: [0.5, 1.5]", 0.5, Precision.round(solution.getPoint()[0], 3), 1e-9);
        assertEquals("Expected objective value: 1.5", 1.5, Precision.round(solution.getValue(), 3), 1e-9);
    }

    /**
     * Test optimization with a problem with two constraints.
     */
    @Test
    public void testTwoConstraints() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 1.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 2.0)
        };
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints);
        assertEquals("Expected solution: [0.5, 1.5]", 0.5, Precision.round(solution.getPoint()[0], 3), 1e-9);
        assertEquals("Expected objective value: 1.5", 1.5, Precision.round(solution.getValue(), 3), 1e-9);
    }

    /**
     * Test optimization with a problem with a single constraint and a non-negative constraint.
     */
    @Test
    public void testSingleConstraintNonNegative() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint constraint = new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 1.0);
        LinearConstraint nonNegativeConstraint = new LinearConstraint(new double[][]{{1.0, 1.0}}, Relation.GEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, new LinearConstraint[] {constraint, nonNegativeConstraint});
        assertEquals("Expected solution: [0.5, 1.5]", 0.5, Precision.round(solution.getPoint()[0], 3), 1e-9);
        assertEquals("Expected objective value: 1.5", 1.5, Precision.round(solution.getValue(), 3), 1e-9);
    }

    /**
     * Test optimization with a problem with two constraints and a non-negative constraint.
     */
    @Test
    public void testTwoConstraintsNonNegative() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-5.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[][]{{1.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 1.0),
            new LinearConstraint(new double[][]{{2.0, 1.0}, {1.0, 1.0}}, Relation.LEQ, 2.0),
            new LinearConstraint(new double[][]{{1.0, 1.0}}, Relation.GEQ, 0.0)
        };
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints);
        assertEquals("Expected solution: [0.5, 1.5]", 0.5, Precision.round(solution.getPoint()[0], 3), 1e-9);
        assertEquals("Expected objective value: 1.5", 1.5, Precision.round(solution.getValue(), 3), 1e-9);
    }

}