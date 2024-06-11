import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    private GaussNewtonOptimizer optimizer;
    private double[] targetValues;
    private RealMatrix weightMatrix;
    private double[] startPoint;
    
    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
        targetValues = new double[]{1.0, 2.0, 3.0, 4.0};
        weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                               {0.0, 1.0, 0.0, 0.0},
                                                               {0.0, 0.0, 1.0, 0.0},
                                                               {0.0, 0.0, 0.0, 1.0}});
        startPoint = new double[]{0.0, 0.0, 0.0, 0.0};
    }
    
    @Test
    public void testDoOptimize_TypicalCase() {
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Assert the optimized point values
        double[] optimizedPoint = result.getPoint();
        assertEquals(1.0, optimizedPoint[0], 0.0001);
        assertEquals(2.0, optimizedPoint[1], 0.0001);
        assertEquals(3.0, optimizedPoint[2], 0.0001);
        assertEquals(4.0, optimizedPoint[3], 0.0001);
        
        // Assert the computed objective values
        double[] objectiveValues = result.getValue();
        assertEquals(1.0, objectiveValues[0], 0.0001);
        assertEquals(2.0, objectiveValues[1], 0.0001);
        assertEquals(3.0, objectiveValues[2], 0.0001);
        assertEquals(4.0, objectiveValues[3], 0.0001);
    }
    
    @Test
    public void testDoOptimize_WithZeroWeights() {
        weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                               {0.0, 0.0, 0.0, 0.0},
                                                               {0.0, 0.0, 0.0, 0.0},
                                                               {0.0, 0.0, 0.0, 1.0}});
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Assert the optimized point values
        double[] optimizedPoint = result.getPoint();
        assertEquals(1.0, optimizedPoint[0], 0.0001);
        assertEquals(0.0, optimizedPoint[1], 0.0001);
        assertEquals(0.0, optimizedPoint[2], 0.0001);
        assertEquals(4.0, optimizedPoint[3], 0.0001);
        
        // Assert the computed objective values
        double[] objectiveValues = result.getValue();
        assertEquals(1.0, objectiveValues[0], 0.0001);
        assertEquals(0.0, objectiveValues[1], 0.0001);
        assertEquals(0.0, objectiveValues[2], 0.0001);
        assertEquals(4.0, objectiveValues[3], 0.0001);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_WithSingularMatrix() {
        weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                               {0.0, 0.0, 0.0, 1.0},
                                                               {0.0, 0.0, 0.0, 1.0},
                                                               {0.0, 0.0, 0.0, 1.0}});
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        optimizer.doOptimize();
    }
    
}