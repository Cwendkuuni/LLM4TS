import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer simplexOptimizer;

    @Before
    public void setUp() {
        simplexOptimizer = new SimplexOptimizer();
    }

    @Test
    public void testOptimizeInternal() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        GoalType goalType = GoalType.MINIMIZE;

        double[] startPoint = {1, 2};

        PointValuePair expected = new PointValuePair(new double[] {0, 0}, 0);

        PointValuePair result = simplexOptimizer.optimizeInternal(100, f, goalType,
                new SimpleValueChecker(0.01, 0.01), new InitialGuess(startPoint), new NelderMeadSimplex(2));

        assertArrayEquals(expected.getPoint(), result.getPoint(), 0.01);
        assertEquals(expected.getValue(), result.getValue(), 0.01);
    }

    @Test
    public void testOptimizeInternalWithNullArgumentException() {
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        GoalType goalType = GoalType.MINIMIZE;

        double[] startPoint = {1, 2};

        try {
            simplexOptimizer.optimizeInternal(100, f, goalType,
                    new SimpleValueChecker(0.01, 0.01), new InitialGuess(startPoint));
            fail("Expected NullArgumentException");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimizeConverged() {
        simplexOptimizer.setSimplex(new NelderMeadSimplex(2));

        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        GoalType goalType = GoalType.MINIMIZE;

        double[] startPoint = {1, 2};

        PointValuePair expected = new PointValuePair(new double[] {0, 0}, 0);

        PointValuePair result = simplexOptimizer.doOptimize();

        assertArrayEquals(expected.getPoint(), result.getPoint(), 0.01);
        assertEquals(expected.getValue(), result.getValue(), 0.01);
    }

    @Test
    public void testDoOptimizeNotConverged() {
        simplexOptimizer.setSimplex(new NelderMeadSimplex(2));

        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };

        GoalType goalType = GoalType.MAXIMIZE;

        double[] startPoint = {1, 2};

        PointValuePair result = simplexOptimizer.doOptimize();

        assertNull(result);
    }

    @Test
    public void testParseOptimizationDataWithSimplex() {
        AbstractSimplex simplex = new NelderMeadSimplex(2);

        simplexOptimizer.parseOptimizationData(simplex);

        assertEquals(simplex, simplexOptimizer.getSimplex());
    }

    @Test
    public void testParseOptimizationDataWithoutSimplex() {
        simplexOptimizer.setSimplex(null);

        simplexOptimizer.parseOptimizationData();

        assertNull(simplexOptimizer.getSimplex());
    }
}