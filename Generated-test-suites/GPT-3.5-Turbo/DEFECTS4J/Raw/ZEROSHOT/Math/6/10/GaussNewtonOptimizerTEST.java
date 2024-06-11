import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
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

public class GaussNewtonOptimizerTest {

    private final double[] target = {1, 2, 3};
    private final RealMatrix weightMatrix = new BlockRealMatrix(new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
    private final double[] startPoint = {0, 0, 0};
    private final ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
    
    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setTarget(target);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertTrue(result != null);
        assertEquals(target.length, result.getPoint().length);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(target);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        optimizer.doOptimize();
    }
    
    @Test
    public void testDoOptimizeConverged() {
        ConvergenceChecker<PointVectorValuePair> convergedChecker = new ConvergenceChecker<PointVectorValuePair>() {
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                return iteration >= 10;
            }
        };
        
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, convergedChecker);
        optimizer.setTarget(target);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertTrue(result != null);
        assertEquals(target.length, result.getPoint().length);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularMatrix() {
        RealMatrix singularMatrix = new BlockRealMatrix(new double[][] {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.setTarget(target);
        optimizer.setWeight(singularMatrix);
        optimizer.setStartPoint(startPoint);
        
        optimizer.doOptimize();
    }
    
    @Test(expected = MathInternalError.class)
    public void testDoOptimizeInternalError() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        // Set null target to cause internal error.
        optimizer.setTarget(null);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        
        optimizer.doOptimize();
    }
}