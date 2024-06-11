package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 1});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.RegulaFalsiMethod.REGULA_FALSI);
        double root = solver.solve(new UnivariateRealFunction[] { function }, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 1e-7);
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 1});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.IllinoisMethod.ILLINOIS);
        double root = solver.solve(new UnivariateRealFunction[] { function }, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 1e-7);
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 1});
        BaseSecantSolver solver = new PegasusSolver(1e-6, PegasusSolver.PegasusMethod.PEGASUS);
        double root = solver.solve(new UnivariateRealFunction[] { function }, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, root, 1e-7);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidMethod() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1, 1}, new double[]{0, 1, 1, 1});
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, 1e-6, BaseSecantSolver.Method.INVALID);
        solver.solve(new UnivariateRealFunction[] { function }, 0.0, 1.0, AllowedSolution.ANY_SIDE);
    }
}