import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {
    
    @Test
    public void testDoOptimize_noChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertThrows(NullArgumentException.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_converged() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        
        double[] target = {-1, 0, 1};
        double[][] weightData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix weight = new BlockRealMatrix(weightData);
        double[] startPoint = {0, 0, 0};
        
        optimizer.setMaxIterations(100);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        double[] point = result.getPoint();
        double[] objectiveValues = result.getValue();
        
        assertEquals(3, point.length);
        assertEquals(3, objectiveValues.length);
        
        assertTrue(optimizer.getCost() < 1e-10);
    }
    
    @Test
    public void testDoOptimize_notConverged() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        
        double[] target = {-1, 0, 1};
        double[][] weightData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix weight = new BlockRealMatrix(weightData);
        double[] startPoint = {10, 10, 10};
        
        optimizer.setMaxIterations(100);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        double[] point = result.getPoint();
        double[] objectiveValues = result.getValue();
        
        assertEquals(3, point.length);
        assertEquals(3, objectiveValues.length);
        
        assertTrue(optimizer.getCost() > 1e-10);
    }
    
    @Test
    public void testDoOptimize_singularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        
        double[] target = {-1, 0, 1};
        double[][] weightData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix weight = new BlockRealMatrix(weightData);
        double[] startPoint = {0, 0, 0};
        
        optimizer.setMaxIterations(100);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(ConvergenceException.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_singularMatrixException_useQR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        
        double[] target = {-1, 0, 1};
        double[][] weightData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix weight = new BlockRealMatrix(weightData);
        double[] startPoint = {0, 0, 0};
        
        optimizer.setMaxIterations(100);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(ConvergenceException.class, () -> optimizer.doOptimize());
    }
    
    @Test
    public void testDoOptimize_internalError() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        
        double[] target = {-1, 0, 1};
        double[][] weightData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix weight = new BlockRealMatrix(weightData);
        double[] startPoint = {0, 0, 0};
        
        optimizer.setMaxIterations(0);
        optimizer.setTarget(target);
        optimizer.setWeight(weight);
        optimizer.setStartPoint(startPoint);
        
        assertThrows(MathInternalError.class, () -> optimizer.doOptimize());
    }
}