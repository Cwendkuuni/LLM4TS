package org.apache.commons.math.optimization.general;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.NoBracketingException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.LinearPointValuePair;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link AbstractLeastSquaresOptimizer} class.
 */
public class AbstractLeastSquaresOptimizerTest {

    /** Test function for the optimizer. */
    private static final LinearObjectiveFunction TEST_FUNCTION = new LinearObjectiveFunction(new Array2DRowRealMatrix(
            new double[][] { { 2.0, -1.0 }, { -1.0, 2.0 } }),
            new RealVector(new double[] { 4.0, -4.0 }));

    /** Test initial point. */
    private static final double[] TEST_START_POINT = new double[] { 0.0, 0.0 };

    /** Test weights. */
    private static final double[] TEST_WEIGHTS = new double[] { 1.0, 1.0 };

    /** Test target values. */
    private static final double[] TEST_TARGET = new double[] { 2.0, 2.0 };

    /** Test constraints. */
    private static final LinearConstraintSet TEST_CONSTRAINTS = new LinearConstraintSet();

    /** Test optimizer under test. */
    private AbstractLeastSquaresOptimizer optimizer;

    /** Test objective function. */
    private LinearPointValuePair objectiveFunctionValue;

    /** Test point. */
    private double[] testPoint;

    /** Test objective function value. */
    private double[] testObjective;

    /** Test jacobian. */
    private RealMatrix testJacobian;

    /** Test error. */
    private double testError;

    /** Test cost. */
    private double testCost;

    /** Test convergence checker. */
    private VectorialConvergenceChecker checker;

    /** Before method. */
    @Before
    public void setUp() {
        optimizer = new LevenbergMarquardtOptimizer();
        checker = new SimpleVectorialValueChecker();
        optimizer.setConvergenceChecker(checker);
        optimizer.setMaxIterations(10);
        optimizer.setMaxEvaluations(100);
    }

    /** Test optimization with no constraints.
     * @throws FunctionEvaluationException if optimization fails
     */
    @Test
    public void testOptimizationWithoutConstraints() throws FunctionEvaluationException {
        objectiveFunctionValue = optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
        testPoint = objectiveFunctionValue.getPoint();
        testObjective = objectiveFunctionValue.getValue();
        testJacobian = optimizer.getJacobian();
        testError = optimizer.getError();
        testCost = optimizer.getCost();

        // Check optimization result
        assertArrayEquals(TEST_TARGET, testPoint, 1e-9);
        assertEquals(0.0, testObjective[0], 1e-9);
        assertEquals(0.0, testObjective[1], 1e-9);
        assertNotNull(testJacobian);
        assertEquals(0.0, testError, 1e-9);
        assertEquals(0.0, testCost, 1e-9);

        // Check convergence
        assertTrue(optimizer.isConverged());
    }

    /** Test optimization with constraints.
     * @throws FunctionEvaluationException if optimization fails
     * @throws IllegalArgumentException if constraints are invalid
     */
    @Test
    public void testOptimizationWithConstraints() throws FunctionEvaluationException, IllegalArgumentException {
        LinearConstraint constraint = new LinearConstraint(new double[] { 1.0 }, Operator.LEQ, 1.0);
        TEST_CONSTRAINTS.addConstraint(constraint);
        objectiveFunctionValue = optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT,
                TEST_CONSTRAINTS);
        testPoint = objectiveFunctionValue.getPoint();
        testObjective = objectiveFunctionValue.getValue();
        testJacobian = optimizer.getJacobian();
        testError = optimizer.getError();
        testCost = optimizer.getCost();

        // Check optimization result
        assertArrayEquals(TEST_TARGET, testPoint, 1e-9);
        assertEquals(0.0, testObjective[0], 1e-9);
        assertEquals(0.0, testObjective[1], 1e-9);
        assertNotNull(testJacobian);
        assertEquals(0.0, testError, 1e-9);
        assertEquals(0.0, testCost, 1e-9);

        // Check constraints satisfaction
        assertTrue(constraint.isSatisfied(testPoint));

        // Check convergence
        assertTrue(optimizer.isConverged());
    }

    /** Test optimization with incorrect start point dimension.
     * @throws IllegalArgumentException if start point dimension is incorrect
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithIncorrectStartPointDimension() {
        optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, new double[] { 0.0 });
    }

    /** Test optimization with unbounded domain.
     * @throws UnboundedDomainException if optimization fails
     */
    @Test(expected = UnboundedDomainException.class)
    public void testOptimizationWithUnboundedDomain() throws UnboundedDomainException {
        optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT,
                new LinearConstraintSet(new LinearConstraintSet(new LinearConstraint(new double[] { 1.0 },
                        Operator.LEQ, -1.0))));
    }

    /** Test optimization with maximum number of iterations exceeded.
     * @throws MaxEvaluationsExceededException if optimization fails
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizationWithMaxIterationsExceeded() throws MaxEvaluationsExceededException {
        optimizer.setMaxIterations(0);
        optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
    }

    /** Test optimization with maximum number of evaluations exceeded.
     * @throws MaxEvaluationsExceededException if optimization fails
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testOptimizationWithMaxEvaluationsExceeded() throws MaxEvaluationsExceededException {
        optimizer.setMaxEvaluations(0);
        optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
    }

    /** Test optimization with incorrect convergence checker.
     * @throws IllegalArgumentException if convergence checker is incorrect
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithIncorrectConvergenceChecker() {
        optimizer.setConvergenceChecker(null);
        optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
    }

    /** Test optimization with incorrect number of iterations.
     * @throws FunctionEvaluationException if optimization fails
     */
    @Test
    public void testOptimizationWithIncorrectNumberOfIterations() throws FunctionEvaluationException {
        optimizer.setMaxIterations(5);
        objectiveFunctionValue = optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
        testPoint = objectiveFunctionValue.getPoint();
        testObjective = objectiveFunctionValue.getValue();
        testJacobian = optimizer.getJacobian();
        testError = optimizer.getError();
        testCost = optimizer.getCost();

        // Check optimization result
        assertArrayEquals(TEST_TARGET, testPoint, 1e-9);
        assertEquals(0.0, testObjective[0], 1e-9);
        assertEquals(0.0, testObjective[1], 1e-9);
        assertNotNull(testJacobian);
        assertEquals(0.0, testError, 1e-9);
        assertEquals(0.0, testCost, 1e-9);

        // Check convergence
        assertFalse(optimizer.isConverged());
    }

    /** Test optimization with incorrect number of evaluations.
     * @throws FunctionEvaluationException if optimization fails
     */
    @Test
    public void testOptimizationWithIncorrectNumberOfEvaluations() throws FunctionEvaluationException {
        optimizer.setMaxEvaluations(5);
        objectiveFunctionValue = optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
        testPoint = objectiveFunctionValue.getPoint();
        testObjective = objectiveFunctionValue.getValue();
        testJacobian = optimizer.getJacobian();
        testError = optimizer.getError();
        testCost = optimizer.getCost();

        // Check optimization result
        assertArrayEquals(TEST_TARGET, testPoint, 1e-9);
        assertEquals(0.0, testObjective[0], 1e-9);
        assertEquals(0.0, testObjective[1], 1e-9);
        assertNotNull(testJacobian);
        assertEquals(0.0, testError, 1e-9);
        assertEquals(0.0, testCost, 1e-9);

        // Check convergence
        assertFalse(optimizer.isConverged());
    }

    /** Test optimization with incorrect goal type.
     * @throws IllegalArgumentException if goal type is incorrect
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationWithIncorrectGoalType() {
        optimizer.setGoalType(GoalType.MINIMIZE_VECTOR);
        optimizer.optimize(TEST_FUNCTION, TEST_TARGET, TEST_WEIGHTS, TEST_START_POINT);
    }

}