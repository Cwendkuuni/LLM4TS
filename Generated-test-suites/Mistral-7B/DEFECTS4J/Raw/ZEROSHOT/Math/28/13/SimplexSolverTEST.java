import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBoundingBox;
import org.apache.commons.math3.optim.linear.SimplexConstraint;
import org.apache.commons.math3.optim.linear.SimplexObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolver() throws Exception {
        // Test case for a simple linear optimization problem
        LinearObjectiveFunction objFunction = new SimplexObjectiveFunction(new double[]{-2.0, 3.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new SimplexConstraint(2.0, "<", 5.0),
                new SimplexConstraint(2.0, ">", 0.0),
                new SimplexConstraint(3.0, "<", 8.0),
                new SimplexConstraint(3.0, ">", 0.0),
                new SimplexConstraint(1.0, "<=", 3.0),
                new SimplexConstraint(1.0, ">=", 0.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean isRestrictedToNonNegative = false;
        double epsilon = 1e-6;
        int maxUlps = 10;

        AbstractLinearOptimizer solver = new SimplexSolver(epsilon, maxUlps);
        PointValuePair solution = solver.optimize(objFunction, constraints, goalType, isRestrictedToNonNegative);

        LinearPointValuePair expectedSolution = new LinearPointValuePair(new double[]{3.0, 0.0, 0.0}, -2.0);
        Assert.assertEquals(expectedSolution.getPoint(), solution.getPoint(), epsilon);
        Assert.assertEquals(expectedSolution.getValue(), solution.getValue(), epsilon);

        // Test case for an unbounded solution
        LinearObjectiveFunction unboundedObjFunction = new SimplexObjectiveFunction(new double[]{0.0, 0.0}, new double[]{0.0, 0.0});
        PointValuePair unboundedSolution = solver.optimize(unboundedObjFunction, new LinearConstraint[0], goalType, isRestrictedToNonNegative);
        Assert.fail("Expected UnboundedSolutionException, but did not get one.");

        // Test case for a problem with no feasible solution
        LinearObjectiveFunction noFeasibleSolutionObjFunction = new SimplexObjectiveFunction(new double[]{-1000.0, 3.0, -1.0}, new double[]{0.0, 1.0, 1.0});
        PointValuePair noFeasibleSolution = solver.optimize(noFeasibleSolutionObjFunction, constraints, goalType, isRestrictedToNonNegative);
        Assert.fail("Expected NoFeasibleSolutionException, but did not get one.");

        // Test case for a problem with a maximum number of iterations
        SimplexTableau simplexTableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0}), new LinearConstraint[0], goalType, isRestrictedToNonNegative, epsilon, maxUlps);
        for (int i = 0; i < SimplexSolver.MAX_ITERATIONS; i++) {
            solver.doIteration(simplexTableau);
        }
        PointValuePair maxIterationsSolution = solver.optimize(new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0}), new LinearConstraint[0], goalType, isRestrictedToNonNegative);
        Assert.fail("Expected MaxCountExceededException, but did not get one.");
    }

}