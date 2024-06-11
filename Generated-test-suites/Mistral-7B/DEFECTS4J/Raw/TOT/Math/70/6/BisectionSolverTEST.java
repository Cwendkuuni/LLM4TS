package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    @Test
    public void testSolverSimple() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 1);
        BisectionSolver solver = new BisectionSolver();
        double actual = solver.solve(f, -2.0, 2.0);
        double expected = 0.0;
        assertEquals(expected, actual, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolverMaxIterations() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0}, 1);
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        solver.solve(f, -2.0, 2.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolverFunctionEvaluationException() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction f = new FunctionWithException();
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -2.0, 2.0);
    }

    @Test
    public void testSolverEdgeCase() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0, 1.0E-10}, 1);
        double min = -1.0;
        double max = 1.0;
        BisectionSolver solver = new BisectionSolver();
        double actual = solver.solve(f, min, max);
        double expected = 0.0;
        assertEquals(expected, actual, 1e-6);
    }

    @Test
    public void testSolverErrorCase() throws MaxIterationsExceededException, PrecisionException {
        UnivariateFunction f = new FunctionWithDivergentBehaviour();
        double min = -1.0;
        double max = 1.0;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(f, min, max);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

}

class FunctionWithException implements UnivariateFunction {

    @Override
    public double value(double x) {
        throw new ArithmeticException("Divide by zero");
    }

}

class FunctionWithDivergentBehaviour implements UnivariateFunction {

    @Override
    public double value(double x) {
        double result = Math.log(Math.abs(x) + 1.0);
        if (Double.isNaN(result)) {
            result = Double.POSITIVE_INFINITY;
        }
        return result;
    }

}