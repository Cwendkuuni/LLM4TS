package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFitnessFunction;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        UnivariateOptimizer optimizer = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        double[] dataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] dataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

        GaussianFitter fitter = new GaussianFitter(optimizer);
        fitter.addObservedPoint(dataX[0], dataY[0]);
        for (int i = 1; i < dataX.length; i++) {
            fitter.addObservedPoint(dataX[i], dataY[i]);
        }

        double[] initialGuess = {1.0, 4.0, 1.0};
        double[] parameters = fitter.fit(initialGuess);

        ParametricUnivariateFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-8;
        for (int i = 0; i < parameters.length; i++) {
            double expected = i == 0 ? 1000000.0 : dataX[findMaxIndex(dataY)];
            double actual = parameters[i];
            double diff = Math.abs(expected - actual);
            assertTrue("Parameter " + i + " does not match expected value.", diff <= tolerance);
        }
    }

    @Test
    public void testFitWithoutInitialGuess() {
        UnivariateOptimizer optimizer = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        double[] dataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] dataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

        GaussianFitter fitter = new GaussianFitter(optimizer);
        for (double x : dataX) {
            fitter.addObservedPoint(x, dataY[findIndex(dataX, x)]);
        }

        ObjectiveFunction objectiveFunction = new PointValuePairFitnessFunction(
                new Gaussian.Parametric(), new WeightedObservedPoint[0], GoalType.MINIMIZE);

        PointValuePair result = optimizer.optimize(objectiveFunction, null, null, MaxEval.UNLIMITED);
        double[] parameters = result.getPoint();

        ParametricUnivariateFunction gaussian = new Gaussian.Parametric();
        double tolerance = 1e-8;
        for (int i = 0; i < parameters.length; i++) {
            double expected = i == 0 ? 1000000.0 : dataX[findMaxIndex(dataY)];
            double actual = parameters[i];
            double diff = Math.abs(expected - actual);
            assertTrue("Parameter " + i + " does not match expected value.", diff <= tolerance);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullData() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.addObservedPoint(null, 0.0);
    }

    @Test(expected = NullPointerException.class)
    public void testNullObservations() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.setObservedPoints(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.setObservedPoints(Collections.emptyList().toArray(new WeightedObservedPoint[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfParameters() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.addObservedPoint(4.0, 5.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidObservedPoint() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.addObservedPoint(4.0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullOptimizer() {
        GaussianFitter fitter = new GaussianFitter(null);
    }

    @Test
    public void testGetObservedPoints() {
        UnivariateOptimizer optimizer = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        double[] dataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] dataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

        GaussianFitter fitter = new GaussianFitter(optimizer);
        for (double x : dataX) {
            fitter.addObservedPoint(x, dataY[findIndex(dataX, x)]);
        }

        WeightedObservedPoint[] observations = fitter.getObservedPoints();
        assertArrayEquals(dataX.length, observations.length);
        for (int i = 0; i < observations.length; i++) {
            assertEquals(dataX[i], observations[i].getX());
            assertEquals(dataY[i], observations[i].getY());
        }
    }

    @Test
    public void testSetObservedPoints() {
        UnivariateOptimizer optimizer = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        double[] dataX = {4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168, 4.06943698, 4.07525716, 4.08237071, 4.08366408};
        double[] dataY = {531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0, 1447024.0, 717104.0, 620014.0};

        GaussianFitter fitter = new GaussianFitter(optimizer);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[dataX.length];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(dataX[i], dataY[i], 1.0);
            fitter.addObservedPoint(observations[i]);
        }

        fitter.setObservedPoints(observations);

        WeightedObservedPoint[] expectedObservedPoints = fitter.getObservedPoints();
        assertArrayEquals(dataX.length, observations.length);
        for (int i = 0; i < observations.length; i++) {
            assertEquals(dataX[i], expectedObservedPoints[i].getX());
            assertEquals(dataY[i], expectedObservedPoints[i].getY());
        }
    }

    @Test
    public void testGetOptimizer() {
        UnivariateOptimizer optimizer = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        GaussianFitter fitter = new GaussianFitter(optimizer);
        UnivariateOptimizer actualOptimizer = fitter.getOptimizer();

        assertEquals(optimizer, actualOptimizer);
    }

    @Test
    public void testSetOptimizer() {
        UnivariateOptimizer optimizer1 = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        UnivariateOptimizer optimizer2 = new UnivariateOptimizer() {
            @Override
            public PointValuePair optimize(ObjectiveFunction f,
                                            InitialGuess initialGuess,
                                            SimpleBounds bounds,
                                            MaxEval maxEval) throws Exception {
                throw new UnsupportedOperationException();
            }
        };

        GaussianFitter fitter = new GaussianFitter(optimizer1);
        fitter.setOptimizer(optimizer2);

        UnivariateOptimizer actualOptimizer = fitter.getOptimizer();

        assertEquals(optimizer2, actualOptimizer);
    }

    private int findIndex(double[] data, double value) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == value) {
                return i;
            }
        }
        throw new RuntimeException("Value not found.");
    }

    private int findMaxIndex(double[] data) {
        int maxIndex = 0;
        double maxValue = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] > maxValue) {
                maxIndex = i;
                maxValue = data[i];
            }
        }
        return maxIndex;
    }
}