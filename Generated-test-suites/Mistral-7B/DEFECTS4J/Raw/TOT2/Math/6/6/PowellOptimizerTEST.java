package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalTypeChecker;
import org.apache.commons.math3.optim.nonlinear.SimplePointValuePair;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionCreator;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerFactory;
import org.apache.commons.math3.special.E;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * Test optimization of a simple function.
     */
    @org.junit.Test
    public void testSimpleOptimization() {
        final double epsilon = 1e-5;

        // Create the optimizer.
        final PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        // Define the function to optimize.
        final UnivariateFunction f = new UnivariateFunctionCreator() {
            @Override
            public UnivariateFunction createFunction(double x) {
                return new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return Math.pow(x - 3.0, 2.0);
                    }
                };
            }
        }.createFunction(0.0);

        // Optimize the function.
        final UnivariateOptimizer opt = UnivariateOptimizerFactory.create("Powell", optimizer);
        final PointValuePair solution = opt.optimize(f, new SimpleGoalTypeChecker(GoalType.MINIMIZE), new MaxEval(1000));

        // Check the solution.
        assertEquals(9.0, solution.getPoint(), epsilon);
        assertEquals(0.0, solution.getValue(), epsilon);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @org.junit.Test
    public void testMultiLocalMinima() {
        final double epsilon = 1e-5;

        // Create the optimizer.
        final PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        // Define the function to optimize.
        final UnivariateFunction f = new UnivariateFunctionCreator() {
            @Override
            public UnivariateFunction createFunction(double x) {
                return new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return Math.pow(x - 2.0, 2.0) * (Math.sin(x) - 0.5 * Math.cos(2 * x));
                    }
                };
            }
        }.createFunction(0.0);

        // Optimize the function.
        final UnivariateOptimizer opt = UnivariateOptimizerFactory.create("Powell", optimizer);
        final PointValuePair solution = opt.optimize(f, new SimpleGoalTypeChecker(GoalType.MINIMIZE), new MaxEval(1000));

        // Check the solution.
        assertEquals(-2.14142137, solution.getPoint(), epsilon);
        assertEquals(3.28518933, solution.getValue(), epsilon);
    }

    /**
     * Test optimization of a function with no local minima.
     */
    @org.junit.Test(expected = org.apache.commons.math3.optim.NoSolutionException.class)
    public void testNoLocalMinima() {
        final double epsilon = 1e-5;

        // Create the optimizer.
        final PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        // Define the function to optimize.
        final UnivariateFunction f = new UnivariateFunctionCreator() {
            @Override
            public UnivariateFunction createFunction(double x) {
                return new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return Math.sin(x) / x;
                    }
                };
            }
        }.createFunction(0.0);

        // Optimize the function.
        final UnivariateOptimizer opt = UnivariateOptimizerFactory.create("Powell", optimizer);
        final PointValuePair solution = opt.optimize(f, new SimpleGoalTypeChecker(GoalType.MINIMIZE), new MaxEval(1000));

        // Check for no solution.
        fail();
    }

    /**
     * Test optimization with an invalid input.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        // Create the optimizer with an invalid input.
        new PowellOptimizer(-1.0, 1.0);
    }

    /**
     * Test optimization with a convergence checker.
     */
    @org.junit.Test
    public void testConvergenceChecker() {
        final double epsilon = 1e-5;

        // Create the optimizer.
        final PowellOptimizer optimizer = new PowellOptimizer(1e-5, 1e-5);

        // Define a convergence checker.
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iterationNumber, PointValuePair startValue, PointValuePair currentValue) {
                return Math.abs(currentValue.getValue() - startValue.getValue()) < epsilon;
            }
        };

        // Define the function to optimize.
        final UnivariateFunction f = new UnivariateFunctionCreator() {
            @Override
            public UnivariateFunction createFunction(double x) {
                return new UnivariateFunction() {
                    @Override
                    public double value(double x) {
                        return Math.pow(x - 3.0, 2.0) + E.E;
                    }
                };
            }
        }.createFunction(0.0);

        // Optimize the function.
        final UnivariateOptimizer opt = UnivariateOptimizerFactory.create("Powell", optimizer, checker);
        final PointValuePair solution = opt.optimize(f, new SimpleGoalTypeChecker(GoalType.MINIMIZE), new MaxEval(1000));

        // Check the solution.
        assertEquals(9.0, solution.getPoint(), epsilon);
        assertEquals(0.0, solution.getValue(), epsilon);
    }
}