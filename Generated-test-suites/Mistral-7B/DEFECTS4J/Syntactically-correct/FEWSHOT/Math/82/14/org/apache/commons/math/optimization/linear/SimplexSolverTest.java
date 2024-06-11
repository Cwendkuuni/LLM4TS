package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        LinearObjectiveFunction objF = new LinearObjectiveFunction(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.GREATER_EQUAL));
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, Relationship.LESS_EQUAL));
        LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, 3.0, Relationship.LESS_EQUAL);
        constraints.addConstraint(constraint);
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(objF, constraints);
        assertEquals(PointValuePair.Pair.ZERO, solution.getPoint());
        assertEquals(-5.0, solution.getValue(), 0.001);
    }

    @Test(expected = OptimizationException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction objF = new LinearObjectiveFunction(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0});
        SimplexSolver solver = new SimplexSolver();
        solver.optimize(objF, new LinearConstraintSet());
    }

    @Test
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objF = new LinearObjectiveFunction(new double[]{-5.0, -3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.GREATER_THAN));
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objF, constraints);
            fail();
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithNegativeCoefficients() {
        LinearObjectiveFunction objF = new LinearObjectiveFunction(new double[]{-5.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.GREATER_EQUAL));
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, Relationship.LESS_EQUAL));
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(objF, constraints);
        assertEquals(new double[]{1.0, 1.0}, solution.getPoint());
        assertEquals(-2.0, solution.getValue(), 0.001);
    }

    @Test
    public void testSolveWithPositiveCoefficients() {
        LinearObjectiveFunction objF = new LinearObjectiveFunction(new double[]{5.0, 3.0}, new double[]{1.0, 1.0});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.GREATER_EQUAL));
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, 1.0, Relationship.LESS_EQUAL));
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(objF, constraints);
        assertEquals(new double[]{1.0, 1.0}, solution.getPoint());
        assertEquals(0.0, solution.getValue(), 0.001);
    }
}