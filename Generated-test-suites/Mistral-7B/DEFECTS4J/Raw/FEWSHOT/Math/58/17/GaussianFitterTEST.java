package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math.analysis.function.Gaussian;
import org.apache.commons.math.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math.exception.NullArgumentException;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.ZeroException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.fitting.CurveFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.apache.commons.math.optim.initialization.Guess;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link GaussianFitter} class.
 */
public class GaussianFitterTest {
    private static final double EPSILON = 1e-9;
    private static final double[] TEST_DATA = { 4.0254623, 531026.0,
            4.03128248, 984167.0, 4.03839603, 1887233.0, 4.04421621, 2687152.0,
            4.05132976, 3461228.0, 4.05326982, 3580526.0, 4.05779662, 3439750.0,
            4.0636168, 2877648.0, 4.06943698, 2175960.0, 4.07525716, 1447024.0,
            4.08237071, 717104.0, 4.08366408, 620014.0 };
    private static final double[] INITIAL_GUESS = { 1.0, 4.0, 0.5 };
    private static final double[] EXACT_SOLUTION = { 1.23456, 4.12345, 0.23456 };
    private GaussianFitter fitter;
    private WeightedObservedPoint[] observations;

    @Before
    public void setUp() {
        observations = new WeightedObservedPoint[TEST_DATA.length];
        for (int i = 0; i < TEST_DATA.length; i++) {
            observations[i] = new WeightedObservedPoint(TEST_DATA[i], 1.0);
        }
        fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
    }

    @Test
    public void testFit() {
        double[] result = fitter.fit(INITIAL_GUESS);
        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullData() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitEmptyData() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit(new WeightedObservedPoint[0]);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitInitialGuessNull() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((double[]) null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInitialGuessNegative() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = { -1.0, 4.0, 0.5 };
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitWithEmptyInitialGuess() {
        double[] result = fitter.fit();
        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test
    public void testAddObservedPoint() {
        fitter.addObservedPoint(4.0254623, 531026.0);
        fitter.addObservedPoint(4.03128248, 984167.0);
        double[] result = fitter.fit();
        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testAddNullObservedPoint() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(null, 531026.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testAddObservedPointNullWeight() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoint(4.0254623, null);
    }

    @Test
    public void testFitWithMultipleObservedPoints() {
        for (double data : TEST_DATA) {
            fitter.addObservedPoint(data, 1.0);
        }
        double[] result = fitter.fit();
        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.fit((double[][]) null);
    }

    @Test
    public void testFitWithInitialGuess() {
        double[] initialGuess = { 0.5, 4.0, 0.5 };
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }

    @Test
    public void testFitWithGuessAndData() {
        double[] initialGuess = { 0.5, 4.0, 0.5 };
        for (double data : TEST_DATA) {
            fitter.addObservedPoint(data, 1.0);
        }
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals(EXACT_SOLUTION, result, EPSILON);
    }
}

class LevenbergMarquardtOptimizer implements DifferentiableMultivariateVectorialOptimizer {
    @Override
    public Guess minimize(ParametricUnivariateRealFunction function, double[] initialGuess) {
        // Implement the Levenberg-Marquardt optimization algorithm here.
        return null;
    }
}

class WeightedObservedPointComparator implements Comparator<WeightedObservedPoint> {
    @Override
    public int compare(WeightedObservedPoint p1, WeightedObservedPoint p2) {
        if (p1 == null && p2 == null) {
            return 0;
        }
        if (p1 == null) {
            return -1;
        }
        if (p2 == null) {
            return 1;
        }
        if (p1.getX() < p2.getX()) {
            return -1;
        }
        if (p1.getX() > p2.getX()) {
            return 1;
        }
        if (p1.getY() < p2.getY()) {
            return -1;
        }
        if (p1.getY() > p2.getY()) {
            return 1;
        }
        if (p1.getWeight() < p2.getWeight()) {
            return -1;
        }
        if (p1.getWeight() > p2.getWeight()) {
            return 1;
        }
        return 0;
    }
}