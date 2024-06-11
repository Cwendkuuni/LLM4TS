import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
        assertEquals(100, optimizer.getMaximalIterationCount());
        assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
        assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 0, 0, -1, 0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMinWithInvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        optimizer.localMin(true, null, GoalType.MINIMIZE, 0, 0, 0, 0, -1);
    }
    
    @Test
    public void testLocalMinWithValidInput() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        double x = optimizer.localMin(true, new TestFunction(), GoalType.MINIMIZE, 0, 6, 3, 0.0001, 0.0001);
        
        assertEquals(4.9999, x, 0.0001);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        optimizer.doOptimize();
    }
    
    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        double x = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 6, 3);
        
        assertEquals(4.9999, x, 0.0001);
    }
    
    @Test
    public void testOptimizeWithoutStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        double x = optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 6);
        
        assertEquals(4.9999, x, 0.0001);
    }
    
    private class TestFunction implements UnivariateRealFunction {
        
        public double value(double x) {
            return Math.pow((x - 5), 2);
        }
    }
}