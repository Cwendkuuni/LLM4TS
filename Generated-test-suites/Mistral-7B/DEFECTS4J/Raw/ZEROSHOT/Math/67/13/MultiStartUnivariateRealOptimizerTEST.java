package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultistartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.function.UnivariateFunction;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Tests method {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a simple polynomial function.
     */
    @Test
    public void testMultiStartOptimizePolynomialFunction() {
        final UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{0.0, 1.0});

        // Test with 2 starts
        int numStarts = 2;
        double[] starts = new double[]{1.0, 3.0};
        MultistartUnivariateRealOptimizer optimizer = new MultistartUnivariateRealOptimizer(
                new FixedPointUnivariateOptimizer(1e-7), numStarts, new UniformRandomGenerator());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);

        double expectedMin = StatUtils.min(starts);
        double optimalValue = result.getPoint();

        Assert.assertArrayEquals(new double[]{expectedMin, optimalValue}, new double[]{result.getValue(), optimalValue}, 0.0);

        // Test with 5 starts
        numStarts = 5;
        starts = new double[]{1.0, 3.0, 5.0, 7.0, 9.0};
        optimizer = new MultistartUnivariateRealOptimizer(
                new FixedPointUnivariateOptimizer(1e-7), numStarts, new UniformRandomGenerator());
        result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);

        double[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(starts, Arrays.copyOfRange(optima, 0, numStarts), 0.0);
        double[] optimaValues = optimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[]{StatUtils.min(starts), result.getValue()}, Arrays.copyOfRange(optimaValues, 0, numStarts + 1), 0.0);
    }

    /**
     * Tests method {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a complex function.
     */
    @Test
    public void testMultiStartOptimizeComplexFunction() {
        final UnivariateFunction f = new RastriginFunction();

        // Test with 3 starts
        int numStarts = 3;
        MultistartUnivariateRealOptimizer optimizer = new MultistartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(new NelderMeadSimplexSearch.DefaultMaxEval(1000)), numStarts, new UniformRandomGenerator());
        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        double minValue = StatUtils.min(new double[]{result.getValue(), result.getPoint()});
        double optimalValue = result.getValue();

        Assert.assertEquals(Math.abs(minValue - optimalValue), 0.0, 1e-6);

        // Test with 10 starts
        numStarts = 10;
        optimizer = new MultistartUnivariateRealOptimizer(
                new NelderMeadSimplexOptimizer(new NelderMeadSimplexSearch.DefaultMaxEval(1000)), numStarts, new UniformRandomGenerator());
        result = optimizer.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);

        double[] optima = optimizer.getOptima();
        Assert.assertArrayEquals(new double[numStarts], Arrays.copyOfRange(optima, 0, numStarts), 0.0);
        double[] optimaValues = optimizer.getOptimaValues();
        Assert.assertArrayEquals(new double[]{StatUtils.min(Arrays.copyOfRange(optima, 0, numStarts)), result.getValue()}, Arrays.copyOfRange(optimaValues, 0, numStarts + 1), 0.0);
    }
}

class RastriginFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        double sum = 0.0;
        for (double i = -10.0; i <= 10.0; i += 0.1) {
            sum += Math.pow(10.0 * Math.PI * Math.sqrt(Math.pow(x + i, 2.0) / 2.0) - Math.sin(2.0 * Math.PI * x - Math.PI / 4.0), 2.0);
        }
        sum += Math.pow(10.0 * Math.PI * Math.sqrt(Math.pow(x, 2.0) / 2.0), 2.0);
        return sum;
    }
}