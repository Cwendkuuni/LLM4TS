import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.nonlinear.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    GaussNewtonOptimizer optimizer;
    ConvergenceChecker<PointValuePair> convergenceChecker;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(GoalType.MINIMIZE, new SimpleVectorValueChecker());
        convergenceChecker = optimizer.getConvergenceChecker();
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSimpleConstructorWithDefaultSettings() {
        optimizer = new GaussNewtonOptimizer(true);
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(optimizer.getConvergenceChecker().getClass(), SimpleVectorValueChecker.class);
    }

    @Test
    public void testSimpleConstructorWithDefaultSettingsAndChecker() {
        optimizer = new GaussNewtonOptimizer(true, convergenceChecker);
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertSame(optimizer.getConvergenceChecker(), convergenceChecker);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorWithNullChecker() {
        optimizer = new GaussNewtonOptimizer(true, null);
    }

    @Test
    public void testOptimize_Convergence() {
        double[] data = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] startPoint = new double[]{5.0, 6.0};
        optimizer.setObjectiveFunction(new FooFunction(data));
        optimizer.setStartPoint(startPoint);

        PointValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[]{2.0, 2.0}, result.getValue(), 0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimize_NonConvergence() {
        double[] data = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] startPoint = new double[]{5.0, 6.0};
        optimizer.setObjectiveFunction(new NonConvergingFunction(data));
        optimizer.setStartPoint(startPoint);
        optimizer.optimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testOptimize_SingularMatrixException() {
        double[] data = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] startPoint = new double[]{5.0, 6.0};
        optimizer.setObjectiveFunction(new SingularFunction(data));
        optimizer.setStartPoint(startPoint);
        optimizer.optimize();
    }

    @Test(expected = MathInternalError.class)
    public void testOptimize_MathInternalError() {
        double[] data = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] startPoint = new double[]{5.0, 6.0};
        optimizer.setObjectiveFunction(new MathErrorFunction(data));
        optimizer.setStartPoint(startPoint);
        optimizer.optimize();
    }

    @Test
    public void testSetObjectiveFunction_NullFunction() {
        optimizer.setObjectiveFunction(null);
    }

    @Test
    public void testSetStartPoint_NullPoint() {
        optimizer.setObjectiveFunction(new FooFunction(new double[0]));
        optimizer.setStartPoint(null);
    }

    static class FooFunction implements GeneralRealValuedFunction {
        private double[] data;

        public FooFunction(double[] data) {
            this.data = data;
        }

        @Override
        public double value(RealVector x) {
            double sum = 0;
            for (int i = 0; i < data.length; ++i) {
                double xi = x.getEntry(i);
                sum += Math.pow(data[i] - xi, 2);
            }
            return sum;
        }

        @Override
        public RealVector gradient(RealVector x) {
            RealVector grad = new ArrayRealVector(new double[data.length]);
            for (int i = 0; i < data.length; ++i) {
                double xi = x.getEntry(i);
                grad.setEntry(i, 2 * (data[i] - xi));
            }
            return grad;
        }
    }

    static class NonConvergingFunction implements GeneralRealValuedFunction {
        private double[] data;

        public NonConvergingFunction(double[] data) {
            this.data = data;
        }

        @Override
        public double value(RealVector x) {
            return Double.POSITIVE_INFINITY;
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(new double[data.length]);
        }
    }

    static class SingularFunction implements GeneralRealValuedFunction {
        private double[] data;

        public SingularFunction(double[] data) {
            this.data = data;
        }

        @Override
        public double value(RealVector x) {
            RealMatrix jacobian = optimizer.computeWeightedJacobian(x);
            SingularValueDecomposition svd = new SingularValueDecomposition(jacobian);
            return svd.getSingularValues()[0];
        }

        @Override
        public RealVector gradient(RealVector x) {
            return optimizer.computeWeightedJacobian(x).getColumnVector(0);
        }
    }

    static class MathErrorFunction implements GeneralRealValuedFunction {
        private double[] data;

        public MathErrorFunction(double[] data) {
            this.data = data;
        }

        @Override
        public double value(RealVector x) {
            throw new MathInternalError();
        }

        @Override
        public RealVector gradient(RealVector x) {
            return new ArrayRealVector(new double[data.length]);
        }
    }
}