import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BrentOptimizerTest {

    @Test
    public void testConstructorWithChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = mock(ConvergenceChecker.class);
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.1, checker);
        
        assertNotNull(optimizer);
    }

    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0.0, result.getValue(), 1e-15);
    }

    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        ObjectiveFunction function = new ObjectiveFunction(x -> x * x);
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1.0, 1.0);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2.0, 4.0);
        
        UnivariatePointValuePair result = optimizer.best(pointA, pointB, true);
        
        assertEquals(1.0, result.getValue(), 1e-15);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallRelativeTolerance() {
        new BrentOptimizer(0.0, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveAbsoluteThreshold() {
        new BrentOptimizer(0.01, -0.001);
    }
}