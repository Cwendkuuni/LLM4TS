package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.BesselFunction;
import org.apache.commons.math3.special.Psi;
import org.apache.commons.math3.special.SpecialFunction;

import org.apache.commons.math.analysis.solvers.BisectionSolver;

/**
 * Test class for the BisectionSolver class.
 */
public class BisectionSolverTest {

    /**
     * Test constructor with given function.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBisectionSolverConstructorWithFunction()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver(f);
        double root = 0.75;
        double result = bisectionSolver.solve(0.0, 1.0);
        assertEquals("The computed root must be equal to the expected root.", root, result, 1e-6);
    }

    /**
     * Test default constructor.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testBisectionSolverDefaultConstructor()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        bisectionSolver.setFunction(f);
        double root = 0.75;
        double result = bisectionSolver.solve(0.0, 1.0);
        assertEquals("The computed root must be equal to the expected root.", root, result, 1e-6);
    }

    /**
     * Test solving function with given minimum, maximum and initial values.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testSolveWithMinimumMaximumAndInitial()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        double initial = 0.5;
        double root = 0.75;
        double result = bisectionSolver.solve(f, min, max, initial);
        assertEquals("The computed root must be equal to the expected root.", root, result, 1e-6);
    }

    /**
     * Test solving function with given minimum and maximum values.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test
    public void testSolveWithMinimumAndMaximum()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        double root = 0.75;
        double result = bisectionSolver.solve(f, min, max);
        assertEquals("The computed root must be equal to the expected root.", root, result, 1e-6);
    }

    /**
     * Test edge case: function values at minimum and maximum are equal.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMinimumAndMaximumWithEqualFunctionValues()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 0.0, 0.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 1.0;
        double max = 1.0;
        bisectionSolver.solve(f, min, max);
    }

    /**
     * Test edge case: function value at minimum is greater than function value at maximum.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveWithMinimumAndMaximumWithFunctionValueAtMinimumGreaterThanFunctionValueAtMaximum()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 0.0, 0.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = -1.0;
        double max = 1.0;
        bisectionSolver.solve(f, min, max);
    }

    /**
     * Test edge case: function value at initial value is equal to zero.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testSolveWithMinimumMaximumAndInitialWithFunctionValueAtInitialEqualToZero()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0, 0.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        double initial = 0.0;
        bisectionSolver.solve(f, min, max, initial);
    }

    /**
     * Test exception handling: max iterations exceeded.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        bisectionSolver.setMaxIterations(0);
        double min = 0.0;
        double max = 1.0;
        bisectionSolver.solve(f, min, max);
    }

    /**
     * Test exception handling: function evaluation.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException()
        throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{Double.POSITIVE_INFINITY, 0.0, 0.0, 0.0, 0.0});
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        bisectionSolver.solve(f, min, max);
    }

    /**
     * Test exception handling: max value is too large.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     * @throws NumberIsTooLargeException
     */
    @org.junit.Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException()
        throws MaxIterationsExceededException, FunctionEvaluationException, NumberIsTooLargeException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return BesselFunction.BesselI(0, x) / Psi.digamma(1);
            }
        };
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 0.0;
        double max = Double.POSITIVE_INFINITY;
        bisectionSolver.solve(f, min, max);
    }

    /**
     * Test exception handling: singular matrix.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     * @throws SingularMatrixException
     */
    @org.junit.Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException()
        throws MaxIterationsExceededException, FunctionEvaluationException, SingularMatrixException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0}, {1.0, 1.0}
                });
                RealVector x0 = new ArrayRealVector(new double[]{0.0, 0.0});
                return SpecialFunction.sin(x) / matrix.inverse().operate(x0);
            }
        };
        BisectionSolver bisectionSolver = new BisectionSolver();
        double min = 0.0;
        double max = 1.0;
        bisectionSolver.solve(f, min, max);
    }

}