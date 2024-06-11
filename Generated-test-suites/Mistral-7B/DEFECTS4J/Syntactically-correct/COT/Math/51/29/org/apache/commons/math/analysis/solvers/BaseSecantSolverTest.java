package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFinderException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for the BaseSecantSolver class.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#ILLINOIS} method.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testSolveIllinois() throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Create a solver with the Illinois method
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Solve the equation
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);

        // Check the result
        assertEquals("The root is not correct", 2.0, Precision.round(root, 2));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link BaseSecantSolver#ILLINOIS} method and {@link AllowedSolution#LEFT_SIDE} allowed solution.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testSolveIllinoisLeftSide()
            throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        // Create a solver with the Illinois method and left side allowed solution
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS,
                AllowedSolution.LEFT_SIDE);

        // Solve the equation
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);

        // Check the result
        assertEquals("The root is not correct", -1.0, Precision.round(root, 2));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#PEGASUS} method.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testSolvePegasus()
            throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }
        };

        // Create a solver with the Pegasus method
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Solve the equation
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);

        // Check the result
        assertEquals("The root is not correct", 1.0, Precision.round(root, 2));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link BaseSecantSolver#PEGASUS} method and {@link AllowedSolution#RIGHT_SIDE} allowed solution.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testSolvePegasusRightSide()
            throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.cos(x) - 0.5;
            }
        };

        // Create a solver with the Pegasus method and right side allowed solution
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS,
                AllowedSolution.RIGHT_SIDE);

        // Solve the equation
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);

        // Check the result
        assertEquals("The root is not correct", 1.0, Precision.round(root, 2));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with {@link BaseSecantSolver#REGULA_FALSI} method.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testSolveRegulaFalsi()
            throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        // Create a solver with the Regula Falsi method
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Solve the equation
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);

        // Check the result
        assertEquals("The root is not correct", 2.0, Precision.round(root, 2));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with {@link BaseSecantSolver#REGULA_FALSI} method and {@link AllowedSolution#LEFT_SIDE} allowed solution.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testSolveRegulaFalsiLeftSide()
            throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };

        // Create a solver with the Regula Falsi method and left side allowed solution
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI,
                AllowedSolution.LEFT_SIDE);

        // Solve the equation
        double root = solver.solve(10, f, -3.0, 3.0, 0.0);

        // Check the result
        assertEquals("The root is not correct", Math.log(2), Precision.round(root, 6));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with all possible combinations of methods and allowed solutions.
     *
     * @throws UnsupportedOperationException if the {@code solve} method with the given arguments is not supported
     * @throws RootFinderException if the root cannot be found
     * @throws MaxEvalException if the maximum number of evaluations is reached
     */
    @Test
    public void testAllMethodsAndAllowedSolutions()
            throws UnsupportedOperationException, RootFinderException, MaxEvalException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        // Create a solver for all possible combinations of methods and allowed solutions
        for (BaseSecantSolver.Method method : BaseSecantSolver.Method.values()) {
            for (AllowedSolution allowed : AllowedSolution.values()) {
                // Create a solver
                BaseSecantSolver solver = null;
                switch (method) {
                case REGULA_FALSI:
                    solver = new BaseSecantSolver(1e-6, method, allowed);
                    break;
                case ILLINOIS:
                    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS, allowed);
                    break;
                case PEGASUS:
                    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, allowed);
                    break;
                }

                // Solve the equation
                double root = solver.solve(10, f, -3.0, 3.0, 0.0);

                // Check the result
                switch (allowed) {
                case ANY_SIDE:
                    assertEquals("The root is not correct", -1.0, Precision.round(root, 2));
                    break;
                case LEFT_SIDE:
                    assertEquals("The root is not correct", -1.0, Precision.round(root, 2));
                    break;
                case RIGHT_SIDE:
                    assertEquals("The root is not correct", 1.0, Precision.round(root, 2));
                    break;
                case BELOW_SIDE:
                    assertEquals("The root is not correct", -1.0, Precision.round(root, 2));
                    break;
                case ABOVE_SIDE:
                    assertEquals("The root is not correct", 1.0, Precision.round(root, 2));
                    break;
                }
            }
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}
     * with bracketing condition not satisfied.
     *
     * @throws UnsupportedOperationException if the method is not supported
     * @throws RootFinderException if the root cannot be found
     */
    @Test(expected = RootFinderException.class)
    public void testVerifyBracketingFail() throws UnsupportedOperationException {
        // Create a solver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Set initial bounds with the same value
        solver.setMin(2.0);
        solver.setMax(2.0);

        // Call the method that will throw an exception
        solver.verifyBracketing(2.0, 2.0);
    }
}