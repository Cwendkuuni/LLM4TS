package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test class for BisectionSolver.
 */
public class BisectionSolverTest {

    /**
     * Test constructor with UnivariateRealFunction argument.
     */
    @Test
    public void testConstructorWithFunction() {
        UnivariateFunction function = Polynomials.createPolynomial(3, new double[]{-1.0, 1.0, -1.0, 1.0});
        BisectionSolver solver = new BisectionSolver(function);
    }

    /**
     * Test no-argument constructor.
     */
    @Test
    public void testNoArgumentConstructor() {
        BisectionSolver solver = new BisectionSolver();
    }

    /**
     * Test solve method with minimum and maximum arguments.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveWithMinAndMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction function = Polynomials.createPolynomial(1, new double[]{1.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.0, 2.0);
        assertEquals(1.0, root, 1E-6);
    }

    /**
     * Test solve method with minimum, maximum, and initial arguments.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveWithMinAndMaxAndInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction function = Polynomials.createPolynomial(1, new double[]{1.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(function, 1.0, 2.0, 1.5);
        assertEquals(1.0, root, 1E-6);
    }

    /**
     * Test solve method with deprecated solve method.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testSolveWithDeprecatedMethod() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction function = Polynomials.createPolynomial(1, new double[]{1.0});
        BisectionSolver solver = new BisectionSolver();
        double root = solver.solve(1.0, 2.0);
        assertEquals(1.0, root, 1E-6);
    }

    /**
     * Test max iterations exceeded exception.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction function = f -> Math.pow(Math.E, -Math.log(Math.abs(f)));
        BisectionSolver solver = new BisectionSolver();
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, -1.0, 0.0));
    }

    /**
     * Test function evaluation exception.
     * @throws MaxIterationsExceededException
     * @throws FunctionEvaluationException
     */
    @Test
    public void testFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateFunction function = f -> { throw new ArithmeticException("Divide by zero"); };
        BisectionSolver solver = new BisectionSolver();
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(function, 0.0, 1.0));
    }

    /**
     * Test unsupported operation exception.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedOperationException() {
        UnivariateRealFunction function = f -> {};
        BisectionSolver solver = new BisectionSolver(function);
        solver.solve(new UnivariateRealVector(new double[]{1.0, 2.0}));
    }
}