package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1.0E-6, solver.getAbsoluteAccuracy(), 1E-8);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructor() {
        new BisectionSolver(new UnivariateFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        });
    }

    @Test
    public void testSolveWithMinMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3);
        double expectedResult = 0.5;
        double min = -2.0;
        double max = 2.0;
        double result = solver.solve(f, min, max);
        assertEquals(expectedResult, result, Precision.SAFE_EPSILON);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3);
        double min = -2.0;
        double max = 2.0;
        solver.setMaximalIterationCount(0);
        solver.solve(f, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = new FunctionWithZeroException();
        double min = 0.0;
        double max = 1.0;
        solver.solve(f, min, max);
    }

    @Test
    public void testSolveWithInitialValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, 2.0, 1.0}, 3));
        double min = -2.0;
        double max = 2.0;
        double initial = 0.0;
        double expectedResult = 1.0;
        double result = solver.solve(f, min, max, initial);
        assertEquals(expectedResult, result, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveWithInitialValueTooCloseToMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3));
        double min = -2.0;
        double max = 2.0;
        double initial = min + 1.0E-10;
        try {
            solver.solve(f, min, max, initial);
            fail("Should have thrown MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithInitialValueTooCloseToMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3));
        double min = -2.0;
        double max = 2.0;
        double initial = max - 1.0E-10;
        try {
            solver.solve(f, min, max, initial);
            fail("Should have thrown MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithInitialValueInBetweenMinAndMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3));
        double min = -2.0;
        double max = 2.0;
        double initial = -1.0;
        double expectedResult = 0.5;
        double result = solver.solve(f, min, max, initial);
        assertEquals(expectedResult, result, Precision.SAFE_EPSILON);
    }

    @Test
    public void testSolveWithMinEqualsMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3));
        double min = 0.0;
        double max = 0.0;
        try {
            solver.solve(f, min, max);
            fail("Should have thrown FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testSolveWithMinGreaterThanMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3));
        double min = 2.0;
        double max = -2.0;
        try {
            solver.solve(f, min, max);
            fail("Should have thrown FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyInterval() {
        BisectionSolver solver = new BisectionSolver();
        UnivariateFunction f = FunctionUtils.negate(new PolynomialFunction(new double[] {1.0, -1.0, 1.0}, 3));
        double min = 2.0;
        double max = -2.0;
        solver.verifyInterval(min, max);
        fail("Should have thrown FunctionEvaluationException");
    }

    @Test
    public void testMidpoint() {
        BisectionSolver solver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        assertEquals(0.5, solver.UnivariateRealSolverUtils.midpoint(min, max), Precision.SAFE_EPSILON);
    }

    private static class FunctionWithZeroException implements UnivariateFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (Double.doubleEquals(x, 0.0)) {
                throw new ZeroException();
            }
            return 0.0;
        }
    }
}