import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.simple.ObjectiveFunctionTestUtils;
import org.apache.commons.math3.optim.nonlinear.simple.linear.ArrayConstraint2D;
import org.apache.commons.math3.optim.nonlinear.simple.linear.ArrayLinearObjectiveFunction2D;
import org.apache.commons.math3.optim.nonlinear.simple.linear.ArrayVectorValuePair2D;
import org.apache.commons.math3.optim.nonlinear.simple.linear.SimplexTableau2D;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        LinearObjectiveFunction objFct = new ArrayLinearObjectiveFunction2D(new double[][]{
                new double[]{1.0, 2.0, 3.0},
                new double[]{-3.0, -1.0, 1.0}
        }, new double[]{0.0, 2.0});

        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new ArrayConstraint2D(new double[][]{
                new double[]{1.0, 1.0, 1.0},
                new double[]{1.0, 1.0, 1.0}
        }, Relationship.LEQ, new double[]{3.0});

        SimpleGoalType goalType = SimpleGoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(objFct, constraints, goalType);

        Assert.assertEquals(new double[]{0.0, 1.5, 1.5}, solution.getPoint(), 0.0);
        Assert.assertEquals(new double[]{-1.5, -1.5, -3.0}, solution.getValue(), 0.0);
    }

    @Test
    public void testSolveUnboundedExample() {
        LinearObjectiveFunction objFct = new ArrayLinearObjectiveFunction2D(new double[][]{
                new double[]{1.0, -1.0},
                new double[]{0.0, 0.0}
        }, new double[]{0.0, 0.0});

        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new ArrayConstraint2D(new double[][]{
                new double[]{1.0, 1.0},
                new double[]{1.0, 1.0}
        }, Relationship.LEQ, new double[]{0.0});

        SimpleGoalType goalType = SimpleGoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objFct, constraints, goalType);
            Assert.fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolveNoFeasibleSolutionExample() {
        LinearObjectiveFunction objFct = new ArrayLinearObjectiveFunction2D(new double[][]{
                new double[]{1.0, 2.0},
                new double[]{0.0, 0.0}
        }, new double[]{0.0, 0.0});

        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new ArrayConstraint2D(new double[][]{
                new double[]{1.0, 1.0},
                new double[]{1.0, 1.0}
        }, Relationship.LEQ, new double[]{1.0});

        SimpleGoalType goalType = SimpleGoalType.MINIMIZE;
        SimplexSolver solver = new SimplexSolver();
        try {
            solver.optimize(objFct, constraints, goalType);
            Assert.fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test
    public void testSolveWithEpsilon() {
        LinearObjectiveFunction objFct = new ArrayLinearObjectiveFunction2D(new double[][]{
                new double[]{1.0, 2.0, 3.0},
                new double[]{-3.0, -1.0, 1.0}
        }, new double[]{0.0, 2.0});

        LinearConstraint[] constraints = new LinearConstraint[1];
        constraints[0] = new ArrayConstraint2D(new double[][]{
                new double[]{1.0, 1.0, 1.0},
                new double[]{1.0, 1.0, 1.0}
        }, Relationship.LEQ, new double[]{3.0});

        SimpleGoalType goalType = SimpleGoalType.MINIMIZE;
        double epsilon = 1.0e-4;
        SimplexSolver solver = new SimplexSolver(epsilon, 10);
        PointValuePair solution = solver.optimize(objFct, constraints, goalType);

        Assert.assertEquals(new double[]{0.0, 1.5, 1.5}, solution.getPoint(), epsilon);
        Assert.assertEquals(new double[]{-1.5, -1.5, -3.0}, solution.getValue(), epsilon);
    }
}