package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.simple.RiddersFunction;
import org.apache.commons.math3.optim.nonlinear.simple.UnivariateTestFunctions;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * {@code rel = 1e-5} and {@code abs = 1e-5}.
     */
    @Test
    public void testBrentOptimizerRelAbs() {
        double rel = 1e-5;
        double abs = 1e-5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, null);

        UnivariateFunction f = new RiddersFunction();
        PointValuePair result = optimizer.optimize(f, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-Math.log(12.0), result.getValue(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, ConvergenceChecker)}
     * with {@code rel = 1e-5} and {@code abs = 1e-5}.
     */
    @Test
    public void testBrentOptimizerRelChecker() {
        double rel = 1e-5;
        double abs = 1e-5;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                assertEquals(previous, current);
                return true;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        UnivariateFunction f = new RiddersFunction();
        PointValuePair result = optimizer.optimize(f, 0.0, 1.0);
        assertEquals(0.5, result.getPoint(), 1e-6);
        assertEquals(-Math.log(12.0), result.getValue(), 1e-6);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double)} with
     * {@code rel = 1e-6}.
     */
    @Test
    public void testBrentOptimizerRel() {
        double rel = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel);

        UnivariateFunction f = UnivariateTestFunctions.SIN;
        PointValuePair result = optimizer.optimize(f, -1.0, 1.0);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * {@code rel = 1e-8} and {@code abs = 0}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelAbsZero() {
        double rel = 1e-8;
        double abs = 0.0;
        new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * {@code rel = -1e-6}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerRelNegative() {
        double rel = -1e-6;
        double abs = 1e-6;
        new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * {@code rel = 1e-16}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelSmall() {
        double rel = 1e-16;
        double abs = 1e-6;
        new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double)} with
     * {@code rel = 1e-16}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelSmall2() {
        double rel = 1e-16;
        new BrentOptimizer(rel);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * {@code abs = -1e-6}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsNegative() {
        double rel = 1e-6;
        double abs = -1e-6;
        new BrentOptimizer(rel, abs, null);
    }

    /**
     * Test for {@link BrentOptimizer#BrentOptimizer(double)} with
     * {@code abs = -1e-6}.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsNegative2() {
        double abs = -1e-6;
        new BrentOptimizer(1e-6, abs);
    }
}