package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.function.ConstantFunction;
import org.apache.commons.math3.analysis.function.LogisticFunction;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.analysis.function.SineFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolverConstruction() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverNullFunction() {
        new BisectionSolver(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBisectionSolverZeroFunction() {
        UnivariateRealFunction f = new ConstantFunction(0.0);
        new BisectionSolver(f);
    }

    @Test
    public void testBisectionSolverZero() throws MaxIterationsExceededException {
        UnivariateRealFunction f = new ConstantFunction(0.0);
        BisectionSolver solver = new BisectionSolver(f);
        double result = solver.solve(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testBisectionSolverLogisticFunction() throws MaxIterationsExceededException, NumberIsTooSmallException {
        UnivariateRealFunction f = new LogisticFunction(1.0);
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testBisectionSolverSineFunction() throws MaxIterationsExceededException {
        UnivariateRealFunction f = new SineFunction();
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, Double.NEGATIVE_PI, Double.PI);
        assertEquals(0.0, result, 1e-9);
    }

    @Test
    public void testBisectionSolverPowFunction() throws MaxIterationsExceededException {
        UnivariateRealFunction f = new Pow(2.0, new ConstantFunction(0.5));
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(f, 1.0, 4.0);
        assertEquals(2.0, result, 1e-9);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverMaxIterationsExceeded() throws MaxIterationsExceededException {
        UnivariateRealFunction f = new SineFunction();
        BisectionSolver solver = new BisectionSolver();
        solver.setMaxIterations(1);
        solver.solve(f, Double.NEGATIVE_PI, Double.PI);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionEvaluationException() throws FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException("Function evaluation exception");
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
}