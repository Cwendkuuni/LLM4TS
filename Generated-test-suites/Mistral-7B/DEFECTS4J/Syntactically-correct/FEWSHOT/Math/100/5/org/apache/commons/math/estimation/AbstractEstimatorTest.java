package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Parameterized.Parameters(name = "{index}: test problem with {0} measurements and {1} parameters")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {new DummyEstimationProblem(2, 1), "2 measurements, 1 parameter"},
                {new DummyEstimationProblem(3, 2), "3 measurements, 2 parameters"},
                {new DummyEstimationProblem(2, 3), "2 measurements, 3 parameters"},
                {new DummyEstimationProblem(1, 2), "1 measurement, 2 parameters"},
                {new DummyEstimationProblem(1, 1), "1 measurement, 1 parameter"}
        };
        return Arrays.asList(data);
    }

    @Parameterized.Constructor
    public AbstractEstimatorTest(EstimationProblem problem) {
        this.estimator = new DummyEstimator();
        this.problem = problem;
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        estimator.initializeEstimate(problem);
        assertEquals(problem.getMeasurements().length, estimator.getRows());
        assertEquals(problem.getAllParameters().length, estimator.getCols());
        assertNull(estimator.getJacobian());
        assertNull(estimator.getResiduals());
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0.001);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        RealMatrix expectedJacobian = new RealMatrixImpl(new double[][]{
                {-1.0 / Math.sqrt(problem.getMeasurements()[0].getWeight()), 1.0 / Math.sqrt(problem.getMeasurements()[0].getWeight())},
                {-1.0 / Math.sqrt(problem.getMeasurements()[1].getWeight()), 1.0 / Math.sqrt(problem.getMeasurements()[1].getWeight())}
        });
        RealMatrix expectedResiduals = new RealMatrixImpl(new double[][]{
                problem.getMeasurements()[0].getResidual(),
                problem.getMeasurements()[1].getResidual()
        });
        assertEquals(expectedJacobian.getRowDimension(), estimator.getJacobian().length);
        assertEquals(expectedJacobian.getColumnDimension(), estimator.getCols());
        assertArrayEquals(expectedJacobian.getData(), estimator.getJacobian(), 0.001);
        assertArrayEquals(expectedResiduals.getData(), estimator.getResiduals(), 0.001);
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.getJacobianEvaluations());
        assertEquals(Math.sqrt(problem.getChiSquare() / (problem.getMeasurements().length - problem.getAllParameters().length)), estimator.getCost(), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[][] expectedCovariances = new double[][]{
                {1.0 / (problem.getMeasurements().length - problem.getAllParameters().length), -1.0 / (problem.getMeasurements().length - problem.getAllParameters().length), 0.0},
                {-1.0 / (problem.getMeasurements().length - problem.getAllParameters().length), 1.0 / (problem.getMeasurements().length - problem.getAllParameters().length), 0.0},
                {0.0, 0.0, 1.0}
        };
        RealMatrix expectedJTJ = new RealMatrixImpl(expectedCovariances);
        RealMatrix expectedCovarianceMatrix = expectedJTJ.inverse();
        double[][] actualCovariances = estimator.getCovariances(problem);
        assertEquals(expectedCovarianceMatrix.getRowDimension(), actualCovariances.length);
        assertEquals(expectedCovarianceMatrix.getColumnDimension(), actualCovariances[0].length);
        assertArrayEquals(expectedCovariances, actualCovariances, 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] expectedErrors = new double[]{Math.sqrt(problem.getChiSquare() / (problem.getMeasurements().length - problem.getAllParameters().length)),
                Math.sqrt(problem.getChiSquare() / (problem.getMeasurements().length - problem.getAllParameters().length)),
                1.0};
        assertEquals(expectedErrors.length, estimator.guessParametersErrors(problem).length);
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0.001);
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        RealVector expectedParameters = problem.getAllParameters();
        assertEquals(expectedParameters.getDimension(), estimator.getParameters().length);
        assertArrayEquals(expectedParameters.toArray(), estimator.getParameters(), 0.001);
    }

    @Test
    public void testMaxCostEvalException() throws EstimationException {
        estimator = new DummyEstimator();
        estimator.setMaxCostEval(1);
        problem.setMeasurements(new WeightedMeasurement[1]);
        problem.setAllParameters(new EstimatedParameter[1]);
        estimator.initializeEstimate(problem);
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }
}

class DummyEstimationProblem implements EstimationProblem {

    private final WeightedMeasurement[] measurements;
    private final EstimatedParameter[] allParameters;

    public DummyEstimationProblem(int measurementCount, int parameterCount) {
        this.measurements = new WeightedMeasurement[measurementCount];
        this.allParameters = new EstimatedParameter[parameterCount];
    }

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return measurements;
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return allParameters;
    }

    @Override
    public double getChiSquare() {
        return 0.0;
    }
}

class DummyEstimator extends AbstractEstimator {

    private double[] parameters;

    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        estimator.initializeEstimate(problem);
        estimator.estimateIteratively(problem, 100);
        this.parameters = problem.getAllParameters();
    }

    @Override
    public EstimatedParameter[] getParameters() {
        return parameters;
    }
}