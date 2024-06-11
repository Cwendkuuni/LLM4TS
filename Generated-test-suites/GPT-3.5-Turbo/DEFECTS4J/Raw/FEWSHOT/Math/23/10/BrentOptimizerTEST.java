import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BaseAbstractUnivariateOptimizer;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class BrentOptimizerTest {

    private static final double RELATIVE_TOLERANCE = 1e-6;
    private static final double ABSOLUTE_TOLERANCE = 1e-6;
    
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testDoOptimize() {
        // Mock objective function
        BaseAbstractUnivariateOptimizer.Function objectiveFunction = mock(BaseAbstractUnivariateOptimizer.Function.class);
        when(objectiveFunction.value(anyDouble())).thenReturn(1.0);

        // Set objective function
        optimizer.setFunction(objectiveFunction);
        
        // Set other parameters
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Run optimization
        optimizer.doOptimize();

        // Verify objective function calls
        verify(objectiveFunction, times(3)).value(anyDouble());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_RelativeToleranceNotStrictlyPositive() {
        new BrentOptimizer(0, ABSOLUTE_TOLERANCE);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteToleranceNotStrictlyPositive() {
        new BrentOptimizer(RELATIVE_TOLERANCE, 0);
    }
}