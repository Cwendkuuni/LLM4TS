import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBoundingBox;
import org.apache.commons.math3.optimization.SimpleConstraints;
import org.apache.commons.math3.optimization.SimpleGoalType;
import org.apache.commons.math3.optimization.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraintSet constraints;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        objectiveFunction = new SimpleLinearObjectiveFunctionSet(new double[]{-1.0, 2.0}, new double[]{0.0});
        constraints = new SimpleConstraints(new LinearConstraintSet(new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, 0.0, Double.POSITIVE_INFINITY),
                new LinearConstraint(new double[]{1.0}, 0.0, Double.POSITIVE_INFINITY)
        }), new LinearConstraintSet());
    }

    @Test
    public void testSimpleOptimization() {
        LinearObjectiveFunction optimizedFunction = solver.optimize(new LinearOptimizationProblem(objectiveFunction, constraints));
        Assert.assertEquals(0.0, optimizedFunction.getValue(new double[]{1.0}), solver.epsilon);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() {
        LinearObjectiveFunction unboundedFunction = new SimpleLinearObjectiveFunctionSet(new double[]{Double.POSITIVE_INFINITY}, new double[]{0.0});
        LinearConstraint unboundedConstraint = new LinearConstraint(new double[]{-1.0}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        constraints = new SimpleConstraints(new LinearConstraintSet(new LinearConstraint[]{unboundedConstraint}), new LinearConstraintSet());
        solver.optimize(new LinearOptimizationProblem(unboundedFunction, constraints));
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        LinearObjectiveFunction objectiveFunction = new SimpleLinearObjectiveFunctionSet(new double[]{-1.0, 2.0}, new double[]{0.0});
        SimpleLinearObjectiveFunctionSet infiniteObjectiveFunction = new SimpleLinearObjectiveFunctionSet(new double[]{-1.0, 2.0}, new double[]{Double.POSITIVE_INFINITY});
        constraints = new SimpleConstraints(new LinearConstraintSet(new LinearConstraint[]{
                new LinearConstraint(new double[]{-1.0}, 0.0, Double.POSITIVE_INFINITY),
                new LinearConstraint(new double[]{1.0}, 0.0, Double.POSITIVE_INFINITY)
        }), new LinearConstraintSet());

        SimpleBoundingBox boundingBox = new SimpleBoundingBox(new double[]{-1.0, -1.0}, new double[]{1.0, 1.0});
        LinearOptimizationProblem problem = new LinearOptimizationProblem(objectiveFunction, constraints, new SimpleGoalType(SimpleGoalType.MINIMIZE), boundingBox);
        SimplexSolver solver = new SimplexSolver(1.0e-15, 50);
        solver.setMaxIterations(100);
        solver.optimize(problem);
        solver.optimize(new LinearOptimizationProblem(infiniteObjectiveFunction, constraints));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() {
        LinearObjectiveFunction objectiveFunction = new SimpleLinearObjectiveFunctionSet(new double[]{Double.POSITIVE_INFINITY}, new double[]{0.0});
        LinearConstraint infeasibleConstraint = new LinearConstraint(new double[]{1.0}, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        constraints = new SimpleConstraints(new LinearConstraintSet(new LinearConstraint[]{infeasibleConstraint}), new LinearConstraintSet());
        solver.optimize(new LinearOptimizationProblem(objectiveFunction, constraints));
    }
}