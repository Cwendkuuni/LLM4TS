import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PowellOptimizerTest {
    @Mock
    private ConvergenceChecker<PointValuePair> convergenceChecker;

    private PowellOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new PowellOptimizer(0.001, 0.001, convergenceChecker);
    }

    @Test
    public void testDoOptimize() {
        // Mock input values
        double[] startPoint = {1, 2, 3};
        double[] expectedPoint = {1.5, 2.5, 3.5};
        double expectedObjectiveValue = 10.5;
        GoalType goalType = GoalType.MINIMIZE;

        // Mock method calls
        when(optimizer.getGoalType()).thenReturn(goalType);
        when(optimizer.getStartPoint()).thenReturn(startPoint);
        when(optimizer.computeObjectiveValue(expectedPoint)).thenReturn(expectedObjectiveValue);

        // Execute the method to be tested
        PointValuePair result = optimizer.doOptimize();

        // Verify the result
        assertEquals(expectedPoint, result.getPoint(), 0.001);
        assertEquals(expectedObjectiveValue, result.getValue(), 0.001);
    }

    @Test
    public void testNewPointAndDirection() {
        // Mock input values
        double[] point = {1, 2, 3};
        double[] direction = {0.5, 0.5, 0.5};
        double alpha = 0.5;
        double[] expectedPoint = {1.25, 2.25, 3.25};
        double[] expectedDirection = {0.25, 0.25, 0.25};

        // Execute the method to be tested
        double[][] result = optimizer.newPointAndDirection(point, direction, alpha);

        // Verify the result
        assertEquals(expectedPoint, result[0], 0.001);
        assertEquals(expectedDirection, result[1], 0.001);
    }

    @Test
    public void testLineSearch() {
        // Mock input values
        double[] startPoint = {1, 2, 3};
        double[] direction = {0.5, 0.5, 0.5};
        double alpha = 0.5;
        double expectedValue = 10.5;
        UnivariatePointValuePair expectedOptimum = new UnivariatePointValuePair(alpha, expectedValue);

        // Mock method calls
        when(optimizer.computeObjectiveValue(startPoint)).thenReturn(10.0);
        when(optimizer.computeObjectiveValue(any(double[].class))).thenReturn(expectedValue);

        // Execute the method to be tested
        UnivariatePointValuePair result = optimizer.new LineSearch(0.001, 0.001).search(startPoint, direction);

        // Verify the result
        assertEquals(expectedOptimum, result);
    }
}