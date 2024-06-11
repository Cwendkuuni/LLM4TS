package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

/**
 * Test class for {@link BaseSecantSolver}.
 */
@RunWith(Parameterized.class)
public class BaseSecantSolverTest {

    /** The solver to test. */
    private BaseSecantSolver solver;

    /** The function to solve. */
    private UnivariateRealFunction function;

    /** The minimum value of the interval. */
    private double min;

    /** The maximum value of the interval. */
    private double max;

    /** The allowed solution. */
    private AllowedSolution allowedSolution;

    /**
     * Constructor.
     *
     * @param function Univariate real function
     * @param min Minimum value of the interval
     * @param max Maximum value of the interval
     * @param allowedSolution Allowed solution
     */
    public BaseSecantSolverTest(UnivariateRealFunction function, double min, double max, AllowedSolution allowedSolution) {
        this.function = function;
        this.min = min;
        this.max = max;
        this.allowedSolution = allowedSolution;
    }

    /**
     * Generates test data for the {@link #solveTest(BaseSecantSolver, UnivariateRealFunction, double, double, AllowedSolution)}
     * method.
     *
     * @return Collection of test data
     */
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -2.0, 2.0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -2.0, 1.0, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -2.0, 3.0, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -3.0, 2.0, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -3.0, 3.0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -1.0, 1.0, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), -1.0, 2.0, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), 0.0, 2.0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), 0.0, 1.0, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.pow(x, 2) - 1), 0.0, 3.0, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.exp(x) - 2), -5.0, 0.0, AllowedSolution.BELOW_SIDE},
                {new PolynomialFunction(x -> Math.exp(x) - 2), -5.0, 1.0, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.exp(x) - 2), -5.0, 2.0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.exp(x) - 2), 0.0, 2.0, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.exp(x) - 2), 1.0, 2.0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.sin(x)), -3.0, 3.0, AllowedSolution.ANY_SIDE},
                {new PolynomialFunction(x -> Math.sin(x)), 0.0, 2.0, AllowedSolution.BELOW_SIDE},
                {new PolynomialFunction(x -> Math.sin(x)), 0.0, 3.0, AllowedSolution.RIGHT_SIDE},
                {new PolynomialFunction(x -> Math.sin(x)), -2.0, 2.0, AllowedSolution.LEFT_SIDE},
                {new PolynomialFunction(x -> Math.sin(x)), -2.0, 1.0, AllowedSolution.ABOVE_SIDE},
        });
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, AllowedSolution)}
     * method.
     *
     * @param solver The solver to test
     * @param function The function to solve
     * @param min The minimum value of the interval
     * @param max The maximum value of the interval
     * @param allowedSolution The allowed solution
     */
    @Test(expected = NoBracketingException.class)
    public void solveTest() {
        solver = new BaseSecantSolver(allowedSolution.getMethod()) {
            @Override
            public double solve(int maxEval, UnivariateRealFunction f,
                                double min, double max, double startValue) {
                return super.solve(maxEval, f, min, max, startValue);
            }
        };
        solver.solve(100, function, min, max, allowedSolution);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, AllowedSolution)}
     * method with a custom solver.
     *
     * @param solver The solver to test
     * @param function The function to solve
     * @param min The minimum value of the interval
     * @param max The maximum value of the interval
     * @param allowedSolution The allowed solution
     */
    @Test
    public void solveTest(BaseSecantSolver solver, UnivariateRealFunction function,
                          double min, double max, AllowedSolution allowedSolution) {
        try {
            solver.solve(100, function, min, max, allowedSolution);
            assertEquals("The solver should have found a solution", allowedSolution.getValue(), solver.getRoot());
        } catch (NoBracketingException | MathIllegalArgumentException | MaxEvalException e) {
            // Expected exceptions
        }
    }
}

enum AllowedSolution {
    /**
     * Solution on any side of the root.
     */
    ANY_SIDE(Double.NaN),
    /**
     * Solution on the left side of the root.
     */
    LEFT_SIDE(1.0),
    /**
     * Solution on the right side of the root.
     */
    RIGHT_SIDE(-1.0),
    /**
     * Solution below the root.
     */
    BELOW_SIDE(-2.0),
    /**
     * Solution above the root.
     */
    ABOVE_SIDE(2.0);

    /** The value of the solution. */
    private final double value;

    /**
     * Constructor.
     *
     * @param value Value of the solution
     */
    AllowedSolution(double value) {
        this.value = value;
    }

    /**
     * Gets the value of the solution.
     *
     * @return Value of the solution
     */
    public double getValue() {
        return value;
    }

    /**
     * Gets the method of the solver.
     *
     * @return Method of the solver
     */
    public Method getMethod() {
        switch (this) {
            case LEFT_SIDE:
            case BELOW_SIDE:
                return Method.ILLINOIS;
            default:
                return Method.REGULA_FALSI;
        }
    }
}