package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons. math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

public class BisectionSolverTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructor() {
        new BisectionSolver(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    public void testSolvePositiveResult() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1.5, 1.5);
        assertEquals(0.0, result, 1E-9);
    }

    @Test
    public void testSolveNegativeResult() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, -1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1.5, 1.5);
        assertEquals(-0.0, result, 1E-9);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterations() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver(1);
        solver.solve(f, -1.5, 1.5);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluation() throws Exception {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new FunctionEvaluationException();
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -1.5, 1.5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveNumberIsTooSmall() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver(0.0);
        solver.solve(f, -1.5, 1.5);
    }

    @Test
    public void testSolveZeroFunction() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{0.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, -1.5, 1.5);
        assertEquals(0.0, result, 1E-9);
    }

    @Test
    public void testSolveDegenerateFunction() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{0.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();
        assertEquals(Double.NaN, solver.solve(f, 0.0, 1.0), 0.0);
    }
}