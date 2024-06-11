package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    // Tested class under consideration
    private BaseSecantSolver baseSecantSolver;

    // Tested function under consideration
    private UnivariateRealFunction testFunction;

    @Before
    public void setUp() {
        // Set up the test function
        testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }

            @Override
            public double derivate(double x) {
                return 2.0 * x;
            }
        };

        // Set up the solver with default settings
        baseSecantSolver = new BaseSecantSolver.SecantSolver(0.001, 0.0001, new BaseSecantSolver.SecantSolver.Method() {});
    }

    @Test
    public void testSolver_BasicFunctionality() throws NoBracketingException, MaxEvalException {
        double root = baseSecantSolver.solve(50, testFunction, -5.0, 5.0, 0.0);
        Assert.assertEquals(2.0, root, 0.0001);
    }

    @Test
    public void testSolver_Bracketing() throws NoBracketingException, MaxEvalException {
        try {
            baseSecantSolver.solve(50, testFunction, 5.0, 5.0, 0.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            baseSecantSolver.solve(50, testFunction, -5.0, -5.0, 0.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            baseSecantSolver.solve(50, testFunction, 0.0, 5.0, 0.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            baseSecantSolver.solve(50, testFunction, -5.0, 0.0, 0.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            baseSecantSolver.solve(50, testFunction, 5.0, 0.0, 0.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_IllegalArgumentException() {
        baseSecantSolver.solve(50, testFunction, -5.0, 5.0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_IllegalArgumentException_AllowedSolution() {
        baseSecantSolver = new BaseSecantSolver(0.001, 0.0001, new BaseSecantSolver.Method() {});
        baseSecantSolver.solve(50, testFunction, -5.0, 5.0, new AllowedSolution() {
            @Override
            public int getValue() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolver_UnsupportedOperationException() {
        baseSecantSolver = new BaseSecantSolver(0.001, 0.0001, new BaseSecantSolver.Method() {});
        baseSecantSolver.allowed = null;
        baseSecantSolver.solve(50, testFunction, -5.0, 5.0, 0.0);
    }
}

enum AllowedSolution {
    LEFT_SIDE,
    RIGHT_SIDE,
    BELOW_SIDE,
    ABOVE_SIDE,
    ANY_SIDE;
}