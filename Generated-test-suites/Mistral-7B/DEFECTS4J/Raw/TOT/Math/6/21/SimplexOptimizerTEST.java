package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.initialguess.AbstractInitialGuess;
import org.apache.commons.math3.optim.nonlinear.simplextools.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.simplextools.NelderMeadSimplexBuilder;
import org.apache.commons.math3.optim.point.*;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test optimization of a simple function with a single minimum.
     */
    @org.junit.Test
    public void testOptimizationOfSimpleFunctionWithMinimum() {
        // Define a function with a single minimum.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(x[1] - 2, 2);
            }
        };

        // Define an initial simplex.
        AbstractInitialGuess initialGuess = new DistanceInitialGuess(2, 5);
        NelderMeadSimplex simplex = new NelderMeadSimplexBuilder()
            .setMaxIterations(50)
            .setInitialGuess(initialGuess)
            .build();

        // Define an optimizer with a simple convergence checker.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 0));

        // Perform optimization.
        PointValuePair optimum = optimizer.optimize(50, f, GoalType.MINIMIZE,
                new OptimizationData[] { simplex });

        // Check the result.
        assertEquals("The optimization did not find the minimum.", 1.0, optimum.getPoint()[0], 1e-5);
        assertEquals("The optimization did not find the minimum value.",
                0.0, optimum.getValue(), 1e-5);
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @org.junit.Test
    public void testOptimizationOfFunctionWithMultipleLocalMinima() {
        // Define a function with multiple local minima.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
            }
        };

        // Define an initial simplex.
        AbstractInitialGuess initialGuess = new DistanceInitialGuess(2, 5);
        NelderMeadSimplex simplex = new NelderMeadSimplexBuilder()
            .setMaxIterations(50)
            .setInitialGuess(initialGuess)
            .build();

        // Define an optimizer with a simple convergence checker.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 0));

        // Perform optimization.
        PointValuePair optimum1 = optimizer.optimize(50, f, GoalType.MINIMIZE,
                new OptimizationData[] { simplex });

        // Check the first result.
        assertEquals("The optimization did not find the first minimum.",
                0.0, optimum1.getValue(), 1e-5);

        // Reset the simplex and perform optimization again.
        simplex.reset();
        PointValuePair optimum2 = optimizer.optimize(50, f, GoalType.MINIMIZE,
                new OptimizationData[] { simplex });

        // Check the second result.
        assertEquals("The optimization did not find the second minimum.",
                0.0, optimum2.getValue(), 1e-5);

        // Check that the minima are different.
        assertNotSame("The optimization found the same minimum twice.",
                optimum1.getPoint(), optimum2.getPoint());
    }

    /**
     * Test optimization with an invalid number of dimensions.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testOptimizationWithInvalidNumberOfDimensions() {
        // Define an optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 0));

        // Define an invalid multivariate function.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                throw new RuntimeException("Function requires 2 dimensions.");
            }
        };

        // Perform optimization.
        optimizer.optimize(50, f, GoalType.MINIMIZE, new OptimizationData[0]);
    }

    /**
     * Test optimization with a null multivariate function.
     */
    @org.junit.Test(expected = NullArgumentException.class)
    public void testOptimizationWithNullMultivariateFunction() {
        // Define an optimizer.
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-5, 0));

        // Perform optimization with a null multivariate function.
        optimizer.optimize(50, null, GoalType.MINIMIZE, new OptimizationData[0]);
    }
}