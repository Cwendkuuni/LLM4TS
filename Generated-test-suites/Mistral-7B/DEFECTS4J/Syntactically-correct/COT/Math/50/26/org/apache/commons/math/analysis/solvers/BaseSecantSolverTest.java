package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.math3.analysis.solvers.AllowedSolution.*;

public class BaseSecantSolverTest {

    private static final double DELTA = 1e-9;
    private static final double EPSILON = 1e-10;

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(new RegulaFalsiSolver.Config(EPSILON, EPSILON));
    }

    @Test
    public void testConstructor_withAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(DELTA, Method.REGULA_FALSI);
        Assert.assertEquals(DELTA, solver.getAbsoluteAccuracy(), EPSILON);
        Assert.assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testConstructor_withRelativeAccuracy_absoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0 / 1000.0, DELTA, Method.REGULA_FALSI);
        Assert.assertEquals(1.0 / 1000.0, solver.getRelativeAccuracy(), EPSILON);
        Assert.assertEquals(DELTA, solver.getAbsoluteAccuracy(), EPSILON);
        Assert.assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testConstructor_withRelativeAccuracy_absoluteAccuracy_functionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1.0 / 1000.0, DELTA, DELTA / 2.0, Method.REGULA_FALSI);
        Assert.assertEquals(1.0 / 1000.0, solver.getRelativeAccuracy(), EPSILON);
        Assert.assertEquals(DELTA, solver.getAbsoluteAccuracy(), EPSILON);
        Assert.assertEquals(DELTA / 2.0, solver.getFunctionValueAccuracy(), EPSILON);
        Assert.assertEquals(Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testSolve_whenFunctionIsIdentity() {
        UnivariateFunction function = f -> f.getValue();
        double result = solver.solve(new PolynomialFunction(x -> x, 0.0, 1.0), function, 0.0, 1.0, ALL);
        Assert.assertEquals(0.5, result, DELTA);
    }

    @Test
    public void testSolve_whenFunctionHasSingleRoot() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 2.0);
        double result = solver.solve(function, 0.0, 2.0, ALL);
        Assert.assertEquals(-sqrt(2.0), result, DELTA);
    }

    @Test
    public void testSolve_whenFunctionHasTwoRoots() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 4.0);
        try {
            solver.solve(function, 0.0, 4.0, ALL);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_whenFunctionHasTwoRootsWithDifferentSigns() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 4.0);
        try {
            solver.solve(function, 0.0, 4.0, LEFT);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 4.0, RIGHT);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 4.0, ABOVE);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 4.0, BELOW);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 4.0, ANY_SIDE);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            UnivariateFunction functionRight = new PolynomialFunction(x -> x * x - 4.0);
            solver.solve(functionRight, 2.0, 6.0, LEFT);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            UnivariateFunction functionLeft = new PolynomialFunction(x -> x * x - 4.0);
            solver.solve(functionLeft, -6.0, 2.0, RIGHT);
            Assert.fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_whenFunctionIsMonotonicDecreasing() {
        UnivariateFunction function = new PolynomialFunction(x -> FunctionUtils.pow(x, 2) - 4.0);
        try {
            solver.solve(function, 0.0, 0.0, ALL);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 0.0, LEFT);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 0.0, RIGHT);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 0.0, ABOVE);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 0.0, BELOW);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }

        try {
            solver.solve(function, 0.0, 0.0, ANY_SIDE);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testSolve_whenFunctionIsMonotonicIncreasing() {
        UnivariateFunction function = new PolynomialFunction(x -> FunctionUtils.pow(x, 2) + 4.0);
        double result = solver.solve(function, 0.0, 0.0, ALL);
        Assert.assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testSolve_whenFunctionHasSymmetricRoots() {
        UnivariateFunction function = new PolynomialFunction(x -> x * x - 4.0);
        double result = solver.solve(function, 0.0, 0.0, ANY_SIDE);
        Assert.assertEquals(-2.0 * sqrt(2.0) / 2.0, result, DELTA);
    }

    private static double sqrt(double x) {
        return Math.sqrt(x);
    }
}