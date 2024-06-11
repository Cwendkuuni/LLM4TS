import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_Minimize_Standard() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(0);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertEquals(0, result.getPoint(), 1e-10);
        Assert.assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize_Minimize_SingleMaximum() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertEquals(10, result.getPoint(), 1e-10);
        Assert.assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize_Minimize_FunctionWithMultipleMinima() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(2);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertEquals(0, result.getPoint(), 1e-10);
        Assert.assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize_Maximize_Standard() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertEquals(10, result.getPoint(), 1e-10);
        Assert.assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize_Maximize_SingleMinimum() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertEquals(0, result.getPoint(), 1e-10);
        Assert.assertEquals(0, result.getValue(), 1e-10);
    }

    @Test
    public void testDoOptimize_Maximize_FunctionWithMultipleMaxima() {
        // Arrange
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(2);
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Act
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Assert
        Assert.assertEquals(10, result.getPoint(), 1e-10);
        Assert.assertEquals(0, result.getValue(), 1e-10);
    }
}