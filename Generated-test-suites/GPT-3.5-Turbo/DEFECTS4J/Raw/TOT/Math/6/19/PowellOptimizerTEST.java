import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testDoOptimize_TypicalCase() {
        // Typical case test
        double rel = 1e-6;
        double abs = 1e-6;
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair expected = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 0.0);
  
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_NullConvergenceChecker() {
        // Null convergence checker test
        double rel = 1e-6;
        double abs = 1e-6;
        double[] startPoint = {-1.0, -2.0, -3.0};
        PointValuePair expected = new PointValuePair(new double[]{-1.0, -2.0, -3.0}, 0.0);
  
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, null);
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }
    
    @Test
    public void testDoOptimize_ZeroTolerance() {
        // Zero tolerance test
        double rel = 0.0;
        double abs = 0.0;
        double[] startPoint = {0.0, 0.0, 0.0};
        PointValuePair expected = new PointValuePair(new double[]{0.0, 0.0, 0.0}, 0.0);
  
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class) 
    public void testDoOptimize_InvalidRelTolerance() {
        // Invalid relative tolerance test
        double rel = 1e-16;
        double abs = 1e-6;
  
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class) 
    public void testDoOptimize_InvalidAbsTolerance() {
        // Invalid absolute tolerance test
        double rel = 1e-6;
        double abs = 0.0;
  
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
    }
    
    @Test
    public void testDoOptimize_Converged() {
        // Convergence checker test
        double rel = 1e-6;
        double abs = 1e-6;
        double[] startPoint = {1.0, 2.0, 3.0};
        PointValuePair expected = new PointValuePair(new double[]{2.0, 3.0, 4.0}, 0.0);
        
        ConvergenceChecker<PointValuePair> convergenceChecker = (i, prev, curr) -> 
            i >= 10 || Math.abs(prev.getValue() - curr.getValue()) < 1e-6;
  
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, convergenceChecker);
        optimizer.setStartPoint(startPoint);
        optimizer.setGoalType(GoalType.MINIMIZE);
        PointValuePair result = optimizer.optimize();
        
        assertArrayEquals(expected.getPoint(), result.getPoint(), 1e-6);
        assertEquals(expected.getValue(), result.getValue(), 1e-6);
    }

}