import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        double[] targetValues = {1, 2, 3};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        double[] startPoint = {0, 0, 0};
        
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertArrayEquals(new double[] {1, 2, 3}, result.getPoint(), 0.0001);
    }
}