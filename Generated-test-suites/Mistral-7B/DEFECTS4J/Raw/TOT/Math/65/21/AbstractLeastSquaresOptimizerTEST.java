package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecompositionImpl;
import org.apache.commons.math3.optim.AbstractVectorOptimizer;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.SimpleVectorOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleVectorFunction;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.GaussianRandomStartingPointGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** Objective function for tests. */
    private static final ObjectiveFunction FUNCTION = new SimpleVectorFunction("f", 2, new double[] {
            x -> (x[0] - 1) * (x[0] - 1) + (x[1] - 1) * (x[1] - 1),
            x -> (x[0] - 2) * (x[0] - 2) + (x[1] - 3) * (x[1] - 3)
    });

    /** Starting point for tests. */
    private static final double[] START_POINT = new double[] { 1.5, 2.5 };

    /** Target values for tests. */
    private static final double[] TARGET_VALUES = new double[] { 0, 0 };

    /** Weights for tests. */
    private static final double[] WEIGHTS = new double[] { 1, 1 };

    /** Optimizer for tests. */
    private AbstractLeastSquaresOptimizer optimizer;

    /** Starting points generator for tests. */
    private AveragedStartingPointGenerator startingPointGenerator;

    /** Random starting points generator for tests. */
    private GaussianRandomStartingPointGenerator randomStartingPointGenerator;

    /** Tested optimizer. */
    private SimpleVectorOptimizer testedOptimizer;

    /**
     * Set up before each test.
     * @throws Exception if initialization fails
     */
    @Before
    public void setUp() throws Exception {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException {
                // This method is empty, but required to be present.
                return new VectorialPointValuePair(point, objective);
            }
        };
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(20);
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker(1e-5));
        testedOptimizer = new SimpleVectorOptimizer(optimizer, Functions.NO_NULL_VECTOR,
                new MaxEval(10));

        startingPointGenerator = new AveragedStartingPointGenerator(new double[][] { START_POINT }, 1);
        randomStartingPointGenerator = new GaussianRandomStartingPointGenerator(new double[] { 0.0, 0.0 }, 1);
    }

    /**
     * Test that optimizer correctly computes the optimal point with averaged
     * starting point.
     * @throws Exception if optimization fails
     */
    @Test
    public void testAveragedStartingPoint() throws Exception {
        PointValuePair result = testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS,
                startingPointGenerator.getStartingPoint());
        assertNotNull(result);
        assertEquals(TARGET_VALUES, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
    }

    /**
     * Test that optimizer correctly handles FunctionEvaluationException with
     * averaged starting point.
     * @throws Exception if optimization fails
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testAveragedStartingPointFailedEvaluation() throws Exception {
        testedOptimizer.optimize(new FunctionWithFunctionEvaluationException(FUNCTION), TARGET_VALUES, WEIGHTS,
                startingPointGenerator.getStartingPoint());
    }

    /**
     * Test that optimizer correctly handles MaxIterationsExceededException with
     * averaged starting point.
     * @throws Exception if optimization fails
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testAveragedStartingPointMaxIterationsExceeded() throws Exception {
        optimizer.setMaxIterations(0);
        testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS,
                startingPointGenerator.getStartingPoint());
    }

    /**
     * Test that optimizer correctly computes the optimal point with random
     * starting point.
     * @throws Exception if optimization fails
     */
    @Test
    public void testRandomStartingPoint() throws Exception {
        PointValuePair result = testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS,
                randomStartingPointGenerator.getStartingPoint());
        assertNotNull(result);
        assertEquals(TARGET_VALUES, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
    }

    /**
     * Test that optimizer correctly handles FunctionEvaluationException with
     * random starting point.
     * @throws Exception if optimization fails
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testRandomStartingPointFailedEvaluation() throws Exception {
        testedOptimizer.optimize(new FunctionWithFunctionEvaluationException(FUNCTION), TARGET_VALUES, WEIGHTS,
                randomStartingPointGenerator.getStartingPoint());
    }

    /**
     * Test that optimizer correctly handles MaxIterationsExceededException with
     * random starting point.
     * @throws Exception if optimization fails
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRandomStartingPointMaxIterationsExceeded() throws Exception {
        optimizer.setMaxIterations(0);
        testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS,
                randomStartingPointGenerator.getStartingPoint());
    }

    /**
     * Test that optimizer correctly computes the optimal point with the given
     * objective function.
     * @throws Exception if optimization fails
     */
    @Test
    public void testOptimizer() throws Exception {
        PointValuePair result = testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS, START_POINT);
        assertNotNull(result);
        assertEquals(TARGET_VALUES, result.getPoint(), 1e-5);
        assertEquals(0, result.getValue(), 1e-5);
    }

    /**
     * Test that optimizer correctly handles FunctionEvaluationException.
     * @throws Exception if optimization fails
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizerFailedEvaluation() throws Exception {
        testedOptimizer.optimize(new FunctionWithFunctionEvaluationException(FUNCTION), TARGET_VALUES, WEIGHTS,
                START_POINT);
    }

    /**
     * Test that optimizer correctly handles MaxIterationsExceededException.
     * @throws Exception if optimization fails
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizerMaxIterationsExceeded() throws Exception {
        optimizer.setMaxIterations(0);
        testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS, START_POINT);
    }

    /**
     * Test that optimizer correctly handles MaxEvaluationsExceededException.
     * @throws Exception if optimization fails
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizerMaxEvaluationsExceeded() throws Exception {
        optimizer.setMaxEvaluations(0);
        testedOptimizer.optimize(FUNCTION, TARGET_VALUES, WEIGHTS, START_POINT);
    }

    /**
     * Function with FunctionEvaluationException.
     */
    private static class FunctionWithFunctionEvaluationException
            extends SimpleVectorFunction {

        /** Objective function. */
        private final ObjectiveFunction function;

        /**
         * Constructor.
         * @param function objective function
         */
        public FunctionWithFunctionEvaluationException(ObjectiveFunction function) {
            super("f", 2, new double[] { 0, 0 });
            this.function = function;
        }

        @Override
        public double value(double[] x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x, LocalizedFormats.UNSUPPORTED_OPERATION,
                    "FunctionWithFunctionEvaluationException cannot evaluate value");
        }

        @Override
        public double[] gradient(double[] x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x, LocalizedFormats.UNSUPPORTED_OPERATION,
                    "FunctionWithFunctionEvaluationException cannot evaluate gradient");
        }

        @Override
        public double[][] jacobian(double[] x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x, LocalizedFormats.UNSUPPORTED_OPERATION,
                    "FunctionWithFunctionEvaluationException cannot evaluate jacobian");
        }

        @Override
        public double value(double[] x) {
            return function.value(x);
        }

        @Override
        public double[] gradient(double[] x) {
            return function.gradient(x);
        }

        @Override
        public double[][] jacobian(double[] x) {
            return function.jacobian(x);
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }

    }

}