package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.SimpleUnivariateValueChecker;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the PowellOptimizer class.
 *
 * @author John Smith
 */
public class PowellOptimizerTest extends AbstractMathTestCase {
    /**
     * Test object.
     */
    private PowellOptimizer optimizer;

    /**
     * Set up the test.
     *
     * @throws NumberIsTooSmallException if the relative tolerance is too small.
     */
    @Before
    public void setUp() throws NumberIsTooSmallException {
        optimizer = new PowellOptimizer(0.001, 1.0);
    }

    /**
     * Test the PowellOptimizer with a test function.
     *
     * @throws Exception if the optimization fails.
     */
    @Test
    public void testBasicOptimization() throws Exception {
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 2);
            }
        };
        PointValuePair result = optimizer.optimize(new SimpleBounds(0.0, 4.0), function, null);
        assertEquals(1.9615242270705335, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{2.0}, result.getPoint(), 1e-5);
    }

    /**
     * Test the PowellOptimizer with a test function and a custom convergence checker.
     *
     * @throws Exception if the optimization fails.
     */
    @Test
    public void testCustomConvergenceChecker() throws Exception {
        class MyConvergenceChecker extends SimpleUnivariateValueChecker {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue() < 1e-6;
            }
        }
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 2) + Math.sin(Math.PI * x);
            }
        };
        PowellOptimizer optimizerWithChecker = new PowellOptimizer(0.001, 1.0, new MyConvergenceChecker());
        PointValuePair result = optimizerWithChecker.optimize(new SimpleBounds(0.0, 4.0), function, null);
        assertEquals(1.2246592416241646, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{2.0}, result.getPoint(), 1e-5);
    }

    /**
     * Test the PowellOptimizer with a multivariate function.
     *
     * @throws Exception if the optimization fails.
     */
    @Test
    public void testMultivariateOptimization() throws Exception {
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 2, 2) + Math.pow(x[1] - 2, 2) + Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };
        PointValuePair result = optimizer.optimize(new SimpleBounds(new double[]{0.0, 0.0}, new double[]{4.0, 4.0}), function, null);
        assertEquals(7.789269983784922, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{2.0, 2.0}, result.getPoint(), 1e-5);
    }

    /**
     * Test the PowellOptimizer with a multivariate function and a custom convergence checker.
     *
     * @throws Exception if the optimization fails.
     */
    @Test
    public void testMultivariateCustomConvergenceChecker() throws Exception {
        class MyMultivariateConvergenceChecker implements ConvergenceChecker<PointValuePair> {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return (current.getValue() < 1e-6 && previous.getValue() > 1e-6);
            }
        }
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 2, 2) + Math.pow(x[1] - 2, 2) + Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };
        PowellOptimizer optimizerWithChecker = new PowellOptimizer(0.001, 1.0, new MyMultivariateConvergenceChecker());
        PointValuePair result = optimizerWithChecker.optimize(new SimpleBounds(new double[]{0.0, 0.0}, new double[]{4.0, 4.0}), function, null);
        assertEquals(7.789269983784922, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{2.0, 2.0}, result.getPoint(), 1e-5);
    }

    /**
     * Test the PowellOptimizer with a multivariate function and a custom line search.
     *
     * @throws Exception if the optimization fails.
     */
    @Test
    public void testMultivariateCustomLineSearch() throws Exception {
        class MyLineSearch extends BrentOptimizer {
            @Override
            protected BracketFinder getBracketFinder() {
                return null; // Use the default bracket finder.
            }

            @Override
            protected UnivariateOptimizer getLineSearchOptimizer() {
                return new FibonacciSearch(); // Use a custom line search.
            }
        }
        MultivariateFunction function = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(x[0] - 2, 2) + Math.pow(x[1] - 2, 2) + Math.sin(Math.PI * x[0]) * Math.sin(Math.PI * x[1]);
            }
        };
        PowellOptimizer optimizerWithCustomLineSearch = new PowellOptimizer(0.001, 1.0, null, null, new MyLineSearch());
        PointValuePair result = optimizerWithCustomLineSearch.optimize(new SimpleBounds(new double[]{0.0, 0.0}, new double[]{4.0, 4.0}), function, null);
        assertEquals(7.789269983784922, result.getValue(), 1e-5);
        assertArrayEquals(new double[]{2.0, 2.0}, result.getPoint(), 1e-5);
    }
}