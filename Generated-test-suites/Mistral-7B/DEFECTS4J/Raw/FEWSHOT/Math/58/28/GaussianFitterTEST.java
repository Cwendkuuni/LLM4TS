package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NotFoundException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.StartPointInitializer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.vector.event.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.vector.linear.RandomSearch;
import org.apache.commons.math3.optim.nonlinear.vector.linear.SimpleVector;
import org.apache.commons.math3.optim.nonlinear.vector.linear.WeightedSumSquaredModel;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descript.DescriptiveStatistics;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GaussianFitterTest {

    @Test
    public void testFit() throws NotFoundException, MaxEvalException, OutOfRangeException, ZeroException,
            NotStrictlyPositiveException {
        // Given
        double[] xValues = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168,
                4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] yValues = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0,
                           1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[xValues.length];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(xValues[i], yValues[i], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new RandomSearch());
        fitter.addObservedPoints(observations);

        // When
        double[] parameters = fitter.fit();

        // Then
        UnivariateFunction gaussianFunction = new Gaussian.Parametric();
        double expectedMean = 4.05132976;
        double expectedStdDev = 0.10148929;

        double[] expectedFunctionValues = new double[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            expectedFunctionValues[i] = gaussianFunction.value(xValues[i], parameters);
        }

        DescriptiveStatistics statistics = new DescriptiveStatistics(expectedFunctionValues);
        double actualMean = statistics.getMean();
        double actualStdDev = new StandardDeviation(expectedFunctionValues).getValue();

        assertEquals("Mean is not as expected.", expectedMean, actualMean, 0.001);
        assertEquals("StdDev is not as expected.", expectedStdDev, actualStdDev, 0.001);

        SimpleVector initialGuess = new SimpleVector(new double[]{1000.0, expectedMean, expectedStdDev});
        OptimizationData data = new OptimizationData(gaussianFunction, initialGuess, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), 100);
        StoppingCondition stoppingCondition = new MaxEval(100);
        Optimizer optimizer = new RandomSearch(new StartPointInitializer(data));
        RealVector optimizedParameters = optimizer.optimize(data);

        List<Double> optimizedParameterList = Arrays.asList(optimizedParameters.toArray());
        assertTrue("Optimized parameters do not match guessed parameters.",
                optimizedParameterList.contains(Double.valueOf(parameters[0])) &&
                optimizedParameterList.contains(Double.valueOf(parameters[1])) &&
                optimizedParameterList.contains(Double.valueOf(parameters[2])));
    }

    @Test
    public void testFitWithInitialGuess() throws NotFoundException, MaxEvalException, OutOfRangeException,
            ZeroException, NotStrictlyPositiveException {
        // Given
        double[] xValues = { 4.0254623, 4.03128248, 4.03839603, 4.04421621, 4.05132976, 4.05326982, 4.05779662, 4.0636168,
                4.06943698, 4.07525716, 4.08237071, 4.08366408 };
        double[] yValues = { 531026.0, 984167.0, 1887233.0, 2687152.0, 3461228.0, 3580526.0, 3439750.0, 2877648.0, 2175960.0,
                           1447024.0, 717104.0, 620014.0 };

        WeightedObservedPoint[] observations = new WeightedObservedPoint[xValues.length];
        for (int i = 0; i < observations.length; i++) {
            observations[i] = new WeightedObservedPoint(xValues[i], yValues[i], 1.0);
        }

        double[] initialGuess = {100.0, 4.05, 0.1};
        GaussianFitter fitter = new GaussianFitter(new RandomSearch());
        fitter.addObservedPoints(observations);

        // When
        double[] parameters = fitter.fit(initialGuess);

        // Then
        UnivariateFunction gaussianFunction = new Gaussian.Parametric();
        double expectedMean = 4.05132976;
        double expectedStdDev = 0.10148929;

        double[] expectedFunctionValues = new double[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            expectedFunctionValues[i] = gaussianFunction.value(xValues[i], parameters);
        }

        DescriptiveStatistics statistics = new DescriptiveStatistics(expectedFunctionValues);
        double actualMean = statistics.getMean();
        double actualStdDev = new StandardDeviation(expectedFunctionValues).getValue();

        assertEquals("Mean is not as expected.", expectedMean, actualMean, 0.001);
        assertEquals("StdDev is not as expected.", expectedStdDev, actualStdDev, 0.001);

        SimpleVector initialGuessVector = new SimpleVector(initialGuess);
        OptimizationData data = new OptimizationData(gaussianFunction, initialGuessVector, new SimpleBounds(0.0, Double.POSITIVE_INFINITY), 100);
        StoppingCondition stoppingCondition = new MaxEval(100);
        Optimizer optimizer = new RandomSearch(new StartPointInitializer(data));
        RealVector optimizedParameters = optimizer.optimize(data);

        List<Double> optimizedParameterList = Arrays.asList(optimizedParameters.toArray());
        assertTrue("Optimized parameters do not match guessed parameters.",
                optimizedParameterList.containsAll(Arrays.asList(initialGuess)));
    }
}