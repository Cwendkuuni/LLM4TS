import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer.BrentOptions;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer.IterationInfo;
import org.apache.commons.math3.optim.point.PointValuePairList;
import org.apache.commons.math3.optim.point.PointValuePairList.PointValuePairListBuilder;
import org.apache.commons.math3.optim.point.VisitMethod;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.GaussianFitter.ParameterGuesser;
import org.apache.commons.math.optimization.fitting.GaussianFitter.ParameterGuesser.ParameterGuesserException;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;

public class GaussianFitterTest {

    /**
     * Test method for {@link GaussianFitter#GaussianFitter(DifferentiableMultivariateVectorialOptimizer)}.
     */
    @Test
    public void testConstructor() {
        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        assertNotNull(fitter);
    }

    /**
     * Test method for {@link GaussianFitter#fit(double[], double[], double[], double[], double[], double[], double[], double[], double[])}.
     */
    @Test
    public void testFit() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.5, 1.0),
            new WeightedObservedPoint(3.5, 15.2, 2.0),
            new WeightedObservedPoint(4.5, 20.3, 3.0),
            new WeightedObservedPoint(5.5, 25.1, 4.0),
            new WeightedObservedPoint(6.5, 29.6, 5.0)
        };

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        double[] result = fitter.fit(points);
        assertEquals(3.0, result[0], 1e-6);
        assertEquals(4.5, result[1], 1e-6);
        assertEquals(1.5, result[2], 1e-6);

        fitter = new GaussianFitter(new BrentMinimizer());
        result = fitter.fit(points, 1.0, 5.0, 0.1, 10.0, 50.0, 0.1, 50.0, 0.1);
        assertEquals(3.01, result[0], 1e-6);
        assertEquals(4.51, result[1], 1e-6);
        assertEquals(1.49, result[2], 1e-6);
    }

    /**
     * Test method for {@link GaussianFitter#fit()}.
     */
    @Test
    public void testFitNoInitialGuess() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.5, 1.0),
            new WeightedObservedPoint(3.5, 15.2, 2.0),
            new WeightedObservedPoint(4.5, 20.3, 3.0),
            new WeightedObservedPoint(5.5, 25.1, 4.0),
            new WeightedObservedPoint(6.5, 29.6, 5.0)
        };

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        double[] result = fitter.fit(points);
        assertEquals(3.0, result[0], 1e-6);
        assertEquals(4.5, result[1], 1e-6);
        assertEquals(1.5, result[2], 1e-6);
    }

    /**
     * Test method for {@link GaussianFitter#ParameterGuesser#guess()}.
     */
    @Test
    public void testGuess() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.5, 1.0),
            new WeightedObservedPoint(3.5, 15.2, 2.0),
            new WeightedObservedPoint(4.5, 20.3, 3.0),
            new WeightedObservedPoint(5.5, 25.1, 4.0),
            new WeightedObservedPoint(6.5, 29.6, 5.0)
        };

        ParameterGuesser guesser = new ParameterGuesser(points);
        double[] result = guesser.guess();

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        double[] fitResult = fitter.fit(points);

        assertEquals(result[0], fitResult[0], 1e-6);
        assertEquals(result[1], fitResult[1], 1e-6);
        assertEquals(result[2], fitResult[2], 1e-6);
    }

    /**
     * Test method for {@link GaussianFitter#ParameterGuesser#guess()} with 2 points.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testGuessWith2Points() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 10.5, 1.0),
            new WeightedObservedPoint(3.5, 15.2, 2.0)
        };

        new ParameterGuesser(points).guess();
    }

    /**
     * Test method for {@link GaussianFitter#fit(ObjectiveFunction, double[], double[], double[], double[], double[], double[], double[], double[])}.
     */
    @Test
    public void testFitWithCustomObjectiveFunction() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 2.5, 1.0),
            new WeightedObservedPoint(3.5, 3.5, 2.0),
            new WeightedObservedPoint(4.5, 4.5, 3.0),
            new WeightedObservedPoint(5.5, 5.5, 4.0),
            new WeightedObservedPoint(6.5, 6.5, 5.0)
        };

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            public double value(double[] parameters, double x) {
                double y = Gaussian.Parametric.value(x, parameters);
                return Math.pow(y - 5.0, 2.0);
            }

            public double[] gradient(double[] parameters, double x) {
                double[] gradient = new double[3];
                double y = Gaussian.Parametric.value(x, parameters);
                gradient[0] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[0]);
                gradient[1] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[1]);
                gradient[2] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[2]);
                return gradient;
            }
        };

        SimpleBounds bounds = new SimpleBounds(new double[] { 0.0, 0.0, 0.0 }, new double[] { 10.0, 10.0, 10.0 });
        double[] initialGuess = new double[] { 1.0, 2.5, 1.5 };

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        double[] result = fitter.fit(objectiveFunction, bounds, initialGuess);
        assertEquals(2.5, result[0], 1e-6);
        assertEquals(2.5, result[1], 1e-6);
        assertEquals(0.5, result[2], 1e-6);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ObjectiveFunction, double[], double[], double[], double[], double[], double[], double[], double[], double[], int)}.
     */
    @Test
    public void testFitWithCustomObjectiveFunctionAndCustomInitialGuess() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 2.5, 1.0),
            new WeightedObservedPoint(3.5, 3.5, 2.0),
            new WeightedObservedPoint(4.5, 4.5, 3.0),
            new WeightedObservedPoint(5.5, 5.5, 4.0),
            new WeightedObservedPoint(6.5, 6.5, 5.0)
        };

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            public double value(double[] parameters, double x) {
                double y = Gaussian.Parametric.value(x, parameters);
                return Math.pow(y - 5.0, 2.0);
            }

            public double[] gradient(double[] parameters, double x) {
                double[] gradient = new double[3];
                double y = Gaussian.Parametric.value(x, parameters);
                gradient[0] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[0]);
                gradient[1] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[1]);
                gradient[2] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[2]);
                return gradient;
            }
        };

        SimpleBounds bounds = new SimpleBounds(new double[] { 0.0, 0.0, 0.0 }, new double[] { 10.0, 10.0, 10.0 });
        double[] initialGuess = new double[] { 1.0, 2.5, 1.5 };

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        double[] result = fitter.fit(objectiveFunction, bounds, initialGuess, 100, GoalType.MINIMIZE, null);
        assertEquals(2.5, result[0], 1e-6);
        assertEquals(2.5, result[1], 1e-6);
        assertEquals(0.5, result[2], 1e-6);
    }

    /**
     * Test method for {@link GaussianFitter#fit(ObjectiveFunction, double[], double[], double[], double[], double[], double[], double[], double[], double[], int, AveragedStartingPointInitializer)}.
     */
    @Test
    public void testFitWithCustomObjectiveFunctionAndCustomInitialGuessAndInitializer() {
        WeightedObservedPoint[] points = new WeightedObservedPoint[] {
            new WeightedObservedPoint(2.5, 2.5, 1.0),
            new WeightedObservedPoint(3.5, 3.5, 2.0),
            new WeightedObservedPoint(4.5, 4.5, 3.0),
            new WeightedObservedPoint(5.5, 5.5, 4.0),
            new WeightedObservedPoint(6.5, 6.5, 5.0)
        };

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            public double value(double[] parameters, double x) {
                double y = Gaussian.Parametric.value(x, parameters);
                return Math.pow(y - 5.0, 2.0);
            }

            public double[] gradient(double[] parameters, double x) {
                double[] gradient = new double[3];
                double y = Gaussian.Parametric.value(x, parameters);
                gradient[0] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[0]);
                gradient[1] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[1]);
                gradient[2] = 2.0 * (y - 5.0) * (Gaussian.Parametric.gradient(x, parameters)[2]);
                return gradient;
            }
        };

        SimpleBounds bounds = new SimpleBounds(new double[] { 0.0, 0.0, 0.0 }, new double[] { 10.0, 10.0, 10.0 });
        double[] initialGuess = new double[] { 1.0, 2.5, 1.5 };

        GaussianFitter fitter = new GaussianFitter(new BrentMinimizer());
        AveragedStartingPointInitializer initializer = new AveragedStartingPointInitializer(10);
        IterationInfo iterationInfo = new IterationInfo();
        fitter.setMaxIterations(100);
        PointValuePairList results = fitter.fit(objectiveFunction, bounds, initialGuess, 10, GoalType.MINIMIZE,
                null, initializer, null, null, null, iterationInfo);

        PointValuePairListBuilder builder = new PointValuePairListBuilder();
        for (WeightedObservedPoint point : points) {
            builder.append(new PointValuePair(point.getX(), point.getY()));
        }

        PointValuePairList testData = builder.build();
        ObjectiveFunction testObjectiveFunction = new ObjectiveFunction() {
            public double value(double[] parameters, double x) {
                double y = Gaussian.Parametric.value(x, parameters);
                return Math.pow(y - testData.get(0).getKey(), 2.0);
            }

            public double[] gradient(double[] parameters, double x) {
                throw new UnsupportedOperationException();
            }
        };

        SimpleBounds testBounds = new SimpleBounds(new double[] { 0.0, 0.0, 0.0 }, new double[] { 10.0, 10.0, 10.0 });
        double[] testInitialGuess = new double[] { 1.0, result[0], 1.5 };

        GaussianFitter testFitter = new GaussianFitter(new BrentMinimizer());
        PointValuePairList testResults = testFitter.fit(testObjectiveFunction, testBounds, testInitialGuess, 10,
                GoalType.MINIMIZE, null);

        double[] testResult = testResults.get(0).getValue();
        assertEquals(result[0], testResult[0], 1e-6);
        assertEquals(result[1], testResult[1], 1e-6);
        assertEquals(result[2], testResult[2], 1e-6);
    }
}