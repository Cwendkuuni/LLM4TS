import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FreemanShurGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Gradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.JacobianVectorFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NewtonRaphsonLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleLineSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.TransversalityCondition;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GaussianFitterTest {

    @Test
    public void testFitter() {
        Double[][] points = new Double[][]{
                {4.0254623, 531026.0},
                {4.03128248, 984167.0},
                {4.03839603, 1887233.0},
                {4.04421621, 2687152.0},
                {4.05132976, 3461228.0},
                {4.05326982, 3580526.0},
                {4.05779662, 3439750.0},
                {4.0636168, 2877648.0},
                {4.06943698, 2175960.0},
                {4.07525716, 1447024.0},
                {4.08237071, 717104.0},
                {4.08366408, 620014.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i][0], points[i][1], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);
        double[] parameters = fitter.fit();

        assertEquals(3, parameters.length);
        assertEquals(531026.0, parameters[0], 0.001);
        assertEquals(4.03154289, parameters[1], 0.001);
        assertEquals(0.03888772, parameters[2], 0.001);

        ParametricUnivariateRealFunction f = new ParametricUnivariateRealFunction() {
            private final ParametricUnivariateRealFunction g = new Gaussian.Parametric();

            @Override
            public double value(double x, double[] p) {
                return g.value(x, p);
            }

            @Override
            public double[] gradient(double x, double[] p) {
                double[] v = {Double.POSITIVE_INFINITY,
                        Double.POSITIVE_INFINITY,
                        Double.POSITIVE_INFINITY};
                try {
                    v = g.gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };

        MultivariateFunction optimFunction = new MultivariateFunction() {
            @Override
            public PointValuePair value(double[] parameters) {
                JacobianVectorFunction jacobian = new JacobianVectorFunction(f) {
                    @Override
                    public Gradient gradient(double[] p) {
                        return new Gradient() {
                            @Override
                            public double[] values() {
                                double[] grad = new double[3];
                                try {
                                    grad = g.gradient(parameters[0], parameters[1], parameters[2]);
                                } catch (NotStrictlyPositiveException e) {
                                    // Do nothing.
                                }
                                return grad;
                            }
                        };
                    }
                };
                NewtonRaphsonLineSearch lineSearch = new NewtonRaphsonLineSearch(jacobian, new SimpleBounds(0.0, 10.0), 1.0E-6, 1.0E-6);
                TransversalityCondition transversalityCondition = new TransversalityCondition(new SimpleLineSearch(1.0, new MaxEval(f, new InitialGuess(parameters))));
                GoalType goalType = GoalType.MINIMIZE;
                return lineSearch.optimize(f, new InitialGuess(parameters), transversalityCondition, null, goalType, 1000);
            }
        };

        PointValuePair optimResult = optimFunction.value(parameters);
        double[] optimParameters = optimResult.getPoint();
        assertArrayEquals(parameters, optimParameters, 0.001);
    }

    @Test
    public void testFitterWithInitialGuess() {
        Double[][] points = new Double[][]{
                {4.0254623, 531026.0},
                {4.03128248, 984167.0},
                {4.03839603, 1887233.0},
                {4.04421621, 2687152.0},
                {4.05132976, 3461228.0},
                {4.05326982, 3580526.0},
                {4.05779662, 3439750.0},
                {4.0636168, 2877648.0},
                {4.06943698, 2175960.0},
                {4.07525716, 1447024.0},
                {4.08237071, 717104.0},
                {4.08366408, 620014.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i][0], points[i][1], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] initialGuess = new double[]{500000.0, 4.05, 0.05};
        fitter.addObservedPoints(observations);
        fitter.setInitialGuess(initialGuess);
        double[] parameters = fitter.fit();

        assertEquals(3, parameters.length);
        assertEquals(531026.0, parameters[0], 0.001);
        assertEquals(4.03154289, parameters[1], 0.001);
        assertEquals(0.03888772, parameters[2], 0.001);

        assertArrayEquals(initialGuess, parameters, 0.001);
    }

    @Test
    public void testFitterWithWrongNumberOfObservations() {
        Double[][] points = new Double[][]{
                {4.0254623, 531026.0},
                {4.03128248, 984167.0},
                {4.03839603, 1887233.0},
                {4.04421621, 2687152.0},
                {4.05132976, 3461228.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i][0], points[i][1], 1.0);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);

        try {
            fitter.fit();
            Assert.fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("The number of observations is less than 3.", e.getMessage());
        }
    }

    @Test
    public void testFitterWithZeroWeightObservations() {
        Double[][] points = new Double[][]{
                {4.0254623, 531026.0, 0.0},
                {4.03128248, 984167.0, 0.0},
                {4.03839603, 1887233.0, 0.0},
                {4.04421621, 2687152.0, 0.0},
                {4.05132976, 3461228.0, 0.0},
                {4.05326982, 3580526.0, 0.0},
                {4.05779662, 3439750.0, 0.0},
                {4.0636168, 2877648.0, 0.0},
                {4.06943698, 2175960.0, 0.0},
                {4.07525716, 1447024.0, 0.0},
                {4.08237071, 717104.0, 0.0},
                {4.08366408, 620014.0, 0.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i][0], points[i][1], points[i][2]);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);

        try {
            fitter.fit();
            Assert.fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            assertEquals("Some observations have zero weight.", e.getMessage());
        }
    }

    @Test
    public void testFitterWithInvalidObservations() {
        Double[][] points = new Double[][]{
                {4.0254623, 531026.0, 0.0},
                {4.03128248, null, 0.0},
                {4.03839603, 1887233.0, 0.0},
                {4.04421621, 2687152.0, 0.0},
                {4.05132976, 3461228.0, 0.0},
                {4.05326982, 3580526.0, 0.0},
                {4.05779662, 3439750.0, 0.0},
                {4.0636168, 2877648.0, 0.0},
                {4.06943698, null, 0.0},
                {4.07525716, 1447024.0, 0.0},
                {4.08237071, 717104.0, 0.0},
                {4.08366408, 620014.0, 0.0}
        };
        WeightedObservedPoint[] observations = new WeightedObservedPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            observations[i] = new WeightedObservedPoint(points[i][0], points[i][1], points[i][2]);
        }

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);

        try {
            fitter.fit();
            Assert.fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("observation point"));
        }
    }
}