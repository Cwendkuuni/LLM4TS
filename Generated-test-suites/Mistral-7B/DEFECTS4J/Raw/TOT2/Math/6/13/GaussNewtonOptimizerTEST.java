package org.apache.commons.math3.optimization.general;

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
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.UnboundedDomainException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    /**
     * Tests the default constructor with default settings.
     */
    @Test
    public void testDefaultConstructor() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertEquals(true, optimizer.isUseLU());
        assertEquals(SimpleVectorValueChecker.class, optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Tests the constructor with LU decomposition and a custom convergence checker.
     */
    @Test
    public void testLUConstructorWithCustomChecker() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair current, PointVectorValuePair previous) {
                // A custom convergence checker that always returns true.
                return true;
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, customChecker);
        assertEquals(true, optimizer.isUseLU());
        assertEquals(customChecker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Tests the constructor with QR decomposition and a custom convergence checker.
     */
    @Test
    public void testQRConstructorWithCustomChecker() {
        ConvergenceChecker<PointVectorValuePair> customChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair current, PointVectorValuePair previous) {
                // A custom convergence checker that always returns true.
                return true;
            }
        };
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, customChecker);
        assertEquals(false, optimizer.isUseLU());
        assertEquals(customChecker.getClass(), optimizer.getConvergenceChecker().getClass());
    }

    /**
     * Tests the {@link #doOptimize()} method with valid inputs.
     * @throws SingularMatrixException if an error occurs during the decomposition of the Hessian matrix.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test
    public void testDoOptimizeValidInputs() throws SingularMatrixException, ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 2.0};
        double[] startPoint = new double[]{0.0, 0.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] expectedValues = new double[]{1.5, 1.5};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
        PointVectorValuePair result = optimizer.doOptimize();
        assertEquals(expectedValues, result.getPoint(), 0.001);
    }

    /**
     * Tests the {@link #doOptimize()} method when the target array is empty.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeEmptyTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setTarget(new double[0]);
        optimizer.doOptimize();
    }

    /**
     * Tests the {@link #doOptimize()} method when the start point array is null.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setStartPoint(null);
        optimizer.doOptimize();
    }

    /**
     * Tests the {@link #doOptimize()} method when the weight matrix is null.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setWeight(null);
        optimizer.doOptimize();
    }

    /**
     * Tests the {@link #doOptimize()} method when the convergence checker is null.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(null);
        optimizer.doOptimize();
    }

    /**
     * Tests the {@link #doOptimize()} method when the convergence checker does not implement {@link ConvergenceChecker}.
     */
    @Test
    public void testDoOptimizeInvalidChecker() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(new Object());
        assertThrows(ClassCastException.class, () -> optimizer.doOptimize());
    }

    /**
     * Tests the {@link #doOptimize()} method when the convergence checker has an incorrect number of arguments.
     */
    @Test
    public void testDoOptimizeCheckerInvalidArguments() {
        class InvalidChecker implements ConvergenceChecker<PointValuePair> {
            @Override
            public boolean converged(int iteration, PointValuePair current, PointValuePair previous) {
                throw new UnsupportedOperationException();
            }
        }
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        optimizer.setConvergenceChecker(new InvalidChecker());
        assertThrows(ClassCastException.class, () -> optimizer.doOptimize());
    }

    /**
     * Tests the {@link #doOptimize()} method with invalid inputs.
     * @throws SingularMatrixException if an error occurs during the decomposition of the Hessian matrix.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test
    public void testDoOptimizeInvalidInputs() throws SingularMatrixException, ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, -2.0};
        double[] startPoint = new double[]{0.0, 0.0};
        double[] weights = new double[]{1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
        assertThrows(IllegalArgumentException.class, () -> optimizer.doOptimize());
    }

    /**
     * Tests the {@link #doOptimize()} method with invalid inputs when the convergence checker is a {@link MaxEval}.
     * @throws SingularMatrixException if an error occurs during the decomposition of the Hessian matrix.
     * @throws ConvergenceException if the maximizer fails to converge.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeInvalidInputsWithMaxEvalChecker() throws SingularMatrixException, ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, -2.0};
        double[] startPoint = new double[]{0.0, 0.0};
        double[] weights = new double[]{1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(new MaxEval(1000));
        optimizer.doOptimize();
    }

    /**
     * Tests the {@link #doOptimize()} method when an error occurs during the decomposition of the Hessian matrix.
     */
    @Test(expected = SingularMatrixException.class)
    public void testDoOptimizeSingularMatrixException() throws SingularMatrixException, ConvergenceException {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = new double[]{1.0, 0.0};
        double[] startPoint = new double[]{0.0, 0.0};
        double[] weights = new double[]{1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        double[] x = new double[]{1.0, 1.0};
        RealMatrix J = new SingularValueDecomposition(new BlockRealMatrix(x)).getSolution().transpose();
        RealMatrix H = J.transpose().multiply(J);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
        optimizer.doOptimize();
    }
}