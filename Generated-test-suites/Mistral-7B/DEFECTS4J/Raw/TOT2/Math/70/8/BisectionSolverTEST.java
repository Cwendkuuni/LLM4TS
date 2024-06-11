package org.junit.jupiter.params.provider.test;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        assertEquals(0.0, bisectionSolver.solve(f, 0.0, 1.0, 0.5), 0.01);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBisectionSolverNoFunctionGiven() {
        BisectionSolver bisectionSolver = new BisectionSolver();
        bisectionSolver.solve(0.0, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testBisectionSolverMaxIterationsExceeded() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0});
        BisectionSolver bisectionSolver = new BisectionSolver(1, 1E-6);
        bisectionSolver.solve(f, 0.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBisectionSolverFunctionEvaluationException() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0}, new double[]{Double.NaN});
        BisectionSolver bisectionSolver = new BisectionSolver();
        bisectionSolver.solve(f, 0.0, 1.0);
    }

    @Test
    public void testBisectionSolverDefaultConstructor() {
        BisectionSolver bisectionSolver = new BisectionSolver();
        assertThrows(IllegalArgumentException.class, () -> bisectionSolver.solve(0.0, 1.0));
    }

    @Test
    public void testBisectionSolverWithInitialValue() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0});
        BisectionSolver bisectionSolver = new BisectionSolver(f, 1.0);
        assertEquals(0.75, bisectionSolver.solve(0.0, 1.0), 0.01);
    }

    @Test
    public void testBisectionSolverEdgeCases() {
        UnivariateFunction f1 = new PolynomialFunction(new double[]{0.0, 2.0, 2.0}, new double[]{0.0, 0.0, 0.0});
        UnivariateFunction f2 = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{1.0, 1.0, 1.0});

        BisectionSolver bisectionSolver1 = new BisectionSolver();
        BisectionSolver bisectionSolver2 = new BisectionSolver();

        try {
            bisectionSolver1.solve(f1, 0.0, 0.0);
        } catch (MaxIterationsExceededException e) {
            assertEquals(e.getMessage(), "Maximal number of iterations (100) was exceeded.");
        }

        try {
            bisectionSolver2.solve(f2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "min and max must not be equal.");
        }
    }
}

//