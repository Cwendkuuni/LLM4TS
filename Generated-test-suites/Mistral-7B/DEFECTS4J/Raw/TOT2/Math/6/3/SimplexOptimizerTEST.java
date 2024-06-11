package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.SimplexOptimizer;
import org.apache.commons.math3.optimization.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optimization.simplex.NelderMeadSimplex;
import org.apache.commons.math3.optimization.simplex.NelderMeadSimplexBuilder;
import org.apache.commons.math3.optimization.simplex.SimplexPoint;
import org.junit.Test;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {
    /**
     * Test for the optimization of a simple polynomial function.
     */
    @Test
    public void testSimpleOptimization() {
        // Create the optimizer with a simple convergence checker.
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define the objective function.
        MultivariateFunction f = new PolynomialFunction(2, 2, 1.0, -1.0, 1.0, 1.0);

        // Initialize the optimizer with a simplex.
        AbstractSimplex simplex = new NelderMeadSimplex(new NelderMeadSimplexBuilder(new AveragedStartingPointGenerator(2)));
        optimizer.setSimplex(simplex);

        // Optimize the function.
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, new double[2]);
        SimplexPoint expected = new SimplexPoint(new double[] { -1.0, -1.0 }, -1.0);
        assertEquals(expected.getPoint(), result.getPoint(), 1.0e-6);
        assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
    }

    /**
     * Test for the optimization of a function with a local minimum at the edge of the search space.
     */
    @Test
    public void testEdgeCaseOptimization() {
        // Create the optimizer with a simple convergence checker.
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define the objective function.
        MultivariateFunction f = new PolynomialFunction(2, 2, 0.0, 0.0, 1.0, 1.0);

        // Initialize the optimizer with a simplex.
        AbstractSimplex simplex = new NelderMeadSimplex(new NelderMeadSimplexBuilder(new AveragedStartingPointGenerator(2)));
        optimizer.setSimplex(simplex);

        // Optimize the function.
        PointValuePair result = optimizer.optimize(100, f, GoalType.MINIMIZE, new double[2]);
        SimplexPoint expected = new SimplexPoint(new double[] { 1.0, 1.0 }, 0.0);
        assertEquals(expected.getPoint(), result.getPoint(), 1.0e-6);
        assertEquals(expected.getValue(), result.getValue(), 1.0e-6);
    }

    /**
     * Test for the optimization of a function with a singular simplex.
     */
    @Test(expected = RuntimeException.class)
    public void testSingularSimplexOptimization() {
        // Create the optimizer with a simple convergence checker.
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define the objective function.
        MultivariateFunction f = new PolynomialFunction(2, 2, 0.0, 0.0, 0.0, 0.0);

        // Initialize the optimizer with a simplex.
        AbstractSimplex simplex = new NelderMeadSimplex(new NelderMeadSimplexBuilder(new AveragedStartingPointGenerator(2)));
        optimizer.setSimplex(simplex);

        // Optimize the function.
        optimizer.optimize(100, f, GoalType.MINIMIZE, new double[2]);
    }
}