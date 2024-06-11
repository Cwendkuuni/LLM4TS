import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        // Initialize test data
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        
        // Create GaussNewtonOptimizer instance
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        // Test optimization
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Assertions
        assertNotNull(result);
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getPoint()[2], 0.0001);    
    }
}