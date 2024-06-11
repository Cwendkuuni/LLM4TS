package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci1;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.JacobianFree;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GaussianFitterTest {

    @Test
    public void testFit() throws Exception {
        Double[] expectedResults = {0.1, 1.5, 0.3};
        WeightedObservedPoint[] observations = createObservations();
        GaussianFitter fitter = new GaussianFitter(new Fibonacci1(100));
        Double[] actualResults = fitter.fit(expectedResults);
        assertEquals(expectedResults[0], actualResults[0], 1e-12);
        assertEquals(expectedResults[1], actualResults[1], 1e-12);
        assertEquals(expectedResults[2], actualResults[2], 1e-12);

        ObjectiveFunction objectiveFunction = (x, p) -> Gaussian.Parametric.value(x, p[0], p[1], p[2]);
        InitialGuess initialGuess = InitialGuess.IDENTITY;
        UnivariateOptimizer optimizer = new JacobianFree(objectiveFunction, initialGuess, new SimpleBounds(0.0, 10.0), GoalType.MINIMIZE);
        PointValuePair pointValuePair = optimizer.optimize(new MaxEval(100));
        Double[] actualParameters = pointValuePair.getPoint();
        assertEquals(expectedResults[0], actualParameters[0], 1e-12);
        assertEquals(expectedResults[1], actualParameters[1], 1e-12);
        assertEquals(expectedResults[2], actualParameters[2], 1e-12);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitNullObservations() {
        GaussianFitter fitter = new GaussianFitter(new Fibonacci1(100));
        fitter.fit((WeightedObservedPoint[]) null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitTooFewObservations() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[1];
        GaussianFitter fitter = new GaussianFitter(new Fibonacci1(100));
        fitter.fit(observations);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeSigma() throws Exception {
        WeightedObservedPoint[] observations = createObservations();
        GaussianFitter fitter = new GaussianFitter(new Fibonacci1(100));
        Double[] initialGuess = {1.0, 1.5, -0.1};
        fitter.fit(initialGuess);
    }

    @Test
    public void testFitEmptyInitialGuess() throws Exception {
        WeightedObservedPoint[] observations = createObservations();
        GaussianFitter fitter = new GaussianFitter(new Fibonacci1(100));
        Double[] initialGuess = {};
        Double[] actualResults = fitter.fit(initialGuess);
        assertEquals(0.0, actualResults[0], 1e-12);
        assertEquals(0.0, actualResults[1], 1e-12);
        assertEquals(0.0, actualResults[2], 1e-12);
    }

    @Test
    public void testGuessParameters() throws Exception {
        WeightedObservedPoint[] observations = createObservations();
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
        Double[] guessedParameters = parameterGuesser.guess();
        assertEquals(3, guessedParameters.length);
        assertEquals(1.0, guessedParameters[0], 1e-12);
        assertEquals(1.5, guessedParameters[1], 1e-12);
        assertEquals(0.3, guessedParameters[2], 1e-12);
    }

    private WeightedObservedPoint[] createObservations() {
        return new WeightedObservedPoint[]{
                new WeightedObservedPoint(2.0, 1.0, 1.0),
                new WeightedObservedPoint(3.5, 2.5, 2.0),
                new WeightedObservedPoint(4.5, 3.0, 3.0),
                new WeightedObservedPoint(5.5, 3.5, 4.0),
                new WeightedObservedPoint(6.5, 4.0, 5.0),
                new WeightedObservedPoint(7.5, 4.5, 6.0),
                new WeightedObservedPoint(8.5, 5.0, 7.0),
                new WeightedObservedPoint(9.5, 5.5, 8.0),
                new WeightedObservedPoint(10.5, 6.0, 9.0)};
    }

    @Test
    public void testInterpolateXAtY() throws Exception {
        WeightedObservedPoint[] observations = createObservations();
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
        Double x = parameterGuesser.interpolateXAtY(observations, 0, 1, 3.5);
        assertEquals(3.5, x, 1e-12);
    }

    @Test(expected = ZeroException.class)
    public void testInterpolateXAtYZeroStep() throws Exception {
        WeightedObservedPoint[] observations = createObservations();
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
        parameterGuesser.interpolateXAtY(observations, 0, 0, 3.5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testInterpolateXAtYOutOfRange() throws Exception {
        WeightedObservedPoint[] observations = createObservations();
        WeightedObservedPoint[] boundsPoints = getInterpolationBoundsPoints(observations, 0, 1, 15.0);
        GaussianFitter.ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
        parameterGuesser.interpolateXAtY(observations, 0, 1, boundsPoints[0].getX());
    }

    private WeightedObservedPoint[] getInterpolationBoundsPoints(WeightedObservedPoint[] observations,
                                                               int startIdx, int idxStep, double y) {
        return new WeightedObservedPoint[]{
                observations[startIdx + idxStep],
                observations[startIdx]};
    }
}