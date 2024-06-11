package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.*;
import static org.apache.commons.math3.analysis.solvers.BaseSecantSolver.Method.*;

public class BaseSecantSolverTest {

    @Test
    public void testBracketing() {
        UnivariateRealFunction function = f -> Math.pow(Math.sin(f), 2) - 0.25;

        try {
            BaseSecantSolver solver = new BaseSecantSolver(1e-6, FUNCTION);
            double root = solver.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(0.0, root, 1e-7);
        } catch (NoBracketingException e) {
            fail("Bracketing should be established.");
        }
    }

    @Test
    public void testSolver() {
        UnivariateRealFunction function = f -> Math.pow(Math.sin(f), 2) - 0.25;

        try {
            BaseSecantSolver solver = new BaseSecantSolver(1e-6, FUNCTION, METHOD.REGULA_FALSI);
            double root = solver.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(0.0, root, 1e-7);
        } catch (NoBracketingException | MaxEvalException | PrecisionRequiredException e) {
            fail("Test should pass.");
        }
    }

    @Test
    public void testDifferentMethods() {
        UnivariateRealFunction function = f -> Math.pow(Math.sin(f), 2) - 0.25;

        try {
            // Regula Falsi
            BaseSecantSolver solverRegulaFalsi = new BaseSecantSolver(1e-6, FUNCTION, METHOD.REGULA_FALSI);
            double rootRegulaFalsi = solverRegulaFalsi.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(0.0, rootRegulaFalsi, 1e-7);

            // Illinois
            BaseSecantSolver solverIllinois = new BaseSecantSolver(1e-6, FUNCTION, METHOD.ILLINOIS);
            double rootIllinois = solverIllinois.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(0.0, rootIllinois, 1e-7);

            // Pegasus
            BaseSecantSolver solverPegasus = new BaseSecantSolver(1e-6, FUNCTION, METHOD.PEGASUS);
            double rootPegasus = solverPegasus.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(0.0, rootPegasus, 1e-7);
        } catch (NoBracketingException | MaxEvalException | PrecisionRequiredException e) {
            fail("Test should pass.");
        }
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateRealFunction function = f -> Math.sin(f) - 0.5;

        try {
            // Test left side
            BaseSecantSolver solverLeft = new BaseSecantSolver(1e-6, FUNCTION, METHOD.PEGASUS, AllowedSolution.LEFT_SIDE);
            double rootLeft = solverLeft.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(Precision.PI_OVER_2, rootLeft, 1e-7);

            // Test right side
            BaseSecantSolver solverRight = new BaseSecantSolver(1e-6, FUNCTION, METHOD.PEGASUS, AllowedSolution.RIGHT_SIDE);
            double rootRight = solverRight.solve(100, function, -10, 10, 0.0);
            Assert.assertEquals(-Precision.PI_OVER_2, rootRight, 1e-7);

            // Test below side
            BaseSecantSolver solverBelow = new BaseSecantSolver(1e-6, FUNCTION, METHOD.PEGASUS, AllowedSolution.BELOW_SIDE);
            double rootBelow = solverBelow.solve(100, function, -10, 10, 0.0);
            Assert.assertTrue(rootBelow < 0);

            // Test above side
            BaseSecantSolver solverAbove = new BaseSecantSolver(1e-6, FUNCTION, METHOD.PEGASUS, AllowedSolution.ABOVE_SIDE);
            double rootAbove = solverAbove.solve(100, function, -10, 10, 0.0);
            Assert.assertTrue(rootAbove > 0);
        } catch (NoBracketingException | MaxEvalException | PrecisionRequiredException e) {
            fail("Test should pass.");
        }
    }
}