package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setup() {
        estimator = new TestEstimator();
        problem = new TestEstimationProblem();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(5);
        assertEquals(5, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();

        assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();

        assertEquals(4, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testExceedMaxCostEval() {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        for (int i = 0; i < 10; i++) {
            estimator.updateResidualsAndCost();
        }
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(problem);
        StandardDeviation sd = new StandardDeviation();
        RealVector residuals = problem.getResiduals();
        double expectedRMS = sd.evaluate(residuals).getValue();
        assertEquals(expectedRMS, rms, 1e-6);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = 10.0;
        assertEquals(expectedChiSquare, chiSquare, 1e-6);
    }

    @Test
    public void testGetCovariances() {
        try {
            double[][] covariances = estimator.getCovariances(problem);
            RealMatrix covarianceMatrix = new Array2DRowRealMatrix(covariances);
            assertEquals(3.0, covarianceMatrix.getEntry(0, 0), 1e-6);
            assertEquals(0.0, covarianceMatrix.getEntry(0, 1), 1e-6);
            assertEquals(0.0, covarianceMatrix.getEntry(0, 2), 1e-6);
            assertEquals(0.0, covarianceMatrix.getEntry(1, 0), 1e-6);
            assertEquals(1.0, covarianceMatrix.getEntry(1, 1), 1e-6);
            assertEquals(0.0, covarianceMatrix.getEntry(1, 2), 1e-6);
            assertEquals(0.0, covarianceMatrix.getEntry(2, 0), 1e-6);
            assertEquals(0.0, covarianceMatrix.getEntry(2, 1), 1e-6);
            assertEquals(3.0, covarianceMatrix.getEntry(2, 2), 1e-6);
        } catch (EstimationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGuessParametersErrors() {
        try {
            double[] errors = estimator.guessParametersErrors(problem);
            assertEquals(3.0, errors[0], 1e-6);
            assertEquals(0.0, errors[1], 1e-6);
            assertEquals(0.0, errors[2], 1e-6);
        } catch (EstimationException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}

class TestEstimator extends AbstractEstimator {

    @Override
    public void estimate(EstimationProblem problem)
            throws EstimationException {
        // empty implementation
    }

}

class TestEstimationProblem implements EstimationProblem {

    @Override
    public double[] getAllParameters() {
        return new double[]{1.0, 2.0, 3.0};
    }

    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[]{
            new WeightedMeasurement(new ArrayRealVector(new double[]{1.0, 2.0, 3.0}), 1.0),
            new WeightedMeasurement(new ArrayRealVector(new double[]{1.1, 2.1, 3.1}), 1.0),
            new WeightedMeasurement(new ArrayRealVector(new double[]{1.2, 2.2, 3.2}), 1.0),
            new WeightedMeasurement(new ArrayRealVector(new double[]{1.3, 2.3, 3.3}), 1.0),
            new WeightedMeasurement(new ArrayRealVector(new double[]{1.4, 2.4, 3.4}), 1.0),
            new WeightedMeasurement(new ArrayRealVector(new double[]{1.5, 2.5, 3.5}), 1.0)};
    }

    @Override
    public RealVector getResiduals() {
        return new ArrayRealVector(new double[]{-0.1, 0.1, -0.2, 0.2, -0.3, 0.3});
    }

}

class WeightedMeasurement implements MeasuredValue {

    private RealVector values;
    private double weight;

    public WeightedMeasurement(RealVector values, double weight) {
        this.values = values;
        this.weight = weight;
    }

    @Override
    public RealVector getValue() {
        return values;
    }

    public double getWeight() {
        return weight;
    }

    public double getResidual() {
        RealVector expected = getValue();
        RealVector measured = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        return expected.subtract(measured).maxComponent();
    }

    public double getPartial(double p) {
        return getValue().getEntry(0) * getValue().getEntry(0) * p;
    }

}