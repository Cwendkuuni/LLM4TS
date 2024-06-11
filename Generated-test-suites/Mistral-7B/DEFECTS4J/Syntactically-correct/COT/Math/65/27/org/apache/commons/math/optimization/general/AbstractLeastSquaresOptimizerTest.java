package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractLeastSquaresOptimizerTest {

    @InjectMocks
    private AbstractLeastSquaresOptimizer optimizer;

    @Mock
    private DifferentiableMultivariateVectorialFunction function;

    /**
     * Test constructor without arguments.
     */
    @Test
    public void testDefaultConstructor() {
        AbstractLeastSquaresOptimizer optimizerInstance = new AbstractLeastSquaresOptimizer();
        Assert.assertNotNull(optimizerInstance);
    }

    /**
     * Test setMaxIterations method.
     */
    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(2);
        Assert.assertEquals(2, optimizer.getMaxIterations());
    }

    /**
     * Test getIterations method.
     */
    @Test
    public void testGetIterations() {
        Assert.assertEquals(0, optimizer.getIterations());
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
    }

    /**
     * Test setMaxEvaluations method.
     */
    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(2);
        Assert.assertEquals(2, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        Assert.assertEquals(0, optimizer.getEvaluations());
    }

    /**
     * Test incrementIterationsCounter method.
     */
    @Test(expected = OptimizationException.class)
    public void testIncrementIterationsCounterMaxIterationsExceeded()
            throws OptimizationException {

        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
    }

    /**
     * Test updateJacobian method.
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianFunctionEvaluationException()
            throws FunctionEvaluationException {

        optimizer.jF = mock(MultivariateMatrixFunction.class);
        when(optimizer.jF.value(optimizer.point)).thenThrow(new FunctionEvaluationException());
        optimizer.updateJacobian();
    }

    /**
     * Test updateJacobian method with a jacobian of incorrect dimensions.
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianFunctionEvaluationExceptionJacobianDimensionsMismatch()
            throws FunctionEvaluationException {

        final int[] dimensions = {2, 3};
        optimizer.rows = dimensions[0];
        optimizer.cols = dimensions[1];
        optimizer.jacobian = new double[optimizer.rows][optimizer.cols];
        when(optimizer.jF.value(optimizer.point)).thenReturn(optimizer.jacobian);

        optimizer.updateJacobian();
        Assert.assertArrayEquals(optimizer.jacobian, optimizer.jF.value(optimizer.point));
        Assert.assertEquals(optimizer.rows, optimizer.jacobian.length);
        Assert.assertEquals(optimizer.cols, optimizer.jacobian[0].length);

        optimizer.jacobian[0] = new double[optimizer.cols + 1];

        optimizer.updateJacobian();
    }

    /**
     * Test updateJacobian method.
     */
    @Test
    public void testUpdateJacobian() {
        optimizer.jF = mock(MultivariateMatrixFunction.class);
        when(optimizer.jF.value(optimizer.point)).thenReturn(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        optimizer.updateJacobian();
        Assert.assertArrayEquals(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, optimizer.jacobian);
    }

    /**
     * Test updateResidualsAndCost method.
     * @throws FunctionEvaluationException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCostFunctionEvaluationException()
            throws FunctionEvaluationException {

        optimizer.objective = new double[optimizer.rows];
        when(optimizer.function.value(optimizer.point)).thenThrow(new FunctionEvaluationException());
        optimizer.updateResidualsAndCost();
    }

    /**
     * Test getCost method.
     */
    @Test
    public void testGetCost() {
        optimizer.cost = 1.0;
        Assert.assertEquals(1.0, optimizer.getCost(), 0.0001);
    }

    /**
     * Test getRMS method.
     */
    @Test
    public void testGetRMS() {
        final double[] target = new double[]{1.0, 2.0};
        final double[] weights = new double[]{3.0, 4.0};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.point = new double[]{0.0, 0.0};
        optimizer.objective = new double[]{1.0, 2.0};
        optimizer.residuals = new double[]{0.5, 0.5};
        optimizer.cost = 1.0;

        double expectedRMS = Math.sqrt(0.5 * 3.0 + 0.5 * 4.0) / Math.sqrt(2.0);
        Assert.assertEquals(expectedRMS, optimizer.getRMS(), 0.0001);
    }

    /**
     * Test getChiSquare method.
     */
    @Test
    public void testGetChiSquare() {
        final double[] target = new double[]{1.0, 2.0};
        final double[] weights = new double[]{3.0, 4.0};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.point = new double[]{0.0, 0.0};
        optimizer.objective = new double[]{1.0, 2.0};
        optimizer.residuals = new double[]{0.5, 0.5};

        double expectedChiSquare = 0.5 * (Math.pow(0.5, 2.0) / 3.0 + Math.pow(0.5, 2.0) / 4.0);
        Assert.assertEquals(expectedChiSquare, optimizer.getChiSquare(), 0.0001);
    }

    /**
     * Test getCovariances method.
     * @throws FunctionEvaluationException
     * @throws OptimizationException
     */
    @Test
    public void testGetCovariances()
            throws FunctionEvaluationException, OptimizationException {

        optimizer.jacobian = new double[][]{{1.0, 1.0}, {2.0, 3.0}};
        optimizer.updateJacobian();

        double[][] expectedCovariances = new double[][]{{0.5, -0.1}, {-0.1, 0.25}};
        try {
            Assert.assertArrayEquals(expectedCovariances, optimizer.getCovariances());
        } catch (SingularMatrixException e) {
            Assert.fail();
        }
    }

    /**
     * Test guessParametersErrors method.
     * @throws FunctionEvaluationException
     * @throws OptimizationException
     */
    @Test
    public void testGuessParametersErrors()
            throws FunctionEvaluationException, OptimizationException {

        optimizer.jacobian = new double[][]{{1.0, 1.0}, {2.0, 3.0}};
        optimizer.updateJacobian();
        double[] expectedErrors = new double[]{Math.sqrt(0.5), Math.sqrt(0.25)};
        Assert.assertArrayEquals(expectedErrors, optimizer.guessParametersErrors());
    }

    /**
     * Test optimize method with a mock function.
     * @throws FunctionEvaluationException
     * @throws OptimizationException
     * @throws IllegalArgumentException
     */
    @Test
    public void testOptimizeMockFunction()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(5);

        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{3.0, 4.0};
        double[] startPoint = new double[]{0.5, 0.5};

        optimizer.function = mock(DifferentiableMultivariateVectorialFunction.class);
        when(optimizer.function.value(optimizer.point)).thenReturn(new double[]{1.1, 2.2});
        when(optimizer.function.jacobian()).thenReturn(mock(MultivariateMatrixFunction.class));

        PointValuePair result = optimizer.optimize(optimizer.function, target, weights, startPoint);
        Assert.assertArrayEquals(new double[]{1.0, 2.0}, result.getPoint(), 0.0001);
        Assert.assertArrayEquals(new double[]{0.0, 0.0}, optimizer.point, 0.0001);
    }

    /**
     * Test optimize method with a function that doesn't meet the convergence criteria.
     * @throws FunctionEvaluationException
     * @throws OptimizationException
     * @throws IllegalArgumentException
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizeDoesntConverge()
            throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {

        optimizer.setMaxIterations(2);
        optimizer.setMaxEvaluations(5);

        double[] target = new double[]{1.0, 2.0};
        double[] weights = new double[]{3.0, 4.0};
        double[] startPoint = new double[]{0.5, 0.5};

        optimizer.function = mock(DifferentiableMultivariateVectorialFunction.class);
        when(optimizer.function.value(optimizer.point)).thenReturn(new double[]{1.1, 2.2});
        when(optimizer.function.jacobian()).thenReturn(mock(MultivariateMatrixFunction.class));
        when(optimizer.function.jacobian().value(optimizer.point)).thenReturn(new double[][]{
            {1.1, 1.2},
            {2.3, 3.4}
        });
        when(optimizer.checker.isConverged(optimizer)).thenReturn(Boolean.FALSE);

        optimizer.optimize(optimizer.function, target, weights, startPoint);
    }
}