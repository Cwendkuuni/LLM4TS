package org.apache.commons.cli.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer();

        // Test constructor
        assertNotNull(optimizer);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);

        // Test optimization with a simple function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = Double.NEGATIVE_INFINITY;
        double max = Double.POSITIVE_INFINITY;
        double startValue = 0;

        try {
            double optimum = optimizer.optimize(function, goalType, min, max, startValue);
            assertEquals(-1.0, optimum, 0);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations should not be exceeded");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not fail");
        }

        // Test optimization with a more complex function
        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) * Math.exp(-x);
            }
        };
        double min2 = -3.0;
        double max2 = 3.0;
        double startValue2 = 0.0;

        try {
            double optimum2 = optimizer.optimize(function2, goalType, min2, max2, startValue2);
            assertEquals(-1.5708, optimum2, 0.001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations should not be exceeded");
        } catch (FunctionEvaluationException e) {
            fail("Function evaluation should not fail");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeAbsoluteAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeRelativeAccuracy() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(-1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeMaxIterations() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(-1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeMaxEvaluations() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxEvaluations(-1);
    }
}