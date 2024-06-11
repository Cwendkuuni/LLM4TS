import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.LinearObjectiveFunctionGradient;
import org.apache.commons.math3.optimization.NonNegativeConstraint;
import org.apache.commons.math3.optimization.SimpleTableau;
import org.apache.commons.math3.optimization.linear.GoalType;
import org.apache.commons.math3.optimization.linear.LinearConstraintSet;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private LinearConstraintSet constraints;
    private LinearObjectiveFunctionSet objectiveFunctions;
    private SimplexSolver simplexSolver;

    @Before
    public void setup() {
        double[] a1 = {1.0, 2.0, 3.0};
        double[] b1 = {1.0, -1.0, 1.0};
        LinearConstraint constraint1 = new LinearConstraint(a1, GoalType.LEQ, b1[0]);
        LinearConstraint constraint2 = new LinearConstraint(a1, GoalType.LEQ, b1[1]);
        LinearConstraint constraint3 = new LinearConstraint(a1, GoalType.LEQ, b1[2]);
        LinearConstraint constraint4 = new LinearConstraint(a1, GoalType.EQ, b1[3]);
        LinearConstraint constraint5 = new LinearConstraint(a1, GoalType.GEQ, b1[4]);
        constraints = new LinearConstraintSet();
        constraints.add(constraint1);
        constraints.add(constraint2);
        constraints.add(constraint3);
        constraints.add(constraint4);
        constraints.add(constraint5);

        double[] coefficients = {1.0, 2.0, 3.0};
        double constant = 5.0;
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(coefficients, constant);
        objectiveFunctions = new LinearObjectiveFunctionSet(objectiveFunction);

        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testOptimize() throws Exception {
        PointValuePair solution = simplexSolver.optimize(objectiveFunctions, constraints);
        Assert.assertEquals(1.0 / 7.0, solution.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{3.0 / 7.0, 1.0 / 7.0, 1.0 / 7.0}, solution.getPoint(), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolutionException() throws Exception {
        double[] coefficients = {1.0, 2.0, 3.0};
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(coefficients, Double.NEGATIVE_INFINITY);
        objectiveFunctions = new LinearObjectiveFunctionSet(objectiveFunction);
        simplexSolver.optimize(objectiveFunctions, constraints);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() throws Exception {
        SimplexSolver simplexSolver = new SimplexSolver(1e-12, 10);
        LinearObjectiveFunctionSet objectiveFunctions = new LinearObjectiveFunctionSet(new LinearObjectiveFunctionSet(new LinearObjectiveFunction(new double[]{1.0}, 0.0)));
        LinearConstraintSet constraints = new LinearConstraintSet();
        simplexSolver.optimize(objectiveFunctions, constraints);
    }
}