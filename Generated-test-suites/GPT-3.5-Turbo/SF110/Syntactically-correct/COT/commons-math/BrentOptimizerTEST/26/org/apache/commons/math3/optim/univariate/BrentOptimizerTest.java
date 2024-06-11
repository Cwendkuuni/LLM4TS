
import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorThrowsExceptionForSmallRelativeTolerance() {
        new BrentOptimizer(0.0, 1.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorThrowsExceptionForNonPositiveAbsoluteTolerance() {
        new BrentOptimizer(1.0, 0.0);
    }
    
    @Test
    public void testConstructorSetsParametersCorrectly() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        assertEquals(1.0, optimizer.getRelativeThreshold(), Precision.EPSILON);
        assertEquals(0.5, optimizer.getAbsoluteThreshold(), Precision.EPSILON);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        optimizer.setMin(0.0);
        optimizer.setMax(1.0);
        optimizer.setStartValue(0.5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(0, result.getPoint(), Precision.EPSILON);
        assertEquals(0, result.getValue(), Precision.EPSILON);
    }
}
