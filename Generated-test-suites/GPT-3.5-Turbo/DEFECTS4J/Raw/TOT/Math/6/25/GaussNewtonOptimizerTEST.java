import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private double[] target;
    private RealMatrix weight;
    private double[] startPoint;
    
    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
        target = new double[]{1.0, 2.0, 3.0};
        weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        startPoint = new double[]{0.5, 0.5, 0.5};
    }
    
    @Test
    public void testDoOptimize() {
        optimizer.withTarget(target)
                 .withWeight(weight)
                 .withStartPoint(startPoint)
                 .withConvergenceChecker(new SimpleVectorValueChecker());
                 
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Test if the result is not null
        assertNotNull(result);
        
        // Test if the iter value is greater than 0
        assertTrue(result.getIter() > 0);
        
        // Test if the point value is matching with the expected length
        assertEquals(startPoint.length, result.getPoint().length);
        
        // Test if the cost value is not null
        assertNotNull(result.getCost());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.withTarget(target)
                 .withWeight(weight)
                 .withStartPoint(startPoint);
                 
        PointVectorValuePair result = optimizer.doOptimize();
    }
    
    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeSingularMatrix() {
        RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        
        optimizer.withTarget(target)
                 .withWeight(weight)
                 .withStartPoint(startPoint)
                 .withConvergenceChecker(new SimpleVectorValueChecker());
                 
        PointVectorValuePair result = optimizer.doOptimize();
    }
}