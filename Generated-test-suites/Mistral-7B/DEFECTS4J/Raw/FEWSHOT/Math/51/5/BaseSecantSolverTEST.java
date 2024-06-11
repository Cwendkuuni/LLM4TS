package org.apache.commons.cli.test; // Make sure to import necessary classes

import static org.junit.Assert.*;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver.*;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math3.special.Erf;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        // Test default constructor
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS);
        assertNotNull(solver);

        // Test constructor with absolute accuracy and method
        solver = new BaseSecantSolver(1e-8, Method.PEGASUS);
        assertNotNull(solver);

        // Test constructor with relative accuracy, absolute accuracy, and method
        solver = new BaseSecantSolver(1e-5, 1e-10, Method.PEGASUS);
        assertNotNull(solver);

        // Test constructor with relative accuracy, absolute accuracy, function value accuracy, and method
        solver = new BaseSecantSolver(1e-5, 1e-10, 1e-8, Method.PEGASUS);
        assertNotNull(solver);
    }

    @Test
    public void testSolveRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double result = solver.solve(function, -5, 5, AllowedSolution.ANY_SIDE);
        double expected = Math.PI / 2;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolveIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        double result = solver.solve(function, -5, 5, AllowedSolution.ANY_SIDE);
        double expected = Math.log(2);
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolvePegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Erf.erf(x) * 0.5;
            }
        };

        double result = solver.solve(function, -5, 5, AllowedSolution.ANY_SIDE);
        double expected = 0;
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolveWithBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        double result = solver.solve(function, -5, 5, AllowedSolution.LEFT_SIDE);
        double expected = Math.PI / 2;
        assertEquals(expected, result, 1e-6);

        result = solver.solve(function, -5, 5, AllowedSolution.RIGHT_SIDE);
        expected = -(Math.PI / 2);
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS, 1e-8);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        double result = solver.solve(function, -5, 5, AllowedSolution.ANY_SIDE);
        double expected = Math.log(2);
        assertEquals(expected, result, 1e-6);

        result = solver.solve(function, -5, 5, AllowedSolution.LEFT_SIDE);
        expected = Math.log(2) - 1e-8;
        assertEquals(expected, result, 1e-9);
    }
}