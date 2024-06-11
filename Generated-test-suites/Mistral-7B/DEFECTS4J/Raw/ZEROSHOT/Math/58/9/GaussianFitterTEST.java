package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.points.WeightedObservedPoint;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;

/**
 * Tests for {@link GaussianFitter}.
 */
public class GaussianFitterTest {

    /**
     * Tests {@code GaussianFitter#fit(double[], double[], double[])} method.
     */
    @Test
    public void testFit() {
        double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        DifferentiableMultivariateVectorFunction optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        for (int i = 0; i < dataX.length; ++i) {
            fitter.addObservedPoint(new WeightedObservedPoint(dataX[i], dataY[i], 1.0));
        }

        double[] result = fitter.fit();
        ParametricUnivariateRealFunction fittedFunction = new Gaussian.Parametric();
        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 1e-6);
        assertEquals(dataX[0], result[1], 1e-6);
        assertEquals(1.248457882231815, result[2], 1e-6);

        double[] initialGuess = { 1.0, dataX[0], 1.0 };
        result = fitter.fit(initialGuess);
        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 1e-6);
        assertEquals(dataX[0], result[1], 1e-6);
        assertEquals(1.248457882231815, result[2], 1e-6);

        try {
            fitter.fit(null);
            fail();
        } catch (NullPointerException expected) {
            // Expected
        }

        try {
            fitter.fit(new double[0]);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
            // Expected
        }

        try {
            fitter.fit(new double[] { 1.0, -1.0, 1.0 });
            fail();
        } catch (NotStrictlyPositiveException expected) {
            // Expected
        }
    }

    /**
     * Tests {@code GaussianFitter#fit()} method.
     */
    @Test
    public void testFitWithoutInitialGuess() {
        double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        DifferentiableMultivariateVectorFunction optimizer = new LevenbergMarquardtOptimizer();
        GaussianFitter fitter = new GaussianFitter(optimizer);
        for (int i = 0; i < dataX.length; ++i) {
            fitter.addObservedPoint(new WeightedObservedPoint(dataX[i], dataY[i], 1.0));
        }

        PointValuePair result = fitter.optimize(new GoalType(0), null);
        ParametricUnivariateRealFunction fittedFunction = new Gaussian.Parametric();
        assertEquals(3, result.getPoint().length);
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(dataX[0], result.getPoint()[1], 1e-6);
        assertEquals(1.248457882231815, result.getPoint()[2], 1e-6);

        assertEquals(fittedFunction.value(dataX[0], result.getPoint()), dataY[0], 1e-6);
    }

    /**
     * Tests {@code GaussianFitter#guess()} method.
     */
    @Test
    public void testGuess() {
        double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        WeightedObservedPoint[] data = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; ++i) {
            data[i] = new WeightedObservedPoint(dataX[i], 1.0);
        }

        ParameterGuesser guesser = new ParameterGuesser(data);
        double[] result = guesser.guess();
        assertEquals(3, result.length);
        assertEquals(1.0, result[0], 1e-6);
        assertEquals(dataX[0], result[1], 1e-6);
        assertEquals(1.437253172765765, result[2], 1e-6);
    }
}

class ParameterGuesser {
    // The same code as in the original class
}