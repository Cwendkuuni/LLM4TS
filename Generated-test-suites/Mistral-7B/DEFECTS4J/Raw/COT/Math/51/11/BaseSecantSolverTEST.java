package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.UnknownRootException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.math.analysis.solvers.SimpleBracketedUnivariateSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.regulafalsi.RegulaFalsiConfiguration;
import org.apache.commons.math.analysis.solvers.regulafalsi.RegulaFalsiSolverFactory;
import org.apache.commons.math.analysis.solvers.secant.SecantConfiguration;
import org.apache.commons.math.analysis.solvers.secant.SecantSolverFactory;
import org.apache.commons.math.analysis.solvers.simple.SimpleBracketedUnivariateSolverFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Test class for {@link BaseSecantSolver}.
 */
@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private static final double EPSILON = 1e-12;

    private UnivariateRealSolver solver;
    private double min;
    private double max;
    private double expectedResult;
    private double expectedFunctionValue;

    @Parameters(name = "{index}: [min, max, expectedResult, expectedFunctionValue]")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        // Polynomial function
                        {-1.0, 1.0, 0.0, 0.0},
                        {0.0, 1.0, 1.0, 1.0},
                        {-1.5, 1.5, 0.0, 0.0},
                        {-2.0, 0.0, -1.0, 1.0},
                        {1.5, 3.0, 2.0, 4.0},
                        // Rational function
                        {-2.0, 1.0, 0.0, -1.0 / 3.0},
                        {-1.0, 1.0, 1.0, 1.0 / 3.0},
                        {-1.5, 2.5, 0.0, (-2.0 + Math.sqrt(17.0)) / 6.0},
                        {-2.5, 1.5, 0.0, (-1.0 + Math.sqrt(10.0)) / 3.0},
                        // Trigonometric function
                        {-2.0 * Math.PI, 2.0 * Math.PI, Math.PI, Math.sin(Math.PI)},
                        {-Math.PI, Math.PI, 0.0, Math.cos(0.0)},
                        {-Math.PI / 2.0, Math.PI / 2.0, 0.0, Math.sin(0.0)},
                        {Math.PI / 4.0, 3.0 * Math.PI / 2.0, Math.PI / 2.0, Math.cos(Math.PI / 2.0)},
                        {Math.PI / 2.0, 2.0 * Math.PI, 0.0, Math.sin(0.0)},
                        // Exponential function
                        {-5.0, 0.0, Math.E, Math.E},
                        {-10.0, -1.0, Math.E, Math.E},
                        {-Math.E, 0.0, -Math.E, Math.E},
                        // Logarithmic function
                        {1.0, 2.0, Math.log(2.0), -Math.log(1.0)},
                        {Math.E, Math.E * 2.0, Math.log(Math.E * 2.0), Math.log(Math.E)},
                        // Sinusoidal function
                        {-2.0 * Math.PI, 2.0 * Math.PI, Math.sin(Math.PI), -Math.sin(Math.PI)},
                        {-Math.PI, Math.PI, Math.sin(0.0), Math.sin(0.0)}});
    }

    public BaseSecantSolverTest(double min, double max, double expectedResult, double expectedFunctionValue) {
        this.min = min;
        this.max = max;
        this.expectedResult = expectedResult;
        this.expectedFunctionValue = expectedFunctionValue;
    }

    @Test
    public void testSolve() throws Exception {
        UnivariateRealFunction f = createTestFunction();
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // Test with default solver (Regula Falsi)
        solver = new BaseSecantSolver(EPSILON, RegulaFalsiSolver.class);
        double result = solver.solve(100, f, min, max, allowedSolution);
        assertEquals(expectedResult, result, EPSILON);
        assertEquals(expectedFunctionValue, f.value(result), EPSILON);

        // Test with Regula Falsi Solver
        RegulaFalsiSolver rfSolver = new RegulaFalsiSolver();
        solver = new BaseSecantSolver(EPSILON, rfSolver);
        result = solver.solve(100, f, min, max, allowedSolution);
        assertEquals(expectedResult, result, EPSILON);
        assertEquals(expectedFunctionValue, f.value(result), EPSILON);

        // Test with Illinois Solver
        solver = new BaseSecantSolver(EPSILON, IllinoisSolver.class);
        result = solver.solve(100, f, min, max, allowedSolution);
        assertEquals(expectedResult, result, EPSILON);
        assertEquals(expectedFunctionValue, f.value(result), EPSILON);

        // Test with Pegasus Solver
        solver = new BaseSecantSolver(EPSILON, PegasusSolver.class);
        result = solver.solve(100, f, min, max, allowedSolution);
        assertEquals(expectedResult, result, EPSILON);
        assertEquals(expectedFunctionValue, f.value(result), EPSILON);

        // Test with Secant Solver (not a bracketing method)
        SecantSolver secantSolver = new SecantSolver();
        UnivariateRealSolver secantBaseSolver = new BaseSecantSolver(EPSILON, secantSolver);
        result = secantBaseSolver.solve(100, f, min, max);
        assertEquals(expectedResult, result, EPSILON);
        assertEquals(expectedFunctionValue, f.value(result), EPSILON);

        // Test with simple bracketing solver
        SimpleBracketedUnivariateSolverFactory factory = new SimpleBracketedUnivariateSolverFactory();
        UnivariateRealSolver simpleSolver = factory.createSolver();
        solver = new BaseSecantSolver(EPSILON, simpleSolver);
        result = solver.solve(100, f, min, max, allowedSolution);
        assertEquals(expectedResult, result, EPSILON);
        assertEquals(expectedFunctionValue, f.value(result), EPSILON);
    }

    @Test(expected = UnknownRootException.class)
    public void testUnknownRootException() {
        UnivariateRealFunction f = createTestFunctionWithRootAtInfinity();
        solver = new BaseSecantSolver(EPSILON, RegulaFalsiSolver.class);
        solver.solve(100, f, min, max, AllowedSolution.ABOVE_SIDE);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        UnivariateRealFunction f = createTestFunctionWithRootAtInfinity();
        solver = new BaseSecantSolver(EPSILON, RegulaFalsiSolver.class);
        solver.solve(100, f, min, max, AllowedSolution.BELOW_SIDE);
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        UnivariateRealFunction f = createTestFunctionWithMaxEval();
        solver = new BaseSecantSolver(EPSILON, RegulaFalsiSolver.class);
        solver.solve(0, f, min, max, AllowedSolution.ANY_SIDE);
    }

    private UnivariateRealFunction createTestFunction() {
        return new PolynomialFunction(new ArrayRealVector(new double[]{-1.0, 1.0, 0.0}));
    }

    private UnivariateRealFunction createTestFunctionWithRootAtInfinity() {
        return new PolynomialFunction(new ArrayRealVector(new double[]{1.0, 0.0, 0.0}));
    }

    private UnivariateRealFunction createTestFunctionWithMaxEval() {
        UnivariateFunction f = x -> Math.log1p(Math.sin(x));
        return (UnivariateRealFunction) FunctionUtils.makeUnivariate(f);
    }
}