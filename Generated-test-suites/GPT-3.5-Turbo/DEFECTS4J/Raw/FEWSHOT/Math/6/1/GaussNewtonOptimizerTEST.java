import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[] { 1.0, 2.0, 3.0 });
        optimizer.setStartPoint(new double[] { 0.0, 0.0, 0.0 });
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[] { 1.0, 2.0, 3.0 });
        optimizer.setStartPoint(new double[] { 0.0, 0.0, 0.0 });
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], 1e-15);
        assertEquals(0.0, result.getPoint()[1], 1e-15);
        assertEquals(0.0, result.getPoint()[2], 1e-15);
        assertEquals(14.0, optimizer.getCost(), 1e-15);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_SingularMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[] { 1.0, 2.0, 3.0 });
        optimizer.setStartPoint(new double[] { 0.0, 0.0, 0.0 });

        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] { { 0, 0, 0 }, { 0, 0, 0 }, { 0, 0, 0 } });
        optimizer.setWeight(weightMatrix);

        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize_LU() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setTarget(new double[] { 1.0, 2.0, 3.0 });
        optimizer.setStartPoint(new double[] { 0.0, 0.0, 0.0 });
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], 1e-15);
        assertEquals(0.0, result.getPoint()[1], 1e-15);
        assertEquals(0.0, result.getPoint()[2], 1e-15);
        assertEquals(14.0, optimizer.getCost(), 1e-15);
    }

    @Test
    public void testDoOptimize_QR() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        optimizer.setTarget(new double[] { 1.0, 2.0, 3.0 });
        optimizer.setStartPoint(new double[] { 0.0, 0.0, 0.0 });
        PointVectorValuePair result = optimizer.doOptimize();
        
        assertEquals(3, result.getPoint().length);
        assertEquals(0.0, result.getPoint()[0], 1e-15);
        assertEquals(0.0, result.getPoint()[1], 1e-15);
        assertEquals(0.0, result.getPoint()[2], 1e-15);
        assertEquals(14.0, optimizer.getCost(), 1e-15);
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimize_InvalidConvergence() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[] { 1.0, 2.0, 3.0 });
        optimizer.setStartPoint(new double[] { 0.0, 0.0, 0.0 });
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }
}