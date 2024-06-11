package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        final BaseSecantSolver solver = new RegulaFalsiSolver(0.001);
        try {
            double root = solver.solve(10, function, -3.0, 3.0, 0.0);
            assertEquals(0.0, root, 0.001);
        } catch (NoBracketingException e) {
            fail("Solver should have found bracketing.");
        } catch (MaxIterationsExceededException e) {
            fail("Solver should have converged.");
        }

        try {
            solver.solve(10, function, -3.0, 3.0, 4.0);
            fail("Solver should have reported no bracketing.");
        } catch (NoBracketingException e) {
            // Expected
        }

        try {
            solver.solve(10, function, -3.0, 3.0, 1.5);
            fail("Solver should have reported an exception.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testIllinoisSolver() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        final BaseSecantSolver solver = new IllinoisSolver(0.001);
        try {
            double root = solver.solve(10, function, -3.0, 3.0, 0.0);
            assertEquals(Math.PI / 2, root, 0.001);
        } catch (NoBracketingException e) {
            fail("Solver should have found bracketing.");
        } catch (MaxIterationsExceededException e) {
            fail("Solver should have converged.");
        }

        try {
            solver.solve(10, function, -3.0, 3.0, 1.5708);
            fail("Solver should have reported an exception.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testPegasusSolver() {
        final UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        final BaseSecantSolver solver = new PegasusSolver(0.001);
        try {
            double root = solver.solve(10, function, -2.0, 0.0, 0.0);
            assertEquals(Math.log(2), root, 0.001);
        } catch (NoBracketingException e) {
            fail("Solver should have found bracketing.");
        } catch (MaxIterationsExceededException e) {
            fail("Solver should have converged.");
        }

        try {
            solver.solve(10, function, -2.0, 0.0, 1.0);
            fail("Solver should have reported an exception.");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}