import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    
    private SimplexOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }
    
    @Test
    public void testDoOptimize() {
        // Test case 1
        double[] startPoint1 = {1.0, 2.0, 3.0};
        double[][] simplex1 = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        AbstractSimplex abstractSimplex1 = new AbstractSimplex(simplex1);
        optimizer.setSimplex(abstractSimplex1);
        PointValuePair expected1 = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 0.0);
        PointValuePair actual1 = optimizer.doOptimize();
        assertArrayEquals(expected1.getPoint(), actual1.getPoint(), 0.0);
        assertEquals(expected1.getValue(), actual1.getValue(), 0.0);
        
        // Test case 2
        double[] startPoint2 = {0.0, 0.0};
        double[][] simplex2 = {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}};
        AbstractSimplex abstractSimplex2 = new AbstractSimplex(simplex2);
        optimizer.setSimplex(abstractSimplex2);
        PointValuePair expected2 = new PointValuePair(new double[]{0.0, 0.0}, 0.0);
        PointValuePair actual2 = optimizer.doOptimize();
        assertArrayEquals(expected2.getPoint(), actual2.getPoint(), 0.0);
        assertEquals(expected2.getValue(), actual2.getValue(), 0.0);
    }
    
}