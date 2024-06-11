package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.util.Precision;

/**
 * Test cases for PowellOptimizer class.
 */
public class PowellOptimizerTest extends AbstractMathTestCase {

    /**
     * Test constructor with default values and default convergence checker.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNotNull(optimizer.getConvergenceChecker());
        assertTrue(optimizer.isConvergenceCheckerSet());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 1e-9);
        assertNull(optimizer.getLineSearch());
        assertFalse(optimizer.isLineSearchSet());
    }

    /**
     * Test constructor with user-defined convergence checker and default
     * convergence thresholds.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testUserDefinedConvergenceChecker() throws Exception {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(0.001, 0.0001);
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        assertFalse(optimizer.isConvergenceCheckerSet());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 1e-9);
        assertTrue(optimizer.isLineSearchSet());
        assertNotNull(optimizer.getLineSearch());
        assertEquals(0.001, optimizer.getLineSearch().getRelativeThreshold(), 1e-9);
        assertEquals(0.0, optimizer.getLineSearch().getAbsoluteThreshold());
    }

    /**
     * Test constructor with user-defined convergence thresholds and user-defined
     * convergence checker.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testUserDefinedThresholdsAndConvergenceChecker() throws Exception {
        SimpleUnivariateValueChecker checker = new SimpleUnivariateValueChecker(0.001, 0.0001);
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.0001, checker, 1e-5, 1e-7);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertNull(optimizer.getConvergenceChecker());
        assertFalse(optimizer.isConvergenceCheckerSet());
        assertEquals(0.01, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(1e-7, optimizer.getAbsoluteThreshold(), 1e-9);
        assertTrue(optimizer.isLineSearchSet());
        assertNotNull(optimizer.getLineSearch());
        assertEquals(0.001, optimizer.getLineSearch().getRelativeThreshold(), 1e-9);
        assertEquals(1e-5, optimizer.getLineSearch().getAbsoluteThreshold());
    }

    /**
     * Test optimization with a simple function.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testSimpleFunction() throws Exception {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = new double[] { 2.0, 2.0 };
        double[] expectedResult = new double[] { 0.0, 0.0 };
        PointValuePair result = optimizer.optimize(startPoint);
        assertArrayEquals(expectedResult, result.getPoint(), 1e-9);
        assertEquals(0.0, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with a multimodal function.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testMultimodalFunction() throws Exception {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = new double[] { 1.0, 1.0 };
        double[] expectedResult = new double[] { 0.5, 0.5 };
        PointValuePair result = optimizer.optimize(startPoint);
        assertArrayEquals(expectedResult, result.getPoint(), 1e-9);
        assertEquals(0.25, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with a non-convex function.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = MaxEvalException.class)
    public void testNonConvexFunction() throws Exception {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0] - 1.0, 2) + Math.pow(point[1] - 1.0, 2) - 1.0;
            }
        };
        optimizer.optimize(startPoint, f);
    }

    /**
     * Test optimization with a non-differentiable function.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testNondifferentiableFunction() throws Exception {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        double[] startPoint = new double[] { 1.0, 1.0 };
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double[] point) {
                return Math.abs(point[0] - 1.0) + Math.abs(point[1] - 1.0);
            }
        };
        PointValuePair result = optimizer.optimize(startPoint, f);
        assertEquals(2.0, result.getValue(), 1e-9);
        assertArrayEquals(new double[] { 1.0, 1.0 }, result.getPoint(), 1e-9);
    }
}