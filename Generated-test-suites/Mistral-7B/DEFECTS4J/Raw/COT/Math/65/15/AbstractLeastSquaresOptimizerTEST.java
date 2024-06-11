package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoSolutionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private DifferentiableMultivariateVectorialFunction function;
    private double[] targetValues;
    private double[] weights;
    private double[] startPoint;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
                // This is an empty method, as we're only testing the base class functionality
                return new VectorialPointValuePair(new double[0], new double[0]);
            }
        };

        function = (f -> f.value(new double[0]))::new;
        targetValues = new double[]{1.0, 2.0, 3.0};
        weights = new double[]{1.0, 1.0, 1.0};
        startPoint = new double[]{0.0, 0.0, 0.0};
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(1);
        Assert.assertEquals(1, optimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxIterations() {
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testGetIterations() {
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testIncrementIterationsCounter_maxIterationsReached() {
        optimizer.setMaxIterations(1);
        optimizer.incrementIterationsCounter();
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(1);
        Assert.assertEquals(1, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        optimizer.setMaxEvaluations(10);
        Assert.assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        optimizer.incrementObjectiveEvaluations();
        Assert.assertEquals(1, optimizer.getEvaluations());
    }

    @Test(expected = MaxEvaluationsExceededException.class)
    public void testIncrementObjectiveEvaluations_maxEvaluationsReached() {
        optimizer.setMaxEvaluations(1);
        optimizer.incrementObjectiveEvaluations();
        optimizer.incrementObjectiveEvaluations();
    }

    @Test
    public void testSetConvergenceChecker() {
        optimizer.setConvergenceChecker(new SimpleVectorialValueChecker());
        Assert.assertSame(new SimpleVectorialValueChecker(), optimizer.getConvergenceChecker());
    }

    @Test
    public void testUpdateJacobian_noExceptionThrown() {
        try {
            optimizer.updateJacobian();
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateJacobian_functionEvaluationExceptionThrown() {
        optimizer.jF = (f -> {
            throw new FunctionEvaluationException(new double[0], LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 1, 3);
        })::new;
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost_noExceptionThrown() {
        try {
            optimizer.updateResidualsAndCost();
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testUpdateResidualsAndCost_functionEvaluationExceptionThrown() {
        optimizer.function = (f -> {
            throw new FunctionEvaluationException(new double[0], LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 1, 3);
        });
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double[] objective = new double[]{1.1, 2.2, 3.3};
        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] residualsWeights = new double[]{1.0, 1.0, 1.0};
        double[] residuals = new double[]{0.1, 0.2, 0.3};
        optimizer.objective = objective;
        optimizer.targetValues = target;
        optimizer.residualsWeights = residualsWeights;
        optimizer.residuals = residuals;
        Assert.assertEquals(1.118033988749895, optimizer.getRMS(), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double[] objective = new double[]{1.1, 2.2, 3.3};
        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] residualsWeights = new double[]{1.0, 1.0, 1.0};
        double chiSquare = 0.0;
        optimizer.objective = objective;
        optimizer.targetValues = target;
        optimizer.residualsWeights = residualsWeights;
        for (int i = 0; i < target.length; i++) {
            double residual = target[i] - objective[i];
            chiSquare += residual * residual / residualsWeights[i];
        }
        Assert.assertEquals(4.125, optimizer.getChiSquare(), 0.001);
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariances_functionJacobianEvaluationExceptionThrown() {
        optimizer.function = (f -> {
            throw new FunctionEvaluationException(new double[0], LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 1, 3);
        });
        optimizer.jF = (f -> {
            throw new FunctionEvaluationException(new double[0][], LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 3, 3);
        })::new;
        optimizer.incrementObjectiveEvaluations();
        optimizer.incrementJacobianEvaluations();
        try {
            optimizer.getCovariances();
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (OptimizationException e) {
            Assert.fail("OptimizationException should not be thrown");
        }
    }

    @Test(expected = OptimizationException.class)
    public void testGetCovariances_singularProblem() {
        double[][] jTj = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = i; j < 3; j++) {
                jTj[i][j] = i == j ? 1.0 : 0.0;
            }
        }
        optimizer.jTj = jTj;
        try {
            optimizer.getCovariances();
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (OptimizationException e) {
            Assert.assertEquals(LocalizedFormats.UNABLE_TO_COMPUTE_COVARIANCE_SINGULAR_PROBLEM, e.getMessage());
        }
    }

    @Test
    public void testGuessParametersErrors() {
        double[] objective = new double[]{1.1, 2.2, 3.3};
        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] residualsWeights = new double[]{1.0, 1.0, 1.0};
        double[] covar = new double[3][3];
        optimizer.objective = objective;
        optimizer.targetValues = target;
        optimizer.residualsWeights = residualsWeights;
        try {
            optimizer.getCovariances();
            for (int i = 0; i < 3; i++) {
                covar[i][i] = optimizer.jTj[i][i];
            }
        } catch (FunctionEvaluationException e) {
            // Expected
        } catch (OptimizationException e) {
            Assert.fail("OptimizationException should not be thrown");
        }
        double[] errors = optimizer.guessParametersErrors();
        for (int i = 0; i < errors.length; i++) {
            Assert.assertEquals(Math.sqrt(covar[i][i]) * Math.sqrt(optimizer.getChiSquare() / (optimizer.rows - optimizer.cols)),
                              errors[i], 0.001);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_targetValuesLengthNotEqualWeightsLength() {
        optimizer.optimize(function, targetValues, new double[]{1.0}, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_noSolutionFound() throws NoSolutionException {
        function = (f -> new double[]{Double.NaN, Double.NaN, Double.NaN})::new;
        try {
            optimizer.optimize(function, targetValues, weights, startPoint);
        } catch (NoSolutionException e) {
            // Expected
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown");
        }
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_functionEvaluationExceptionThrown() throws FunctionEvaluationException {
        function = (f -> {
            throw new FunctionEvaluationException(new double[0], LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, 1, 3);
        });
        try {
            optimizer.optimize(function, targetValues, weights, startPoint);
        } catch (NoSolutionException e) {
            // Expected
        } catch (Exception e) {
            Assert.assertEquals(FunctionEvaluationException.class, e.getClass());
        }
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, NoSolutionException {
        double[] objective = new double[]{1.1, 2.2, 3.3};
        double[] target = new double[]{1.0, 2.0, 3.0};
        double[] weights = new double[]{1.0, 1.0, 1.0};
        double[] startPoint = new double[]{0.0, 0.0, 0.0};
        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        double[] expectedObjective = new double[]{0.0, 0.0, 0.0};
        optimizer.optimize(function, target, weights, startPoint);
        Assert.assertArrayEquals(expectedPoint, optimizer.point, 0.0);
        Assert.assertArrayEquals(expectedObjective, optimizer.objective, 0.0);
    }
}