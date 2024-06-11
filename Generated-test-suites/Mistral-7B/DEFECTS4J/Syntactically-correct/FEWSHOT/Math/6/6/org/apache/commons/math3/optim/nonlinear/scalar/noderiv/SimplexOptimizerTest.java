package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.NoDerivativesException;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleXOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Simplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Simplex.SimplexPoint;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeSimpleExample() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1.0E-6, 1.0E-6));
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) throws NoDerivativesException {
                return FunctionUtils.sqrt(FunctionUtils.sum(x));
            }
        };
        double[] startPoint = {1.0, 1.0};
        GoalType goalType = GoalType.MINIMIZE;
        PointValuePair solution = optimizer.optimize(1000, f, goalType, startPoint);
        assertTrue(solution != null);
        System.out.println("Optimum found: " + FunctionUtils.arrayToString(solution.getPoint()));
        System.out.println("Optimum value: " + solution.getValue());
    }

    @Test(expected = NullArgumentException.class)
    public void testNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.optimize(1000, null, GoalType.MINIMIZE, new double[0]);
    }

    @Test(expected = UnboundedDomainException.class)
    public void testFunctionWithUnboundedDomain() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) throws NoDerivativesException {
                if (x[0] < 0) {
                    return Double.POSITIVE_INFINITY;
                }
                return Math.exp(-Math.pow(x[0], 2.0));
            }
        };
        double[] startPoint = {1.0};
        GoalType goalType = GoalType.MINIMIZE;
        optimizer.optimize(1000, f, goalType, startPoint);
    }

    @Test
    public void testGetPoints() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Simplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        double[][] expectedPoints = new double[][]{{0.0, 0.0}, {1.0, 0.0}, {0.5, 0.5}};
        double[] startPoint = {0.5, 0.5};
        optimizer.optimize(1000, null, GoalType.MINIMIZE, startPoint);
        SimplexPoint[] points = simplex.getPoints();
        assertArrayEquals(expectedPoints[0].length, points.length, "Invalid number of points");
        for (int i = 0; i < points.length; i++) {
            assertArrayEquals(expectedPoints[i], points[i].getPoint(), "Invalid point " + i);
        }
    }

    @Test
    public void testGetWorstPoints() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Simplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        double[][] expectedPoints = new double[][]{{0.0, 0.0}, {1.0, 0.0}, {0.5, 0.5}};
        double[] startPoint = {0.5, 0.5};
        optimizer.optimize(1000, null, GoalType.MINIMIZE, startPoint);
        SimplexPoint[] points = simplex.getPoints();
        SimplexPoint[] worstPoints = simplex.getWorstPoints();
        assertArrayEquals(expectedPoints.length, points.length, "Invalid number of points");
        for (int i = 0; i < worstPoints.length; i++) {
            assertArrayEquals(expectedPoints[2 - i], worstPoints[i].getPoint(), "Invalid worst point " + i);
        }
    }

    @Test
    public void testIterate() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        Simplex simplex = new Simplex();
        optimizer.setSimplex(simplex);
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) throws NoDerivativesException {
                return FunctionUtils.sum(x);
            }
        };
        double[] startPoint = {1.0, 1.0};
        optimizer.optimize(1000, f, GoalType.MINIMIZE, startPoint);
        int iteration = 0;
        while (!optimizer.isOptimized()) {
            optimizer.iterate();
            iteration++;
        }
        double[] optPoint = optimizer.getPoint();
        double optValue = optimizer.getValue();
        System.out.println("Optimum found: " + FunctionUtils.arrayToString(optPoint));
        System.out.println("Optimum value: " + optValue);
        System.out.println("Iterations: " + iteration);
    }
}