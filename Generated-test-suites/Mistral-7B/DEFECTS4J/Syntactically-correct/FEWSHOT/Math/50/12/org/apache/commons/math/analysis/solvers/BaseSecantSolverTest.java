package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.commons.math.analysis.solvers.AbstractUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test constructor with default arguments and default method
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.REGULA_FALSI, solver.getMethod());

        // Test constructor with custom arguments and default method
        BaseSecantSolver solver1 = new BaseSecantSolver(0.001, 1e-8);
        assertEquals(0.001, solver1.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-8, solver1.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.REGULA_FALSI, solver1.getMethod());

        // Test constructor with custom arguments and custom method
        BaseSecantSolver solver2 = new BaseSecantSolver(0.001, 1e-8, 1e-8, Method.PEGASUS);
        assertEquals(0.001, solver2.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-8, solver2.getAbsoluteAccuracy(), 1e-9);
        assertEquals(Method.PEGASUS, solver2.getMethod());

        // Test constructor with function value accuracy
        BaseSecantSolver solver3 = new BaseSecantSolver(0.001, 1e-8, 1e-8, 1e-8, Method.ILLINOIS);
        assertEquals(0.001, solver3.getRelativeAccuracy(), 1e-9);
        assertEquals(1e-8, solver3.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-8, solver3.getFunctionValueAccuracy(), 1e-9);
        assertEquals(Method.ILLINOIS, solver3.getMethod());

        // Test solve method with default arguments and default method
        UnivariateRealFunction function = (x) -> FastMath.pow(x, 2) - 2;
        double root = solver.solve(10, function, -5, 5, 0);
        assertEquals(0.5, root, 1e-5);

        // Test solve method with custom arguments and default method
        double root1 = solver1.solve(10, function, -5, 5, 0.5);
        assertEquals(0.5, root1, 1e-5);

        // Test solve method with custom arguments and custom method
        double root2 = solver2.solve(10, function, -5, 5, 0.5);
        assertEquals(0.5, root2, 1e-5);

        // Test solve method with function value accuracy
        double root3 = solver3.solve(10, function, -5, 5, 0.5);
        assertEquals(0.5, root3, 1e-5);

        // Test solve method with invalid arguments (null function)
        try {
            solver.solve(10, null, -5, 5, 0.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test solve method with invalid arguments (min > max)
        try {
            solver.solve(10, function, 5, -5, 0.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        // Test solve method with invalid arguments (allowed solution out of bounds)
        try {
            BaseSecantSolver solver4 = new BaseSecantSolver(0.001, 1e-8, 1e-8, Method.ILLINOIS);
            solver4.solve(10, function, -5, 5, AllowedSolution.BELOW_SIDE);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }

        // Test solve method with invalid arguments (allowed solution out of bounds)
        try {
            BaseSecantSolver solver5 = new BaseSecantSolver(0.001, 1e-8, 1e-8, Method.ILLINOIS);
            solver5.solve(10, function, -5, 5, AllowedSolution.ABOVE_SIDE);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }

        // Test solve method with custom method: RegulaFalsiSolver
        BaseSecantSolver solverRegulaFalsi = new BaseSecantSolver(0.001, 1e-8, 1e-8, Method.REGULA_FALSI);
        double rootRegulaFalsi = solverRegulaFalsi.solve(10, function, -5, 5, 0.5);
        assertEquals(0.5, rootRegulaFalsi, 1e-5);

        // Test solve method with custom method: IllinoisSolver
        BaseSecantSolver solverIllinois = new BaseSecantSolver(0.001, 1e-8, 1e-8, Method.ILLINOIS);
        double rootIllinois = solverIllinois.solve(10, function, -5, 5, 0.5);
        assertEquals(0.5, rootIllinois, 1e-5);

        // Test solve method with custom method: PegasusSolver
        BaseSecantSolver solverPegasus = new BaseSecantSolver(0.001, 1e-8, 1e-8, Method.PEGASUS);
        double rootPegasus = solverPegasus.solve(10, function, -5, 5, 0.5);
        assertEquals(0.5, rootPegasus, 1e-5);

        // Test solve method with custom method: SecantSolver (not implemented in BaseSecantSolver)
        try {
            SecantSolver secantSolver = new SecantSolver(0.001, 1e-8);
            secantSolver.solve(10, function, -5, 5, 0.5);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
}