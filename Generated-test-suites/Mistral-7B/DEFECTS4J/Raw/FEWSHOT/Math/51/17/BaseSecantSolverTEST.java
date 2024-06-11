package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test default constructor
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());

        // Test constructor with absolute accuracy
        solver = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);

        // Test constructor with relative and absolute accuracy
        solver = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.01, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);

        // Test constructor with all accuries
        solver = new BaseSecantSolver(0.01, 0.001, 0.0001, BaseSecantSolver.Method.ILLINOIS);
        assertEquals(0.01, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0001, solver.getFunctionValueAccuracy(), 0.0);

        // Test method getter
        assertEquals(BaseSecantSolver.Method.ILLINOIS, solver.getMethod());

        // Test solve with RegulaFalsi method
        UnivariateFunction function = new PolynomialFunction(x -> Math.pow(x, 2) - 4.0);
        double root = 2.0;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solverRegulaFalsi = new RegulaFalsiSolver(0.001);
        double result = solverRegulaFalsi.solve(function, -5.0, 5.0);
        assertTrue(Precision.abs(result - root) < 0.001);

        // Test solve with Illinois method
        BaseSecantSolver solverIllinois = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        result = solverIllinois.solve(function, -5.0, 5.0);
        assertTrue(Precision.abs(result - root) < 0.001);

        // Test solve with Pegasus method
        BaseSecantSolver solverPegasus = new BaseSecantSolver(0.001, BaseSecantSolver.Method.PEGASUS);
        result = solverPegasus.solve(function, -5.0, 5.0);
        assertTrue(Precision.abs(result - root) < 0.001);

        // Test solving with wrong method
        BaseSecantSolver solverWrongMethod = new BaseSecantSolver(0.001, BaseSecantSolver.Method.SECANT);
        try {
            result = solverWrongMethod.solve(function, -5.0, 5.0);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }

        // Test solving with inverted interval
        function = x -> Math.sin(x);
        solverIllinois = new BaseSecantSolver(0.001, BaseSecantSolver.Method.ILLINOIS);
        try {
            result = solverIllinois.solve(function, -Math.PI, Math.PI);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }
    }
}