package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.special.ErfLog;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class GaussianFitterTest {

    @Parameters(name = "{index}: fit({0}) -> {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{1.0, 2.5, 1.5}, new double[]{1.25, 2.5, 1.2}},
                {new double[]{1.0, 3.0, 2.0}, new double[]{1.5, 3.0, 1.5}},
                {new double[]{1.0, 4.0, 1.0}, new double[]{1.75, 4.0, 1.0}},
                {new double[]{1.0, 5.0, 0.5}, new double[]{2.0, 5.0, 0.5}},
                {new double[]{1.0, 10.0, 2.5}, new double[]{3.5, 10.0, 2.5}},
                {new double[]{1.0, -1.0, 1.5}, new IllegalArgumentException("Negative mean")},
                {new double[]{1.0, 1.0, -1.0}, new IllegalArgumentException("Negative standard deviation")},
                {new double[]{1.0, 1.0, 0.0}, new ZeroException()},
                {new double[]{1.0, null, 1.0}, new NullPointerException("Observed points array cannot be null")},
                {new double[]{null, 1.0, 1.0}, new NullPointerException("Observed points array cannot be null")},
                {new double[]{1.0, 1.0, 1.0}, new NotStrictlyPositiveException("Initial guesses must be strictly positive")},
                {new double[]{1.0, 1.0, 1.0}, new OutOfMemoryError("Insufficient memory to initialize the optimizer")},
                {new double[]{1.0, 1.0, 1.0}, new RuntimeException("Unexpected error occurred")}
        });
    }

    private double[] input;
    private double[] expectedOutput;

    public GaussianFitterTest(double[] input, double[] expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Test
    public void testFit() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(2.5, input[0]);
        for (int i = 1; i < input.length; i++) {
            fitter.addObservedPoint(input[i], input[i]);
        }
        double[] output = fitter.fit(input);
        assertEquals("Incorrect fit results", Arrays.toString(expectedOutput), Arrays.toString(output));
    }

    @Test
    public void testFitInitialGuess() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        UnivariateFunction function = new Gaussian.Parametric();
        double[] initialGuess = new double[]{1.0, 2.5, 1.5};
        double[] result = fitter.fit(initialGuess);
        assertArrayEquals("Incorrect fit results", expectedOutput, result);
        assertEquals("Incorrect function value at input", function.value(2.5, result), input[0], 1e-8);
        assertEquals("Incorrect function derivative at input",
                Arrays.toString(function.gradient(2.5, result)),
                Arrays.toString(fitter.getLastIterate().getGradient()), 1e-8);
    }

    @Test
    public void testFitEmptyObservedPoints() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(IllegalArgumentException.class, () -> fitter.fit());
    }

    @Test
    public void testFitNullObservedPoints() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(NullPointerException.class, () -> fitter.fit(null));
    }

    @Test
    public void testFitWrongNumberOfArguments() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(IllegalArgumentException.class, () -> fitter.fit(new double[]{1.0}));
    }

    @Test
    public void testFitNegativeMean() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(IllegalArgumentException.class, () -> fitter.fit(new double[]{-1.0, 1.0, 1.5}));
    }

    @Test
    public void testFitNegativeStandardDeviation() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(IllegalArgumentException.class, () -> fitter.fit(new double[]{1.0, 1.0, -1.0}));
    }

    @Test
    public void testFitZeroStandardDeviation() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(ZeroException.class, () -> fitter.fit(new double[]{1.0, 1.0, 0.0}));
    }

    @Test
    public void testFitNullInitialGuess() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(NullPointerException.class, () -> fitter.fit(null));
    }

    @Test
    public void testFitInvalidInitialGuess() {
        UnivariariateFunction function = new Gaussian.Parametric();
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        PointValuePair solution = optimizer.optimize(function, new SimpleBounds(0.0, Double.POSITIVE_INFINITY),
                new InitialGuess(new double[]{1.0, 1.0, 1.0}), GoalType.MINIMIZE, new MaxEval(100));
        double[] initialGuess = solution.getPoint();
        assertThrows(NotStrictlyPositiveException.class, () -> fitter.fit(initialGuess));
    }

    @Test
    public void testFitMemoryError() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        double[] input = new double[Integer.MAX_VALUE];
        assertThrows(OutOfMemoryError.class, () -> fitter.fit(input));
    }

    @Test
    public void testFitUnexpectedError() {
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        assertThrows(RuntimeException.class, () -> fitter.fit(new double[]{1.0, 1.0, 1.0}));
    }

    private static double mean(List<Double> list) {
        double sum = 0.0;
        int count = 0;
        for (double value : list) {
            sum += value;
            count++;
        }
        return sum / count;
    }

    private static double stddev(List<Double> list) {
        double meanValue = mean(list);
        double sum = 0.0;
        int count = 0;
        for (double value : list) {
            double diff = value - meanValue;
            sum += diff * diff;
            count++;
        }
        return Math.sqrt(sum / count);
    }

    @Test
    public void testFitVerification() {
        List<Double> inputX = Arrays.asList(2.5, 3.0, 4.0, 5.0, 10.0, -1.0, 1.0, null);
        List<Double> inputY = Arrays.asList(1.25, 1.5, 1.75, 2.0, 3.5, IllegalArgumentException.class,
                1.0, 1.5, 1.0, 1.75, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0,
                1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        List<Double> expectedOutputX = Arrays.asList(1.25, 2.5, 1.2, 3.0, 1.5, 4.0, 1.75, 5.0);
        List<Double> expectedOutputY = Arrays.asList(1.25, 2.5, 1.2, 3.0, 1.5, 4.0, 1.75, 5.0);
        UnivariateOptimizer optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        for (int i = 0; i < inputX.size(); i++) {
            fitter.addObservedPoint(inputX.get(i), inputY.get(i));
        }
        double[] output = fitter.fit();
        assertEquals("Incorrect number of output parameters", 3, output.length);
        assertEquals("Incorrect mean value", expectedOutputX.get(0), output[0], 1e-8);
        assertEquals("Incorrect mean value", expectedOutputX.get(1), output[1], 1e-8);
        assertEquals("Incorrect standard deviation value", expectedOutputX.get(2), output[2], 1e-8);
        UnivariateFunction function = new Gaussian.Parametric();
        for (int i = 0; i < inputX.size(); i++) {
            assertEquals("Incorrect function value at input", expectedOutputY.get(i),
                         function.value(inputX.get(i), output), 1e-8);
        }
    }
}