import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    // ###############################
    // Test optimizeInternal() method
    // ###############################

    @Test
    public void testOptimizeInternal_Default() {
        // Test optimizeInternal with default input parameters

        // Create objective function (f(x, y) = x^2 + y^2)
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2);
            }
        };

        // Set start point (0, 0)
        OptimizationData startData = new InitialGuess(new double[]{0, 0});

        // Call optimizeInternal method
        PointValuePair result = optimizer.optimizeInternal(100, objectiveFunction, GoalType.MINIMIZE, startData);

        // Check if the result matches the expected minimum point (0, 0)
        assertArrayEquals(new double[]{0, 0}, result.getPoint(), 0.0001);
        assertEquals(0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeInternal_Custom() {
        // Test optimizeInternal with custom input parameters

        // Create objective function (f(x) = x^3 + 2x^2 + 3x + 4)
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 3) + 2 * Math.pow(point[0], 2) + 3 * point[0] + 4;
            }
        };

        // Set start point (1)
        OptimizationData startData = new InitialGuess(new double[]{1});

        // Call optimizeInternal method
        PointValuePair result = optimizer.optimizeInternal(100, objectiveFunction, GoalType.MAXIMIZE, startData);

        // Check if the result matches the expected maximum point (-1.523, 0)
        assertArrayEquals(new double[]{-1.523}, result.getPoint(), 0.001);
        assertEquals(2.908, result.getValue(), 0.001);
    }

    // ###############################
    // Test setSimplex() method
    // ###############################

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = new Simplex(new double[]{1, 2, 3});
        optimizer.setSimplex(simplex);

        // Check if the simplex is set correctly
        assertSame(simplex, optimizer.getSimplex());
    }

    // ###############################
    // Test optimize() method
    // ###############################

    @Test
    public void testOptimize_EdgeCases() {
        // Test optimize method with edge cases

        // Create objective function (f(x) = x^2 + 1)
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2) + 1;
            }
        };

        // Test with lower bound as start point (-100)
        PointValuePair result = optimizer.optimize(100, objectiveFunction, GoalType.MINIMIZE, new double[]{-100});
        assertArrayEquals(new double[]{-100}, result.getPoint(), 0.0001);
        assertEquals(10001, result.getValue(), 0.0001);

        // Test with upper bound as start point (100)
        result = optimizer.optimize(100, objectiveFunction, GoalType.MINIMIZE, new double[]{100});
        assertArrayEquals(new double[]{100}, result.getPoint(), 0.0001);
        assertEquals(10001, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimize_NullObjectiveFunction() {
        // Test optimize method with null objective function

        PointValuePair result = optimizer.optimize(100, null, GoalType.MINIMIZE, new double[]{0});

        // Check if the result is null
        assertNull(result);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimize_NullStartPoint() {
        // Test optimize method with null start point

        // Create objective function (f(x) = x^2)
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2);
            }
        };

        optimizer.optimize(100, objectiveFunction, GoalType.MINIMIZE, null);
    }

    // ###############################
    // Test doOptimize() method
    // ###############################

    @Test
    public void testDoOptimize_Typical() {
        // Test doOptimize method with typical input

        // Create objective function (f(x) = x^2)
        MultivariateFunction objectiveFunction = new MultivariateFunction() {
            public double value(double[] point) {
                return Math.pow(point[0], 2);
            }
        };

        // Set simplex
        AbstractSimplex simplex = new Simplex(new double[]{1});
        optimizer.setSimplex(simplex);

        // Set start point (0.5)
        optimizer.setStartPoint(new double[]{0.5});

        PointValuePair result = optimizer.doOptimize();

        // Check if the result matches the expected minimum point (0)
        assertArrayEquals(new double[]{0}, result.getPoint(), 0.0001);
        assertEquals(0, result.getValue(), 0.0001);
    }

    // ###############################
    // Test convergence checker
    // ###############################

    @Test
    public void testConvergenceChecker_Default() {
        // Test convergence checker with default values

        // Set convergence checker with default values
        ConvergenceChecker<PointValuePair> convergenceChecker = optimizer.getConvergenceChecker();

        // Create test points
        PointValuePair point1 = new PointValuePair(new double[]{1}, 100);
        PointValuePair point2 = new PointValuePair(new double[]{2}, 200);

        // Check if the points are not considered converged
        assertFalse(convergenceChecker.converged(1, point1, point2));
    }

    @Test
    public void testConvergenceChecker_Custom() {
        // Test convergence checker with custom values

        // Set convergence checker with custom values
        ConvergenceChecker<PointValuePair> convergenceChecker = new SimpleValueChecker(0.001, 0.001);
        optimizer.setConvergenceChecker(convergenceChecker);

        // Create test points
        PointValuePair point1 = new PointValuePair(new double[]{1}, 100);
        PointValuePair point2 = new PointValuePair(new double[]{1.001}, 100.1);

        // Check if the points are considered converged
        assertTrue(convergenceChecker.converged(1, point1, point2));
    }

    // ###############################
    // Test deprecated methods
    // ###############################

    @Test
    public void testDeprecatedMethods() {
        // Test deprecated constructor and setSimplex method

        // Create simplex
        AbstractSimplex simplex = new Simplex(new double[]{1, 2, 3});

        // Create optimizer using deprecated constructor
        SimplexOptimizer deprecatedOptimizer = new SimplexOptimizer();

        // Set simplex using deprecated setSimplex method
        deprecatedOptimizer.setSimplex(simplex);

        // Check if the simplex is set correctly
        assertSame(simplex, deprecatedOptimizer.getSimplex());
    }

}