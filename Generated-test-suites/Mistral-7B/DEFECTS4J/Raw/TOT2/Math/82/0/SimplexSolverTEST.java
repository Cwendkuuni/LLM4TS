import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationException;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraintViolation;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleLinearOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimplexSolverTest {

    @Test
    public void testEmptyProblem() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = {};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0}, new double[]{2.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(objectiveFunction);
        SimpleLinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(new LinearProblem(constraints, constraintSet, objectiveFunctionSet, GoalType.MINIMIZE, null, null));
        Assert.assertNull(solution);
    }

    @Test
    public void testSimpleProblem() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = {
                new LinearConstraint(1.0, 1.0, 2.0, Relationship.LEQ),
                new LinearConstraint(1.0, 2.0, 3.0, Relationship.LEQ)
        };
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -2.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(objectiveFunction);
        SimpleLinearOptimizer optimizer = new SimplexSolver();
        PointValuePair solution = optimizer.optimize(new LinearProblem(constraints, constraintSet, objectiveFunctionSet, GoalType.MINIMIZE, null, null));
        Assert.assertArrayEquals(new double[]{3.0 / 7.0, 4.0 / 7.0}, solution.getPoint(), 0.0);
        Assert.assertEquals(-13.0 / 7.0, solution.getValue(), 0.0);
    }

    @Test(expected = OptimizationException.class)
    public void testUnboundedSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = {};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0}, new double[]{0.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(objectiveFunction);
        SimpleLinearOptimizer optimizer = new SimplexSolver();
        optimizer.optimize(new LinearProblem(constraints, constraintSet, objectiveFunctionSet, GoalType.MINIMIZE, null, null));
    }

    @Test(expected = OptimizationException.class)
    public void testNoFeasibleSolution() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = {
                new LinearConstraint(1.0, 1.0, 2.0, Relationship.LEQ),
                new LinearConstraint(1.0, 2.0, -1.0, Relationship.LEQ)
        };
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -2.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(objectiveFunction);
        SimpleLinearOptimizer optimizer = new SimplexSolver();
        optimizer.optimize(new LinearProblem(constraints, constraintSet, objectiveFunctionSet, GoalType.MINIMIZE, null, null));
    }

    @Test(expected = SimpleOptimizationException.class)
    public void testExceptionOnIteration() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = {};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0}, new double[]{2.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet();
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(objectiveFunction);
        SimpleLinearOptimizer optimizer = new SimplexSolver() {
            @Override
            protected void doIteration(SimplexTableau tableau) throws OptimizationException {
                throw new SimpleOptimizationException("Test exception");
            }
        };
        PointValuePair solution = optimizer.optimize(new LinearProblem(constraints, constraintSet, objectiveFunctionSet, GoalType.MINIMIZE, null, null));
        Assert.assertNull(solution);
    }

    @Test
    public void testRestrictToNonNegative() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver(true);
        LinearConstraint[] constraints = {
                new LinearConstraint(1.0, 1.0, 2.0, Relationship.LEQ),
                new LinearConstraint(1.0, 2.0, 3.0, Relationship.LEQ)
        };
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 2.0}, new double[]{-3.0, -2.0});
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        LinearObjectiveFunctionSet objectiveFunctionSet = new LinearObjectiveFunctionSet(objectiveFunction);
        SimpleLinearOptimizer optimizer = new SimplexSolver(true);
        PointValuePair solution = optimizer.optimize(new LinearProblem(constraints, constraintSet, objectiveFunctionSet, GoalType.MINIMIZE, null, null));
        Assert.assertArrayEquals(new double[]{Math.max(0.0, 3.0 / 7.0), Math.max(0.0, 4.0 / 7.0)}, solution.getPoint(), 0.0);
        Assert.assertEquals(-13.0 / 7.0, solution.getValue(), 0.0);
    }
}