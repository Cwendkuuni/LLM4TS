package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.analysis.ParametricUnivariateRealFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci11;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NoDerivativesProvider;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimpleBoundsVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.UnivariateFunctionGrad;
import org.apache.commons.math3.optim.points.PointValuePairList;
import org.apache.commons.math3.optim.points.WelchPointGenerator;
import org.apache.commons.math3.stat.distribution.GaussianDistribution;
import org.apache.commons.math3.stat.distribution.UniformDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GaussianFitterTest {

    private static final double ACCURACY = 1e-8;
    private static final double EPSILON = 1e-15;
    private static final int N = 10;
    private static final int MAX_ITERATIONS = 100;

    @Test
    public void testFitWithInitialGuess() {
        double[][] data = new double[][]{
                {3.5, 101.3},
                {3.8, 154.4},
                {4.2, 235.6},
                {4.5, 347.8},
                {4.8, 528.9},
                {5.1, 725.3},
                {5.4, 940.8},
                {5.7, 1186.1},
                {6.0, 1463.2},
                {6.3, 1782.3}
        };

        GaussianFitter fitter = new GaussianFitter(new Fibonacci11());
        double[] initialGuess = new double[]{1.0, 3.5, 0.5};
        double[] result = fitter.fit(initialGuess);

        assertArrayEquals(new double[]{1.0125545, 3.5043908, 0.5068285}, result, ACCURACY);

        assertArrayEquals(new double[]{1.0125545, 3.5043908, 0.5068285},
                fitter.fit(), ACCURACY);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        double[][] data = new double[][]{
                {3.5, 101.3},
                {3.8, 154.4},
                {4.2, 235.6},
                {4.5, 347.8},
                {4.8, 528.9},
                {5.1, 725.3},
                {5.4, 940.8},
                {5.7, 1186.1},
                {6.0, 1463.2},
                {6.3, 1782.3}
        };

        GaussianFitter fitter = new GaussianFitter(new Fibonacci11());

        PointValuePairList pvl = fitter.fit(data, GoalType.MINIMIZE, new MaxEval(MAX_ITERATIONS));
        ObjectiveFunction f = new UnivariateFunctionGrad() {
            @Override
            public double value(double[] x) {
                return Math.pow(new Gaussian(x[0], x[1]).value(x[2]) - getDataValue(0, 0), 2)
                       + Math.pow(new Gaussian(x[0], x[1]).value(x[2]) - getDataValue(0, 1), 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[3];
                double mu = x[0];
                double sigma = x[1];
                double xx = x[2];
                double dmu = 0.001;
                double dsigma = 0.001;

                grad[0] = 2 * (new Gaussian(mu, sigma).value(xx) - getDataValue(0, 0)) *
                          new Gaussian(mu, sigma).gradient(xx)[0];
                grad[1] = 2 * (new Gaussian(mu, sigma).value(xx) - getDataValue(0, 1)) *
                          new Gaussian(mu, sigma).gradient(xx)[1];
                grad[2] = 2 * (new Gaussian(mu, sigma).value(xx) - getDataValue(0, 0)) *
                          new Gaussian(mu, sigma).gradient(xx)[2];

                return grad;
            }
        };
        double[] initialBounds = {0.1, 0.1, 0.1};
        double[] result = NoDerivativesProvider.CONVERGED.getValue();

        double[] x = fitter.optimize(f, new SimpleBounds(initialBounds), new SimpleBoundsVectorValuePair(new SimpleBounds(initialBounds), new SimpleBounds(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY})));
        assertArrayEquals(result, x, EPSILON);

        double[] parameters = new double[]{result[0], result[1], result[2]};
        assertEquals(GaussianDistribution.class, Gaussian.class.getSuperclass());
        ParametricUnivariateRealFunction gaussian = new Gaussian.Parametric();
        double expectedMean = parameters[1];
        double expectedStdDev = parameters[2];
        double mu = gaussian.getParameter(1);
        double sigma = gaussian.getParameter(2);
        double tolerance = 1e-5;
        assertEquals(expectedMean, mu, tolerance);
        assertEquals(expectedStdDev, sigma, tolerance);
    }

    @Test
    public void testWelchInitialGuess() {
        double[][] data = new double[][]{
                {3.5, 101.3},
                {3.8, 154.4},
                {4.2, 235.6},
                {4.5, 347.8},
                {4.8, 528.9},
                {5.1, 725.3},
                {5.4, 940.8},
                {5.7, 1186.1},
                {6.0, 1463.2},
                {6.3, 1782.3}
        };

        GaussianFitter fitter = new GaussianFitter(new Fibonacci11());

        List<Pair<Double, Double>> xyData = new ArrayList<>();
        for (double[] entry : data) {
            xyData.add(new Pair<>(entry[0], entry[1]));
        }

        List<Double> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        for (Pair<Double, Double> pair : xyData) {
            xData.add(pair.getKey());
            yData.add(pair.getValue());
        }

        PointValuePairList pvl = fitter.fit(yData.toArray(), GoalType.MINIMIZE, new MaxEval(MAX_ITERATIONS));
        ObjectiveFunction f = new UnivariateFunctionGrad() {
            @Override
            public double value(double[] x) {
                double mu = x[0];
                double sigma = x[1];
                double sumXi = 0.0;
                double sumXiSq = 0.0;
                double sumXiYi = 0.0;

                for (int i = 0; i < xyData.size(); i++) {
                    double xi = xyData.get(i).getKey();
                    double yi = xyData.get(i).getValue();
                    sumXi += xi;
                    sumXiSq += xi * xi;
                    sumXiYi += xi * yi;
                }

                double sumYi = yData.stream().mapToDouble(d -> d).sum();
                double sumSS = (sumXiSq - Math.pow(sumXi, 2)) / (xyData.size() - 1);
                double ss = sigma * sigma;
                double chiSq = ((sumYi - (mu * sumXi)) * (sumYi - (mu * sumXi))) / (ss * (xyData.size() - 1));

                return chiSq;
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };

        double[] initialBounds = new WelchPointGenerator().generateInitialGuess(data[0][0], data, N);
        double[] result = NoDerivativesProvider.CONVERGED.getValue();

        double[] x = fitter.optimize(f, new SimpleBounds(initialBounds), new SimpleBoundsVectorValuePair(new SimpleBounds(initialBounds), new SimpleBounds(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY})));
        assertArrayEquals(result, x, EPSILON);

        double[] parameters = new double[]{result[0], result[1], result[2]};
        assertEquals(GaussianDistribution.class, Gaussian.class.getSuperclass());
        ParametricUnivariateRealFunction gaussian = new Gaussian.Parametric();
        double expectedMean = parameters[1];
        double expectedStdDev = parameters[2];
        double mu = gaussian.getParameter(1);
        double sigma = gaussian.getParameter(2);
        double tolerance = 1e-5;
        assertEquals(expectedMean, mu, tolerance);
        assertEquals(expectedStdDev, sigma, tolerance);
    }

    private double getDataValue(int i, int j) {
        return new Gaussian(getDataParams()[i][0], getDataParams()[i][1]).value(getDataParams()[i][2]);
    }

    private double[][] getDataParams() {
        double[][] data = new double[][]{
                {3.5, 101.3},
                {3.8, 154.4},
                {4.2, 235.6},
                {4.5, 347.8},
                {4.8, 528.9},
                {5.1, 725.3},
                {5.4, 940.8},
                {5.7, 1186.1},
                {6.0, 1463.2},
                {6.3, 1782.3}
        };
        double[][] params = new double[data.length][3];
        for (int i = 0; i < data.length; i++) {
            params[i] = new double[]{data[i][0], data[i][1], i};
        }
        return params;
    }
}