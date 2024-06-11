package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.VectorFormat;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.apache.commons.math3.optim.UnboundedSolutionException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linearregression.LeastSquaresOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.linearregression.LevenbergMarquardtOptimizer;

/**
 * Test class for {@link AbstractLeastSquaresOptimizer}.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** Test data. */
    private static final double[][] X = new double[][] {
            { 1.0, 0.0, 1.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 1.0, 1.0 },
            { 0.0, 1.0, 1.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };

    /** Test target values. */
    private static final double[] y = new double[] { 1.0, 2.0, 3.0, 2.0, 1.0, 1.0 };

    /** Test weights. */
    private static final double[] weights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };

    /** Test starting point. */
    private static final double[] startPoint = new double[] { 0.0, 0.0, 0.0 };

    /** Test minimum point. */
    private static final double[] minPoint = new double[] { 0.5, 0.5, 0.5 };

    /** Test convergence threshold. */
    private static final double EPSILON = 1e-10;

    /** Test max iterations. */
    private static final int MAX_ITERATIONS = 10;

    /** Test max evaluations. */
    private static final int MAX_EVALUATIONS = 100;

    /** Test convergence checker. */
    private static final SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker(EPSILON);

    /** Test least squares optimizer. */
    @org.junit.Test
    public void testSimpleLinearRegression() {
        LeastSquaresOptimizer optimizer = new LeastSquaresOptimizer();
        optimizer.setConvergenceChecker(checker);
        optimizer.setMaxIterations(MAX_ITERATIONS);
        optimizer.setMaxEvaluations(MAX_EVALUATIONS);

        PointValuePair result = optimizer.optimize(
                new LinearRegressionFunction(X, y, weights), y, weights, startPoint);

        assertNotNull(result.getPoint());
        assertEquals(minPoint.length, result.getPoint().length);
        assertArrayEquals(minPoint, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-12);
        assertFalse(Double.isNaN(result.getValue()));
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertNotNull(optimizer.getCovariances());

        // test error handling
        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), null, weights, startPoint);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, null, startPoint);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, weights, null);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, weights, new double[3]);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), null, weights, startPoint);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, null, startPoint);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, weights, new double[3]);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, weights, startPoint, 10);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        try {
            optimizer.optimize(new LinearRegressionFunction(X, y, weights), y, weights, startPoint, -1);
            assertFalse(true);
        } catch (IllegalArgumentException iae) {
            // expected
        }

        // test unbounded solution
        try {
            optimizer.setMaxIterations(0);
            PointValuePair result2 = optimizer.optimize(
                    new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
            assertNull(result2.getPoint());
            assertEquals(Double.POSITIVE_INFINITY, result2.getValue(), 1e-12);
        } catch (NonConvergentIterationException ncie) {
            // expected
        } catch (MaxEvaluationsExceededException mee) {
            // not expected
        } catch (SingularMatrixException sme) {
            // not expected
        }

        // test max iterations exceeded
        try {
            optimizer.setMaxIterations(0);
            optimizer.setMaxEvaluations(0);
            PointValuePair result3 = optimizer.optimize(
                    new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
            assertNull(result3.getPoint());
            assertEquals(Double.POSITIVE_INFINITY, result3.getValue(), 1e-12);
        } catch (MaxIterationsExceededException mie) {
            // expected
        } catch (MaxEvaluationsExceededException mee) {
            // not expected
        } catch (SingularMatrixException sme) {
            // not expected
        }

        // test max evaluations exceeded
        try {
            optimizer.setMaxIterations(MAX_ITERATIONS);
            optimizer.setMaxEvaluations(0);
            PointValuePair result4 = optimizer.optimize(
                    new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
            assertNull(result4.getPoint());
            assertEquals(Double.POSITIVE_INFINITY, result4.getValue(), 1e-12);
        } catch (MaxIterationsExceededException mie) {
            // not expected
        } catch (MaxEvaluationsExceededException mee) {
            // expected
        } catch (SingularMatrixException sme) {
            // not expected
        }

        // test convergence
        try {
            optimizer.setMaxIterations(MAX_ITERATIONS);
            PointValuePair result5 = optimizer.optimize(
                    new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
            assertNotNull(result5.getPoint());
            assertEquals(minPoint.length, result5.getPoint().length);
            assertArrayEquals(minPoint, result5.getPoint(), 1e-4);
            assertEquals(0.0, result5.getValue(), 1e-6);
            assertEquals(GoalType.MINIMIZE, result5.getGoalType());
            assertNotNull(optimizer.getCovariances());
        } catch (FunctionEvaluationException fee) {
            assertFalse(true);
        } catch (NonConvergentIterationException ncie) {
            assertFalse(true);
        } catch (MaxEvaluationsExceededException mee) {
            assertFalse(true);
        } catch (SingularMatrixException sme) {
            assertFalse(true);
        } catch (UnboundedSolutionException use) {
            assertFalse(true);
        }

        // test Hessian
        try {
            LeastSquaresOptimizer optimizer2 = new LeastSquaresOptimizer();
            optimizer2.setConvergenceChecker(checker);
            optimizer2.setMaxIterations(MAX_ITERATIONS);
            optimizer2.setMaxEvaluations(MAX_EVALUATIONS);
            PointValuePair result6 = optimizer2.optimize(
                    new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
            assertNotNull(result6.getPoint());
            double[] point6 = result6.getPoint();
            double[] covar6 = optimizer2.getCovariances();
            RealMatrix hessian = new Array2DRowRealMatrix(covar6);
            assertNotNull(hessian);
            RealVector xi = VectorFormat.DENSE.format(point6);
            RealVector lambda = VectorFormat.DENSE.format(hessian.operate(xi));
            assertEquals(-2.0, lambda.getEntry(0), 1e-6);
            assertEquals(-2.0, lambda.getEntry(1), 1e-6);
            assertEquals(-2.0, lambda.getEntry(2), 1e-6);
        } catch (FunctionEvaluationException fee) {
            assertFalse(true);
        } catch (NonConvergentIterationException ncie) {
            assertFalse(true);
        } catch (MaxEvaluationsExceededException mee) {
            assertFalse(true);
        } catch (SingularMatrixException sme) {
            assertFalse(true);
        } catch (UnboundedSolutionException use) {
            assertFalse(true);
        }

        // test Jacobian
        try {
            LeastSquaresOptimizer optimizer3 = new LeastSquaresOptimizer();
            optimizer3.setConvergenceChecker(checker);
            optimizer3.setMaxIterations(MAX_ITERATIONS);
            optimizer3.setMaxEvaluations(MAX_EVALUATIONS);
            PointValuePair result7 = optimizer3.optimize(
                    new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
            assertNotNull(result7.getPoint());
            double[] point7 = result7.getPoint();
            MultivariateMatrixFunction jF7 = optimizer3.getJacobian();
            assertNotNull(jF7);
            RealVector xi7 = VectorFormat.DENSE.format(point7);
            RealVector ji = jF7.value(xi7);
            assertEquals(3.0, ji.getEntry(0), 1e-6);
            assertEquals(-2.0, ji.getEntry(1), 1e-6);
            assertEquals(-2.0, ji.getEntry(2), 1e-6);
            assertEquals(0.0, ji.getEntry(3), 1e-6);
            assertEquals(0.0, ji.getEntry(4), 1e-6);
            assertEquals(0.0, ji.getEntry(5), 1e-6);
        } catch (FunctionEvaluationException fee) {
            assertFalse(true);
        } catch (NonConvergentIterationException ncie) {
            assertFalse(true);
        } catch (MaxEvaluationsExceededException mee) {
            assertFalse(true);
        } catch (SingularMatrixException sme) {
            assertFalse(true);
        } catch (UnboundedSolutionException use) {
            assertFalse(true);
        }

        // test Levenberg-Marquardt
        LevenbergMarquardtOptimizer optimizer4 = new LevenbergMarquardtOptimizer();
        optimizer4.setConvergenceChecker(checker);
        optimizer4.setMaxIterations(MAX_ITERATIONS);
        optimizer4.setMaxEvaluations(MAX_EVALUATIONS);
        PointValuePair result8 = optimizer4.optimize(
                new LinearRegressionFunction(X, y, weights), y, weights, startPoint);
        assertNotNull(result8.getPoint());
        assertEquals(minPoint.length, result8.getPoint().length);
        assertArrayEquals(minPoint, result8.getPoint(), 1e-4);
        assertEquals(0.0, result8.getValue(), 1e-6);
        assertEquals(GoalType.MINIMIZE, result8.getGoalType());
        assertNotNull(optimizer4.getCovariances());
    }

}