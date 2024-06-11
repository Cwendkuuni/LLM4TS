package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optim.AbstractOptimizationTest;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.StoppingCriteria;
import org.apache.commons.math3.optim.linear.GaussNewtonOptimizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends AbstractOptimizationTest {

    /**
     * Test optimization with default settings.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGaussNewtonOptimizer() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] weights = {1.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public PointValuePair value(PointValuePair initialGuess) {
                return optimizer.optimize(targetValues, initialGuess, weightMatrix, null);
            }
        };
        PointValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix, null);
        assertEquals(3, result.getValue().length);
        assertEquals(0.0, result.getValue()[0], EPSILON);
        assertEquals(1.0, result.getValue()[1], EPSILON);
        assertEquals(2.0, result.getValue()[2], EPSILON);
        assertEquals(0.0, result.getPoint()[0], EPSILON);
        assertEquals(1.0, result.getPoint()[1], EPSILON);
        assertEquals(2.0, result.getPoint()[2], EPSILON);
    }

    /**
     * Test optimization with custom convergence checker.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGaussNewtonOptimizerCustomChecker() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new MyConvergenceChecker());
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] weights = {1.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public PointValuePair value(PointValuePair initialGuess) {
                return optimizer.optimize(targetValues, initialGuess, weightMatrix, null);
            }
        };
        PointValuePair result = optimizer.optimize(targetValues, startPoint, weightMatrix, null);
        assertEquals(3, result.getValue().length);
        assertEquals(0.0, result.getValue()[0], EPSILON);
        assertEquals(1.0, result.getValue()[1], EPSILON);
        assertEquals(2.0, result.getValue()[2], EPSILON);
        assertEquals(0.0, result.getPoint()[0], EPSILON);
        assertEquals(1.0, result.getPoint()[1], EPSILON);
        assertEquals(2.0, result.getPoint()[2], EPSILON);
    }

    /**
     * Test optimization with custom convergence checker that never converges.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGaussNewtonOptimizerCustomNeverConvergeChecker() throws Exception {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new MyNeverConvergeChecker());
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] startPoint = {0.0, 0.0, 0.0};
        double[] weights = {1.0, 1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(weights);
        OptimizationProblem problem = new OptimizationProblem() {
            @Override
            public PointValuePair value(PointValuePair initialGuess) {
                return optimizer.optimize(targetValues, initialGuess, weightMatrix, null);
            }
        };
        assertThrows(ConvergenceException.class, () -> {
            optimizer.optimize(targetValues, startPoint, weightMatrix, null);
        });
    }

    /**
     * Test optimization with null target values.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGaussNewtonOptimizerNullTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertThrows(NullArgumentException.class, () -> {
            optimizer.optimize(null, null, null, null);
        });
    }

    /**
     * Test optimization with null start point.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGaussNewtonOptimizerNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        assertThrows(NullArgumentException.class, () -> {
            optimizer.optimize(targetValues, null, null, null);
        });
    }

    /**
     * Test optimization with null weights.
     * @throws Exception if an error occurs.
     */
    @Test
    public void testGaussNewtonOptimizerNullWeights() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        double[] targetValues = {1.0, 2.0, 3.0};
        assertThrows(NullArgumentException.class, () -> {
            optimizer.optimize(targetValues, null, null, null);
        });
    }

    /**
     * Custom convergence checker that always converges.
     */
    private static class MyConvergenceChecker implements ConvergenceChecker<PointVectorValuePair> {
        @Override
        public boolean converged(int iteration, PointVectorValuePair current, PointVectorValuePair previous) {
            return true;
        }
    }

    /**
     * Custom convergence checker that never converges.
     */
    private static class MyNeverConvergeChecker implements ConvergenceChecker<PointVectorValuePair> {
        @Override
        public boolean converged(int iteration, PointVectorValuePair current, PointVectorValuePair previous) {
            return false;
        }
    }
}