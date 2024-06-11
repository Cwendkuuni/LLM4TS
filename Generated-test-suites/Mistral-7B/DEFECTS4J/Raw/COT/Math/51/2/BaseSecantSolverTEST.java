package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxCountExceededError;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    private BaseSecantSolver solver;
    private UnivariateRealFunction function;

    public BaseSecantSolverTest(UnivariateRealFunction function) {
        this.function = function;
    }

    @Parameterized.Parameters(name = "{index}: function({0})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {f -> new BaseSecantSolver(1e-6, UnivariateRealSolver.Method.PEGASUS, f)},
                {f -> new BaseSecantSolver(1e-6, UnivariateRealSolver.Method.ILLINOIS, f)},
                {f -> new BaseSecantSolver(1e-6, UnivariateRealSolver.Method.REGULA_FALSI, f)},
                {f -> new BaseSecantSolver(1e-6, 1e-6, UnivariateRealSolver.Method.PEGASUS, f)},
                {f -> new BaseSecantSolver(1e-6, 1e-6, 1e-6, UnivariateRealSolver.Method.PEGASUS, f)},
                {f -> new BaseSecantSolver(1e-6, 1e-7, 1e-6, UnivariateRealSolver.Method.PEGASUS, f)}
        });
    }

    @Before
    public void setup() {
        solver = (BaseSecantSolver) getSolverInstance();
    }

    private BaseSecantSolver getSolverInstance() {
        return (BaseSecantSolver) new BaseSecantSolver(function) {};
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNullFunction() {
        solver = new BaseSecantSolver((UnivariateRealFunction) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonBracketingFunctionMinGreaterThanMax() {
        function = f -> f.value(1.0) > f.value(0.0) ? 1.0 : 0.0;
        solver = getSolverInstance();
        try {
            solver.solve(10, function, 0.0, 1.0, AllowedSolution.ANY_SIDE);
            fail("Should have thrown NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNonBracketingFunctionMinLessThanMinusMax() {
        function = f -> f.value(1.0) < f.value(-1.0) ? 1.0 : 0.0;
        solver = getSolverInstance();
        try {
            solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
            fail("Should have thrown NoBracketingException");
        } catch (NoBracketingException e) {
            // expected
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testLessAccuracyThanFunctionValue() {
        function = f -> f.value(0.0) - Precision.SAFE_MIN;
        solver = getSolverInstance();
        try {
            solver.solve(10, function, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            fail("Should have thrown NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testSolveFunctionZeroAtMin() {
        function = f -> f.value(0.0) == 0.0;
        double solution = solver.solve(10, function, 0.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, solution, PrecisionUtils.SAFE_EPSILON);
    }

    @Test
    public void testSolveFunctionZeroAtMax() {
        function = f -> f.value(1.0) == 0.0;
        double solution = solver.solve(10, function, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(1.0, solution, PrecisionUtils.SAFE_EPSILON);
    }

    @Test
    public void testSolveFunctionZeroBetweenMinAndMax() {
        function = f -> f.value(0.5) == 0.0;
        double solution = solver.solve(10, function, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.5, solution, PrecisionUtils.SAFE_EPSILON);
    }

    @Test(expected = MaxCountExceededError.class)
    public void testMaxEvalReached() {
        UnivariateRealFunction functionForMaxEval = f -> 1.0 / (Math.sqrt(Math.abs(f.value(0.0))) + 1.0);
        solver = new BaseSecantSolver(1e-6, UnivariateRealSolver.Method.PEGASUS, functionForMaxEval);
        solver.solve(1, functionForMaxEval, 0.0, 0.0, AllowedSolution.ANY_SIDE);
    }
}