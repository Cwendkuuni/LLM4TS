import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    @Test
    public void testOptimize_startValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, -1, 1, 0);
            assertEquals(0, result, 0.000001);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }
    
    @Test
    public void testOptimize_noStartValue() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        
        try {
            double result = optimizer.optimize(f, GoalType.MAXIMIZE, 0, Math.PI);
            assertEquals(Math.PI/2, result, 0.000001);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException thrown");
        }
    }
    
}