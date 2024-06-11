import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private ObjectiveFunction testFunction;

    @Before
    public void setUp() {
        this.optimizer = new MultiDirectional();
        this.testFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 2.0, 2.0) + Math.pow(x[1] - 3.0, 2.0);
            }

            @Override
            public double value(double[] x, double[] gradient) {
                // not used in this test
                return value(x);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
    }

    @Test
    public void testIterateSimplex() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, -5.0, 5.0);
        PointValuePair initialGuess = new PointValuePair(2.0, 3.0);
        optimizer.optimize(testFunction, bounds, initialGuess);
        PointValuePair solution = optimizer.getSolution();
        Assert.assertEquals(Arrays.asList(new double[]{-2.0, -3.0}), Arrays.asList(solution.getPoint()));
        Assert.assertNotEquals(Double.NaN, solution.getValue(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNullComparator() {
        optimizer.iterateSimplex(null);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexWithMaxEvaluationsReached() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, -5.0, 5.0);
        optimizer.setMaxIterations(1);
        optimizer.optimize(testFunction, bounds, new PointValuePair(0.0, 0.0));
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexWithFunctionEvaluationException() throws Exception {
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0, -5.0, 5.0);
        ObjectiveFunction exceptionFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) throws Exception {
                throw new FunctionEvaluationException("Test exception");
            }

            @Override
            public double value(double[] x, double[] gradient) {
                // not used in this test
                return Double.NaN;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        optimizer.optimize(exceptionFunction, bounds, new PointValuePair(0.0, 0.0));
    }

}