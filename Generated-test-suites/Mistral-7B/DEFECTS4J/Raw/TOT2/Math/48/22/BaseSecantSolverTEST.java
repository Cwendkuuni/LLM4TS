package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /** Function for testing. */
    private UnivariateRealFunction f;

    /** Sut. */
    private BaseSecantSolver solver;

    /**
     * Set up test.
     */
    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
    }

    /**
     * Test basic functionality of the solver.
     *
     * @throws ConvergenceException          if the solver does not converge.
     * @throws MaxEvalExceededException      if the maximum number of evaluations is exceeded.
     * @throws MathIllegalArgumentException   if the function is not bracketing.
     */
    @Test
    public void testBasicFunctionality() throws ConvergenceException,
            MaxEvalExceededException, MathIllegalArgumentException {
        double root = solver.solve(f, -1.0, 1.0, 0.0);
        assertEquals("Root not found", -2.0, Precision.round(root, 5));
    }

    /**
     * Test that the solver returns the correct root when given a bracketing
     * function.
     *
     * @throws ConvergenceException          if the solver does not converge.
     * @throws MaxEvalExceededException      if the maximum number of evaluations is exceeded.
     * @throws MathIllegalArgumentException   if the function is not bracketing.
     */
    @Test
    public void testBracketingFunctionality() throws ConvergenceException,
            MaxEvalExceededException, MathIllegalArgumentException {
        double root = solver.solve(f, -1.5, 0.5, 0.0);
        assertEquals("Root not found", -1.5, Precision.round(root, 5));
    }

    /**
     * Test that the solver returns the correct root when given a non-bracketing
     * function.
     *
     * @throws ConvergenceException          if the solver does not converge.
     * @throws MaxEvalExceededException      if the maximum number of evaluations is exceeded.
     * @throws MathIllegalArgumentException   if the function is not bracketing.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testNonBracketingFunctionality() throws ConvergenceException,
            MaxEvalExceededException {
        solver.solve(f, 0.5, 0.5, 0.0);
    }

    /**
     * Test that the solver can handle a non-differentiable function.
     *
     * @throws ConvergenceException          if the solver does not converge.
     * @throws MaxEvalExceededException      if the maximum number of evaluations is exceeded.
     */
    @Test
    public void testNonDifferentiableFunctionality() throws ConvergenceException,
            MaxEvalExceededException {
        UnivariateRealFunction fNonDifferentiable = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x >= 0) {
                    return x * Math.log(x);
                } else {
                    return -x * Math.log(-x);
                }
            }
        };
        solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double root = solver.solve(fNonDifferentiable, -1.0, 1.0, 0.0);
        assertEquals("Root not found", 0.0, Precision.round(root, 5));
    }

    /**
     * Test that the solver can handle a singularity at the root.
     *
     * @throws ConvergenceException          if the solver does not converge.
     * @throws MaxEvalExceededException      if the maximum number of evaluations is exceeded.
     */
    @Test
    public void testSingularityAtRootFunctionality() throws ConvergenceException,
            MaxEvalExceededException {
        UnivariateRealFunction fSingularityAtRoot = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return Math.pow(x, 2) - 1;
            }
        };
        solver = new RegulaFalsiSolver(1e-6, new RegulaFalsiSolver.RegulaFalsiMethod());
        double root = solver.solve(fSingularityAtRoot, -1.0, 1.0, 0.0);
        assertEquals("Root not found", 0.0, Precision.round(root, 5));
    }
}