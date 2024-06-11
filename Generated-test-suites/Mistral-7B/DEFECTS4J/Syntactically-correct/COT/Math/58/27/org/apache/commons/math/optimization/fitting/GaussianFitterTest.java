import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.*;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.points.Points2D;
import org.apache.commons.math3.optim.fitting.*;
import org.apache.commons.math3.analysis.function.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFitMethodWithInitialGuess() throws OptimizationException, NumberIsTooSmallException {
        double[] initialGuess = {1.0, 4.0, 0.5};
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
                new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
                new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
                new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
                new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
                new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
                new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
                new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
                new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
                new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0)};

        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        double[] result = gaussianFitter.fit(initialGuess);

        assertEquals(3, result.length);
        assertArrayEquals(new double[] {1.02157596, 4.0444376, 0.49809654}, result, 0.001);
    }

    @Test
    public void testFitMethodWithoutInitialGuess() throws OptimizationException, NumberIsTooSmallException {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
                new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
                new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
                new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
                new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
                new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
                new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
                new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
                new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
                new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0)};

        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        double[] result = gaussianFitter.fit();

        assertEquals(3, result.length);
        assertArrayEquals(new double[] {1.02157596, 4.0444376, 0.49809654}, result, 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testFitMethodWithNullObservationPoints() {
        WeightedObservedPoint[] observations = null;
        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        gaussianFitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithEmptyObservationPoints() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[0];
        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        gaussianFitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithInvalidObservationPoint() {
        WeightedObservedPoint observationPoint = new WeightedObservedPoint(Double.NaN, 531026.0, 1.0);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {observationPoint};
        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        gaussianFitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithZeroWeightObservationPoint() {
        WeightedObservedPoint observationPoint = new WeightedObservedPoint(4.0, 0.0, 0.0);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {observationPoint};
        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        gaussianFitter.fit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFitMethodWithNegativeWeightObservationPoint() {
        WeightedObservedPoint observationPoint = new WeightedObservedPoint(4.0, 531026.0, -1.0);
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {observationPoint};
        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        gaussianFitter.fit();
    }

    @Test(expected = OutOfMemoryError.class)
    public void testFitMethodWithExtremelyLargeNumberOfObservationPoints() {
        int numberOfObservationPoints = 1000000;
        WeightedObservedPoint[] observations = new WeightedObservedPoint[numberOfObservationPoints];
        for (int i = 0; i < numberOfObservationPoints; i++) {
            observations[i] = new WeightedObservedPoint(i, i, 1.0);
        }
        GaussianFitter gaussianFitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        gaussianFitter.addObservedPoints(observations);
        gaussianFitter.fit();
    }

    @Test
    public void testGuessMethod() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
                new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
                new WeightedObservedPoint(4.03128248, 984167.0, 1.0),
                new WeightedObservedPoint(4.03839603, 1887233.0, 1.0),
                new WeightedObservedPoint(4.04421621, 2687152.0, 1.0),
                new WeightedObservedPoint(4.05132976, 3461228.0, 1.0),
                new WeightedObservedPoint(4.05326982, 3580526.0, 1.0),
                new WeightedObservedPoint(4.05779662, 3439750.0, 1.0),
                new WeightedObservedPoint(4.0636168, 2877648.0, 1.0),
                new WeightedObservedPoint(4.06943698, 2175960.0, 1.0),
                new WeightedObservedPoint(4.07525716, 1447024.0, 1.0),
                new WeightedObservedPoint(4.08237071, 717104.0, 1.0),
                new WeightedObservedPoint(4.08366408, 620014.0, 1.0)};

        ParameterGuesser parameterGuesser = new GaussianFitter.ParameterGuesser(observations);
        double[] result = parameterGuesser.guess();

        assertEquals(3, result.length);
        assertArrayEquals(new double[] {1.02157596, 4.0444376, 0.49809654}, result, 0.001);
    }
}