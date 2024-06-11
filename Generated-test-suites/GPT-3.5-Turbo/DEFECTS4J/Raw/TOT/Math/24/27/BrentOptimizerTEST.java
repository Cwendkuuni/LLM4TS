import org.apache.commons.math3.exception.*; 
import org.apache.commons.math3.optimization.univariate.BrentOptimizer; 
import org.apache.commons.math3.optimization.GoalType; 
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private static final double EPSILON = 1e-10;
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                double previousValue = previous.getValue();
                double currentValue = current.getValue();
                return Math.abs(currentValue - previousValue) < EPSILON;
            }
        };
        optimizer = new BrentOptimizer(1e-10, 1e-10, checker);
    }
    
    @Test
    public void testDoOptimize_Minimization() {
        double[] values = {2.0, 4.0, 6.0, 3.0, 1.0};
        double[] expectedResult = {1.0};
        
        double minValue = optimizer.doOptimize(values, GoalType.MINIMIZE);
        
        assertEquals(expectedResult[0], minValue, EPSILON);
    }
    
    @Test
    public void testDoOptimize_Maximization() {
        double[] values = {2.0, 4.0, 6.0, 3.0, 1.0};
        double[] expectedResult = {6.0};
        
        double minValue = optimizer.doOptimize(values, GoalType.MAXIMIZE);
        
        assertEquals(expectedResult[0], minValue, EPSILON);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullArgumentException() {
        double[] values = null;
        
        optimizer.doOptimize(values, GoalType.MINIMIZE);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testDoOptimize_NotStrictlyPositiveException() {
        double[] values = {2.0, -4.0, 6.0};
        
        optimizer.doOptimize(values, GoalType.MINIMIZE);
    }
    
    @Test
    public void testDoOptimize_EmptyArray() {
        double[] values = {};
        
        double minValue = optimizer.doOptimize(values, GoalType.MINIMIZE);
        
        assertEquals(Double.NaN, minValue, EPSILON);
    }
}