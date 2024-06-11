import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTestUtils;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.function.Add;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.function.Power;
import org.apache.commons.math3.optim.nonlinear.function.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.linear.GoalType;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.linear.LussacCircuitExample;
import org.apache.commons.math3.optim.nonlinear.linear.LussacCircuitExampleConstraints;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testSimpleFunction() {
        UnivariateFunction f = new Add(new Power(x, 2), new Power(x, -2));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        PointValuePair solution = optimizer.optimize(f, 0, 5, GoalType.MINIMIZE);
        assertEquals(0, solution.getPoint()[0], 1e-5);
        assertEquals(-1.0 / 3.0, solution.getValue(), 1e-5);
    }

    @Test
    public void testLinearFunction() {
        UnivariateFunction f = new LinearFunction(new double[]{-1.0, 2.0}, 0);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        PointValuePair solution = optimizer.optimize(f, -5, 5, GoalType.MINIMIZE);
        assertEquals(1.0, solution.getPoint()[0], 1e-5);
        assertEquals(0.0, solution.getValue(), 1e-5);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullFunction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        optimizer.optimize(null, 0, 5, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        optimizer.optimize(new Power(x, 2), 0, 5, GoalType.MAXIMIZE);
    }

    @Test
    public void testLinearConstraints() {
        UnivariateFunction f = new Add(new Power(x, 2), new Power(x, -2));
        LinearConstraintSet constraints = new LinearConstraints(
                new double[][]{
                        {1.0, 1.0}, {0.0, 1.0}, {1.0, -1.0}, {0.0, -1.0}
                }, new double[]{0.0, 2.0, -2.0, 0.0});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        PointValuePair solution = optimizer.optimize(f, constraints, 0, 5, GoalType.MINIMIZE);
        assertEquals(0, solution.getPoint()[0], 1e-5);
        assertEquals(-1.0 / 3.0, solution.getValue(), 1e-5);
        assertTrue(SimpleOptimizationTestUtils.areClose(solution.getPoint(), new double[]{0.0}, 1e-5));
        assertTrue(SimpleOptimizationTestUtils.areClose(solution.getValue(), -1.0 / 3.0, 1e-5));
        assertTrue(constraints.isSatisfied(solution.getPoint()));
    }

    @Test
    public void testLussacCircuitExample() {
        LinearObjectiveFunction objective = new LussacCircuitExample().getObjectiveFunction();
        LinearConstraintSet constraints = new LussacCircuitExampleConstraints();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6));
        PointValuePair solution = optimizer.optimize(objective, constraints, null, null, GoalType.MINIMIZE);
        assertEquals(2.0, solution.getPoint()[0], 1e-5);
        assertEquals(11.0, solution.getValue(), 1e-5);
    }

    @Test(expected = ConvergenceException.class)
    public void testNonconvergence() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 3.0, -2.0}, 3);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        PointValuePair solution = optimizer.optimize(f, 0, 5, GoalType.MINIMIZE);
        fail("Should not have found a solution.");
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        UnivariateFunction f = new Add(new Power(x, 2), new Power(x, -2));
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6));
        optimizer.setMaxIterations(1);
        PointValuePair solution = optimizer.optimize(f, 0, 5, GoalType.MINIMIZE);
        fail("Should have thrown SingularMatrixException.");
    }

}