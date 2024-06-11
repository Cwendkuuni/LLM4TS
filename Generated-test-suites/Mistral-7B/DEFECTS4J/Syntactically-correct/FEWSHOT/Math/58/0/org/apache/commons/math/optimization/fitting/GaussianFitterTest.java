package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.NonLinearConstraint;
import org.apache.commons.math3.optim.NonLinearPointValuePair;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonLinearOptimizer;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearEqualityConstraint;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearInequalityConstraint;
import org.apache.commons.math3.optim.nonlinear.constraints.LinearRangeConstraint;
import org.apache.commons.math3.optim.nonlinear.local.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.local.NelderMeadSimplexInitialGuess;
import org.apache.commons.math3.optim.nonlinear.local.NelderMeadSimplexMaxEval;
import org.apache.commons.math3.optim.nonlinear.local.NelderMeadSimplexPointValueFunction;

import org.junit.Test;

import org.apache.commons.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.commons.math.optimization.fitting.GaussianFitter.ParameterGuesser;
import org.apache.commons.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    @Test
    public void testFit() throws Exception {
        Double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716 };
        Double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
            addObservedPoint(points[i]);
        }

        Double[] result = new GaussianFitter(new NelderMeadSimplex()).fit(points);
        assertArrayEquals(new Double[] { 7314.63893014523, 4.06357746745337, 0.401451620272978 }, result, 0.001);
    }

    @Test
    public void testFitWithInitialGuess() throws Exception {
        Double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716 };
        Double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
            addObservedPoint(points[i]);
        }

        Double[] initialGuess = { 10000.0, 4.0, 1.0 };
        Double[] result = new GaussianFitter(new NelderMeadSimplex()).fit(points, initialGuess);
        assertArrayEquals(new Double[] { 7314.63893014523, 4.06357746745337, 0.401451620272978 }, result, 0.001);
    }

    @Test
    public void testFitWithEmptyData() {
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        try {
            fitter.fit();
            fail("Expected ZeroException");
        } catch (ZeroException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithNullData() {
        GaussianFitter fitter = new GaussianFitter(new NelderMeadSimplex());
        try {
            fitter.fit((WeightedObservedPoint[]) null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidInitialGuess() throws Exception {
        Double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716 };
        Double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
            addObservedPoint(points[i]);
        }

        Double[] initialGuess = { -1.0, 4.0, 1.0 };
        try {
            new GaussianFitter(new NelderMeadSimplex()).fit(points, initialGuess);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testGuess() throws Exception {
        Double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716 };
        Double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
            addObservedPoint(points[i]);
        }

        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(points);
        Double[] result = guesser.guess();
        assertEquals(3, result.length);
        assertEquals(14659.27169909958, result[0], 0.001);
        assertEquals(4.06358381432399, result[1], 0.001);
        assertEquals(0.401451619726041, result[2], 0.001);
    }

    @Test
    public void testGuessWithEmptyData() {
        ParameterGuesser guesser = new GaussianFitter.ParameterGuesser(new WeightedObservedPoint[0]);
        assertNull(guesser.guess());
    }

    @Test
    public void testGuessWithNullData() {
        try {
            new GaussianFitter.ParameterGuesser((WeightedObservedPoint[]) null);
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected
        }
    }

    private void addObservedPoint(WeightedObservedPoint point) {
        if (observedPoints == null) {
            observedPoints = new WeightedObservedPoint[1];
            observedPoints[0] = point;
        } else {
            Double[] newPoints = new Double[observedPoints.length + 1];
            System.arraycopy(observedPoints, 0, newPoints, 0, observedPoints.length);
            newPoints[observedPoints.length] = point.getX();
            observedPoints = newPoints;
        }
    }

    private Double[] observedPoints;

    @Test
    public void testFitWithLinearConstraints() throws Exception {
        Double[] dataX = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716 };
        Double[] dataY = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] points = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            points[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
            addObservedPoint(points[i]);
        }

        // Constraints
        LinearConstraint constraint1 = new LinearEqualityConstraint(0.0, 0.0, 1.0);
        LinearConstraint constraint2 = new LinearInequalityConstraint(1.0, 0.0, 1.0);
        LinearConstraint constraint3 = new LinearRangeConstraint(0.5, 2.0, 1.0);

        PointValueFunction objectiveFunction = new NelderMeadSimplexPointValueFunction(
            (UnivariateFunction) (x) -> FunctionUtils.value(new Gaussian.Parametric().value(x[0]), x[1], x[2]));
        PointValueFunction constraintFunction1 = new NelderMeadSimplexPointValueFunction(
            (UnivariateFunction) (x) -> constraint1.value(x));
        PointValueFunction constraintFunction2 = new NelderMeadSimplexPointValueFunction(
            (UnivariateFunction) (x) -> constraint2.value(x));
        PointValueFunction constraintFunction3 = new NelderMeadSimplexPointValueFunction(
            (UnivariateFunction) (x) -> constraint3.value(x));

        NonLinearPointValuePair[] constraintsValues = { new NonLinearPointValuePair(constraintFunction1, new Double[] { 0.0 }),
                new NonLinearPointValuePair(constraintFunction2, new Double[] { 0.0 }),
                new NonLinearPointValuePair(constraintFunction3, new Double[] { 0.0 }) };

        InitialGuess initialGuess = new NelderMeadSimplexInitialGuess(3);
        MaxEval maxEval = new NelderMeadSimplexMaxEval(200);

        NonLinearOptimizer optimizer = new NelderMeadSimplex(initialGuess, objectiveFunction, constraintsValues, maxEval);

        Double[] result = new GaussianFitter(optimizer).fit(points);
        assertArrayEquals(new Double[] { 7314.63893014523, 4.06357746745337, 0.401451620272978 }, result, 0.001);
    }
}