package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.special.BesselFunctions;
import org.apache.commons.math3.special.Erf;

public class BracketingNthOrderBrentSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-8;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-12;
    private static final int MAXIMAL_ORDER = 10;

    @org.junit.Test
    public void testBrentNthOrderSimpleRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        double root = solver.solve(100, f, -3.0, 3.0, 0.0);
        assertEquals("The algorithm did not find the root of x^2 - 2", -1.0, root, ABSOLUTE_ACCURACY);
    }

    @org.junit.Test
    public void testBrentNthOrderComplexRoot() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        double root = solver.solve(100, f, -3.0, 3.0, 0.0);
        assertEquals("The algorithm did not find the root of sin(x) - 0.5", 1.2772, root, ABSOLUTE_ACCURACY);
    }

    @org.junit.Test(expected = NumberIsTooSmallException.class)
    public void testBrentNthOrderTooSmallMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, 1);
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x) - 0.5);
            }
        };
        solver.solve(100, f, -3.0, 3.0, 0.0);
    }

    @org.junit.Test(expected = NoBracketingException.class)
    public void testBrentNthOrderNoBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        solver.solve(100, f, -3.0, 2.0, 0.0);
    }

    @org.junit.Test
    public void testBrentNthOrderCloseRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        double root1 = solver.solve(100, f, -3.0, 2.0, 0.0);
        double root2 = solver.solve(100, f, -2.0, 2.0, 0.0);
        double expectedRoot1 = -1.3527;
        double expectedRoot2 = -1.3528;
        assertEquals("The algorithm did not find the first root", expectedRoot1, root1, ABSOLUTE_ACCURACY);
        assertEquals("The algorithm did not find the second root", expectedRoot2, root2, ABSOLUTE_ACCURACY);
        assertTrue("The roots are not close enough", Math.abs(root1 - root2) < 1e-6);
    }

    @org.junit.Test
    public void testBrentNthOrderBesselFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return BesselFunctions.I0(x) - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        double root = solver.solve(100, f, 0.0, 4.0, 0.0);
        double expectedRoot = 2.4048;
        assertEquals("The algorithm did not find the root of I0(x) - 1", expectedRoot, root, ABSOLUTE_ACCURACY);
    }

    @org.junit.Test(expected = MaxEvalExceededException.class)
    public void testBrentNthOrderMaxEvalExceeded() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.exp(-x) * Math.sin(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        solver.solve(1, f, -3.0, 3.0, 0.0);
    }

    @org.junit.Test
    public void testBrentNthOrderErrorHandling() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Test error");
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        try {
            solver.solve(100, f, -3.0, 3.0, 0.0);
            fail("An exception should have been thrown");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testBrentNthOrderComplexFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return FunctionUtils.sqrt(Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2)) - 1;
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        double root = solver.solve(100, f, -3.0, 3.0, 0.0);
        double expectedRoot = 1.3169578969253664;
        assertEquals("The algorithm did not find the root", expectedRoot, root, ABSOLUTE_ACCURACY);
    }

    @org.junit.Test
    public void testBrentNthOrderErrorFunction() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new IllegalArgumentException("Test error");
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        try {
            solver.solve(100, f, -3.0, 3.0, 0.0);
            fail("An exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testBrentNthOrderFunctionValueOutOfRange() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Erf.erf(x);
            }
        };
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, MAXIMAL_ORDER);
        try {
            solver.solve(100, f, -5.0, 5.0, 0.0);
            fail("An exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}