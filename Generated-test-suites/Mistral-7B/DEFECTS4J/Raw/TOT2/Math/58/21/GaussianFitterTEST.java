import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.GaussianCovarianceInitializer;
import org.apache.commons.math3.optim.initializer.SimpleInitialGuess;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.LeastSquaresOptimizer;
import org.apache.commons.math3.optim.points.DoublePointValues;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.optim.points.SimplePoints2D;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        // Test with valid input
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 2.5, 1.0),
            new WeightedObservedPoint(2.0, 4.5, 2.0),
            new WeightedObservedPoint(3.0, 7.5, 3.0),
            new WeightedObservedPoint(4.0, 10.5, 4.0),
            new WeightedObservedPoint(5.0, 14.5, 5.0)
        };
        GaussianFitter fitter = new GaussianFitter(new LeastSquaresOptimizer());
        double[] parameters = fitter.fit(data);
        double[] expectedParameters = new double[] { 5.0, 2.5, 2.0 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);

        // Test with empty data
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        try {
            fitter.fit(new WeightedObservedPoint[0]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null data
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        try {
            fitter.fit((WeightedObservedPoint[]) null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with wrong number of observations
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        WeightedObservedPoint[] dataWrongLength = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 2.5, 1.0),
            new WeightedObservedPoint(2.0, 4.5, 2.0),
            new WeightedObservedPoint(3.0, 7.5, 3.0),
        };
        try {
            fitter.fit(dataWrongLength);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null observations
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        try {
            fitter.fit((WeightedObservedPoint[]) null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with null optimizer
        fitter = new GaussianFitter(null);
        try {
            fitter.fit(data);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test initialization with initial guess
        double[] initialGuess = new double[] { 10.0, 5.0, 2.0 };
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        parameters = fitter.fit(initialGuess);
        Assert.assertArrayEquals(new double[] { 5.0, 2.5, 2.0 }, parameters, 0.001);

        // Test initialization with no initial guess
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        parameters = fitter.fit();
        Assert.assertArrayEquals(new double[] { 5.0, 2.5, 2.0 }, parameters, 0.001);

        // Test initial guess with out of range values
        initialGuess = new double[] { 0.0, 2.5, 2.0 };
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        try {
            fitter.fit(initialGuess);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test initial guess with negative values
        initialGuess = new double[] { -1.0, 2.5, 2.0 };
        fitter = new GaussianFitter(new LeastSquaresOptimizer());
        try {
            fitter.fit(initialGuess);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with wrong number of initial guesses
        initialGuess = new double[] { 10.0, 5.0, 2.0, 4.0 };
        try {
            GaussianFitter fitter2 = new GaussianFitter(new LeastSquaresOptimizer());
            fitter2.fit(initialGuess);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with null initial guess
        try {
            GaussianFitter fitter3 = new GaussianFitter(new LeastSquaresOptimizer());
            fitter3.fit((double[]) null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with optimizer that doesn't implement DifferentiableMultivariateVectorialOptimizer
        ObjectiveFunction func = new ObjectiveFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }

            @Override
            public PointValuePair gradient(double[] x) {
                return new PointValuePair(new double[] { 2 * x[0], 2 * x[1] }, value(x));
            }
        };
        GaussianFitter fitter4 = new GaussianFitter(func);
        try {
            fitter4.fit(data);
            Assert.fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testBasicGuess() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[] {
            new WeightedObservedPoint(1.0, 2.5, 1.0),
            new WeightedObservedPoint(2.0, 4.5, 2.0),
            new WeightedObservedPoint(3.0, 7.5, 3.0),
            new WeightedObservedPoint(4.0, 10.5, 4.0),
            new WeightedObservedPoint(5.0, 14.5, 5.0)
        };
        ParameterGuesser guesser = new ParameterGuesser(data);
        double[] parameters = guesser.guess();
        double[] expectedParameters = new double[] { 5.0, 2.5, 2.0 };
        Assert.assertArrayEquals(expectedParameters, parameters, 0.001);

        WeightedObservedPoint[] emptyData = new WeightedObservedPoint[0];
        try {
            guesser = new ParameterGuesser(emptyData);
            parameters = guesser.guess();
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        WeightedObservedPoint[] dataNull = null;
        try {
            guesser = new ParameterGuesser(dataNull);
            parameters = guesser.guess();
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }
}