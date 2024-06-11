import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class PowellOptimizerTest {

    @Test
    public void testOptimize() {
        double rel = 0.1;
        double abs = 0.01;
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);
        
        GoalType goal = GoalType.MINIMIZE;
        double[] startPoint = {1, 2, 3};
        double objectiveValue = 0.0;
        PointValuePair expected = new PointValuePair(startPoint, objectiveValue);
        PointValuePair result = optimizer.optimize(startPoint.length, objectiveValue, startPoint, goal);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdIsTooSmall() {
        double rel = 1e-16;
        double abs = 0.01;
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteThresholdIsNotPositive() {
        double rel = 0.1;
        double abs = -0.01;
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);
    }
    
    @Test
    public void testConstructor_DefaultValues() {
        double rel = 0.1;
        double abs = 0.01;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
    }
    
    @Test
    public void testConstructor_AllValues() {
        double rel = 0.1;
        double abs = 0.01;
        double lineRel = 0.1;
        double lineAbs = 0.01;
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs, checker);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
        assertEquals(lineRel, optimizer.getLine().getRelativeThreshold(), 0);
        assertEquals(lineAbs, optimizer.getLine().getAbsoluteThreshold(), 0);
    }
    
    @Test
    public void testConstructor_DefaultLineValues() {
        double rel = 0.1;
        double abs = 0.01;
        double lineRel = 1e-15;
        double lineAbs = 1e-307;
        ConvergenceChecker<PointValuePair> checker = null;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, lineRel, lineAbs, checker);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0);
        assertEquals(lineRel, optimizer.getLine().getRelativeThreshold(), 0);
        assertEquals(lineAbs, optimizer.getLine().getAbsoluteThreshold(), 0);
    }
    
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer();
        
        PointValuePair expected = null;  // TODO: Add expected result
        PointValuePair result = optimizer.doOptimize();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNewPointAndDirection() {
        PowellOptimizer optimizer = new PowellOptimizer();
        
        double[] p = {1, 2, 3};
        double[] d = {0, 1, 0};
        double optimum = 0.5;
        
        double[][] expected = null;  // TODO: Add expected result
        double[][] result = optimizer.newPointAndDirection(p, d, optimum);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testLineSearch_Search() {
        PowellOptimizer optimizer = new PowellOptimizer();
        
        double[] p = {1, 2, 3};
        double[] d = {0, 1, 0};
        
        double expected = 0.0;  // TODO: Add expected result
        double result = optimizer.getLine().search(p, d);
        
        assertEquals(expected, result, 0);
    }

}