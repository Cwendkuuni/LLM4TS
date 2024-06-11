package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnbracketedException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(100, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, 1e-10);
        } catch (MaxEvalException e) {
            // This exception should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new IllinoisSolver(1e-6);
        try {
            double root = solver.solve(100, f, -2.0, 2.0, 0.0);
            assertEquals(0.0, root, 1e-10);
        } catch (MaxEvalException | NumberIsTooSmallException | UnbracketedException e) {
            // These exceptions should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testPegasusSolver() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new PegasusSolver(1e-6);
        try {
            double root = solver.solve(100, f, -5.0, 5.0, 0.0);
            assertEquals(Math.PI / 2, root, 1e-6);
        } catch (MaxEvalException e) {
            // This exception should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAllowedSolutionLeftSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6, AllowedSolution.LEFT_SIDE);
        try {
            double root = solver.solve(100, f, -2.0, 2.0, 0.0);
            assertEquals(-2.0, root, 1e-10);
        } catch (MaxEvalException | NumberIsTooSmallException | UnbracketedException e) {
            // These exceptions should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAllowedSolutionRightSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6, AllowedSolution.RIGHT_SIDE);
        try {
            double root = solver.solve(100, f, -2.0, 2.0, 0.0);
            assertEquals(2.0, root, 1e-10);
        } catch (MaxEvalException | NumberIsTooSmallException | UnbracketedException e) {
            // These exceptions should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAllowedSolutionBelowSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6, AllowedSolution.BELOW_SIDE);
        try {
            double root = solver.solve(100, f, 0.1, 10.0, 1.0);
            assertEquals(Math.E, root, 1e-6);
        } catch (MaxEvalException | NumberIsTooSmallException | UnbracketedException e) {
            // These exceptions should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testAllowedSolutionAboveSide() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(x) - 2;
            }
        };
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6, AllowedSolution.ABOVE_SIDE);
        try {
            double root = solver.solve(100, f, 100.0, 1000.0, 1000.1);
            assertEquals(Math.log(1000.0) - 2, root, 1e-6);
        } catch (MaxEvalException | NumberIsTooSmallException | UnbracketedException e) {
            // These exceptions should not be thrown in this test case.
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}