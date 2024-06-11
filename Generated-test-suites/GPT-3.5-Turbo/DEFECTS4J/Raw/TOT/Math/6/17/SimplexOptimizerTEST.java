import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

    @Test
    public void testOptimizeInternal_WithValidInputs_ReturnsOptimalValue() {
        // Arrange
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Act
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, startPoint);

        // Assert
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.0001);
        Assert.assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimizeInternal_WithInvalidInputs_ThrowsNullArgumentException() {
        // Arrange
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = null;
        GoalType goalType = GoalType.MINIMIZE;

        // Act & Assert
        try {
            PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, startPoint);
            Assert.fail("Expected NullArgumentException to be thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoOptimize_WithValidInputs_ReturnsOptimalValue() {
        // Arrange
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer();
        optimizer.setSimplex(new AbstractSimplex() {
            public void build(double[] startPoint) {}
            public void evaluate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {}
            public void iterate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {}
            public PointValuePair[] getPoints() {
                return new PointValuePair[] {new PointValuePair(new double[]{1.0, 1.0}, 2.0)};
            }
            public int getSize() {
                return 1;
            }
            public double[] getPoint(int index) {
                return new double[]{1.0, 1.0};
            }
        });
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Act
        PointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, result.getPoint(), 0.0001);
        Assert.assertEquals(2.0, result.getValue(), 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_WithInvalidInputs_ThrowsNullArgumentException() {
        // Arrange
        double[] startPoint = {1.0, 1.0};
        SimplexOptimizer optimizer = new SimplexOptimizer();
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[0] + point[1] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;

        // Act & Assert
        PointValuePair result = optimizer.doOptimize();
    }

    @Test
    public void testParseOptimizationData_WithValidOptimizationData_SetsSimplex() {
        // Arrange
        SimplexOptimizer optimizer = new SimplexOptimizer();
        AbstractSimplex simplex = new AbstractSimplex() {
            public void build(double[] startPoint) {}
            public void evaluate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {}
            public void iterate(MultivariateFunction evalFunc, Comparator<PointValuePair> comparator) {}
            public PointValuePair[] getPoints() {
                return null;
            }
            public int getSize() {
                return 0;
            }
            public double[] getPoint(int index) {
                return null;
            }
        };

        // Act
        optimizer.parseOptimizationData(simplex);

        // Assert
        Assert.assertEquals(simplex, optimizer.simplex);
    }
}