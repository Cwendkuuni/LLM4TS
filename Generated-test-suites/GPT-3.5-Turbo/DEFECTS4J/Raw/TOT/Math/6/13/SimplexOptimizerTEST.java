import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void optimizeInternal_WithMaxEval_ReturnsOptimalValue() {
        // Arrange
        int maxEval = 100;
        double[] startPoint = {1.0, 2.0, 3.0};
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Act
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, startPoint);

        // Assert
        double[] expectedPoint = {0.0, 0.0, 0.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test
    public void optimizeInternal_WithInvalidMaxEval_ThrowsException() {
        // Arrange
        int maxEval = -1;
        double[] startPoint = {1.0, 2.0, 3.0};
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimizeInternal(maxEval, f, goalType, startPoint);
        });
    }

    @Test
    public void optimizeInternal_WithNullStartPoint_ThrowsException() {
        // Arrange
        int maxEval = 100;
        double[] startPoint = null;
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Assert
        assertThrows(NullArgumentException.class, () -> {
            optimizer.optimizeInternal(maxEval, f, goalType, startPoint);
        });
    }

    @Test
    public void optimizeInternal_WithDifferentNumberOfParameters_ThrowsException() {
        // Arrange
        int maxEval = 100;
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction f = new MultivariateFunction() {
            @Override
            public double value(double[] point) {
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) + Math.pow(point[2], 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> {
            optimizer.optimizeInternal(maxEval, f, goalType, startPoint);
        });
    }

    // Add more test cases for other methods

}