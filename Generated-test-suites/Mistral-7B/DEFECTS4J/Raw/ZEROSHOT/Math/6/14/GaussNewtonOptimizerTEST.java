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
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnboundedObjectiveFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private double[] targetValues;
    private double[] startPoint;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        targetValues = new double[]{1.0d, 2.0d, 3.0d};
        startPoint = new double[]{1.5d, 2.5d};
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0d, 0.0d, 0.0d}, {0.0d, 1.0d, 0.0d}, {0.0d, 0.0d, 1.0d}});
    }

    @Test
    public void testEmptyConstructor() {
        Assert.assertThrows(NullArgumentException.class, () -> new GaussNewtonOptimizer());
    }

    @Test
    public void testConstructorWithDefaultSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithCustomSettings() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        Assert.assertFalse(optimizer.isUseLU());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testOptimizeWithValidArguments() throws Exception {
        OptimizableFunction function = new OptimizableFunction() {
            @Override
            public double value(RealVector point) {
                return 0.0d;
            }

            @Override
            public RealVector gradient(RealVector point) {
                RealVector gradient = new ArrayRealVector(new double[]{1.0d, 1.0d}, false);
                return gradient;
            }
        };

        optimizer.setFunction(function);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);

        PointValuePair result = optimizer.optimize();
        Assert.assertArrayEquals(new double[]{2.0d, 2.0d}, result.getPoint(), 0.001);
        Assert.assertEquals(0.0d, result.getValue(), 0.001);
    }

    @Test
    public void testOptimizeWithNullTarget() {
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(null);
        Assert.assertThrows(NullArgumentException.class, () -> optimizer.optimize());
    }

    @Test
    public void testOptimizeWithNullStartPoint() {
        optimizer.setStartPoint(null);
        optimizer.setTarget(targetValues);
        Assert.assertThrows(NullArgumentException.class, () -> optimizer.optimize());
    }

    @Test
    public void testOptimizeWithNullWeight() {
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(null);
        Assert.assertThrows(NullArgumentException.class, () -> optimizer.optimize());
    }

    @Test
    public void testOptimizeWithUnboundedFunction() {
        OptimizableFunction function = new UnboundedObjectiveFunction();
        optimizer.setFunction(function);
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(weightMatrix);
        Assert.assertThrows(UnsupportedOperationException.class, () -> optimizer.optimize());
    }

    @Test
    public void testOptimizeWithSingularWeightMatrix() {
        RealMatrix singularWeightMatrix = new SingularValueDecomposition(new ArrayRealVector(new double[]{1.0d, 1.0d}, false)).getS(0);
        optimizer.setFunction(new OptimizableFunction() {
            @Override
            public double value(RealVector point) {
                return 0.0d;
            }

            @Override
            public RealVector gradient(RealVector point) {
                return new ArrayRealVector(new double[]{1.0d, 1.0d}, false);
            }
        });
        optimizer.setStartPoint(startPoint);
        optimizer.setTarget(targetValues);
        optimizer.setWeight(singularWeightMatrix);
        Assert.assertThrows(SingularMatrixException.class, () -> optimizer.optimize());
    }
}

class OptimizableFunction implements GeneralOptimizable {
    @Override
    public double value(RealVector point) {
        return 0.0d;
    }

    @Override
    public RealVector gradient(RealVector point) {
        RealVector gradient = new ArrayRealVector(new double[]{1.0d, 1.0d}, false);
        return gradient;
    }
}

class UnboundedObjectiveFunction implements GeneralOptimizable {
    @Override
    public double value(RealVector point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public RealVector gradient(RealVector point) {
        throw new UnsupportedOperationException();
    }
}