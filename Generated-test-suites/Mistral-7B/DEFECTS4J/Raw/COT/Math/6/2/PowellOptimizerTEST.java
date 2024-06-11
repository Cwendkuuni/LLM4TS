package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.simple.SquaredFunction;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.ValueGoalType;
import org.apache.commons.math3.test.junit.MathTestUtils;
import org.junit.Test;
import org.junit.Assert;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test PowellOptimizer with default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer();
        PointValuePair result = powellOptimizer.optimize(new SquaredFunction(0.1));
        double expected = 0.0;
        Assert.assertArrayEquals(new double[]{expected, expected}, result.getPoint(), 0.0);
        Assert.assertEquals(expected, result.getValue(), 0.0);
    }

    /**
     * Test PowellOptimizer with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6, new MyCustomChecker());
        UnivariateFunction f = new UnivariateFunctionAdapter(x -> x * x * Math.sin(Math.PI * x)) {
            @Override
            public int getDimension() {
                return 1;
            }
        };
        PointValuePair result = powellOptimizer.optimize(f, new ValueGoalType(2.0));
        double expected = 1.1547005383792516;
        Assert.assertEquals(expected, result.getValue(), 0.001);
    }

    /**
     * Test PowellOptimizer with negative thresholds.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeThresholds() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(-1.0, -1.0);
    }

    /**
     * Test PowellOptimizer with extremely small thresholds.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testExtremelySmallThresholds() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-16, 1e-16);
    }

    /**
     * Test PowellOptimizer with null convergence checker.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1.0, 1.0, null);
    }

    /**
     * Test PowellOptimizer with multidimensional objective function.
     */
    @Test
    public void testMultidimensionalObjectiveFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public int getNumberOfVariables() {
                return 2;
            }

            @Override
            public double value(double[] x) {
                double sum = 0.0;
                for (int i = 0; i < x.length; i++) {
                    sum += x[i] * x[i];
                }
                return sum;
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                Arrays.fill(grad, 2.0);
                return grad;
            }
        };
        PointValuePair result = powellOptimizer.optimize(f);
        double[] expected = new double[]{0.0, 0.0};
        MathTestUtils.assertEquals(expected, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Custom Convergence Checker for testing.
     */
    private static class MyCustomChecker
            extends SimpleUnivariateValueChecker {

        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            return Math.abs(current.getValue() - previous.getValue()) < 0.001;
        }

        @Override
        public String getName() {
            return "My Custom Convergence Checker";
        }
    }
}