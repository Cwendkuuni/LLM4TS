package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

/**
 * Test cases for BracketingNthOrderBrentSolver class.
 */
public class BracketingNthOrderBrentSolverTest {

    /** Test function for testing solver. */
    private static final UnivariateFunction FUNCTION = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return Math.sin(x) - 0.5 * x;
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x) - 0.5;
        }
    };

    /** Test of constructor with default arguments. */
    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(5, solver.getMaximalOrder());
    }

    /** Test of constructor with specified arguments. */
    @Test
    public void testConstructorWithArguments() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-5, 3);
        assertNotNull(solver);
        assertEquals(1e-5, solver.getAbsoluteAccuracy(), 1e-8);
        assertEquals(3, solver.getMaximalOrder());
    }

    /** Test of constructor with illegal arguments. */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithIllegalArguments() {
        new BracketingNthOrderBrentSolver(1, 1);
    }

    /** Test of solving function with default arguments. */
    @Test
    public void testSolveWithDefaultArguments() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double[] solution = solver.solve(10, FUNCTION, -3, 3, AllowedSolution.ANY_SIDE);
        assertArrayEquals(new double[]{-0.93121981, 0.93121981}, solution, 1e-6);
    }

    /** Test of solving function with specified arguments. */
    @Test
    public void testSolveWithSpecifiedArguments() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-7, 4);
        double[] solution = solver.solve(10, FUNCTION, -3, 3, AllowedSolution.ANY_SIDE);
        assertArrayEquals(new double[]{-0.93121981, 0.93121981}, solution, 1e-7);
    }

    /** Test of solving function with illegal arguments. */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithIllegalArguments() {
        new BracketingNthOrderBrentSolver().solve(1, FUNCTION, -3, 3, AllowedSolution.ANY_SIDE);
    }

    /** Test of solving function with no bracketing. */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(1, FUNCTION, 0.1, 0.1, AllowedSolution.ANY_SIDE);
    }

    /** Test of solving function with specified allowed solution. */
    @Test
    public void testSolveWithSpecifiedAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double[] solution = solver.solve(10, FUNCTION, -3, 3, AllowedSolution.LEFT_SIDE);
        assertEquals(-3, solution[0], 1e-6);
        double[] solutionRight = solver.solve(10, FUNCTION, -3, 3, AllowedSolution.RIGHT_SIDE);
        assertEquals(3, solutionRight[0], 1e-6);
    }

}