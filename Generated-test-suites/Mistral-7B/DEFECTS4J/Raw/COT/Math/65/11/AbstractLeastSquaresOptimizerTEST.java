package org.apache.commons.math.optimization.general;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.simple.nsga.NSGA2Optimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizer() {
            @Override
            protected VectorialPointValuePair doOptimize()
                    throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
                // Override actual optimization method with a stub
                return new PointValuePair(new double[2], 0.0);
            }
        };
    }

    @Test
    public void testSetMaxIterations() {
        optimizer.setMaxIterations(10);
        Assert.assertEquals(10, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(10);
        Assert.assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetIterations() {
        optimizer.incrementIterationsCounter();
        Assert.assertEquals(1, optimizer.getIterations());
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceededException() throws OptimizationException {
        optimizer.setMaxIterations(0);
        optimizer.incrementIterationsCounter();
    }

    @Test
    public void testUpdateJacobian() throws FunctionEvaluationException {
        double[][] jacobian = optimizer.jacobian;
        optimizer.updateJacobian();
        Assert.assertNotNull(jacobian);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws FunctionEvaluationException {
        optimizer.setFunction(new FunctionWithException());
        optimizer.updateJacobian();
    }

    @Test
    public void testUpdateResidualsAndCost() throws FunctionEvaluationException {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.updateResidualsAndCost();
        Assert.assertNotNull(optimizer.cost);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMaxEvaluationsExceededException() throws FunctionEvaluationException {
        optimizer.setMaxEvaluations(0);
        optimizer.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] objective = {0.5, 0.5};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setObjective(objective);
        Assert.assertEquals(Math.sqrt(0.5), optimizer.getRMS(), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] objective = {0.5, 0.5};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setObjective(objective);
        Assert.assertEquals(2.0, optimizer.getChiSquare(), 0.01);
    }

    @Test(expected = OptimizationException.class)
    public void testUnableToComputeCovarianceSingularProblem() throws OptimizationException, FunctionEvaluationException {
        optimizer.updateJacobian();
        optimizer.getCovariances();
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        double[] target = {1.0, 2.0};
        double[] weights = {1.0, 1.0};
        double[] objective = {0.5, 0.5};
        double[] jacobian = {{-0.1, -0.2}, {-0.3, -0.4}};
        optimizer.setTargetValues(target);
        optimizer.setResidualsWeights(weights);
        optimizer.setObjective(objective);
        optimizer.setJacobian(jacobian);
        double[] errors = optimizer.guessParametersErrors();
        Assert.assertArrayEquals(new double[]{0.1, 0.2}, errors, 0.01);
    }

    @Test
    public void testOptimize() throws OptimizationException, FunctionEvaluationException, IllegalArgumentException {
        OptimizationProblem problem = new NonLinearLeastSquaresProblem(1, 2, new FunctionWithDependencies());
        PointValuePair result = optimizer.optimize(problem.getFunction(), problem.getTarget(), null, null);
        Assert.assertNotNull(result.getPoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws IllegalArgumentException {
        optimizer.optimize(null, null, null, null);
    }

    @Test
    public void testNSGA2Optimizer() {
        NSGA2Optimizer nsagaOptimizer = new NSGA2Optimizer(10, 2, 100);
        OptimizationProblem problem = new NonLinearLeastSquaresProblem(1, 2, new FunctionWithDependencies());
        PointValuePair result = nsagaOptimizer.optimize(problem.getFunction(), problem.getTarget(), null, null);
        Assert.assertNotNull(result.getPoint());
    }
}

class FunctionWithDependencies implements DifferentiableMultivariateVectorialFunction {
    @Override
    public double[] value(double[] x) {
        return new double[]{x[0] * x[1], x[0] * x[1] * x[1]};
    }

    @Override
    public RealMatrix jacobian(double[] x) {
        return MatrixUtils.createRealMatrix(new double[][]{{x[1], x[0]}, {2 * x[1] * x[1], 3 * x[1] * x[1]}});
    }
}

class FunctionWithException implements DifferentiableMultivariateVectorialFunction {
    @Override
    public double[] value(double[] x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(new double[]{0.0, 0.0}, LocalizedFormats.UNABLE_TO_EVALUATE_FUNCTION);
    }

    @Override
    public RealMatrix jacobian(double[] x) {
        return null;
    }
}