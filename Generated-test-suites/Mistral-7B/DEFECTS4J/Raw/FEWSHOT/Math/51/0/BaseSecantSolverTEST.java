package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> x * x - 2), 0, 1);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> x * x - 2), 0, 1);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> x * x - 2), 0, 1);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testMaxEval() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertThrows(MaxEvalException.class, () -> solver.solve(0, x -> Math.sin(x) / x, -1, 1, 1));
    }

    @Test
    public void testNoBracketing() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertThrows(NoBracketingException.class, () -> solver.solve(new PolynomialFunction(x -> x * x - 1), 1, 1));
    }

    @Test
    public void testBracketing() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> Math.pow(x, 3) - 2), 1, 2);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testAllowedSolutionLeft() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> Math.pow(x, 3) - 2), 1, 2, AllowedSolution.LEFT);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testAllowedSolutionRight() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> Math.pow(x, 3) - 2), 1, 2, AllowedSolution.RIGHT);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testAllowedSolutionBelow() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> Math.pow(x, 3) - 2), 1, 2, AllowedSolution.BELOW);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testAllowedSolutionAbove() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double result = solver.solve(new PolynomialFunction(x -> Math.pow(x, 3) - 2), 1, 2, AllowedSolution.ABOVE);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testNullFunction() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertNull(solver.solve(null, 1, 2));
    }

    @Test
    public void testNullMin() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(new PolynomialFunction(x -> x * x - 2), null, 1));
    }

    @Test
    public void testNullMax() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(new PolynomialFunction(x -> x * x - 2), 1, null));
    }

    @Test
    public void testNullStartValue() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(new PolynomialFunction(x -> x * x - 2), 1, 2, null));
    }

    @Test
    public void testNullAllowedSolution() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertThrows(IllegalArgumentException.class, () -> solver.solve(new PolynomialFunction(x -> x * x - 2), 1, 2, null));
    }

    @Test
    public void testMethodEnumValue() {
        assertEquals(Method.REGULA_FALSI, Method.valueOf("REGULA_FALSI"));
        assertEquals(Method.ILLINOIS, Method.valueOf("ILLINOIS"));
        assertEquals(Method.PEGASUS, Method.valueOf("PEGASUS"));
    }
}