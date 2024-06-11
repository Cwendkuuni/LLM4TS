package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    /**
     * Test of the constructor BracketingNthOrderBrentSolver().
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test
    public void testConstructor() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test of the constructor BracketingNthOrderBrentSolver().
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracy() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-6, 5);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 1e-10);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test of the constructor BracketingNthOrderBrentSolver().
     * @throws NumberIsTooSmallException if maximal order is lower than 2
     */
    @Test
    public void testConstructorWithRelativeAccuracyAndAbsoluteAccuracyAndFunctionValueAccuracy() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-4, 1e-6, 5, 1e-8);
        assertEquals(1e-4, solver.getRelativeAccuracy(), 1e-10);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-10);
        assertEquals(1e-8, solver.getFunctionValueAccuracy(), 1e-10);
        assertEquals(5, solver.getMaximalOrder());
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double)
     * with a simple function that has a unique root in [min, max]
     */
    @Test
    public void testSolveWithSimpleFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double)
     * with a simple function that has a unique root in [min, max] but close to max
     */
    @Test
    public void testSolveWithSimpleFunctionUpperBound() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.1;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double)
     * with a simple function that has a unique root in [min, max] but close to min
     */
    @Test
    public void testSolveWithSimpleFunctionLowerBound() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.1;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double)
     * with a simple function that has a unique root between min and max
     */
    @Test
    public void testSolveWithSimpleFunctionMiddleRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -2.5;
        double max = 2.5;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, 0.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double)
     * with a simple function that has two roots in [min, max]
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithDoubleRootFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0 * Math.pow(Math.PI, 2);
            }
        };
        double min = -3.0;
        double max = 3.0;
        solver.solve(100, f, min, max);
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has a unique root in [min, max] and any side
     */
    @Test
    public void testSolveWithSimpleFunctionAnySide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ANY_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has a unique root in [min, max] and left side
     */
    @Test
    public void testSolveWithSimpleFunctionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.LEFT_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has a unique root in [min, max] and right side
     */
    @Test
    public void testSolveWithSimpleFunctionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has a unique root in [min, max] and below side
     */
    @Test
    public void testSolveWithSimpleFunctionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.BELOW_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has a unique root in [min, max] and above side
     */
    @Test
    public void testSolveWithSimpleFunctionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ABOVE_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double min = -3.0;
        double max = 3.0;
        double root = solver.solve(100, f, min, max);
        assertTrue(Precision.equals(root, -1.0, 1e-10));
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has no root in [min, max]
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithNoRootFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ANY_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 1;
            }
        };
        double min = -3.0;
        double max = 3.0;
        solver.solve(100, f, min, max);
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has a root but it's outside [min, max]
     */
    @Test(expected = NoBracketingException.class)
    public void testSolveWithOutOfBoundsRootFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.ANY_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };
        double min = -3.0;
        double max = 3.0;
        solver.solve(100, f, min, max);
    }

    /**
     * Test of the method void solve(int, UnivariateFunction, double, double, double, AllowedSolution)
     * with a simple function that has no accurate root in [min, max]
     */
    @Test(expected = MaxEvalException.class)
    public void testSolveWithNoAccurateRootFunction() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-18, 5, AllowedSolution.ANY_SIDE);
        UnivariateFunction f = new UnivariateFunction() {

            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        double min = -3.0;
        double max = 3.0;
        solver.solve(100, f, min, max);
    }

}