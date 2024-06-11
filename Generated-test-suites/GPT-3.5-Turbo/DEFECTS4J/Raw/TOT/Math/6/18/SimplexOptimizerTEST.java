import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }
    
    /**
     * Test optimize method with valid arguments.
     */
    @Test
    public void testOptimize_validArguments() {
        double[] startPoint = {1.0, 1.0};
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        
        PointValuePair result = optimizer.optimize(maxEval, f, goalType, startPoint);
        
        assertNotNull(result);
        assertEquals(startPoint, result.getPoint());
        assertEquals(1.0, result.getValue(), 0.0001);
    }
    
    /**
     * Test optimize method with invalid argument (null start point).
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimize_nullStartPoint() {
        double[] startPoint = null;
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        
        optimizer.optimize(maxEval, f, goalType, startPoint);
    }
    
    /**
     * Test optimize method with invalid argument (negative max evaluations).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_negativeMaxEvaluations() {
        double[] startPoint = {1.0, 1.0};
        int maxEval = -100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        
        optimizer.optimize(maxEval, f, goalType, startPoint);
    }
    
    /**
     * Test optimize method with valid arguments (minimize goal type).
     */
    @Test
    public void testOptimize_minimizeGoalType() {
        double[] startPoint = {1.0, 1.0};
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return point[0] * point[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        
        PointValuePair result = optimizer.optimize(maxEval, f, goalType, startPoint);
        
        assertNotNull(result);
        assertEquals(startPoint, result.getPoint());
        assertEquals(1.0, result.getValue(), 0.0001);
    }

}