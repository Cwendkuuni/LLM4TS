package org.apache.commons.math3.optimization.direct; // Import necessary classes

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class PowellOptimizerTest {
    

    private PowellOptimizer optimizer;
    private Double[] initialGuess;
    private UnivariateFunction function;
    private PointValueFunction<Double> pointValueFunction;

    @Before
    public void setup() {
        optimizer = new PowellOptimizer(1e-6, 1e-6);
        initialGuess = new Double[]{1.0, 1.0};
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };
        pointValueFunction = new PointValueFunction<Double>(function, initialGuess);
    }

    @Test
    public void testOptimize() {
        PointValuePair result = optimizer.optimize(pointValueFunction);
        Assert.assertNotNull(result);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteThreshold() {
        new PowellOptimizer(1.0, -1.0, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithRelativeThresholdBelowUlp() {
        new PowellOptimizer(1.0, 1e-8, null);
    }

    @Test
    public void testConstructorWithCustomConvergenceChecker() {
        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-4;
            }
        };
        PowellOptimizer optimizerWithCustomChecker = new PowellOptimizer(1e-6, 1e-6, customChecker);
        PointValuePair result = optimizerWithCustomChecker.optimize(pointValueFunction);
        Assert.assertEquals(0.0, result.getValue(), 1e-6);
    }

    @Test
    public void testOptimizeWithCustomGoalType() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
        PointValuePair result = optimizer.optimize(pointValueFunction);
        Assert.assertEquals(Math.PI * Math.PI, result.getValue(), 1e-6);
    }

    @Test(expected = MaxEval.MaxEvaluationsExceededException.class)
    public void testOptimizeWithMaxEval() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        PointValueFunction<Double> functionWithMaxEval = new PointValueFunction<Double>(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        }, new MaxEval(50));
        optimizer.optimize(functionWithMaxEval);
    }

    @Test
    public void testOptimizeWithInitialGuessOutsideBounds() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        SimpleBounds bounds = new SimpleBounds(-5.0, 5.0);
        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public double[] getInitialPoint(int dimension) {
                return new double[]{-10.0, 10.0};
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6);
        try {
            optimizer.optimize(function, bounds, initialGuess);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Initial guesses out of bounds", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithInvalidInput() {
        UnivariateFunction function = null;
        PointValueFunction<Double> pointValueFunction = new PointValueFunction<Double>(function, null);
        try {
            optimizer.optimize(pointValueFunction);
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertEquals("function", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithInvalidInput2() {
        PointValueFunction<Double> pointValueFunction = new PointValueFunction<Double>(null, initialGuess);
        try {
            optimizer.optimize(pointValueFunction);
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertEquals("pointValueFunction", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithInvalidInput3() {
        PointValueFunction<Double> pointValueFunction = new PointValueFunction<Double>(function, null);
        try {
            optimizer.optimize(pointValueFunction, null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("bounds", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithInvalidInput4() {
        SimpleBounds bounds = new SimpleBounds(0.0, null);
        try {
            optimizer.optimize(function, bounds, initialGuess);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("right endpoint of bounds is null", e.getMessage());
        }
    }

    @Test
    public void testOptimizeWithInvalidInput5() {
        SimpleBounds bounds = new SimpleBounds(null, 0.0);
        try {
            optimizer.optimize(function, bounds, initialGuess);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("left endpoint of bounds is null", e.getMessage());
        }
    }

    
}