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
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.UnconstrainedMinimizer;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerConstructor() {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer();
        Assert.assertTrue(gno.isUseLU());
        Assert.assertNotNull(gno.getConvergenceChecker());

        GaussNewtonOptimizer gnoChecker = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        Assert.assertTrue(gnoChecker.isUseLU());
        Assert.assertNotNull(gnoChecker.getConvergenceChecker());

        GaussNewtonOptimizer gnoLU = new GaussNewtonOptimizer(true);
        Assert.assertTrue(gnoLU.isUseLU());
        Assert.assertNotNull(gnoLU.getConvergenceChecker());

        GaussNewtonOptimizer gnoQR = new GaussNewtonOptimizer(false);
        Assert.assertFalse(gnoQR.isUseLU());
        Assert.assertNotNull(gnoQR.getConvergenceChecker());
    }

    @Test
    public void testDoOptimize_DefaultSettings() throws ConvergenceException, NullArgumentException {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer();
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        gno.setFunction(new UnaryFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2) + Math.pow(x[1] - 1.0, 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                grad[0] = 2 * (x[0] - 1.0);
                grad[1] = 2 * (x[1] - 1.0);
                return grad;
            }
        });

        gno.optimize(x, weightMatrix);

        double[] expectedResult = {0.5, 0.5};
        Assert.assertArrayEquals(expectedResult, x, 0.001);
    }

    @Test
    public void testDoOptimize_LUDecomposition() throws ConvergenceException, NullArgumentException, SingularMatrixException {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(true);
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        gno.setFunction(new UnaryFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2) + Math.pow(x[1] - 1.0, 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                grad[0] = 2 * (x[0] - 1.0);
                grad[1] = 2 * (x[1] - 1.0);
                return grad;
            }
        });

        gno.optimize(x, weightMatrix);

        double[] expectedResult = {0.5, 0.5};
        Assert.assertArrayEquals(expectedResult, x, 0.001);
    }

    @Test
    public void testDoOptimize_QRDecomposition() throws ConvergenceException, NullArgumentException, SingularMatrixException {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(false);
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        gno.setFunction(new UnaryFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2) + Math.pow(x[1] - 1.0, 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                grad[0] = 2 * (x[0] - 1.0);
                grad[1] = 2 * (x[1] - 1.0);
                return grad;
            }
        });

        gno.optimize(x, weightMatrix);

        double[] expectedResult = {0.5, 0.5};
        Assert.assertArrayEquals(expectedResult, x, 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_ConvergenceException() throws ConvergenceException, NullArgumentException {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        double[] x = {1.0, 2.0};
        double[] y = {3.0, 4.0};
        RealMatrix weightMatrix = new SingularValueDecomposition(new ArrayRealVector(new double[]{1.0, 1.0}));

        gno.setFunction(new UnaryFunction() {
            @Override
            public double value(double[] x) {
                return Math.pow(x[0] - 1.0, 2) + Math.pow(x[1] - 1.0, 2);
            }

            @Override
            public double[] gradient(double[] x) {
                double[] grad = new double[x.length];
                grad[0] = 2 * (x[0] - 1.0);
                grad[1] = 2 * (x[1] - 1.0);
                return grad;
            }
        });

        gno.optimize(x, weightMatrix);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullArgumentException() {
        GaussNewtonOptimizer gno = new GaussNewtonOptimizer();
        double[] x = null;
        double[] y = {3.0, 4.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        gno.setFunction(new UnaryFunction() {
            @Override
            public double value(double[] x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double[] gradient(double[] x) {
                throw new UnsupportedOperationException();
            }
        });

        try {
            gno.optimize(x, weightMatrix);
        } catch (NullArgumentException e) {
            // Expected
        }
    }
}