import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    
    private GaussNewtonOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }
    
    @Test
    public void testDoOptimize() {
        double[] target = {1.0, 2.0, 3.0};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0},
                                                                               {0.0, 1.0, 0.0},
                                                                               {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], 0.0);
        assertEquals(0.0, result.getPoint()[1], 0.0);
        assertEquals(0.0, result.getPoint()[2], 0.0);
        
        assertEquals(0.0, result.getValue()[0], 0.0);
        assertEquals(0.0, result.getValue()[1], 0.0);
        assertEquals(0.0, result.getValue()[2], 0.0);
    }
    
    @Test
    public void testDoOptimize_WithConvergenceException() {
        double[] target = {1.0, 2.0};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0},
                                                                               {0.0, 1.0}});
        double[] startPoint = {0.0};
        
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(ConvergenceException.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_NullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(null);
        
        double[] target = {1.0, 2.0, 3.0};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0},
                                                                               {0.0, 1.0, 0.0},
                                                                               {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(NullArgumentException.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_SingularMatrixException() {
        double[] target = {1.0, 2.0, 3.0};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0},
                                                                               {0.0, 1.0, 0.0},
                                                                               {0.0, 0.0, 1.0}});
        double[] startPoint = {1.0, 1.0, 1.0};
        
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(ConvergenceException.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_MathInternalError() {
        double[] target = {1.0, 2.0, 3.0};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0},
                                                                               {0.0, 1.0, 0.0},
                                                                               {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(MathInternalError.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_GettersAndSetters() {
        double[] target = {1.0, 2.0, 3.0};
        Array2DRowRealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0},
                                                                               {0.0, 1.0, 0.0},
                                                                               {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertTrue(weight == optimizer.getWeight());
        assertTrue(target == optimizer.getTarget());
        assertTrue(startPoint == optimizer.getStartPoint());
    }
    
}