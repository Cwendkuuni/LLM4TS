import static org.junit.Assert.*;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testDoOptimize() {
        double[] targetValues = {10.0, 20.0, 30.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                     {0.0, 1.0, 0.0},
                                                                     {0.0, 0.0, 1.0}});
        
        double[] startPoint = {1.0, 1.0, 1.0};
        
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.0001);
        assertArrayEquals(new double[]{10.0, 20.0, 30.0}, result.getValue(), 0.0001);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
    }

    @Test
    public void testUseLUConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        
        assertNotNull(optimizer);
        assertTrue(optimizer.useLU);
    }
    
    @Test
    public void testUseQRConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        
        assertNotNull(optimizer);
        assertFalse(optimizer.useLU);
    }
}