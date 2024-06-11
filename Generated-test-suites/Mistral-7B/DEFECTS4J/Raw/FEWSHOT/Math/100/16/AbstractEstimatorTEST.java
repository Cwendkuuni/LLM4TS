package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new MockEstimationProblem();
        assertEquals(1.0, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new MockEstimationProblem();
        assertEquals(3.0, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new MockEstimationProblem();

        double[][] covariances = estimator.getCovariances(problem);
        assertNotNull(covariances);
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);

        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix actualCovariances = new Array2DRowRealMatrix(covariances);
        assertEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new MockEstimationProblem();

        double[] errors = estimator.guessParametersErrors(problem);
        assertNotNull(errors);
        assertEquals(2, errors.length);
        assertEquals(1.5, errors[0], 0.001);
        assertEquals(1.5, errors[1], 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsSingularProblem() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new SingularEstimationProblem();
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new MockEstimationProblem();
        estimator.initializeEstimate(problem);

        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithInvalidProblem() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new InvalidEstimationProblem();
        estimator.initializeEstimate(problem);
    }

    @Test
    public void testEstimate() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new MockEstimationProblem();

        estimator.setMaxCostEval(10);
        estimator.estimate(problem);

        assertEquals(4, estimator.getCostEvaluations());
        assertEquals(4, estimator.getJacobianEvaluations());
        assertNotNull(estimator.measurements);
        assertNotNull(estimator.parameters);
        assertNotNull(estimator.jacobian);
        assertNotNull(estimator.residuals);
        assertEquals(0.5, estimator.cost, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxEvaluationsReached() {
        AbstractEstimator estimator = new LevenbergMarquardtEstimator();
        EstimationProblem problem = new MockEstimationProblem();

        estimator.setMaxCostEval(1);
        estimator.estimate(problem);
    }

    @Test
    public void testEstimateWithSingularProblem() {
        AbstractEstimator estimator = new SingularEstimator();
        EstimationProblem problem = new SingularEstimationProblem();

        estimator.estimate(problem);

        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.NaN, estimator.cost);
    }

    @Test
    public void testEstimateWithInvalidProblem() {
        AbstractEstimator estimator = new MockEstimator();
        EstimationProblem problem = new InvalidEstimationProblem();

        estimator.estimate(problem);

        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
    }
}

class MockEstimationProblem implements EstimationProblem {
    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[]{
                new WeightedMeasurement(0.5, 1.0, 0.2),
                new WeightedMeasurement(1.0, 1.5, 1.0)
        };
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[]{
                new EstimatedParameter("P1", 0.0),
                new EstimatedParameter("P2", 0.0)
        };
    }
}

class MockEstimator extends AbstractEstimator {
}

class LevenbergMarquardtEstimator extends AbstractEstimator {
}

class SingularEstimator extends AbstractEstimator {
}

class MockEstimatedParameter implements EstimatedParameter {
    private String name;
    private double initialValue;

    public MockEstimatedParameter(String name, double initialValue) {
        this.name = name;
        this.initialValue = initialValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getInitialValue() {
        return initialValue;
    }
}

class MockWeightedMeasurement implements WeightedMeasurement {
    private double weight;
    private double residual;
    private RealVector parameters;

    public MockWeightedMeasurement(double weight, double residual, RealVector parameters) {
        this.weight = weight;
        this.residual = residual;
        this.parameters = parameters;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public double getResidual() {
        return residual;
    }

    @Override
    public RealVector getParameters() {
        return parameters;
    }

    @Override
    public RealVector getPartial(RealVector w) {
        throw new UnsupportedOperationException("getPartial is not supported by MockWeightedMeasurement");
    }
}

class SingularEstimationProblem implements EstimationProblem {
    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[0];
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[0];
    }
}

class InvalidEstimationProblem implements EstimationProblem {
    @Override
    public WeightedMeasurement[] getMeasurements() {
        throw new UnsupportedOperationException("getMeasurements is not supported by InvalidEstimationProblem");
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        throw new UnsupportedOperationException("getAllParameters is not supported by InvalidEstimationProblem");
    }
}