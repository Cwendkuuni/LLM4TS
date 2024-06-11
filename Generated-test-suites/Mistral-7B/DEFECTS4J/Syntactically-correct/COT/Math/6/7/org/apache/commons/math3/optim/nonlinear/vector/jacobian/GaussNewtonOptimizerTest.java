import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerConstructor1() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        Assert.assertTrue(optimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(checker);
        Assert.assertEquals(SimpleVectorValueChecker.class, checker.getClass());
    }

    @Test
    public void testGaussNewtonOptimizerConstructor2() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6, 1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
        Assert.assertFalse(optimizer.isUseLU());
        Assert.assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    @Test
    public void testGaussNewtonOptimizerConstructor3() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        Assert.assertTrue(optimizer.isUseLU());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        Assert.assertNotNull(checker);
        Assert.assertEquals(SimpleVectorValueChecker.class, checker.getClass());
    }

    @Test
    public void testGaussNewtonOptimizerConstructor4() {
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6, 1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerConstructor5() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(null);
    }

    @Test
    public void testDoOptimize_Convergence() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));
        PointVectorValuePair result = optimizer.optimize(new FunctionWithJacobian() {
            @Override
            public double value(RealVector x) {
                double x0 = x.getEntry(0);
                double x1 = x.getEntry(1);
                double x2 = x.getEntry(2);
                return Math.pow(x0 - targetValues[0], 2)
                        + Math.pow(x1 - targetValues[1], 2)
                        + Math.pow(x2 - targetValues[2], 2);
            }

            @Override
            public RealVector gradient(RealVector x) {
                double x0 = x.getEntry(0);
                double x1 = x.getEntry(1);
                double x2 = x.getEntry(2);
                RealVector grad = new ArrayRealVector(new double[]{2 * (x0 - targetValues[0]),
                        2 * (x1 - targetValues[1]),
                        2 * (x2 - targetValues[2])});
                return grad;
            }
        }, startPoint, weightMatrix);

        double[] expectedResult = new double[]{1.0 - (targetValues[0] - startPoint[0]) / 2.0,
                1.0 - (targetValues[1] - startPoint[1]) / 2.0,
                1.0 - (targetValues[2] - startPoint[2]) / 2.0};
        Assert.assertArrayEquals(expectedResult, result.getPoint(), 1e-5);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_Divergence() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));
        PointVectorValuePair result = optimizer.optimize(new FunctionWithJacobian() {
            @Override
            public double value(RealVector x) {
                double x0 = x.getEntry(0);
                double x1 = x.getEntry(1);
                double x2 = x.getEntry(2);
                return Math.pow(x0, 2) + Math.pow(x1, 2) + Math.pow(x2, 2) + 1.0;
            }

            @Override
            public RealVector gradient(RealVector x) {
                double x0 = x.getEntry(0);
                double x1 = x.getEntry(1);
                double x2 = x.getEntry(2);
                return new ArrayRealVector(new double[]{2 * x.getEntry(0),
                        2 * x.getEntry(1),
                        2 * x.getEntry(2)});
            }
        }, startPoint, weightMatrix);

        Assert.fail("Method doOptimize should have thrown a ConvergenceException.");
    }

    @Test(expected = SingularMatrixException.class)
    public void testDoOptimize_SingularMatrixException() {
        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-6, 1e-6));
        PointVectorValuePair result = optimizer.optimize(new FunctionWithJacobian() {
            @Override
            public double value(RealVector x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public RealVector gradient(RealVector x) {
                throw new UnsupportedOperationException();
            }
        }, startPoint, weightMatrix);

        Assert.fail("Method doOptimize should have thrown a SingularMatrixException.");
    }
}

class FunctionWithJacobian {
    public double value(RealVector x) {
        throw new UnsupportedOperationException();
    }

    public RealVector gradient(RealVector x) {
        throw new UnsupportedOperationException();
    }
}