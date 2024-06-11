import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.general.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerWithDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        PointValuePair result = optimizer.optimize(new double[] {1.0, 2.0}, new double[] {3.0, 4.0}, new double[] {1.0, 1.0});
        assertEquals("Expected optimization to succeed.", 0.0, result.getValue()[0], 0.001);
        assertEquals("Expected optimization to succeed.", 0.0, result.getValue()[1], 0.001);
    }

    @Test
    public void testGaussNewtonOptimizerWithConvergenceChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker(0.001));
        PointValuePair result = optimizer.optimize(new double[] {1.0, 2.0}, new double[] {3.0, 4.0}, new double[] {1.0, 1.0});
        assertEquals("Expected optimization to succeed.", 0.0, result.getValue()[0], 0.001);
        assertEquals("Expected optimization to succeed.", 0.0, result.getValue()[1], 0.001);
    }

    @Test
    public void testGaussNewtonOptimizerWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        PointValuePair result = optimizer.optimize(new double[] {1.0, 2.0}, new double[] {3.0, 4.0}, new double[] {1.0, 1.0});
        assertEquals("Expected optimization to succeed.", 0.0, result.getValue()[0], 0.001);
        assertEquals("Expected optimization to succeed.", 0.0, result.getValue()[1], 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(null, new double[] {3.0, 4.0}, new double[] {1.0, 1.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForNullObservedData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new double[] {1.0, 2.0}, null, new double[] {1.0, 1.0});
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArgumentExceptionForNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new double[] {1.0, 2.0}, new double[] {3.0, 4.0}, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForEmptyTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new double[] {}, new double[] {3.0, 4.0}, new double[] {1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForEmptyObservedData() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new double[] {1.0, 2.0}, new double[] {}, new double[] {1.0, 1.0});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForEmptyStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.optimize(new double[] {1.0, 2.0}, new double[] {3.0, 4.0}, new double[] {});
    }

    @Test
    public void testSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] target = {1.0, 0.0};
        double[] observedData = {1.0, 0.0, 0.0};
        double[] startPoint = {1.0, 1.0};
        try {
            optimizer.optimize(target, observedData, startPoint);
            fail("Expected ConvergenceException.");
        } catch (ConvergenceException e) {
            // Expected.
        }
    }
}