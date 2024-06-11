package org.apache.commons.math3.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;

/**
 * Test cases for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Test constructor with default settings and LU decomposition.
     */
    @Test
    public void testDefaultConstructorWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLUDecomposition());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertEquals(SimpleVectorValueChecker.class, checker.getClass());
    }

    /**
     * Test constructor with default settings and QR decomposition.
     */
    @Test
    public void testDefaultConstructorWithQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        assertEquals(false, optimizer.isUseLUDecomposition());
        ConvergenceChecker<PointVectorValuePair> checker = optimizer.getConvergenceChecker();
        assertEquals(SimpleVectorValueChecker.class, checker.getClass());
    }

    /**
     * Test constructor with custom settings.
     */
    @Test
    public void testCustomConstructor() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iter, PointVectorValuePair previous, PointVectorValuePair current) {
                return current.getValue() < 1.0;
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, customChecker);
        assertEquals(true, optimizer.isUseLUDecomposition());
        assertEquals(customChecker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Test optimization with LU decomposition.
     */
    @Test
    public void testOptimizeWithLUDecomposition() throws ConvergenceException {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.1, 0.2, 0.3};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedPoint = {1.0, 1.0, 1.0};
        assertEquals(expectedPoint, result.getPoint());
        double[] expectedObjective = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedObjective, result.getValue());
    }

    /**
     * Test optimization with QR decomposition.
     */
    @Test
    public void testOptimizeWithQRDecomposition() throws ConvergenceException {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.1, 0.2, 0.3};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedPoint = {1.0, 1.0, 1.0};
        assertEquals(expectedPoint, result.getPoint());
        double[] expectedObjective = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedObjective, result.getValue());
    }

    /**
     * Test optimization with null convergence checker.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullConvergenceChecker() {
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.1, 0.2, 0.3};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-square weight matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testOptimizeWithNonSquareWeightMatrix() throws ConvergenceException {
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.1, 0.2};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-numeric target values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNonNumericTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new String[]{"foo", "bar"});
        optimizer.optimize();
    }

    /**
     * Test optimization with null target values.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with null start point.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[]{-1.0, -2.0});
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with non-numeric start point values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithNonNumericStartPointValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[]{1.0, 2.0});
        optimizer.setStartPoint(new String[]{"foo", "bar"});
        optimizer.optimize();
    }

    /**
     * Test optimization with null weight matrix.
     */
    @Test(expected = NullArgumentException.class)
    public void testOptimizeWithNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[]{1.0, 2.0});
        optimizer.setStartPoint(new double[]{0.0, 0.0});
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with empty weight matrix.
     */
    @Test
    public void testOptimizeWithEmptyWeightMatrix() throws ConvergenceException {
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.1, 0.2};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[0][]);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedPoint = {0.1, 0.2};
        assertEquals(expectedPoint, result.getPoint());
        double[] expectedObjective = {0.0, 0.0};
        assertArrayEquals(expectedObjective, result.getValue());
    }

    /**
     * Test optimization with singular weight matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testOptimizeWithSingularWeightMatrix() throws ConvergenceException {
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.1, 0.2};
        RealMatrix weightMatrix = SingularValueDecomposition.singularValueDecomposition(new Array2DRowRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}})).getSolver().getSolution().toArray();
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.optimize();
    }

    /**
     * Test optimization with null convergence checker and non-numeric target values.
     */
    @Test
    public void testOptimizeWithNullConvergenceCheckerAndNonNumericTargetValues() {
        double[] targetValues = {"foo", "bar"};
        double[] startPoint = {0.1, 0.2};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(null);
        optimizer.optimize();
    }

    /**
     * Test optimization with null convergence checker and empty target values.
     */
    @Test
    public void testOptimizeWithNullConvergenceCheckerAndEmptyTargetValues() {
        double[] targetValues = {};
        double[] startPoint = {0.1, 0.2};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}});
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(null);
        optimizer.optimize();
    }
}