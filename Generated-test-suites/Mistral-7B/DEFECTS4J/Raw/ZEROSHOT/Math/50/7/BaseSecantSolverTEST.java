package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ACCURACY = 1e-9;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-7;

    private enum Method {
        REGULA_FALSI,
        ILLINOIS,
        PEGASUS
    }

    private final Method method;

    public BaseSecantSolverTest(Method method) {
        this.method = method;
    }

    @Parameterized.Parameters(name = "{0} method")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Method.REGULA_FALSI},
                {Method.ILLINOIS},
                {Method.PEGASUS}
        });
    }

    @Before
    public void setUp() {
        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, method) {
            @Override
            protected double doSolve() {
                return super.solve(100, function, MIN, MAX, START_VALUE, AllowedSolution.ANY_SIDE);
            }
        };
        TestUtils.testEquals(MIN, solver.getMin(), 0.0);
        TestUtils.testEquals(MAX, solver.getMax(), 0.0);
        TestUtils.testEquals(START_VALUE, solver.getInitialGuess(), 0.0);
        TestUtils.testEquals(method, solver.getMethod(), null);
    }

    @Test
    public void testSolver() throws MaxEvalException, NoBracketingException, UnsupportedOperationException {
        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, method) {
            @Override
            protected double doSolve() {
                return super.solve(100, function, MIN, MAX, START_VALUE, AllowedSolution.ANY_SIDE);
            }
        };

        double root = solver.solve(function, MIN, MAX, START_VALUE, AllowedSolution.ANY_SIDE);
        double expectedRoot = solveExactFunction(function, MIN, MAX);

        TestUtils.testEquals(expectedRoot, root, 1e-9);
    }

    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, method) {
            @Override
            protected double doSolve() {
                return super.solve(100, function, MIN, MAX, START_VALUE, AllowedSolution.ANY_SIDE);
            }
        };

        try {
            solver.solve(function, MIN, MAX, START_VALUE, AllowedSolution.BELOW_SIDE);
            fail("Expected NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperation() {
        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, method) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };

        try {
            solver.solve(function, MIN, MAX, START_VALUE, AllowedSolution.ANY_SIDE);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY, method) {
            @Override
            protected double doSolve() {
                return super.solve(100, function, MIN, MAX, START_VALUE, AllowedSolution.LEFT_SIDE);
            }
        };

        double root = solver.solve(function, MIN, MAX, START_VALUE, AllowedSolution.LEFT_SIDE);
        double expectedRoot = solveExactFunction(function, MIN, MAX);

        if (function.value(MIN) < 0) {
            TestUtils.testLessThan(root, expectedRoot);
        } else if (function.value(MAX) < 0) {
            TestUtils.testGreaterThan(root, expectedRoot);
        } else {
            fail("Expected an allowed solution, but the root is not on the correct side.");
        }
    }

    private static double solveExactFunction(UnivariateRealFunction function, double min, double max) {
        UnivariateRealSolver solver = new BrentSolver(RELATIVE_ACCURACY, ABSOLUTE_ACCURACY, FUNCTION_VALUE_ACCURACY);
        return solver.solve(function, min, max);
    }

    private UnivariateRealFunction function = f -> 4.0 * x * x - 4.0;
    private double MIN = -2.0;
    private double MAX = 2.0;
    private double START_VALUE = 1.0;

}