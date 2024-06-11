
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
    
    private NonLinearConjugateGradientOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    }
    
    @Test
    public void testDoOptimize_minimize() {
        // Arrange
        double[] startPoint = {1.0, 2.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMaxEvaluations(1000);
        
        // Act
        PointValuePair result = optimizer.doOptimize();
        
        // Assert
        double[] expectedPoint = {-1.0, -2.0};
        double expectedObjectiveValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-15);
        Assert.assertEquals(expectedObjectiveValue, result.getValue(), 1e-15);
    }
    
    @Test
    public void testDoOptimize_maximize() {
        // Arrange
        double[] startPoint = {1.0, 2.0};
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMaxEvaluations(1000);
        
        // Act
        PointValuePair result = optimizer.doOptimize();
        
        // Assert
        double[] expectedPoint = {1.0, 2.0};
        double expectedObjectiveValue = 0.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-15);
        Assert.assertEquals(expectedObjectiveValue, result.getValue(), 1e-15);
    }
}

