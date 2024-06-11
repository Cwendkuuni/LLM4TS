package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Tests for {@link BracketingNthOrderBrentSolver}.
 */
public class BracketingNthOrderBrentSolverTest {

    /** A simple function for testing. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x - 0.5, 2);
        }
    };

    /**
     * Test constructor with default arguments.
     */
    @Test
    public void testDefaultConstructor() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test constructor with specific arguments.
     */
    @Test
    public void testCustomConstructor() {
        final double relativeAccuracy = 1e-4;
        final double absoluteAccuracy = 1e-8;
        final int maximalOrder = 3;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(
                relativeAccuracy, absoluteAccuracy, maximalOrder);
        assertEquals(relativeAccuracy, solver.getRelativeAccuracy(), 1e-9);
        assertEquals(absoluteAccuracy, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(maximalOrder, solver.getMaximalOrder());
    }

    /**
     * Test solver with a simple function.
     */
    @Test
    public void testSolve() throws NumberIsTooSmallException {
        final double min = -1.1;
        final double max = 1.1;
        final double expected = 0.5;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double result = solver.solve(FUNCTION, min, max);
        assertTrue(Precision.isClose(result, expected, 1e-9));
    }

    /**
     * Test solver with a simple function and a custom start value.
     */
    @Test
    public void testSolveWithStartValue() throws NumberIsTooSmallException {
        final double min = -1.1;
        final double max = 1.1;
        final double startValue = 0.2;
        final double expected = 0.5;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double result = solver.solve(FUNCTION, min, max, startValue);
        assertTrue(Precision.isClose(result, expected, 1e-9));
    }

    /**
     * Test solver with a simple function and an exception when bracketing is not
     * guaranteed.
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        final double min = 0.1;
        final double max = 0.1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(FUNCTION, min, max);
    }

    /**
     * Test solver with a simple function and a custom allowed solution.
     */
    @Test
    public void testSolveWithAllowedSolution() throws NumberIsTooSmallException {
        final double min = -1.1;
        final double max = 1.1;
        final AllowedSolution allowedSolution = AllowedSolution.BELOW_SIDE;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        final double result = solver.solve(FUNCTION, min, max, allowedSolution);
        assertTrue(Precision.lessThan(result, min));
    }

    /**
     * Test solver with a simple function and a custom maximal order.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testMinimalOrder() {
        final double min = -1.1;
        final double max = 1.1;
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(0);
        solver.solve(FUNCTION, min, max);
    }

}