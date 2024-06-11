import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundingMethod;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.apache.commons.math3.optim.MaxEval;
import static org.apache.commons.math3.optim.PointValuePair.pair;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private RealFunction function;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        function = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(Math.PI * x), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
    }

    @Test
    public void testIterateSimplexDefaultConstructor() throws Exception {
        final double[] xLower = new double[]{-10.0};
        final double[] xUpper = new double[]{10.0};
        final SimpleBounds bounds = new SimpleBounds(xLower, xUpper);
        final FibonacciSearch simplexInitial = new FibonacciSearch(function, bounds, 5);
        optimizer.setInitialGuess(simplexInitial.getSolution());

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        optimizer.optimize();

        Assert.assertTrue(optimizer.isOptimized());
        Assert.assertEquals(0.0, optimizer.getOptimumValue(), 1e-9);
        Assert.assertEquals(Math.PI / 2.0, optimizer.getOptimumPoint().getPointRef()[0], 1e-9);
    }

    @Test
    public void testIterateSimplexCustomConstructor() throws Exception {
        final double[] xLower = new double[]{-10.0};
        final double[] xUpper = new double[]{10.0};
        final SimpleBounds bounds = new SimpleBounds(xLower, xUpper);
        final FibonacciSearch simplexInitial = new FibonacciSearch(function, bounds, 5);
        optimizer = new MultiDirectional(2.5, 0.6);
        optimizer.setInitialGuess(simplexInitial.getSolution());

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        optimizer.optimize();

        Assert.assertTrue(optimizer.isOptimized());
        Assert.assertEquals(0.0, optimizer.getOptimumValue(), 1e-9);
        Assert.assertEquals(Math.PI / 2.0, optimizer.getOptimumPoint().getPointRef()[0], 1e-9);
    }

    @Test(expected = OptimizationException.class)
    public void testIterateSimplexMaxEvalReached() throws Exception {
        final double[] xLower = new double[]{-5.0};
        final double[] xUpper = new double[]{5.0};
        final SimpleBounds bounds = new SimpleBounds(xLower, xUpper);
        final int maxEval = 10;
        final FibonacciSearch simplexInitial = new FibonacciSearch(function, bounds, 5);
        optimizer.setInitialGuess(simplexInitial.getSolution());
        optimizer.setMaxEvaluations(maxEval);

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        optimizer.optimize();

        Assert.assertFalse(optimizer.isOptimized());

    }

    @Test(expected = FunctionEvaluationException.class)
    public void testIterateSimplexFunctionEvaluationException() throws Exception {
        final double[] xLower = new double[]{-10.0};
        final double[] xUpper = new double[]{10.0};
        final SimpleBounds bounds = new SimpleBounds(xLower, xUpper);
        final FibonacciSearch simplexInitial = new FibonacciSearch(function, bounds, 5);
        optimizer = new MultiDirectional(2.5, 0.6);
        optimizer.setInitialGuess(simplexInitial.getSolution());
        optimizer.setFunction(new ObjectiveFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Function evaluation exception");
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        });

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        optimizer.optimize();

    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexIllegalArgumentException() {
        optimizer = new MultiDirectional();
        optimizer.iterateSimplex(null);
    }

}