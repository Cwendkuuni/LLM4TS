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
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealMatrix weightMatrix;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        // Create a simple test function for optimization.
        function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        // Create a weight matrix with all elements equal to 1.
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0}});
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-6, 1e-6));
    }

    // Test constructor with default settings (LU decomposition and SimpleVectorValueChecker).
    @Test
    public void testDefaultConstructor() {
        optimizer = new GaussNewtonOptimizer();
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    // Test constructor with LU decomposition.
    @Test
    public void testLUConstructor() {
        optimizer = new GaussNewtonOptimizer(true, null);
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    // Test constructor with QR decomposition.
    @Test
    public void testQRConstructor() {
        optimizer = new GaussNewtonOptimizer(false, null);
        Assert.assertFalse(optimizer.isUseLU());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    // Test constructor with custom convergence checker.
    @Test
    public void testCustomCheckerConstructor() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-4, 1e-4));
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertEquals(1e-4, optimizer.getConvergenceChecker().getGradientTolerance(), 1e-8);
        Assert.assertEquals(1e-4, optimizer.getConvergenceChecker().getFunctionValueTolerance(), 1e-8);
    }

    // Test doOptimize method with simple test function.
    @Test
    public void testDoOptimize() {
        optimizer.optimize(function, weightMatrix, null);
        double expectedResult = 1.0;
        Assert.assertEquals(expectedResult, optimizer.getPoint()[0], 1e-6);
        Assert.assertEquals(expectedResult * expectedResult, optimizer.getCost(), 1e-6);
    }

    // Test exception handling in doOptimize method.
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullFunction() {
        optimizer.optimize(null, weightMatrix, null);
    }

    // Test exception handling in doOptimize method with null start point.
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullStartPoint() {
        optimizer.optimize(function, null, null);
    }

    // Test exception handling in doOptimize method with null weight matrix.
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullWeightMatrix() {
        optimizer.optimize(function, null, null);
    }

    // Test exception handling in doOptimize method with singular weight matrix.
    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeSingularWeightMatrix() {
        RealMatrix singularWeightMatrix = new SingularValueDecomposition(new ArrayRealVector(new double[]{-1.0, 1.0})).getMatrixU();
        optimizer.optimize(function, singularWeightMatrix, null);
    }

    // Test exception handling in doOptimize method when unable to solve the singular problem.
    @Test(expected = ConvergenceException.class)
    public void testDoOptimizeUnableToSolveSingularProblem() {
        optimizer.optimize(function, weightMatrix, null);
        optimizer.setUseLU(false);
        optimizer.optimize(function, weightMatrix, null);
    }

    // Test exception handling in doOptimize method when the optimization process exceeds the maximum number of iterations.
    @Test(expected = UnsupportedOperationException.class)
    public void testDoOptimizeMaxIterationsExceeded() {
        optimizer.setMaxIterations(0);
        optimizer.optimize(function, weightMatrix, null);
    }
}