package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.PointValuePairChecker;
import org.apache.commons.math3.optimization.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;

/**
 * Test class for PowellOptimizer.
 */
public class PowellOptimizerTest {

    /**
     * Test constructor with default parameters.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{-1, -1});
        assertEquals(-1, optimalSolution.getPoint()[0], 0.001);
        assertEquals(-1, optimalSolution.getPoint()[1], 0.001);
        assertEquals(1.0, optimalSolution.getValue(), 0.001);
    }

    /**
     * Test constructor with user-defined convergence checker and default
     * parameters.
     */
    @Test
    public void testCustomCheckerConstructor() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                double[] p = pvp.getPoint();
                double value = pvp.getValue();
                return (value < 0.01 && Math.abs(p[0]) < 0.01 && Math.abs(p[1]) < 0.01);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0, checker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertNull(optimizer.getAbsoluteThreshold());

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - Math.pow(Math.E, 5);
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{1, 1});
        assertEquals(-1.1, optimalSolution.getPoint()[0], 0.01);
        assertEquals(-1.1, optimalSolution.getPoint()[1], 0.01);
        assertEquals(-0.0001632993171845289, optimalSolution.getValue(), 0.00001);
    }

    /**
     * Test constructor with user-defined convergence checker and custom
     * parameters.
     */
    @Test
    public void testCustomCheckerCustomParametersConstructor() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                double[] p = pvp.getPoint();
                double value = pvp.getValue();
                return (value < 0.01 && Math.abs(p[0]) < 0.01 && Math.abs(p[1]) < 0.01);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001, 0.001, 0.0, checker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.0001, optimizer.getAbsoluteThreshold(), 0.0001);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - Math.pow(Math.E, 5);
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{1, 1});
        assertEquals(-1.1, optimalSolution.getPoint()[0], 0.01);
        assertEquals(-1.1, optimalSolution.getPoint()[1], 0.01);
        assertEquals(-0.0001632993171845289, optimalSolution.getValue(), 0.00001);
    }

    /**
     * Test constructor with custom line search tolerances.
     */
    @Test
    public void testCustomLineSearchTolerancesConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, 0.0001, 0.00001);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - Math.pow(Math.E, 5);
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{1, 1});
        assertEquals(-1.1, optimalSolution.getPoint()[0], 0.01);
        assertEquals(-1.1, optimalSolution.getPoint()[1], 0.01);
        assertEquals(-0.0001632993171845289, optimalSolution.getValue(), 0.00001);
    }

    /**
     * Test optimization with custom convergence checker.
     */
    @Test
    public void testCustomCheckerOptimization() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                double[] p = pvp.getPoint();
                double value = pvp.getValue();
                return (value < 0.01 && Math.abs(p[0]) < 0.01 && Math.abs(p[1]) < 0.01);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0, checker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertNull(optimizer.getAbsoluteThreshold());

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - Math.pow(Math.E, 5);
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{1, 1});
        assertEquals(-1.1, optimalSolution.getPoint()[0], 0.01);
        assertEquals(-1.1, optimalSolution.getPoint()[1], 0.01);
        assertEquals(-0.0001632993171845289, optimalSolution.getValue(), 0.00001);
    }

    /**
     * Test optimization with custom parameters and custom convergence checker.
     */
    @Test
    public void testCustomParametersCustomCheckerOptimization() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                double[] p = pvp.getPoint();
                double value = pvp.getValue();
                return (value < 0.01 && Math.abs(p[0]) < 0.01 && Math.abs(p[1]) < 0.01);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001, 0.001, 0.0, checker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.0001, optimizer.getAbsoluteThreshold(), 0.0001);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - Math.pow(Math.E, 5);
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{1, 1});
        assertEquals(-1.1, optimalSolution.getPoint()[0], 0.01);
        assertEquals(-1.1, optimalSolution.getPoint()[1], 0.01);
        assertEquals(-0.0001632993171845289, optimalSolution.getValue(), 0.00001);
    }

    /**
     * Test optimization with custom line search tolerances and custom
     * convergence checker.
     */
    @Test
    public void testCustomLineSearchTolerancesCustomCheckerOptimization() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                double[] p = pvp.getPoint();
                double value = pvp.getValue();
                return (value < 0.01 && Math.abs(p[0]) < 0.01 && Math.abs(p[1]) < 0.01);
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001, 0.00001, 0.0, checker);
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(0.00001, optimizer.getAbsoluteThreshold(), 0.00001);

        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] * x[0] + x[1] * x[1], 2) - Math.pow(Math.E, 5);
            }
        };

        PointValuePair optimalSolution = optimizer.optimize(function, new double[]{1, 1});
        assertEquals(-1.1, optimalSolution.getPoint()[0], 0.01);
        assertEquals(-1.1, optimalSolution.getPoint()[1], 0.01);
        assertEquals(-0.0001632993171845289, optimalSolution.getValue(), 0.00001);
    }

    /**
     * Test optimization with default convergence checker and custom
     * multivariate function that always returns 0.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testCustomFunctionDefaultCheckerOptimization() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        };

        optimizer.optimize(function, new double[]{0, 0});
    }

    /**
     * Test optimization with custom convergence checker and custom
     * multivariate function that always returns 0.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testCustomCheckerCustomFunctionOptimization() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                double[] p = pvp.getPoint();
                double value = pvp.getValue();
                return false;
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0, checker);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                return 0.0;
            }
        };

        optimizer.optimize(function, new double[]{0, 0});
    }

    /**
     * Test optimization with custom convergence checker and custom
     * multivariate function that returns an exception.
     */
    @Test(expected = RuntimeException.class)
    public void testCustomCheckerCustomFunctionThrowsExceptionOptimization() {
        PointValuePairChecker checker = new PointValuePairChecker() {
            @Override
            public boolean check(PointValuePair pvp, double tolerance) {
                throw new RuntimeException("Test");
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0, checker);
        MultivariateFunction function = new MultivariateFunction() {
            @Override
            public double value(double[] x) {
                throw new RuntimeException("Test");
            }
        };

        optimizer.optimize(function, new double[]{0, 0});
    }
}