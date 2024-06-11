package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.NLSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.NLSolver.AbstractMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.NLSolution;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;

import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testFit() {
        WeightedObservedPoint[] data = new WeightedObservedPoint[]{
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
            new WeightedObservedPoint(4.08366408, 620014.0, 1.0)
        };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        double[] parameters = fitter.fit(data);

        UnivariateFunction function = new ParametricUnivariateRealFunction() {
            private final ParametricUnivariateRealFunction g = new Gaussian.Parametric();

            public double value(double x, double[] p) {
                double v = Double.POSITIVE_INFINITY;
                try {
                    v = g.value(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }

            public double[] gradient(double x, double[] p) {
                double[] v = { Double.POSITIVE_INFINITY,
                              Double.POSITIVE_INFINITY,
                              Double.POSITIVE_INFINITY };
                try {
                    v = g.gradient(x, p);
                } catch (NotStrictlyPositiveException e) {
                    // Do nothing.
                }
                return v;
            }
        };

        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            public double value(double[] parameters) {
                return getSumOfSquaredErrors(parameters, data);
            }

            public double[] gradient(double[] parameters) {
                double[] gradient = new double[3];
                for (WeightedObservedPoint dataPoint : data) {
                    double y = function.value(dataPoint.getX(), parameters);
                    double error = dataPoint.getY() - y;
                    gradient[0] += error;
                    gradient[1] += error * dataPoint.getX();
                    gradient[2] += error * dataPoint.getX() * dataPoint.getX();
                }
                return gradient;
            }
        };

        NLSolver solver = new NLSolver(AbstractMethod.LEVENBERG_MARQUARDT, 3);
        NLSolution solution = solver.optimize(objectiveFunction, new InitialGuess(parameters));
        assertEquals(3, solution.getIterations());
        assertEquals(GoalType.ABS_REDUCE, solution.getGoalType());
        assertEquals(1.0E-10, solution.getStopValue());
        assertEquals(Precision.SAFE_MIN, solution.getSensitivity(), 1.0E-8);
        assertEquals(PrecisionUtils.DOUBLE_EPSILON, solution.getGradientNorm(), 1.0E-8);

        PointValuePair actualFitted = new PointValuePair(function.value(4.0, solution.getPoint()), solution.getPoint());
        PointValuePair expectedFitted = new PointValuePair(523669.246624131, new double[]{ 21399.292, 4.0318628, 0.3733959 });
        assertEquals(expectedFitted.getValue(), actualFitted.getValue(), 1.0E-9);
        assertArrayEquals(expectedFitted.getPoint(), actualFitted.getPoint(), 1.0E-9);
    }

    private double getSumOfSquaredErrors(double[] parameters,
            WeightedObservedPoint[] data) {
        double sum = 0.0;
        for (WeightedObservedPoint dataPoint : data) {
            double y = function.value(dataPoint.getX(), parameters);
            double error = dataPoint.getY() - y;
            sum += error * error;
        }
        return sum;
    }

    private static UnivariateFunction function = new ParametricUnivariateRealFunction() {
        private final ParametricUnivariateRealFunction g = new Gaussian.Parametric();

        public double value(double x, double[] p) {
            double v = Double.POSITIVE_INFINITY;
            try {
                v = g.value(x, p);
            } catch (NotStrictlyPositiveException e) {
                // Do nothing.
            }
            return v;
        }

        public double[] gradient(double x, double[] p) {
            double[] v = { Double.POSITIVE_INFINITY,
                          Double.POSITIVE_INFINITY,
                          Double.POSITIVE_INFINITY };
            try {
                v = g.gradient(x, p);
            } catch (NotStrictlyPositiveException e) {
                // Do nothing.
            }
            return v;
        }
    };
}