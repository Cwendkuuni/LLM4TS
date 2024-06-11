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
import org.apache.commons.math3.optimization.AbstractLeastSquaresOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testConstructorWithDefaultSettings() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        Assert.assertTrue(optimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(checker);
        Assert.assertSame(SimpleVectorValueChecker.class, checker.getClass());
    }

    @Test
    public void testConstructorWithCustomSettings() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        Assert.assertFalse(optimizer.isUseLU());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize_Convergence() throws Exception {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        double[] startPoint = {0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        PointVectorValuePair result = optimizer.optimize(targetValues, weightMatrix, startPoint);

        double[] expectedPoint = {1.0, 1.0, 1.0};
        double[] expectedObjective = {0.0, 0.0, 0.0};
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        Assert.assertArrayEquals(expectedObjective, result.getValue(), 1e-6);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullTarget() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        optimizer.optimize(null, new RealMatrix(3, 3), new double[3]);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullWeight() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        optimizer.optimize(new double[3], null, new double[3]);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullStartPoint() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        optimizer.optimize(new double[3], new RealMatrix(3, 3), null);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_NoConvergence() throws Exception {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        double[] startPoint = {0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, null);
        optimizer.optimize(targetValues, weightMatrix, startPoint);
    }

    @Test(expected = SingularMatrixException.class)
    public void testDoOptimize_SingularMatrixException() throws Exception {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 1.0, 0.0},
                {1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });
        double[] startPoint = {0.0, 0.0, 0.0};

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        optimizer.optimize(targetValues, weightMatrix, startPoint);
    }

    @Test
    public void testComputeObjectiveValue() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] currentPoint = new double[3];

        double[] objectiveValues = optimizer.computeObjectiveValue(startPoint);
        Assert.assertArrayEquals(new double[]{Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}, objectiveValues, 1e-6);

        System.arraycopy(startPoint, 0, currentPoint, 0, 3);
        currentPoint[0] += 0.1;
        objectiveValues = optimizer.computeObjectiveValue(currentPoint);
        double expectedObjectiveValue = Math.pow(0.1, 2.0) + Math.pow(0.2, 2.0) + Math.pow(0.3, 2.0);
        Assert.assertEquals(expectedObjectiveValue, objectiveValues[0], 1e-6);
    }

    @Test
    public void testComputeResiduals() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] targetValues = {1.0, 2.0, 3.0};

        double[] residuals = optimizer.computeResiduals(new double[]{0.0, 0.0, 0.0});
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, residuals, 1e-6);

        double[] currentPoint = new double[3];
        System.arraycopy(startPoint, 0, currentPoint, 0, 3);
        currentPoint[0] += 0.1;
        residuals = optimizer.computeResiduals(currentPoint);
        double expectedResidual = Math.pow(0.1, 2.0) + Math.pow(0.2, 2.0) + Math.pow(0.3, 2.0) - 0.1;
        Assert.assertEquals(expectedResidual, residuals[0], 1e-6);
    }

    @Test
    public void testComputeWeightedJacobian() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1.0e-6, 1.0e-6));
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] currentPoint = new double[3];

        RealMatrix weightedJacobian = optimizer.computeWeightedJacobian(startPoint);
        Assert.assertArrayEquals(new double[][]{new double[3], new double[3], new double[3]}, weightedJacobian.getRowDimensions(), 1);

        for (int i = 0; i < 3; i++) {
            Assert.assertArrayEquals(new double[]{Double.NaN, Double.NaN, Double.NaN}, weightedJacobian.getRow(i), 1);
        }

        System.arraycopy(startPoint, 0, currentPoint, 0, 3);
        currentPoint[0] += 0.1;
        weightedJacobian = optimizer.computeWeightedJacobian(currentPoint);

        double[] jacobian = new double[3];
        double residual = Math.pow(0.1, 2.0) + Math.pow(0.2, 2.0) + Math.pow(0.3, 2.0) - 0.1;
        double weight = 1.0;
        for (int i = 0; i < 3; i++) {
            jacobian[i] = 2.0 * (i + 1) * residual;
        }
        RealMatrix expectedJacobian = new SingularValueDecomposition(new ArrayRealVector(jacobian)).getT();

        for (int i = 0; i < 3; i++) {
            Assert.assertArrayEquals(expectedJacobian.getRow(i), weightedJacobian.getRow(i), 1e-6);
        }
    }
}