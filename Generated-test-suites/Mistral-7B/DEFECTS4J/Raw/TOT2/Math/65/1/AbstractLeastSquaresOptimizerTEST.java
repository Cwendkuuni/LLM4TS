package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.function.FunctionUtils;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.StoppingCriteria;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.Gradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentLineSearchPolicy;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer.BrentTerminationCondition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Test {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** Minimal number of iterations allowed. */
    private static final int MIN_ITERATIONS = 5;

    /** Maximal number of iterations allowed. */
    private static final int MAX_ITERATIONS = 10;

    /** Test function. */
    private static final ObjectiveFunction TEST_FUNCTION = new ObjectiveFunction() {
        @Override
        public double value(double[] x) {
            double result = 0;
            for (int i = 0; i < x.length; i++) {
                result += Math.pow(x[i] - i, 2);
            }
            return result;
        }

        @Override
        public Gradient gradient(double[] x) {
            Gradient result = new Gradient(x.length);
            for (int i = 0; i < x.length; i++) {
                result.setEntry(i, 2 * (x[i] - i));
            }
            return result;
        }
    };

    /** Test point. */
    private double[] testPoint;

    /** Test target. */
    private double[] testTarget;

    /** Test weights. */
    private double[] testWeights;

    /** Test optimizer. */
    private AbstractLeastSquaresOptimizer optimizer;

    /** Test bounds. */
    private SimpleBounds bounds;

    /** Test optimizer instance. */
    private BrentOptimizer brentOptimizer;

    /** Test random number generator. */
    private Random random;

    /**
     * Set up.
     * @throws MathIllegalArgumentException if the bounds are wrong
     */
    @Before
    public void setUp()
        throws MathIllegalArgumentException {
        random = new Random();
        testPoint = new double[]{1.0, 2.0, 3.0, 4.0};
        testTarget = new double[]{0.0, 0.0, 0.0, 0.0};
        testWeights = new double[]{1.0, 1.0, 1.0, 1.0};
        optimizer = new MyAbstractLeastSquaresOptimizer();
        bounds = new SimpleBounds(new double[]{-10.0, -10.0, -10.0, -10.0},
                                new double[]{10.0, 10.0, 10.0, 10.0});
        brentOptimizer = new BrentOptimizer(GoalType.MINIMIZE, bounds,
                new BrentLineSearchPolicy(0.1), new BrentTerminationCondition(MIN_ITERATIONS, 0.0001));
    }

    /**
     * Test basic functionality.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     */
    @Test
    public void testBasic()
        throws FunctionEvaluationException, OptimizationException {
        PointValuePair optimizerResult = optimizer.optimize(TEST_FUNCTION, testTarget, testWeights,
                testPoint);
        PointValuePair brentResult = brentOptimizer.optimize(TEST_FUNCTION, testTarget);
        Assert.assertEquals(brentResult.getValue(), optimizerResult.getValue(), 0.0001);
        Assert.assertArrayEquals(brentResult.getPoint(), optimizerResult.getPoint(), 0.0001);
    }

    /**
     * Test when the target and weights have different dimensions.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     * @throws IllegalArgumentException if the target and weights have wrong dimensions
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDifferentDimensions()
        throws FunctionEvaluationException, OptimizationException {
        double[] wrongTarget = new double[]{1.0, 2.0};
        double[] wrongWeights = new double[]{1.0, 2.0, 3.0};
        optimizer.optimize(TEST_FUNCTION, wrongTarget, wrongWeights, testPoint);
    }

    /**
     * Test when the target and weights are null.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     * @throws IllegalArgumentException if the target and weights are null
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullTargetAndWeights()
        throws FunctionEvaluationException, OptimizationException {
        optimizer.optimize(TEST_FUNCTION, null, testWeights, testPoint);
        optimizer.optimize(TEST_FUNCTION, testTarget, null, testPoint);
    }

    /**
     * Test when the start point is outside the bounds.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     * @throws IllegalArgumentException if the start point is outside the bounds
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOutsideBounds()
        throws FunctionEvaluationException, OptimizationException {
        double[] outsideBoundsPoint = new double[]{5.0, 5.0, 5.0, 5.0};
        optimizer.optimize(TEST_FUNCTION, testTarget, testWeights, outsideBoundsPoint);
    }

    /**
     * Test when the optimizer exceeds the maximum number of iterations.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     * @throws MaxIterationsExceededException if the optimizer exceeds the maximal number of iterations
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded()
        throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxIterations(MIN_ITERATIONS);
        optimizer.optimize(TEST_FUNCTION, testTarget, testWeights, testPoint);
    }

    /**
     * Test when the optimizer exceeds the maximum number of evaluations.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     * @throws MaxIterationsExceededException if the optimizer exceeds the maximal number of evaluations
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxEvaluationsExceeded()
        throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxIterations(MIN_ITERATIONS);
        optimizer.setMaxEvaluations(MIN_ITERATIONS);
        optimizer.optimize(TEST_FUNCTION, testTarget, testWeights, testPoint);
    }

    /**
     * Test when the optimizer fails to converge.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws NoRootsException if the optimizer fails to converge
     */
    @Test(expected = NoRootsException.class)
    public void testNoRoots()
        throws FunctionEvaluationException, NoRootsException {
        optimizer.setMaxIterations(MIN_ITERATIONS);
        optimizer.setMaxEvaluations(MIN_ITERATIONS * 2);
        PointValuePair optimizerResult = optimizer.optimize(FunctionUtils.NO_ROOTS, testTarget, testWeights,
                testPoint);
    }

    /**
     * Test when the optimizer cannot evaluate the objective function or
     * its derivative.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     */
    @Test
    public void testCannotEvaluate()
        throws FunctionEvaluationException, OptimizationException {
        ObjectiveFunction cannotEvaluateFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException("Cannot evaluate the objective function");
            }

            @Override
            public Gradient gradient(double[] x) {
                throw new UnsupportedOperationException("Cannot evaluate the objective function gradient");
            }
        };
        optimizer.setMaxIterations(MIN_ITERATIONS);
        optimizer.setMaxEvaluations(MIN_ITERATIONS * 2);
        try {
            optimizer.optimize(cannotEvaluateFunction, testTarget, testWeights, testPoint);
            Assert.fail("The optimizer should have failed to evaluate the objective function");
        } catch (FunctionEvaluationException e) {
            // expected
        }
    }

    /**
     * Test when the optimizer fails to converge.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws NoRootsException if the optimizer fails to converge
     */
    @Test
    public void testCannotConverge()
        throws FunctionEvaluationException, NoRootsException {
        ObjectiveFunction cannotConvergeFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                double result = 0;
                for (int i = 0; i < x.length; i++) {
                    result += Math.pow(x[i] - i, 2);
                }
                if (Math.random() < 0.1) {
                    throw new NoRootsException();
                }
                return result;
            }

            @Override
            public Gradient gradient(double[] x) {
                Gradient result = new Gradient(x.length);
                for (int i = 0; i < x.length; i++) {
                    result.setEntry(i, 2 * (x[i] - i));
                }
                return result;
            }
        };
        optimizer.setMaxIterations(MIN_ITERATIONS);
        optimizer.setMaxEvaluations(MIN_ITERATIONS * 2);
        try {
            optimizer.optimize(cannotConvergeFunction, testTarget, testWeights, testPoint);
            Assert.fail("The optimizer should have failed to converge");
        } catch (NoRootsException e) {
            // expected
        }
    }

    /**
     * Test when the optimizer throws an exception during the search.
     * @throws FunctionEvaluationException if the optimizer fails during the search
     * @throws OptimizationException if the optimizer fails to converge
     * @throws IllegalArgumentException if the start point is outside the bounds
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionDuringSearch()
        throws FunctionEvaluationException, OptimizationException {
        ObjectiveFunction exceptionDuringSearchFunction = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                throw new IllegalArgumentException("Exception during the search");
            }

            @Override
            public Gradient gradient(double[] x) {
                throw new UnsupportedOperationException("Cannot evaluate the objective function gradient");
            }
        };
        optimizer.setMaxIterations(MIN_ITERATIONS);
        optimizer.setMaxEvaluations(MIN_ITERATIONS * 2);
        try {
            optimizer.optimize(exceptionDuringSearchFunction, testTarget, testWeights, testPoint);
            Assert.fail("The optimizer should have failed with an exception during the search");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}

class MyAbstractLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {

    @Override
    protected VectorialPointValuePair doOptimize()
        throws FunctionEvaluationException, OptimizationException {
        return brentOptimizer.optimize(function, targetValues);
    }

}