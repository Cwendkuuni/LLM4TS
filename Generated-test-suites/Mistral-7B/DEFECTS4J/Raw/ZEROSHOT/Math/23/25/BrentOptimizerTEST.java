import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BrentOptimizerTest {

    private ObjectiveFunction objectiveFunction;
    private BrentOptimizer brentOptimizer;
    private UnivariateOptimizer optimizer;

    @Before
    public void setUp() {
        objectiveFunction = mock(ObjectiveFunction.class);
        optimizer = mock(UnivariateOptimizer.class);

        brentOptimizer = new BrentOptimizer(0.01, 0.001, null);
        brentOptimizer.setObjectiveFunction(objectiveFunction);
        brentOptimizer.setOptimizer(optimizer);
    }

    @Test
    public void testBrentOptimizer_Minimization() {
        double expectedMinimum = 1.0;
        double expectedMinimumValue = 0.5;
        double[] testValues = {1.5, 0.5, 0.8, 1.2};

        when(objectiveFunction.value(0.5)).thenReturn(expectedMinimumValue);
        when(objectiveFunction.value(1.2)).thenReturn(1.2);
        when(objectiveFunction.value(0.8)).thenReturn(0.8);
        when(objectiveFunction.value(1.5)).thenReturn(1.5);
        when(objectiveFunction.value(x)).thenAnswer(invocation -> {
            Assert.assertTrue(x >= 0.0);
            return Math.pow(x, 2);
        });

        ValueInitializer valueInitializer = mock(ValueInitializer.class);
        brentOptimizer.setInitialGuess(valueInitializer);

        OptimizationResult optimizationResult = brentOptimizer.optimize();

        Assert.assertEquals(GoalType.MINIMIZE, optimizationResult.getGoalType());
        Assert.assertEquals(expectedMinimum, optimizationResult.getPoint().getValue(), 0.001);
        Assert.assertEquals(expectedMinimumValue, optimizationResult.getValue(), 0.001);
    }

    @Test
    public void testBrentOptimizer_Maximization() {
        double expectedMaximum = 1.0;
        double expectedMaximumValue = -0.5;
        double[] testValues = {1.5, 0.5, 0.8, 1.2};

        when(objectiveFunction.value(0.5)).thenReturn(expectedMaximumValue);
        when(objectiveFunction.value(1.2)).thenReturn(1.2);
        when(objectiveFunction.value(0.8)).thenReturn(0.8);
        when(objectiveFunction.value(1.5)).thenReturn(1.5);
        when(objectiveFunction.value(x)).thenAnswer(invocation -> {
            Assert.assertTrue(x >= 0.0);
            return -Math.pow(x, 2);
        });

        ValueInitializer valueInitializer = mock(ValueInitializer.class);
        brentOptimizer.setInitialGuess(valueInitializer);

        OptimizationResult optimizationResult = brentOptimizer.optimize();

        Assert.assertEquals(GoalType.MAXIMIZE, optimizationResult.getGoalType());
        Assert.assertEquals(expectedMaximum, optimizationResult.getPoint().getValue(), 0.001);
        Assert.assertEquals(expectedMaximumValue, optimizationResult.getValue(), 0.001);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_InvalidRelativeThreshold() {
        brentOptimizer = new BrentOptimizer(0.0, 0.01, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_InvalidAbsoluteThreshold() {
        brentOptimizer = new BrentOptimizer(0.01, 0.0, null);
    }

    @Test
    public void testBrentOptimizer_ConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        brentOptimizer = new BrentOptimizer(0.01, 0.01, convergenceChecker);

        UnivariatePointValuePair previous = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair current = new UnivariatePointValuePair(0.1, 0.1);

        when(convergenceChecker.converged(anyInt(), eq(previous), eq(current))).thenReturn(true);

        optimizationResult = brentOptimizer.optimize();

        verify(convergenceChecker).converged(anyInt(), eq(previous), eq(current));
    }
}