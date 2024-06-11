package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RuntimeConfiguration;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.Method;

/**
 * Test class for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * method with a polynomial function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test
    public void testPolynomialFunction() throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new PolynomialFunction(new double[] {1, -2, 1});
        final double root = 1.0;
        final double x0 = 2.0;
        final double x1 = 0.0;
        final double solution = solver.solve(maxEval, f, x0, x1, AllowedSolution.ANY_SIDE);
        assertEquals(root, Precision.round(solution, epsilon), epsilon);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with a polynomial function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test
    public void testPolynomialFunctionAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new PolynomialFunction(new double[] {1, -2, 1});
        final double root = 1.0;
        final double x0 = 2.0;
        final double x1 = 0.0;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final double solution = solver.solve(maxEval, f, x0, x1, allowedSolution);
        assertEquals(root, Precision.round(solution, epsilon), epsilon);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * method with a simple function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test
    public void testSimpleFunction() throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }

            @Override
            public double derivative(double x) {
                return 1.0 - Math.cos(x);
            }
        };
        final double root = 1.17619;
        final double x0 = -1.5;
        final double x1 = 1.5;
        final double solution = solver.solve(maxEval, f, x0, x1, AllowedSolution.ANY_SIDE);
        assertEquals(root, Precision.round(solution, epsilon), epsilon);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with a simple function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test
    public void testSimpleFunctionAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }

            @Override
            public double derivative(double x) {
                return 1.0 - Math.cos(x);
            }
        };
        final double root = 1.17619;
        final double x0 = -1.5;
        final double x1 = 1.5;
        final AllowedSolution allowedSolution = AllowedSolution.LEFT_SIDE;
        final double solution = solver.solve(maxEval, f, x0, x1, allowedSolution);
        assertEquals(root, Precision.round(solution, epsilon), epsilon);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * method with an incorrect initial interval.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }

            @Override
            public double derivative(double x) {
                return 1.0 - Math.cos(x);
            }
        };
        final double x0 = 1.5;
        final double x1 = 1.5;
        solver.solve(maxEval, f, x0, x1, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with an incorrect initial interval.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test(expected = NoBracketingException.class)
    public void testNoBracketingAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - x;
            }

            @Override
            public double derivative(double x) {
                return 1.0 - Math.cos(x);
            }
        };
        final double x0 = 1.5;
        final double x1 = 1.5;
        solver.solve(maxEval, f, x0, x1, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)}
     * method with a discontinuous function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test
    public void testDiscontinuousFunction() throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sin(Math.PI / 2 + x) - x;
                } else {
                    return Math.sin(Math.PI / 2 - x) - x;
                }
            }

            @Override
            public double derivative(double x) {
                if (x > 0) {
                    return 1.0 - Math.cos(Math.PI / 2 + x);
                } else {
                    return 1.0 - Math.cos(Math.PI / 2 - x);
                }
            }
        };
        final double root = 1.0;
        final double x0 = -2.0;
        final double x1 = 2.0;
        final double solution = solver.solve(maxEval, f, x0, x1, AllowedSolution.ANY_SIDE);
        assertEquals(root, Precision.round(solution, epsilon), epsilon);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with a discontinuous function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test
    public void testDiscontinuousFunctionAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return Math.sin(Math.PI / 2 + x) - x;
                } else {
                    return Math.sin(Math.PI / 2 - x) - x;
                }
            }

            @Override
            public double derivative(double x) {
                if (x > 0) {
                    return 1.0 - Math.cos(Math.PI / 2 + x);
                } else {
                    return 1.0 - Math.cos(Math.PI / 2 - x);
                }
            }
        };
        final double root = 1.0;
        final double x0 = -2.0;
        final double x1 = 2.0;
        final AllowedSolution allowedSolution = AllowedSolution.RIGHT_SIDE;
        final double solution = solver.solve(maxEval, f, x0, x1, allowedSolution);
        assertEquals(root, Precision.round(solution, epsilon), epsilon);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double)} method
     * with a complex function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testComplexFunction() throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(-x) + Math.log(Math.abs(x));
            }

            @Override
            public double derivative(double x) {
                return -1.0 / (2 * Math.sqrt(Math.abs(x))) + 1.0 / Math.abs(x);
            }
        };
        final double x0 = -2.0;
        final double x1 = 2.0;
        solver.solve(maxEval, f, x0, x1, AllowedSolution.ANY_SIDE);
    }

    /**
     * Tests the {@link BaseSecantSolver#solve(int, UnivariateFunction, double, double, double, AllowedSolution)}
     * method with a complex function.
     *
     * @throws NoBracketingException if the initial interval does not bracket a root
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testComplexFunctionAllowedSolution()
            throws NoBracketingException, MaxEvalException {
        final double epsilon = 1e-9;
        final int maxEval = 100;
        final BaseSecantSolver solver = new BaseSecantSolver(epsilon, Method.ILLINOIS);

        final UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sqrt(-x) + Math.log(Math.abs(x));
            }

            @Override
            public double derivative(double x) {
                return -1.0 / (2 * Math.sqrt(Math.abs(x))) + 1.0 / Math.abs(x);
            }
        };
        final double x0 = -2.0;
        final double x1 = 2.0;
        solver.solve(maxEval, f, x0, x1, AllowedSolution.ANY_SIDE);
    }
}