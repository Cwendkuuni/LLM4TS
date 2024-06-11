package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.vector.linear.AffineFunction;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        new AbstractLeastSquaresOptimizer();
    }

    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxEvaluations());
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSettersAndGetters() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(5);
        optimizer.setMaxIterations(10); // This should not change the value since it is already set to 10
        optimizer.setMaxEvaluations(15);
        assertEquals(10, optimizer.getMaxIterations());
        assertEquals(15, optimizer.getMaxEvaluations());
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        assertSame(optimizer.getConvergenceChecker(), optimizer.getConvergenceChecker());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testIncrementIterationsCounter() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
        assertEquals(2, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceeded() throws OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.incrementIterationsCounter();
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianThrowsFunctionEvaluationException() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationException e = assertThrows(OptimizationException.class, () -> optimizer.updateJacobian());
        assertTrue(e.getCause() instanceof FunctionEvaluationException);
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.updateJacobian();
        assertArrayEquals(new double[][]{{-1.0, -2.0}, {-3.0, -4.0}}, optimizer.jacobian);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostThrowsFunctionEvaluationException() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        OptimizationException e = assertThrows(OptimizationException.class, () -> optimizer.updateResidualsAndCost());
        assertTrue(e.getCause() instanceof FunctionEvaluationException);
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setPoint(new double[]{1.0, 1.0});
        optimizer.updateResidualsAndCost();
        assertEquals(new double[]{1.0, 1.0}, optimizer.getPoint());
        assertEquals(new double[]{-1.0, -1.0}, optimizer.residuals);
        assertEquals(Math.sqrt(2.0), optimizer.cost, 0.001);
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setResiduals(new double[]{0.1, 0.2, 0.3});
        assertEquals(Math.sqrt(0.1 * 1.0 * 1.0 + 0.2 * 1.0 * 1.0 + 0.3 * 1.0 * 1.0), optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        double[] target = new double[]{1.0, 1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setResiduals(new double[]{0.1, 0.2, 0.3});
        assertEquals(0.1 * 0.1 / 1.0 + 0.2 * 0.2 / 1.0 + 0.3 * 0.3 / 1.0, optimizer.getChiSquare(), 0.001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setPoint(new double[]{1.0, 1.0});
        optimizer.updateResidualsAndCost();
        double[][] covariances = optimizer.getCovariances();
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
        assertEquals(1.0 / 3.0, covariances[0][0], 0.001);
        assertEquals(1.0 / 3.0, covariances[0][1], 0.001);
        assertEquals(1.0 / 3.0, covariances[1][0], 0.001);
        assertEquals(1.0 / 3.0, covariances[1][1], 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setPoint(new double[]{1.0, 1.0});
        optimizer.updateResidualsAndCost();
        double[] errors = optimizer.guessParametersErrors();
        assertEquals(2, errors.length);
        assertEquals(Math.sqrt(1.0 / 3.0), errors[0], 0.001);
        assertEquals(Math.sqrt(1.0 / 3.0), errors[1], 0.001);
    }

    @Test
    public void testOptimizeSuccess() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setPoint(new double[]{1.0, 1.0});
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);
        PointValuePair result = optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{1.0, 1.0});
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{0.0, 0.0}, result.getPoint(), 0.001);
        assertEquals(-Math.sqrt(2.0), result.getValue(), 0.001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeThrowsFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setPoint(new double[]{1.0, 1.0});
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);
        OptimizationException e = assertThrows(OptimizationException.class, () -> optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}));
        assertTrue(e.getCause() instanceof FunctionEvaluationException);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimizeThrowsOptimizationException() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setPoint(new double[]{1.0, 1.0});
        optimizer.setMaxIterations(1);
        optimizer.setMaxIterations(1); // This should not change the value since it is already set to 1
        OptimizationException e = assertThrows(OptimizationException.class, () -> optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}, new double[]{1.0, 1.0}));
        assertTrue(e.getMessage().contains("Max iterations exceeded"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidStartPoint() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{0.0, 0.0});
        optimizer.setFunction(function);
        optimizer.setMaxIterations(1);
        optimizer.setMaxEvaluations(1);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, new double[]{1.0, 1.0}, new double[]{1.0}, new double[]{1.0, 1.0}));
        assertTrue(e.getMessage().contains("Invalid start point dimensions"));
    }

    @Test(expected = UnboundedDomainException.class)
    public void testOptimizeWithUnboundedDomain() {
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        ObjectiveFunction function = new AffineFunction(new double[][]{{0.0, 0.0}, {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY}}, new double[]{Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY});
        optimizer.setFunction(function);
        try {
            optimizer.optimize(function, new double[]{0.0, 0.0}, new double[]{1.0, 1.0}, new double[]{0.0, 0.0});
            fail("UnboundedDomainException expected");
        } catch (OptimizationException e) {
            assertTrue(e.getCause() instanceof UnboundedDomainException);
        }
    }
}