package org.apache.commons.math.optimization.general.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealArray3D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.NonLinearConstraints;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleLinearObjectiveFunction;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math.optimization.general.LevenbergMarquardtOptimizer;

/**
 * Test for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** Test function */
    private static final double[][] TEST_FUNCTION = new double[][] {
        { 1.0, 1.0, 1.0 },
        { 1.0, 2.0, 2.0 },
        { 2.0, 1.0, 2.0 },
        { 2.0, 2.0, 4.0 }
    };

    /** Test target values */
    private static final double[] TEST_TARGET_VALUES = new double[] { 3.0, 3.0, 3.0, 3.0 };

    /** Test weights */
    private static final double[] TEST_WEIGHTS = new double[] { 1.0, 1.0, 1.0, 1.0 };

    /** Test start point */
    private static final double[] TEST_START_POINT = new double[] { 1.0, 1.0, 1.0 };

    /** Test optimizer */
    private AbstractLeastSquaresOptimizer optimizer;

    /** Test problem dimensions */
    private int testDimensions;

    /** Test function values */
    private double[] testFunctionValues;

    /** Test target values */
    private double[] testTargetValues;

    /** Test weights */
    private double[] testWeights;

    /** Test start point */
    private double[] testStartPoint;

    /** Test point */
    private double[] testPoint;

    /** Test objective value */
    private double testObjectiveValue;

    /** Test jacobian */
    private double[][] testJacobian;

    /** Test covariances */
    private double[][] testCovariances;

    /** Test errors */
    private double[] testErrors;

    /** Test RMS */
    private double testRMS;

    /** Test ChiSquare */
    private double testChiSquare;

    /** Test point after optimization */
    private double[] testPointAfterOptimization;

    /** Test error checking */
    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
        testDimensions = TEST_FUNCTION.length;
        testFunctionValues = new double[testDimensions];
        testTargetValues = TEST_TARGET_VALUES.clone();
        testWeights = TEST_WEIGHTS.clone();
        testStartPoint = TEST_START_POINT.clone();
        optimizer.setStartPoint(testStartPoint);
        optimizer.setFunction(new SimpleLinearObjectiveFunction(TEST_FUNCTION));
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(500);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-12));
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint);
        testPoint = optimizer.getPoint();
        testObjectiveValue = optimizer.getValue();
        testJacobian = optimizer.getJacobian();
        testCovariances = optimizer.getCovariances();
        testErrors = optimizer.guessParametersErrors();
        testRMS = optimizer.getRMS();
        testChiSquare = optimizer.getChiSquare();
        testPointAfterOptimization = testTargetValues.clone();
    }

    /** Test optimizer with correct dimensions */
    @Test
    public void testCorrectDimensions() {
        Assert.assertEquals(testDimensions, testTargetValues.length);
        Assert.assertEquals(testDimensions, testTargetValues.length);
        Assert.assertEquals(testDimensions, testStartPoint.length);
        Assert.assertEquals(testDimensions, testPoint.length);
        Assert.assertEquals(testDimensions, testFunctionValues.length);
        Assert.assertEquals(testDimensions, optimizer.getIterations());
        Assert.assertEquals(testDimensions, optimizer.getEvaluations());
        Assert.assertEquals(testDimensions, optimizer.getJacobianEvaluations());
        Assert.assertArrayEquals(testTargetValues, testPointAfterOptimization, 1e-12);
    }

    /** Test optimizer with incorrect target values dimension */
    @Test(expected = IllegalArgumentException.class)
    public void testTargetValuesDimensionsIncorrect() {
        double[] testTargetValues2 = TEST_TARGET_VALUES.clone();
        testTargetValues2[0] = 3.0;
        optimizer.setTargetValues(testTargetValues2);
        optimizer.optimize(optimizer.getFunction(), testTargetValues2, testWeights, testStartPoint);
    }

    /** Test optimizer with incorrect weights dimension */
    @Test(expected = IllegalArgumentException.class)
    public void testWeightsDimensionsIncorrect() {
        double[] testWeights2 = TEST_WEIGHTS.clone();
        testWeights2[0] = 2.0;
        optimizer.setWeights(testWeights2);
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights2, testStartPoint);
    }

    /** Test optimizer with incorrect start point dimension */
    @Test(expected = IllegalArgumentException.class)
    public void testStartPointDimensionsIncorrect() {
        double[] testStartPoint2 = TEST_START_POINT.clone();
        testStartPoint2[0] = 3.0;
        optimizer.setStartPoint(testStartPoint2);
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint2);
    }

    /** Test optimizer with non-positive number of degrees of freedom */
    @Test(expected = OptimizationException.class)
    public void testNonPositiveDegreesOfFreedom() {
        optimizer.setTargetValues(new double[] { 3.0, 3.0 });
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint);
    }

    /** Test optimizer with incorrect function */
    @Test(expected = IllegalArgumentException.class)
    public void testFunctionIncorrect() {
        optimizer.setFunction(null);
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint);
    }

    /** Test optimizer with incorrect convergence checker */
    @Test(expected = IllegalArgumentException.class)
    public void testConvergenceCheckerIncorrect() {
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint);
    }

    /** Test optimizer with incorrect bounds */
    @Test(expected = MathRuntimeError.class)
    public void testBoundsIncorrect() {
        AbstractLeastSquaresOptimizer optimizer2 = new LevenbergMarquardtOptimizer();
        NonLinearConstraints constraints = new NonLinearConstraints() {

            @Override
            public int getNumberOfConstraints() {
                return 1;
            }

            @Override
            public double[] getLowerBounds() {
                return new double[] { -1.0 };
            }

            @Override
            public double[] getUpperBounds() {
                return new double[] { 1.0 };
            }

            @Override
            public void checkBound(int index, RealVector point) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        optimizer2.setConstraints(constraints);
        optimizer2.setMaxIterations(100);
        optimizer2.setMaxEvaluations(500);
        optimizer2.setFunction(new SimpleLinearObjectiveFunction(TEST_FUNCTION));
        optimizer2.setTargetValues(testTargetValues);
        optimizer2.setWeights(testWeights);
        optimizer2.setStartPoint(testStartPoint);
        optimizer2.setConvergenceChecker(new SimpleVectorialValueChecker(1e-12));
        optimizer2.optimize(optimizer2.getFunction(), testTargetValues, testWeights, testStartPoint);
    }

    /** Test optimizer with incorrect jacobian dimensions */
    @Test(expected = FunctionEvaluationException.class)
    public void testJacobianIncorrect()
            throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxIterations(100);
        optimizer.setMaxEvaluations(500);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-12));
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint);
        optimizer.setFunction(new SimpleLinearObjectiveFunction(new double[][] { TEST_FUNCTION[0],
                TEST_FUNCTION[1], TEST_FUNCTION[2] }));
        optimizer.optimize(optimizer.getFunction(), testTargetValues, testWeights, testStartPoint);
    }

    /** Test covariances */
    @Test
    public void testCovariances() {
        Assert.assertEquals(testDimensions, testCovariances.length);
        Assert.assertEquals(testDimensions, testCovariances[0].length);
        Assert.assertEquals(testDimensions, testCovariances[0].length);
        Assert.assertArrayEquals(testCovariances, testCovariances, 1e-12);
    }

    /** Test errors */
    @Test
    public void testErrors() {
        Assert.assertEquals(testDimensions, testErrors.length);
        Assert.assertArrayEquals(testErrors, testErrors, 1e-12);
    }

    /** Test RMS */
    @Test
    public void testRMS() {
        Assert.assertEquals(testRMS, 1.5 * Math.sqrt(3.0), 1e-12);
    }

    /** Test ChiSquare */
    @Test
    public void testChiSquare() {
        Assert.assertEquals(testChiSquare, 3.0 * (testDimensions - 1.0), 1e-12);
    }

    /** Test jacobian */
    @Test
    public void testJacobian()
            throws FunctionEvaluationException, OptimizationException {
        Assert.assertEquals(testDimensions, testJacobian.length);
        Assert.assertEquals(testDimensions, testJacobian[0].length);
        Assert.assertArrayEquals(testJacobian, testJacobian, 1e-12);
    }

    /** Test point after optimization */
    @Test
    public void testPointAfterOptimization() {
        Assert.assertArrayEquals(testPointAfterOptimization, testTargetValues, 1e-12);
    }

    /** Test objective value */
    @Test
    public void testObjectiveValue() {
        Assert.assertEquals(testObjectiveValue, 0.0, 1e-12);
    }
}