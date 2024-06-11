import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
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
    
    @Test
    public void testConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertTrue(optimizer.useLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertTrue(checker instanceof SimpleVectorValueChecker);
        
        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(false);
        assertFalse(optimizer2.useLU());
        assertTrue(checker instanceof SimpleVectorValueChecker);
        
        ConvergenceChecker<?> customChecker = new CustomConvergenceChecker();
        GaussNewtonOptimizer optimizer3 = new GaussNewtonOptimizer(customChecker);
        assertTrue(optimizer3.useLU());
        assertEquals(customChecker, optimizer3.getConvergenceChecker());
        
        GaussNewtonOptimizer optimizer4 = new GaussNewtonOptimizer(false, customChecker);
        assertFalse(optimizer4.useLU());
        assertEquals(customChecker, optimizer4.getConvergenceChecker());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[] {1.0});
        optimizer.doOptimize();
    }
    
    @Test
    public void testDoOptimizeConvergenceReached() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker((iter, previous, current) -> true);
        PointVectorValuePair result = optimizer.doOptimize();
        
        // Assert result values
        double[] expectedPoint = optimizer.getStartPoint();
        double[] expectedValue = optimizer.computeObjectiveValue(expectedPoint);
        double[] expectedResiduals = optimizer.computeResiduals(expectedValue);
        
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0);
        assertArrayEquals(expectedValue, result.getValue(), 0.0);
        assertArrayEquals(expectedResiduals, optimizer.computeResiduals(result.getValue()), 0.0);
        assertEquals(optimizer.computeCost(expectedResiduals), optimizer.getCost(), 0.0);
    }
    
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(new double[] {1.0});
        optimizer.setWeight(new BlockRealMatrix(new double[][] {{1.0}}));
        optimizer.setTarget(new double[] {0.0});
        optimizer.doOptimize();
    }
        
    private class CustomConvergenceChecker implements ConvergenceChecker<PointVectorValuePair> {
        @Override
        public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
            return true;
        }
    }
}