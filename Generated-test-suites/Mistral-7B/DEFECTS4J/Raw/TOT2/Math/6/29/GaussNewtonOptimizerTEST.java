package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setUp() {
        double[] weights = new double[]{1.0, 1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker()) {
            {
                setWeight(new ArrayRealVector(weights, false));
            }
        };
    }

    /**
     * Test the constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        Assert.assertTrue(optimizer.isUseLU());
        Assert.assertNotNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test the constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, null);
        Assert.assertFalse(optimizer.isUseLU());
        Assert.assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test the optimization method with a simple example.
     */
    @Test
    public void testSimpleOptimization() throws Exception {
        double[] startPoint = new double[]{-1.0, -1.0};
        PointVectorValuePair result = optimizer.optimize(new InitialGuess(startPoint), new MaxEval(1000));
        double[] expectedPoint = new double[]{1.0, 1.0};
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-15);
    }

    /**
     * Test the optimization method with a non-converging example.
     */
    @Test(expected = ConvergenceException.class)
    public void testNonConvergingOptimization() throws Exception {
        double[] startPoint = new double[]{0.0, 0.0};
        optimizer.optimize(new InitialGuess(startPoint), new MaxEval(1000));
    }

    /**
     * Test the optimization method with a null convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullConvergenceChecker() {
        new GaussNewtonOptimizer(true, null);
    }

    /**
     * Test the optimization method with a null initial guess.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullInitialGuess() {
        optimizer.optimize(null, new MaxEval(1000));
    }

    /**
     * Test the optimization method with a null max evaluations.
     */
    @Test(expected = NullArgumentException.class)
    public void testNullMaxEvaluations() {
        optimizer.optimize(new InitialGuess(new double[]{0.0}), null);
    }

    /**
     * Test the computation of the objective value and the jacobian.
     */
    @Test
    public void testComputeObjectiveValueAndJacobian() {
        double[] point = new double[]{1.0, 1.0};
        double[] objectiveValue = optimizer.computeObjectiveValue(point);
        double[] jacobian = optimizer.computeWeightedJacobian(point);

        Assert.assertArrayEquals(new double[]{0.0, 0.0}, objectiveValue, 1e-15);
        Assert.assertArrayEquals(new double[]{-1.0, -1.0}, jacobian[0], 1e-15);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, jacobian[1], 1e-15);
    }

    /**
     * Test the computation of the residuals.
     */
    @Test
    public void testComputeResiduals() {
        double[] objectiveValue = new double[]{1.0, 1.0, 1.0};
        double[] residuals = optimizer.computeResiduals(objectiveValue);

        Assert.assertArrayEquals(new double[]{-1.0, -1.0, -1.0}, residuals, 1e-15);
    }

    /**
     * Test the solution of the linearized least-squares problem using LU decomposition.
     */
    @Test
    public void testSolveLinearizedLeastSquaresWithLU() {
        RealMatrix a = MatrixUtils.dense2DArray(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        RealMatrix b = MatrixUtils.createColumnVector(new double[]{1.0, 1.0, 1.0});

        DecompositionSolver solver = new LUDecomposition(a).getSolver();
        double[] solution = solver.solve(b.toArray()).toArray();

        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solution, 1e-15);
    }

    /**
     * Test the solution of the linearized least-squares problem using QR decomposition.
     */
    @Test
    public void testSolveLinearizedLeastSquaresWithQR() {
        RealMatrix a = MatrixUtils.dense2DArray(new double[][]{
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        RealMatrix b = MatrixUtils.createColumnVector(new double[]{1.0, 1.0, 1.0});

        DecompositionSolver solver = new QRDecomposition(a).getSolver();
        double[] solution = solver.solve(b.toArray()).toArray();

        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solution, 1e-15);
    }

    /**
     * Test the singular value decomposition of a singular matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularValueDecomposition() {
        RealMatrix a = MatrixUtils.dense2DArray(new double[][]{
            {1.0, 0.0},
            {0.0, 0.0}
        });

        new QRDecomposition(a).getSolver();
    }
}