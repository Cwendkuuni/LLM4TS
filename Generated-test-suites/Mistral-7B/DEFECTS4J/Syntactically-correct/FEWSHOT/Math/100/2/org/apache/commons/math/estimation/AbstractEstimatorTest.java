package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

public class AbstractEstimatorTest {

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblemMock();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        estimator.initializeEstimate(problem);
        assertEquals(0.0, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblemMock();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        estimator.initializeEstimate(problem);
        double[] residuals = estimator.residuals;
        double chiSquare = estimator.getChiSquare(problem);
        for (int i = 0; i < residuals.length; i++) {
            WeightedMeasurement measurement = problem.getMeasurements()[i];
            double expectedResidual = measurement.getResidual();
            double weight = measurement.getWeight();
            assertEquals(expectedResidual, residuals[i], 0.001);
            assertEquals(weight, weight * expectedResidual * expectedResidual, 0.001);
            chiSquare += residuals[i] * residuals[i] / weight;
        }
        assertEquals(chiSquare, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblemMock();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[][] covariances = estimator.getCovariances(problem);
        // check size
        assertEquals(estimator.cols, covariances.length);
        assertEquals(estimator.cols, covariances[0].length);
        // check diagonal
        for (int i = 0; i < estimator.cols; i++) {
            assertEquals(0.0, covariances[i][i] - 1.0, 0.001);
        }
        // check off-diagonal
        for (int i = 0; i < estimator.cols; i++) {
            for (int j = i + 1; j < estimator.cols; j++) {
                assertEquals(0.0, covariances[i][j], 0.001);
                assertEquals(0.0, covariances[j][i], 0.001);
            }
        }
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblemMock();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // do nothing
            }
        };
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        for (int i = 0; i < estimator.parameters.length; i++) {
            assertEquals(0.0, errors[i], 0.001);
        }
    }

}

class EstimationProblemMock implements EstimationProblem {

    private final WeightedMeasurement[] measurements = new WeightedMeasurement[3];
    private final EstimatedParameter[] parameters = new EstimatedParameter[2];

    @Override
    public WeightedMeasurement[] getMeasurements() {
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurementMock(i);
        }
        return measurements;
    }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
        parameters[0] = new EstimatedParameterMock(0);
        parameters[1] = new EstimatedParameterMock(1);
        return parameters;
    }

    @Override
    public int getAllParametersLength() {
        return parameters.length;
    }

    @Override
    public String getParameterName(int index) {
        return String.valueOf(index);
    }

}

class WeightedMeasurementMock implements WeightedMeasurement {

    private final int index;

    public WeightedMeasurementMock(int index) {
        this.index = index;
    }

    @Override
    public double getWeight() {
        return 1.0;
    }

    @Override
    public double getResidual() {
        return index + 1.0;
    }

    @Override
    public double getPartial(double parameter) {
        return Math.pow(parameter, index);
    }

}

class EstimatedParameterMock implements EstimatedParameter {

    private final int index;

    public EstimatedParameterMock(int index) {
        this.index = index;
    }

    @Override
    public double getValue() {
        return 0.0;
    }

    @Override
    public String getName() {
        return String.valueOf(index);
    }

}