package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.*;

public class BaseSecantSolverTest {

    @SuppressWarnings("unchecked")
    private static UnivariateRealFunction createQuadraticFunction(double a, double b, double c) {
        return (UnivariateRealFunction) new PolynomialFunction(new double[]{-b, 2.0 * a, c});
    }

    @Test
    public void testRegulaFalsiMethod() {
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = createQuadraticFunction(1.0, -2.0, 1.0);
        double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testIllinoisMethod() {
        BaseSecantSolver<UnivariateRealFunction> solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        UnivariateRealFunction function = createQuadraticFunction(1.0, -2.0, 1.0);
        double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testPegasusMethod() {
        BaseSecantSolver<UnivariateRealFunction> solver = new PegasusSolver(1e-6, PegasusSolver.PEGASUS);
        UnivariateRealFunction function = createQuadraticFunction(1.0, -2.0, 1.0);
        double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testUnknownMethod() {
        BaseSecantSolver<UnivariateRealFunction> solver = new BaseSecantSolver<>(1e-6, 1.0, Method.UNKNOWN, UnivariateRealFunctionClass.FUNCTION_CLASS_UNBOUNDED);
        UnivariateRealFunction function = FunctionUtils.sin;
        solver.solve(10, function, 0.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolverWithRelativeAndAbsoluteAccuracy() {
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-4, 1e-6, 1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = createQuadraticFunction(1.0, -2.0, 1.0);
        double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        BaseSecantSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-8, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = createQuadraticFunction(1.0, -2.0, 1.0);
        double root = solver.solve(10, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }
}