import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.linear.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test constructor with default settings.
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Test constructor with custom settings.
        GaussNewtonOptimizer optimizerWithChecker = new GaussNewtonOptimizer(new SimpleVectorValueChecker(0.001));

        // Test constructor with LU decomposition.
        GaussNewtonOptimizer optimizerWithLU = new GaussNewtonOptimizer(true);

        // Test constructor with QR decomposition.
        GaussNewtonOptimizer optimizerWithQR = new GaussNewtonOptimizer(false);

        // Test optimization with default settings.
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        PointValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);

        // Test optimization with custom settings.
        optimizerWithChecker.setTarget(targetValues);
        optimizerWithChecker.setWeight(weightMatrix);
        optimizerWithChecker.setStartPoint(startPoint);
        result = optimizerWithChecker.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        Assert.assertTrue(optimizerWithChecker.isConverged());

        // Test optimization with LU decomposition.
        optimizerWithLU.setTarget(targetValues);
        optimizerWithLU.setWeight(weightMatrix);
        optimizerWithLU.setStartPoint(startPoint);
        result = optimizerWithLU.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        Assert.assertTrue(optimizerWithLU.isConverged());

        // Test optimization with QR decomposition.
        optimizerWithQR.setTarget(targetValues);
        optimizerWithQR.setWeight(weightMatrix);
        optimizerWithQR.setStartPoint(startPoint);
        result = optimizerWithQR.optimize();
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
        Assert.assertTrue(optimizerWithQR.isConverged());

        // Test optimization with invalid input.
        optimizer.setTarget(null);
        try {
            optimizer.optimize();
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        optimizer.setWeight(null);
        try {
            optimizer.optimize();
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        optimizer.setStartPoint(null);
        try {
            optimizer.optimize();
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test optimization with unsupported decomposition.
        optimizer.setUseLU(false);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        optimizer.setStartPoint(startPoint);
        try {
            optimizer.optimize();
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected.
        }

        // Test optimization with singular problem.
        double[] targetValuesSingular = {0.0, 0.0, 1.0};
        RealMatrix weightMatrixSingular = new SingularValueDecomposition(new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0}
        })).getMatrixL().transpose();
        optimizerWithLU.setTarget(targetValuesSingular);
        optimizerWithLU.setWeight(weightMatrixSingular);
        optimizerWithLU.setStartPoint(startPoint);
        try {
            optimizerWithLU.optimize();
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected.
        }

        // Test optimization with non-convergence.
        double[] targetValuesNonConvergence = {1.0, 2.0, 3.0};
        RealMatrix weightMatrixNonConvergence = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {1.0, 1.0, 1.0}
        });
        optimizerWithChecker.setTarget(targetValuesNonConvergence);
        optimizerWithChecker.setWeight(weightMatrixNonConvergence);
        optimizerWithChecker.setStartPoint(startPoint);
        result = optimizerWithChecker.optimize();
        Assert.assertArrayEquals(startPoint, result.getPoint(), 0.0);
        Assert.assertFalse(optimizerWithChecker.isConverged());
    }
}