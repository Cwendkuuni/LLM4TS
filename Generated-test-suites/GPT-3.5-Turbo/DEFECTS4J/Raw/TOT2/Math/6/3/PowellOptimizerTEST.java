import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with convergence checker
        double rel = 0.001;
        double abs = 0.01;
        ConvergenceChecker<PointValuePair> checker = new SimplePointChecker<PointValuePair>(rel, abs);
        
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);
        
        assertNotNull(optimizer);
    }

    @Test
    public void testDefaultConstructor() {
        // Test default constructor
        double rel = 0.001;
        double abs = 0.01;
        
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
        
        assertNotNull(optimizer);
    }

    @Test
    public void testOptimizeMethod() {
        // Test the optimize method
        double rel = 0.001;
        double abs = 0.01;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        PointValuePair result = optimizer.optimize();
        
        assertNotNull(result);
    }

    @Test
    public void testNewPointAndDirection() {
        // Test the newPointAndDirection method
        double[] p = {1.0, 2.0};
        double[] d = {0.5, 1.5};
        double alpha = 0.1;
        
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
        double[][] result = optimizer.newPointAndDirection(p, d, alpha);
        
        assertNotNull(result);
        assertEquals(2, result.length);
        assertArrayEquals(new double[]{1.05, 2.15}, result[0], 0.0001);
        assertArrayEquals(new double[]{0.05, 0.15}, result[1], 0.0001);
    }

    @Test
    public void testLineSearch() {
        // Test the LineSearch method
        double rel = 0.001;
        double abs = 0.01;
        PowellOptimizer optimizer = new PowellOptimizer(rel, abs);
        optimizer.setStartPoint(new double[]{1.0, 2.0});
        
        double[] p = {1.0, 2.0};
        double[] d = {0.5, 1.5};
        
        UnivariatePointValuePair result = optimizer.new LineSearch(rel, abs).search(p, d);
        
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0001);
        assertEquals(0.0, result.getPoint(), 0.0001);
    }
}