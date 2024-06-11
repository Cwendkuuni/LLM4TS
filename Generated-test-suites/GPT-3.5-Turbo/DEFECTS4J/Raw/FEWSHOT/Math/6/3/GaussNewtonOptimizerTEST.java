import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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

import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {
    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer();
    }

    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() {
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0};
        RealMatrix weight = new BlockRealMatrix(5, 5);

        PointVectorValuePair result = optimizer.optimize(target, startPoint, weight);
        assertNotNull(result);

        double[] expectedResult = {1.0, 1.0, 1.0};
        double[] actualResult = result.getPoint();
        assertArrayEquals(expectedResult, actualResult, 0.0001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(new double[]{}, new double[]{}, new BlockRealMatrix(0, 0));
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeSingularMatrix() {
        // Will throw SingularMatrixException.
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0};
        RealMatrix weight = new BlockRealMatrix(5, 5);

        optimizer.optimize(target, startPoint, weight);
    }

    @Test(expected = MathInternalError.class)
    public void testDoOptimizeNoConvergence() {
        double[] target = {1, 2, 3, 4, 5};
        double[] startPoint = {0, 0, 0};
        RealMatrix weight = new BlockRealMatrix(5, 5);
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointVectorValuePair>() {
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                return false; // No convergence.
            }
        });

        optimizer.optimize(target, startPoint, weight);
    }
}