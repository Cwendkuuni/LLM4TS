import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    private UnivariateRealFunction function;
    private GoalType goalType;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        function = mock(UnivariateRealFunction.class);
        goalType = GoalType.MINIMIZE;
    }

    @Test
    public void testOptimize_WithValidInput_ReturnsOptimum() throws Exception {
        // Arrange
        when(function.value(2)).thenReturn(10.0);
        double min = 1;
        double max = 3;
        
        // Act
        double optimum = optimizer.optimize(function, goalType, min, max);

        // Assert
        Assert.assertEquals(2.0, optimum, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_WithInvalidInput_ThrowsMaxIterationsExceededException() throws Exception {
        // Arrange
        when(function.value(0)).thenReturn(10.0);
        double min = -1;
        double max = 1;

        // Act
        optimizer.optimize(function, goalType, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_WithInvalidInput_ThrowsFunctionEvaluationException() throws Exception {
        // Arrange
        when(function.value(2)).thenThrow(new FunctionEvaluationException());

        // Act
        optimizer.optimize(function, goalType, 1, 3);
    }

}