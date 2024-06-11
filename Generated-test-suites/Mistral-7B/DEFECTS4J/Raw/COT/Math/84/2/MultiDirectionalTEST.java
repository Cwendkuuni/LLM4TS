import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnconstrainedMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMead;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.PointValuePair.make;
import static org.apache.commons.math3.optim.SimpleBounds.bound;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalConstructor() {
        MultiDirectional md = new MultiDirectional();
        Assert.assertEquals(2.0, md.khi, 0.01);
        Assert.assertEquals(0.5, md.gamma, 0.01);

        MultiDirectional mdCustom = new MultiDirectional(3.0, 0.7);
        Assert.assertEquals(3.0, mdCustom.khi, 0.01);
        Assert.assertEquals(0.7, mdCustom.gamma, 0.01);
    }

    @Test
    public void testIterateSimplex() throws Exception {
        MultiDirectional md = new MultiDirectional();

        PointValuePair[] simplex = {
            make(new double[]{1.0, 1.0}, 0.0),
            make(new double[]{2.0, 2.0}, 4.0),
            make(new double[]{3.0, 3.0}, 9.0),
            make(new double[]{4.0, 4.0}, 16.0)
        };

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        UnconstrainedMinimizer optimizer = new NelderMead(comparator);
        ObjectiveFunction function = x -> x[0] * x[0] + x[1] * x[1];
        GoalType goalType = GoalType.MINIMIZE;

        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0});

        md.iterateSimplex(comparator);
        PointValuePair bestSolution = md.getBestSolution();
        Assert.assertNotNull(bestSolution);
        optimizer.optimize(function, bounds, goalType);
        PointValuePair optimizerResult = optimizer.getSolution();

        Assert.assertEquals(bestSolution.getValue(), optimizerResult.getValue(), 0.01);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        MultiDirectional md = new MultiDirectional();

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        PointValuePair[] simplex = {
            make(new double[]{1.0, 1.0}, 0.0),
            make(new double[]{2.0, 2.0}, 4.0),
            make(new double[]{3.0, 3.0}, 9.0),
            make(new double[]{4.0, 4.0}, 16.0)
        };

        ObjectiveFunction function = x -> {
            throw new ArithmeticException("Divide by zero");
        };

        md.iterateSimplex(comparator);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizationException() throws Exception {
        MultiDirectional md = new MultiDirectional();

        Comparator<PointValuePair> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());

        PointValuePair[] simplex = {
            make(new double[]{1.0, 1.0}, 0.0),
            make(new double[]{2.0, 2.0}, 4.0),
            make(new double[]{3.0, 3.0}, 9.0),
            make(new double[]{4.0, 4.0}, 16.0)
        };

        ObjectiveFunction function = x -> {
            if (Math.abs(x[0]) > 10.0) {
                throw new IllegalArgumentException("Input out of bounds");
            }
            return x[0] * x[0] + x[1] * x[1];
        };

        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{3.0, 3.0});

        md.iterateSimplex(comparator);
    }
}