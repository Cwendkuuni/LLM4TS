package org.apache.commons.math.analysis.solvers.tests;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the {@link BaseSecantSolver} abstract class.
 */
public class BaseSecantSolverTest {

    /**
     * Test solution using {@link RegulaFalsiSolver}.
     */
    @Test
    public void testRegulaFalsi() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };

        final BaseSecantSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6);
        try {
            final double solution = solver.solve(function, -3.0, 3.0);
            Assert.assertEquals("Solution not equal to zero.", 0.0, solution, 1e-6);
        } catch (NoBracketingException e) {
            Assert.fail("Bracketing error.");
        }
    }

    /**
     * Test solution using {@link IllinoisSolver}.
     */
    @Test
    public void testIllinois() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };

        final BaseSecantSolver<UnivariateFunction> solver = new IllinoisSolver(1e-6);
        try {
            final double solution = solver.solve(function, -3.0, 3.0);
            Assert.assertEquals("Solution not equal to zero.", 0.0, solution, 1e-6);
        } catch (NoBracketingException e) {
            Assert.fail("Bracketing error.");
        }
    }

    /**
     * Test solution using {@link PegasusSolver}.
     */
    @Test
    public void testPegasus() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.log(Math.abs(x) + 1) - Math.log(2);
            }
        };

        final BaseSecantSolver<UnivariateFunction> solver = new PegasusSolver(1e-6);
        try {
            final double solution = solver.solve(function, -5.0, 3.0);
            Assert.assertEquals("Solution not equal to zero.", 1.0, solution, 1e-6);
        } catch (NoBracketingException | MathIllegalArgumentException e) {
            Assert.fail("Unexpected exception.");
        }
    }

    /**
     * Test bracketing error with {@link RegulaFalsiSolver}.
     */
    @Test(expected = NoBracketingException.class)
    public void testRegulaFalsiBracketingError() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - Math.cos(x);
            }
        };

        final BaseSecantSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1e-6);
        solver.solve(function, 0.0, 0.0);
    }

    /**
     * Test illegal argument error with {@link IllinoisSolver}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testIllinoisIllegalArgumentError() {
        final UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        };

        final BaseSecantSolver<UnivariateFunction> solver = new IllinoisSolver(1e-6);
        solver.solve(function, 0.0, 0.0);
    }
}