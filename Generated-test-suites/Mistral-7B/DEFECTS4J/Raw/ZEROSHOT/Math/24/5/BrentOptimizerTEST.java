package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.Constants;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBasic() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, 0, 10);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0.7494, result.getPoint(), 1e-5);
        assertEquals(2.4495e-14, result.getValue(), 1e-14);
    }

    @Test
    public void testConvergenceChecker() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x));
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        double rel = 1e-5;
        double abs = 1e-5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                double xDiff = Math.abs(current.getX() - previous.getX());
                double fDiff = Math.abs(current.getValue() - previous.getValue());
                return xDiff < 1e-5 && fDiff < 1e-5;
            }
        });

        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, -Constants.PI, Constants.PI);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(-Constants.PI, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
        assertEquals(optimizer.getIterations(), result.getIterations());
    }

    @Test
    public void testMinimization() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };

        ObjectiveFunctionGradient gf = new ObjectiveFunctionGradient() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[1];
                double sinX = Math.sin(x[0]);
                double cosX = Math.cos(x[0]);
                gradient[0] = -2 * sinX * Math.cos(x[0]) - 2 * cosX * Math.sin(x[0]);
                return gradient;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, 0, 10);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0.7494, result.getPoint(), 1e-5);
        assertEquals(-2.4495e-14, result.getValue(), 1e-14);

        result = optimizer.optimize(gf, 0, 10, GoalType.MINIMIZE);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0.7494, result.getPoint(), 1e-5);
        assertEquals(-2.4495e-14, result.getValue(), 1e-14);
    }

    @Test
    public void testMaximization() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        };

        ObjectiveFunctionGradient gf = new ObjectiveFunctionGradient() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] x) {
                double[] gradient = new double[1];
                double sinX = Math.sin(x[0]);
                double cosX = Math.cos(x[0]);
                gradient[0] = -2 * sinX * Math.cos(x[0]) - 2 * cosX * Math.sin(x[0]);
                return gradient;
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01, null);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, 0, 10);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0.7494, result.getPoint(), 1e-5);
        assertEquals(-2.4495e-14, result.getValue(), 1e-14);

        result = optimizer.optimize(gf, 0, 10, GoalType.MAXIMIZE);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0.7494, result.getPoint(), 1e-5);
        assertEquals(2.4495e-14, result.getValue(), 1e-14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionAbs() {
        new BrentOptimizer(-0.01, 0.01, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionRel() {
        new BrentOptimizer(0.01, -0.01, null);
    }

    @Test
    public void testIterations() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 2 * x + 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, -2, 2);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
        assertEquals(11, optimizer.getIterations());
    }

    @Test
    public void testIterationsNoConvergence() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x) - 0.5);
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, -Constants.PI, Constants.PI);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertArrayEquals(new double[] {-3.141592653589793, 3.141592653589793}, result.getPoint(), 1e-5);
        assertEquals(-0, result.getValue(), 1e-5);
        assertFalse(optimizer.isConverged());
        assertEquals(100, optimizer.getIterations());
    }

    @Test
    public void testIterationsConvergence() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2) - 1;
            }

            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        UnivariateOptimizer.OptimizationResult result = optimizer.optimize(f, 0, 10);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertEquals(0.7494, result.getPoint(), 1e-5);
        assertEquals(-2.4495e-14, result.getValue(), 1e-14);
        assertTrue(optimizer.isConverged());
        assertEquals(11, optimizer.getIterations());
    }
}