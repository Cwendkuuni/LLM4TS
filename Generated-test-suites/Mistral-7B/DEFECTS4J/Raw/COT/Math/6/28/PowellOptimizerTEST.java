package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizerDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testPowellOptimizerCustomConstructor() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold);
        Assert.assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.001);
        Assert.assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.001);
    }

    @Test
    public void testPowellOptimizerCustomConstructorWithChecker() {
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;
        ConvergenceChecker<PointValuePair> checker = new CustomConvergenceChecker();
        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, checker);
        Assert.assertNotNull(optimizer);
        Assert.assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.001);
        Assert.assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.001);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testPowellOptimizerMinimization() {
        PowellOptimizer optimizer = new PowellOptimizer();

        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = new RastriginFunction();
        PointValuePair result = optimizer.optimize(startPoint, function, GoalType.MINIMIZE);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
        Assert.assertTrue(Math.abs(function.value(result.getPoint()) - 0.0) < 0.001);
    }

    @Test
    public void testPowellOptimizerMaximization() {
        PowellOptimizer optimizer = new PowellOptimizer();

        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = new RastriginFunction();
        PointValuePair result = optimizer.optimize(startPoint, function, GoalType.MAXIMIZE);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.001);
        Assert.assertTrue(Math.abs(function.value(result.getPoint()) - 10.0) < 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testPowellOptimizerNegativeAbsoluteThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(1.0, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerNegativeRelativeThreshold() {
        PowellOptimizer optimizer = new PowellOptimizer(0.5, 1.0);
    }

    @Test
    public void testLineSearch() {
        double[] startPoint = {1.0, 2.0};
        double[] searchDirection = {-1.0, -1.0};
        LineSearch lineSearch = new PowellOptimizer().new LineSearch();

        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double alpha) {
                double[] x = new double[2];
                for (int i = 0; i < 2; i++) {
                    x[i] = startPoint[i] + alpha * searchDirection[i];
                }
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }
        };

        UnivariatePointValuePair result = lineSearch.search(startPoint, searchDirection);
        double optimum = result.getPoint();
        Assert.assertTrue(optimum > 0.1 && optimum < 1.9);

        double[] newPoint = new double[2];
        for (int i = 0; i < 2; i++) {
            newPoint[i] = startPoint[i] + searchDirection[i] * optimum;
        }
        double newFunctionValue = function.value(newPoint);
        Assert.assertTrue(newFunctionValue < function.value(startPoint));

        PointValuePair pvp = new PointValuePair(newPoint, newFunctionValue);
        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair resultPowell = optimizer.optimize(startPoint, new MultivariateFunction() {
            @Override
            public PointValuePair value(PointValuePair point) {
                return new PointValuePair(point.getPoint(), function.value(point.getPoint()));
            }
        }, pvp.getPoint(), GoalType.MINIMIZE);
        Assert.assertArrayEquals(newPoint, resultPowell.getPoint(), 0.001);
        Assert.assertTrue(Math.abs(function.value(resultPowell.getPoint()) - newFunctionValue) < 0.001);
    }

    private static class RastriginFunction implements MultivariateFunction {
        @Override
        public double value(PointValuePair point) {
            double[] x = point.getPoint();
            int n = x.length;
            double sum = 0.0;
            for (int i = 0; i < n; i++) {
                sum += Math.pow(x[i], 2) - Math.pow(Math.PI * i, 2) + Math.abs(Math.sin(Math.PI * x[i]));
            }
            return sum;
        }

        @Override
        public PointValuePair gradient(PointValuePair point) {
            double[] x = point.getPoint();
            double[] gradient = new double[x.length];
            for (int i = 0; i < x.length; i++) {
                gradient[i] = -2 * x[i] + 2 * Math.PI * i * Math.cos(Math.PI * x[i]) + Math.cos(Math.PI * x[i]);
            }
            return new PointValuePair(point.getPoint(), gradient);
        }
    }

    private static class CustomConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
            double[] p1 = previous.getPoint();
            double[] p2 = current.getPoint();
            double f1 = previous.getValue();
            double f2 = current.getValue();

            return Arrays.stream(p1).parallel().anyMatch(d -> Math.abs(d - p2[Arrays.indexOf(p1, d)]) < 0.001) &&
                    Math.abs(f1 - f2) < 0.001;
        }
    }
}