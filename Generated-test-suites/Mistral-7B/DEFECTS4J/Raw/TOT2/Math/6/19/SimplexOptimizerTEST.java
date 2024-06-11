import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.SimplexPoint;
import org.apache.commons.math3.optim.SimplexSolver;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.direct.SimplexOptimizer;

public class SimplexOptimizerTest {

    @Test
    public void testSimplexOptimizer_BasicFunctionality() {
        // Create a simplex optimizer with a simple value checker
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-12, 1e-24);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define a simple function for testing
        MultivariateFunction function = new PolynomialFunction(new double[][] {{0.0, 0.0}, {1.0, 2.0}, {2.0, 1.0}}, 0.0);

        // Set the initial simplex
        AbstractSimplex simplex = new SimplexSolver(function, new PointValuePairComparator(), 3) {
            @Override
            public void optimize() {
                // Do nothing as we will be providing the initial simplex manually
            }
        };

        // Set the starting point
        double[] startPoint = {1.5, 1.5};

        // Run the optimization
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, GoalType.MINIMIZE, new OptimizationData[] {simplex, new InitialGuess(startPoint)});

        // Check the result
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 1e-12);
        SimplexPoint optimalPoint = simplex.getPoint(0);
        assertEquals(2.0, optimalPoint.getPoint()[0], 1e-12);
        assertEquals(2.0, optimalPoint.getPoint()[1], 1e-12);
    }

    @Test(expected = NullArgumentException.class)
    public void testSimplexOptimizer_NullSimplex() {
        // Create a simplex optimizer with a simple value checker
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-12, 1e-24);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define a simple function for testing
        MultivariateFunction function = new PolynomialFunction(new double[][] {{0.0, 0.0}, {1.0, 2.0}, {2.0, 1.0}}, 0.0);

        // Set an empty simplex
        AbstractSimplex simplex = null;

        // Run the optimization
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, GoalType.MINIMIZE, new OptimizationData[] {simplex});

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexOptimizer_InvalidGoalType() {
        // Create a simplex optimizer with a simple value checker
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-12, 1e-24);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define a simple function for testing
        MultivariateFunction function = new PolynomialFunction(new double[][] {{0.0, 0.0}, {1.0, 2.0}, {2.0, 1.0}}, 0.0);

        // Set the initial simplex
        AbstractSimplex simplex = new SimplexSolver(function, new PointValuePairComparator(), 3) {
            @Override
            public void optimize() {
                // Do nothing as we will be providing the initial simplex manually
            }
        };

        // Set the starting point
        double[] startPoint = {1.5, 1.5};

        // Run the optimization with an invalid goal type
        PointValuePair result = optimizer.optimize(new MaxEval(100), function, GoalType.MAXIMIZE, new OptimizationData[] {simplex, new InitialGuess(startPoint)});

    }

    @Test
    public void testSimplexOptimizer_EdgeCases() {
        // Create a simplex optimizer with a simple value checker
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-12, 1e-24);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        // Define function A: f(x, y) = x^2 + y^2
        MultivariateFunction functionA = new PolynomialFunction(new double[][] {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}}, 1.0);

        // Set the initial simplex
        AbstractSimplex simplex = new SimplexSolver(functionA, new PointValuePairComparator(), 3) {
            @Override
            public void optimize() {
                // Do nothing as we will be providing the initial simplex manually
            }
        };

        // Set the starting point
        double[] startPointA = {1.0, 1.0};

        // Run the optimization for function A
        PointValuePair resultA = optimizer.optimize(new MaxEval(100), functionA, GoalType.MINIMIZE, new OptimizationData[] {simplex, new InitialGuess(startPointA)});

        // Check the result
        assertNotNull(resultA);
        assertEquals(2.0, resultA.getValue(), 1e-12);

        // Define function B: f(x, y) = x^2 * y
        MultivariateFunction functionB = new PolynomialFunction(new double[][] {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}}, 0.0);

        // Set the initial simplex
        AbstractSimplex simplexB = new SimplexSolver(functionB, new PointValuePairComparator(), 3) {
            @Override
            public void optimize() {
                // Do nothing as we will be providing the initial simplex manually
            }
        };

        // Set the starting point
        double[] startPointB = {0.0, 0.0};

        // Run the optimization for function B
        PointValuePair resultB = optimizer.optimize(new MaxEval(100), functionB, GoalType.MINIMIZE, new OptimizationData[] {simplexB, new InitialGuess(startPointB)});

        // Check the result
        assertNotNull(resultB);
        assertEquals(0.0, resultB.getValue(), 1e-12);

        // Define function C: f(x, y) = x / y
        MultivariateFunction functionC = new PolynomialFunction(new double[][] {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}}, Double.NEGATIVE_INFINITY);

        // Set the initial simplex
        AbstractSimplex simplexC = new SimplexSolver(functionC, new PointValuePairComparator(), 3) {
            @Override
            public void optimize() {
                // Do nothing as we will be providing the initial simplex manually
            }
        };

        // Set the starting point
        double[] startPointC = {1.0, 1.0};

        // Run the optimization for function C
        try {
            PointValuePair resultC = optimizer.optimize(new MaxEval(100), functionC, GoalType.MINIMIZE, new OptimizationData[] {simplexC, new InitialGuess(startPointC)});
            fail("Exception expected for division by zero.");
        } catch (ArithmeticException e) {
            // Expected
        }

        // Define function D: f(x, y) = sin(x) / y
        MultivariateFunction functionD = new PolynomialFunction(new double[][] {{0.0, 0.0}, {Math.PI / 2.0, 0.0}, {0.0, 1.0}}, Double.NEGATIVE_INFINITY);

        // Set the initial simplex
        AbstractSimplex simplexD = new SimplexSolver(functionD, new PointValuePairComparator(), 3) {
            @Override
            public void optimize() {
                // Do nothing as we will be providing the initial simplex manually
            }
        };

        // Set the starting point
        double[] startPointD = {0.0, 1.0};

        // Run the optimization for function D
        try {
            PointValuePair resultD = optimizer.optimize(new MaxEval(100), functionD, GoalType.MINIMIZE, new OptimizationData[] {simplexD, new InitialGuess(startPointD)});
            fail("Exception expected for division by zero or singularity.");
        } catch (ArithmeticException e) {
            // Expected
        }

    }
}