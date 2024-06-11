package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.E;

public class BrentOptimizerTest {

    @org.junit.Test
    public void testConstructor() {
        new BrentOptimizer();
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.doOptimize();
    }

    @org.junit.Test
    public void testOptimizeWithMinFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0;
        double max = Math.PI;
        double startValue = 1.5 * Math.PI;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
        assertEquals(-Math.pow(Math.sin(Math.PI / 2), 2), result.getValue(), 1e-6);
    }

    @org.junit.Test
    public void testOptimizeWithMaxFunction() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.cos(x), 2);
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = 0;
        double max = Math.PI;
        double startValue = 1.5 * Math.PI;

        BrentOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);

        assertEquals(Math.PI / 2, result.getPoint(), 1e-6);
        assertEquals(Math.pow(Math.cos(Math.PI / 2), 2), result.getValue(), 1e-6);
    }

    @org.junit.Test
    public void testOptimizeWithFunctionOutsideInterval() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 5;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @org.junit.Test
    public void testOptimizeWithNegativeAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 0;
        double negativeAccuracy = -1e-6;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(negativeAccuracy));
    }

    @org.junit.Test
    public void testOptimizeWithZeroAccuracy() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 0;
        double zeroAccuracy = 0;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setAbsoluteAccuracy(zeroAccuracy));
    }

    @org.junit.Test
    public void testOptimizeWithNegativeStepSize() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 0;
        double negativeStepSize = -1;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.setMaxEvaluations(negativeStepSize));
    }

    @org.junit.Test
    public void testOptimizeWithTooFewIterations() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 0;
        int tooFewIterations = 1;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterationCount(tooFewIterations);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @org.junit.Test
    public void testOptimizeWithNotStrictlyPositiveGoldenSection() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - Math.E;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 0;
        double notStrictlyPositiveGoldenSection = -0.5 * (3 + Math.sqrt(5));

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.GOLDEN_SECTION = notStrictlyPositiveGoldenSection);
    }

    @org.junit.Test
    public void testOptimizeWithFunctionThrowsException() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("FunctionEvaluationException");
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 0;

        BrentOptimizer optimizer = new BrentOptimizer();
        assertThrows(FunctionEvaluationException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }

    @org.junit.Test
    public void testOptimizeWithMaxIterationCountReached() throws Exception {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 1;
        double startValue = 1;
        int maxIterationCount = 10;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterationCount(maxIterationCount);
        PointValuePair result = optimizer.optimize(function, goalType, min, max, startValue);
        assertThrows(MaxIterationsExceededException.class, () -> optimizer.optimize(function, goalType, min, max, startValue));
    }
}