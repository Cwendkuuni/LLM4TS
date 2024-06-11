import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.WeightedObservedPoint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.NonlinearOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.UnivariateObjectiveFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GaussianFitterTest {

    private static final double EPSILON = 1e-6;
    private static final double[] TEST_DATA = {4.0254623, 531026.0, 4.03128248, 984167.0, 4.03839603, 1887233.0, 4.04421621, 2687152.0, 4.05132976, 3461228.0, 4.05326982, 3580526.0, 4.05779662, 3439750.0, 4.0636168, 2877648.0, 4.06943698, 2175960.0, 4.07525716, 1447024.0, 4.08237071, 717104.0, 4.08366408, 620014.0};
    private static final WeightedObservedPoint[] TEST_POINTS = new WeightedObservedPoint[TEST_DATA.length];
    private static final double[] TEST_PARAMETERS = {1.0, 4.5, 0.2};
    private NonlinearOptimizer optimizer;
    private GaussianFitter fitter;

    @Before
    public void setUp() {
        optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        fitter = new GaussianFitter(optimizer);
        for (int i = 0; i < TEST_DATA.length; i++) {
            TEST_POINTS[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
            fitter.addObservedPoint(TEST_POINTS[i]);
        }
    }

    @Test
    public void testFit() {
        double[] result = fitter.fit(TEST_PARAMETERS);
        Assert.assertArrayEquals(TEST_PARAMETERS, result, EPSILON);
    }

    @Test
    public void testFitInitialGuess() {
        double[] initialGuess = {2.0, 4.0, 0.5};
        double[] result = fitter.fit(initialGuess);
        Assert.assertArrayEquals(TEST_PARAMETERS, result, EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitNullInitialGuess() {
        fitter.fit((double[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitTooFewInitialGuesses() {
        double[] initialGuess = {2.0, 4.0};
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitWithEmptyData() {
        fitter = new GaussianFitter(optimizer);
        Assert.assertArrayEquals(new double[0], fitter.fit(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObservedPointNullPoint() {
        fitter.addObservedPoint(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObservedPointNullWeight() {
        WeightedObservedPoint point = new WeightedObservedPoint(TEST_DATA[0], null);
        fitter.addObservedPoint(point);
    }

    @Test
    public void testAddObservedPoints() {
        WeightedObservedPoint newPoint = new WeightedObservedPoint(5.0, 1.0);
        fitter.addObservedPoint(newPoint);
        Assert.assertEquals(TEST_POINTS.length + 1, fitter.getNumberOfObservedPoints());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNegativeWeight() {
        WeightedObservedPoint point = new WeightedObservedPoint(TEST_DATA[0], -1.0);
        fitter.addObservedPoint(point);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Weight must be non-negative", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithNaNWeight() {
        WeightedObservedPoint point = new WeightedObservedPoint(TEST_DATA[0], Double.NaN);
        fitter.addObservedPoint(point);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Weight must not be NaN", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGuessParameters() {
        WeightedObservedPoint[] points = fitter.getObservations().toArray(new WeightedObservedPoint[0]);
        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        double[] result = guesser.guess();
        Assert.assertArrayEquals(TEST_PARAMETERS, result, EPSILON);
    }

    @Test(expected = NullPointerException.class)
    public void testGuessParametersNull() {
        WeightedObservedPoint[] points = null;
        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        guesser.guess();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessParametersEmpty() {
        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[0]);
        guesser.guess();
    }

    @Test
    public void testFitWithOptimizerNull() {
        try {
            new GaussianFitter(null);
        } catch (Exception e) {
            Assert.assertEquals("Null argument: optimizer", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithOptimizerNotDifferentiable() {
        NonlinearOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.SimpleIterate();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Optimizer must implement the methods 'value' and 'gradient'", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithOptimizerNotMultivariate() {
        NonlinearOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeWeightedSumSquares();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Optimizer must be multivariate", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithObjectiveFunctionNull() {
        NonlinearOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = null;
        GaussianFitter fitter = new GaussianFitter(optimizer, objectiveFunction);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Objective function must not be null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithObjectiveFunctionNotUnivariate() {
        NonlinearOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = new org.apache.commons.math3.optim.nonlinear.scalar.objective.GenericMathProgram();
        GaussianFitter fitter = new GaussianFitter(optimizer, objectiveFunction);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Objective function must be univariate", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitWithObjectiveFunctionNotScalar() {
        NonlinearOptimizer optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.LevenbergMarquardtOptimizer();
        ObjectiveFunction objectiveFunction = new org.apache.commons.math3.optim.nonlinear.vector.LinearConstraintVectorFunction(
                new LinearConstraint(new double[]{0.0}, GoalType.MINIMIZE), new PointValuePair[]{new PointValuePair(new double[]{0.0}, 0.0)});
        GaussianFitter fitter = new GaussianFitter(optimizer, objectiveFunction);
        try {
            fitter.fit();
        } catch (Exception e) {
            Assert.assertEquals("Objective function must be scalar", e.getMessage());
            throw e;
        }
    }

}