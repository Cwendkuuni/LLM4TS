package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleBoundsDomain;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class PowellOptimizerTest {

    @Test
    public void testPowellOptimizer() {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function, new PointValuePair(0.1, 0.1), GoalType.MINIMIZE);

        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testPowellOptimizerCustomChecker() throws NumberIsTooSmallException, NotStrictlyPositiveException {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2) + 0.1 * Math.sin(Math.PI * x[0] * x[1]);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        ConvergenceChecker<PointValuePair> customChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                double error = current.getValue() - previous.getValue();
                return (error < 0.001 && iter > 100);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, customChecker);
        PointValuePair result = optimizer.optimize(function, new PointValuePair(0.1, 0.1), GoalType.MINIMIZE);

        System.out.println("Iterations: " + optimizer.getIterations());

        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testPowellOptimizerCustomBounds() throws NumberIsTooSmallException, NotStrictlyPositiveException {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function, new PointValuePair(1.0, 1.0), GoalType.MINIMIZE,
                new SimpleBoundsDomain(new SimpleBounds(new double[]{0.0, 0.0}, new double[]{1.0, 1.0})));

        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testPowellOptimizerCustomInitialGuess() throws NumberIsTooSmallException, NotStrictlyPositiveException {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        InitialGuess initialGuess = new InitialGuess() {
            @Override
            public PointValuePair getInitialPoint() {
                return new PointValuePair(0.5, 0.5);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function, initialGuess, GoalType.MINIMIZE);

        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }

    @Test
    public void testPowellOptimizerCustomFunction() throws NumberIsTooSmallException, NotStrictlyPositiveException {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + Math.sin(Math.PI * x / 2);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function, new PointValueFunction(0.5), GoalType.MINIMIZE);

        assertEquals(0.0, result.getValue(), 0.001);
        assertEquals(0.5, result.getPoint(), 0.001);
    }

    @Test
    public void testPowellOptimizerCustomCancel() throws InterruptedException {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                throw new RuntimeException();
            }

            @Override
            public int getNumberOfVariables() {
                return 1;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        Thread thread = new Thread(() -> optimizer.optimize(function, new PointValuePair(0.1), GoalType.MINIMIZE));
        thread.start();
        thread.interrupt();
        thread.join();

        assertTrue(optimizer.isOptimizationCancelled());
    }

    @Test
    public void testPowellOptimizerMaxEval() throws NumberIsTooSmallException, NotStrictlyPositiveException {
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0], 2) + Math.pow(x[1], 2);
            }

            @Override
            public int getNumberOfVariables() {
                return 2;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(function, new PointValuePair(1.0, 1.0), GoalType.MINIMIZE,
                new MaxEval(100));

        assertEquals(0.0, result.getValue(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
    }
}