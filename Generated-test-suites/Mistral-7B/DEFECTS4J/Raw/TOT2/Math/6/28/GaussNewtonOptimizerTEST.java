import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerWithDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        PointValuePair expected = new PointValuePair(new double[]{2.5, 1.5, 0.5}, 0.0);
        assertEquals(expected, result);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        optimizer.optimize(null, startPoint, weightMatrix);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        optimizer.optimize(targetValues, null, weightMatrix);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.optimize(targetValues, startPoint, null);
    }

    @Test
    public void testGaussNewtonOptimizerWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        PointValuePair expected = new PointValuePair(new double[]{2.5, 1.5, 0.5}, 0.0);
        assertEquals(expected, result);
    }

    @Test
    public void testGaussNewtonOptimizerWithQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        PointValuePair expected = new PointValuePair(new double[]{2.5, 1.5, 0.5}, 0.0);
        assertEquals(expected, result);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 0.0, 0.0};
        double[] startPoint = new double[]{0.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        optimizer.optimize(targetValues, startPoint, weightMatrix);
    }

    @Test
    public void testCustomConvergenceChecker() {
        double tolerance = 1e-6;
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(tolerance);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        PointValuePair expected = new PointValuePair(new double[]{2.5, 1.5, 0.5}, 0.0);
        assertEquals(expected, result);
        assertTrue(checker.isConverged(result));
    }

    @Test
    public void testNonConvergenceWithCustomChecker() {
        double tolerance = 1e-4;
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(tolerance);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        PointVectorValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix);

        PointValuePair expected = new PointValuePair(new double[]{2.5, 1.5, 0.5}, 0.0);
        assertEquals(expected, result);
        assertFalse(checker.isConverged(result));
    }
}