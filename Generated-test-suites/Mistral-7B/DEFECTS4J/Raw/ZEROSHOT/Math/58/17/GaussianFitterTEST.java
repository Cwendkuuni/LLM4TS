package org.apache.commons.math.optimization.fitting.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunction;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunctionGrad;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.noderiv.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.noderiv.BrentOptimizer.BrentMethod;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.stat.Statistics;
import org.junit.Test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

/**
 * Tests the {@link GaussianFitter} class.
 */
public class GaussianFitterTest {

    /**
     * Tests the GaussianFitter class with valid data.
     */
    @Test
    public void testValidData() {
        double[] xData = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976,
            4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071,
            4.08366408 };
        double[] yData = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0,
            3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0,
            620014.0 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[xData.length];
        for (int i = 0; i < xData.length; i++) {
            data[i] = new WeightedObservedPoint(xData[i], yData[i], 1.0);
        }

        // Test fit method with initial guess
        UnivariateOptimizer optimizer = new BrentOptimizer(BrentMethod.BRENT);
        InitialGuess initialGuess = new InitialGuess() {
            public double[] getInitialGuess(int dimension) {
                return new double[] { 10000.0, 4.0, 0.1 };
            }
        };
        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(data);
        double[] result = fitter.fit(initialGuess);
        double[] expected = { 538333.721527387, 3.998462846896753, 0.155235283286197 };
        assertEquals("Wrong result with initial guess", expected, result, 0.00001);

        // Test fit method without initial guess
        fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(data);
        result = fitter.fit();
        assertEquals("Wrong result without initial guess", expected, result, 0.00001);

        // Test fit method with initial guess provided by ParameterGuesser
        fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoints(data);
        result = fitter.fit();
        assertEquals("Wrong result with initial guess provided by ParameterGuesser",
                     expected, result, 0.00001);

        // Test mean and sigma values
        assertEquals("Wrong mean", 3.998462846896753, result[1], 0.00001);
        assertEquals("Wrong sigma", 0.155235283286197, result[2], 0.00001);

        // Test normalized Gaussian function with fit parameters
        ParametricUnivariateRealFunction gaussianFunction = new Gaussian.Parametric();
        double[] parameters = result;
        Double maxY = Statistics.max(yData);
        double norm = maxY / (Erf.erf(3.0) * Math.sqrt(2.0 * Math.PI));
        double mean = result[1];
        double sigma = result[2];
        Double minX = Statistics.min(xData);
        Double maxX = Statistics.max(xData);
        double x = (maxX + minX) / 2.0;
        double y = norm * Math.exp(-Math.pow(x - mean, 2) / (2 * Math.pow(sigma, 2)));
        assertEquals("Wrong y value for x = " + x, yData[0], y, 0.0001);

        // Test gradient method
        DifferentiableUnivariateFunction function = new Gaussian.Parametric(result);
        DifferentiableUnivariateFunctionGrad functionGrad = function.valueDerivative();
        double[] gradient = functionGrad.value(x);
        double[] expectedGradient = { -(2.0 * (x - mean) / (2.0 * Math.pow(sigma, 2))),
                                       -Math.pow(x - mean, 2) / (2.0 * Math.pow(sigma, 4)),
                                       1.0 / (2.0 * Math.sqrt(2.0 * Math.PI) * Math.pow(sigma, 3)) };
        assertArrayEquals("Wrong gradient", expectedGradient, gradient, 0.0001);
    }

    /**
     * Tests the GaussianFitter class with invalid data.
     */
    @Test
    public void testInvalidData() {
        assertThrows(NullPointerException.class, () -> new GaussianFitter(null));
        assertThrows(NullPointerException.class, () -> new WeightedObservedPoint(null, 0.0));
        assertThrows(OutOfRangeException.class, () -> new GaussianFitter(new BrentOptimizer(BrentMethod.BRENT))
                .fit(new double[] { 1.0, 2.0, 3.0 }));
        assertThrows(OutOfRangeException.class, () -> new GaussianFitter(new BrentOptimizer(BrentMethod.BRENT))
                .addObservedPoint(Double.NaN, 0.0));
        assertThrows(OutOfRangeException.class, () -> new GaussianFitter(new BrentOptimizer(BrentMethod.BRENT))
                .addObservedPoint(0.0, Double.NaN));
        assertThrows(OutOfRangeException.class, () -> new GaussianFitter(new BrentOptimizer(BrentMethod.BRENT))
                .addObservedPoint(Double.NEGATIVE_INFINITY, 0.0));
        assertThrows(OutOfRangeException.class, () -> new GaussianFitter(new BrentOptimizer(BrentMethod.BRENT))
                .addObservedPoint(0.0, Double.NEGATIVE_INFINITY));
    }
}