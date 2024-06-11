package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorialValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize() {
                // Override AbstractLeastSquaresOptimizer's abstract method with a dummy implementation.
                // This is necessary to create AbstractLeastSquaresOptimizer instances in the test cases.
                return new PointValuePair(new double[]{1.0, 2.0}, new double[]{3.0, 4.0});
            }
        };
        optimizer.setMaxIterations(1);
    }

    @Test
    public void testSetMaxIterations() {
        int maxIterations = 2;
        optimizer.setMaxIterations(maxIterations);
        Assert.assertEquals(maxIterations, optimizer.getMaxIterations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxIterations() {
        optimizer.setMaxIterations(-1);
    }

    @Test
    public void testGetIterations() {
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 3;
        optimizer.setMaxEvaluations(maxEvaluations);
        Assert.assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxEvaluations() {
        optimizer.setMaxEvaluations(-1);
    }

    @Test
    public void testGetEvaluations() {
        optimizer.incrementObjectiveEvaluations();
        Assert.assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        optimizer.incrementJacobianEvaluations();
        Assert.assertEquals(1, optimizer.getJacobianEvaluations());
    }

    @Test
    public void testSetConvergenceChecker() {
        SimpleVectorialValueChecker checker = new SimpleVectorialValueChecker(1e-6);
        optimizer.setConvergenceChecker(checker);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetJacobian() {
        double[][] jacobian = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        try {
            optimizer.updateJacobian();
            Assert.assertArrayEquals(jacobian, optimizer.jacobian);
        } catch (FunctionEvaluationException e) {
            throw new AssertionError("Unexpected exception.", e);
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobianWithInvalidFunction() {
        optimizer.function = null;
        optimizer.incrementJacobianEvaluations();
        optimizer.updateJacobian();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateJacobianWithInvalidPointSize() {
        double[] point = new double[3];
        optimizer.point = point;
        optimizer.incrementJacobianEvaluations();
        try {
            optimizer.updateJacobian();
            Assert.fail("Expected exception.");
        } catch (FunctionEvaluationException e) {
            Assert.assertEquals(3, e.getDimensionMismatchSize());
        }
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();

        optimizer.updateResidualsAndCost();

        double[] expectedResiduals = new double[]{-0.5, -0.5};
        double expectedCost = Math.sqrt(2.0);

        Assert.assertArrayEquals(expectedResiduals, optimizer.residuals);
        Assert.assertEquals(expectedCost, optimizer.cost, 0.001);
    }

    @Test
    public void testGetRMS() {
        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] objective = new double[]{0.5, 0.5};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.objective = objective.clone();

        double rms = optimizer.getRMS();

        double expectedRMS = Math.sqrt(0.5);

        Assert.assertEquals(expectedRMS, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = new double[]{1.0, 1.0};
        double[] weights = new double[]{1.0, 1.0};
        double[] objective = new double[]{-0.5, -0.5};
        optimizer.targetValues = target.clone();
        optimizer.residualsWeights = weights.clone();
        optimizer.objective = objective.clone();

        double chiSquare = optimizer.getChiSquare();

        double expectedChiSquare = 1.0;

        Assert.assertEquals(expectedChiSquare, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        double[][] expectedCovariances = new double[][]{{0.1, 0.05}, {0.05, 0.1}};

        optimizer.updateJacobian();

        double[][] covariances = optimizer.getCovariances();

        Assert.assertArrayEquals(expectedCovariances, covariances);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGetCovariancesWithInvalidJacobian() throws OptimizationException {
        optimizer.jacobian = new double[1][2];
        optimizer.getCovariances();
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariancesWithSingularJacobian() throws FunctionEvaluationException, OptimizationException {
        double[][] jacobian = new double[][]{{0.0, 1.0}, {1.0, 0.0}};
        optimizer.jacobian = jacobian;
        try {
            optimizer.getCovariances();
            Assert.fail("Expected exception.");
        } catch (OptimizationException e) {
            // Expected exception.
        }
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] expectedErrors = new double[]{Math.sqrt(0.1), Math.sqrt(0.1)};

        optimizer.updateJacobian();
        double[] errors = optimizer.guessParametersErrors();

        Assert.assertArrayEquals(expectedErrors, errors);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testGuessParametersErrorsWithInvalidJacobian() throws OptimizationException {
        optimizer.jacobian = new double[1][2];
        optimizer.guessParametersErrors();
    }
}