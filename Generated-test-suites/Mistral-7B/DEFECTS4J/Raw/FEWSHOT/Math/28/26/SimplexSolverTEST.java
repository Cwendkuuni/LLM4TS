package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBoundingBox;
import org.apache.commons.math3.optimization.SimpleConstraints;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optimization.nonlinear.scalar.LinearConstraintStatus;
import org.apache.commons.math3.optimization.nonlinear.scalar.MaxCountExceededException;
import org.apache.commons.math3.optimization.nonlinear.scalar.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.nonlinear.scalar.UnboundedSolutionException;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, new double[]{1.0, 1.0, 1.0});
        SimpleConstraints constraints = new SimpleConstraints(new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, LinearConstraintStatus.MUST),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, LinearConstraintStatus.MUST),
                new LinearConstraint(new double[]{1.0, 1.0}, 4.0, LinearConstraintStatus.MUST_NOT)
        });
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.optimize(func, constraints);
        assertEquals(GoalType.MINIMIZE, solution.getGoalType());
        assertEquals(new double[]{0.3333333333333333, 0.6666666666666667}, solution.getPoint(), 0.0);
        assertEquals(-3.0, solution.getValue(), 0.0);
    }

    @Test
    public void testSolveWithFeasibleSolution() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{0.0, 0.0}, new double[]{1.0, 1.0});
        SimpleConstraints constraints = new SimpleConstraints(new LinearConstraint[0]);
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.optimize(func, constraints);
        assertEquals(GoalType.MINIMIZE, solution.getGoalType());
        assertEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);
    }

    @Test
    public void testSolveUnbounded() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-1.0, 3.0}, new double[]{1.0, 1.0});
        SimpleConstraints constraints = new SimpleConstraints(new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, LinearConstraintStatus.MUST),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, LinearConstraintStatus.MUST)
        });
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(func, constraints);
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolveInfeasible() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-2.0, -1.0}, new double[]{1.0, 1.0});
        SimpleConstraints constraints = new SimpleConstraints(new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 1.0, LinearConstraintStatus.MUST_NOT),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, LinearConstraintStatus.MUST)
        });
        SimplexSolver solver = new SimplexSolver();

        try {
            solver.optimize(func, constraints);
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolveMaxIterations() {
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-2.0, -1.0, 3.0}, new double[]{1.0, 1.0, 1.0});
        SimpleConstraints constraints = new SimpleConstraints(new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, LinearConstraintStatus.MUST),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, LinearConstraintStatus.MUST),
                new LinearConstraint(new double[]{1.0, 1.0}, 4.0, LinearConstraintStatus.MUST_NOT)
        });
        SimplexSolver solver = new SimplexSolver(1, 1);

        try {
            solver.optimize(func, constraints);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }

}