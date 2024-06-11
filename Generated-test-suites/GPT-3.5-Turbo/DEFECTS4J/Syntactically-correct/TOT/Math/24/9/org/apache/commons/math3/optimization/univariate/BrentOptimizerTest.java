import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    private final double relThreshold = 1e-6;
    private final double absThreshold = 1e-10;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(relThreshold, absThreshold);
    }
    
    @Test
    public void testDoOptimize_minimizeFunction_returnMinimumValue() {
        // Arrange
        double min = -10;
        double max = 10;
        double startValue = 0;
        double delta = 1e-6;
        
        // Act
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        Assert.assertEquals(min, result.getPoint(), delta);
    }
    
    @Test
    public void testDoOptimize_maximizeFunction_returnMaximumValue() {
        // Arrange
        double min = -10;
        double max = 10;
        double startValue = 0;
        double delta = 1e-6;
        
        // Act
        optimizer.setMin(min);
        optimizer.setMax(max);
        optimizer.setStartValue(startValue);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assert
        Assert.assertEquals(max, result.getPoint(), delta);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void constructor_withInvalidRelativeThreshold_throwNumberIsTooSmallException() {
        // Arrange
        double invalidRelThreshold = 1e-12;
        
        // Act
        optimizer = new BrentOptimizer(invalidRelThreshold, absThreshold);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void constructor_withInvalidAbsoluteThreshold_throwNotStrictlyPositiveException() {
        // Arrange
        double invalidAbsThreshold = -1e-10;
        
        // Act
        optimizer = new BrentOptimizer(relThreshold, invalidAbsThreshold);
    }
}