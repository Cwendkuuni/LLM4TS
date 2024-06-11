package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.univariate.UnivariateFunctionAdapter;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.special.Fresnel;

/**
 * Tests for {@link PowellOptimizer} class.
 */
public class PowellOptimizerTest {

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double)}
     * with default line search tolerances.
     */
    @Test
    public void testPowellOptimizer() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Fresnel.sinc(x[0]) * Fresnel.sinc(x[1]);
            }
        };

        PointValuePair result = new PowellOptimizer(1e-6, 1e-6).optimize(f, GoalType.MINIMIZE, new double[]{-2.0, -2.0});
        assertEquals(-Math.PI / 2, result.getPoint()[0], 1e-6);
        assertEquals(-Math.PI / 2, result.getPoint()[1], 1e-6);
        assertEquals(1.22124605e-16, result.getValue(), 1e-16);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double)}
     * with provided line search tolerances.
     */
    @Test
    public void testPowellOptimizerCustomLineSearchTolerances() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }
        };

        PointValuePair result = new PowellOptimizer(1e-6, 1e-5, 1e-6, 1e-5).optimize(f, GoalType.MINIMIZE, new double[]{1.1, 1.1});
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-16);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, ConvergenceChecker)}
     * with provided custom convergence checker.
     */
    @Test
    public void testPowellOptimizerCustomConvergenceChecker() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.abs(x[0] * x[1]) + 1;
            }
        };

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 0.1;
            }
        };

        PointValuePair result = new PowellOptimizer(1e-6, 1e-6, checker).optimize(f, GoalType.MINIMIZE, new double[]{2.0, 2.0});
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link PowellOptimizer#PowellOptimizer(double, double, double, double, ConvergenceChecker)}
     * with provided custom convergence checker and line search tolerances.
     */
    @Test
    public void testPowellOptimizerCustomConvergenceCheckerAndLineSearchTolerances() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }
        };

        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 0.001;
            }
        };

        PointValuePair result = new PowellOptimizer(1e-6, 1e-5, 1e-6, 1e-5, checker).optimize(f, GoalType.MINIMIZE, new double[]{1.1, 1.1});
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-16);
    }

    /**
     * Test method for {@link PowellOptimizer#doOptimize()}
     * using a {@link UnivariateFunctionAdapter}.
     */
    @Test
    public void testPowellOptimizerUnivariateFunctionAdapter() {
        UnivariateFunction f = new UnivariateFunctionAdapter(new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2.0) + Math.pow(x[1] - 1.0, 2.0);
            }

            public int getNumberOfVariables() {
                return 2;
            }
        });

        PointValuePair result = new PowellOptimizer(1e-6, 1e-6).optimize(f, GoalType.MINIMIZE, new double[]{1.1, 1.1});
        assertEquals(1.0, result.getPoint()[0], 1e-6);
        assertEquals(1.0, result.getPoint()[1], 1e-6);
        assertEquals(0.0, result.getValue(), 1e-16);
    }
}