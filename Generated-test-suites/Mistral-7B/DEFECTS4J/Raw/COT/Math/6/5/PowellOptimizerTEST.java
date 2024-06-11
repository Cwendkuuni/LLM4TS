package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.test.AbstractTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class PowellOptimizerTest {
    private static final double MIN_RELATIVE_TOLERANCE = 2 * Math.ulp(1d);
    private static final double MIN_ABSOLUTE_TOLERANCE = 1e-6;

    @Parameterized.Parameters(name = "{index}: powellOptimizer({0}, {1})")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {2.0, 1.0},
                {0.01, 1.0},
                {0.001, 1.0},
                {1.0, 1.0},
                {0.5, 0.1},
                {0.05, 0.01},
                {1.0e-4, 1.0e-6}
        });
    }

    private double relTol;
    private double absTol;

    public PowellOptimizerTest(double relTol, double absTol) {
        this.relTol = relTol;
        this.absTol = absTol;
    }

    @Test
    public void testConstructor() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        assertNotNull(powellOptimizer);
    }

    @Test
    public void testOptimize() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        double[] startPoint = {1.0, 2.0};
        double[] expectedResult = {1.5, 1.5};
        PointValuePair result = powellOptimizer.optimize(startPoint);
        assertEquals(expectedResult[0], result.getPoint()[0], 0.001);
        assertEquals(expectedResult[0], result.getValue()[0], 0.001);
        assertEquals(expectedResult[1], result.getValue()[1], 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteTolerance() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, -absTol);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeTolerance() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(MIN_RELATIVE_TOLERANCE - 0.001, absTol);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullConvergenceChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectiveFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol, GoalType.MINIMIZE);
        powellOptimizer.optimize(null);
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol, new MyConvergenceChecker());
        double[] startPoint = {1.0, 2.0};
        PointValuePair result = powellOptimizer.optimize(startPoint);
        // Replace this with the expected result based on your custom convergence checker
        assertEquals(3.0, result.getValue()[0], 0.001);
        assertEquals(3.0, result.getValue()[1], 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        powellOptimizer.optimize(new Object[]{}, new UnivariateFunction() {}, GoalType.MAXIMUM);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeWithEmptyStartPoint() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        powellOptimizer.optimize(new Object[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        powellOptimizer.optimize(null);
    }

    @Test
    public void testOptimizeFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - Math.pow(x, 3) + 2;
            }
        };
        double[] startPoint = {1.0};
        PointValuePair result = powellOptimizer.optimize(startPoint, function);
        double expectedResult = -1.0;
        assertEquals(expectedResult, result.getValue()[0], 0.001);
        assertEquals(expectedResult, result.getValue()[1], 0.001);
    }

    @Test
    public void testOptimizeMultiVariateFunction() {
        PowellOptimizer powellOptimizer = new PowellOptimizer(relTol, absTol);
        MultivariateFunction function = new MultivariateFunction2() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) * Math.pow(x[1], 2) - Math.pow(x[0], 3) * Math.pow(x[1], 3) + 2;
            }
        };
        double[] startPoint = {1.0, 1.0};
        PointValuePair result = powellOptimizer.optimize(startPoint, function);
        double[] expectedResult = {1.0, 1.0};
        assertEquals(expectedResult[0], result.getPoint()[0], 0.001);
        assertEquals(expectedResult[0], result.getValue()[0], 0.001);
        assertEquals(expectedResult[1], result.getValue()[1], 0.001);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double currentValue = current.getValue()[0];
            double previousValue = previous.getValue()[0];
            double difference = Math.abs(currentValue - previousValue);
            double convergenceThreshold = Math.max(Math.abs(currentValue), Math.abs(previousValue)) * 0.01;
            return difference < convergenceThreshold;
        }
    }
}

class MultivariateFunction2 implements MultivariateFunction {
    @Override
    public double value(double[] x) {
        // Implement the multivariate function here
    }
}

class AbstractTestCase extends org.apache.commons.math3.test.AbstractTestCase {
}