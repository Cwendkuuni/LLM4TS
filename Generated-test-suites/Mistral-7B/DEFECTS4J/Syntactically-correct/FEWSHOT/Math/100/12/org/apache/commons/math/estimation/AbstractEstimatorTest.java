package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.initializeEstimate(problem);
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertNull(estimator.getJacobian());
        assertNull(estimator.getResiduals());
        assertArrayEquals(new WeightedMeasurement[0], estimator.measurements);
        assertArrayEquals(new EstimatedParameter[0], estimator.parameters);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 0.0;
            }
        };
        assertEquals(0.0, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0.0;
            }
        };
        assertEquals(0.0, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                return new double[0][0];
            }
        };
        RealMatrix expected = new RealMatrixImpl(new double[0][0]);
        RealMatrix actual = estimator.getCovariances(problem);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[0];
            }
        };
        double[] expected = new double[0];
        double[] actual = estimator.guessParametersErrors(problem);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("Test exception");
            }
        };
        estimator.estimate(problem);
    }

    @Test
    public void testUpdateJacobian() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
            int rows = 2;
            int cols = 2;
            double[] jacobian = new double[rows * cols];
            @Override
            protected void updateJacobian() {
                Arrays.fill(jacobian, 1.0);
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[][] expected = new double[][] {{1.0, 1.0}, {1.0, 1.0}};
        double[][] actual = new RealMatrixImpl(new double[rows][cols]).getData();
        estimator.jacobian = actual;
        assertTrue(Arrays.deepEquals(expected, actual));
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
            }
            double[] residuals = new double[2];
            double cost = 0.0;
            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                Arrays.fill(residuals, 1.0);
                cost = 2.0;
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double[] expectedResiduals = new double[] {1.0, 1.0};
        double[] actualResiduals = new double[2];
        estimator.residuals = actualResiduals;
        assertTrue(Arrays.equals(expectedResiduals, actualResiduals));
        assertEquals(2.0, estimator.cost, 0.001);
    }
}

class DummyEstimationProblem implements EstimationProblem {
    @Override
    public WeightedMeasurement[] getMeasurements() {
        return new WeightedMeasurement[0];
    }

    @Override
    public EstimatedParameter[] getAllParameters() {
        return new EstimatedParameter[0];
    }

    @Override
    public EstimatedParameter[] getUnboundParameters() {
        return new EstimatedParameter[0];
    }

    @Override
    public WeightedMeasurement getMeasurement(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EstimatedParameter getParameter(int index) {
        throw new UnsupportedOperationException();
    }
}

class DummyWeightedMeasurement implements WeightedMeasurement {
    @Override
    public double getWeight() {
        return 1.0;
    }

    @Override
    public double getResidual() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getPartial(int index) {
        throw new UnsupportedOperationException();
    }
}

class DummyEstimatedParameter implements EstimatedParameter {
    @Override
    public int getIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getError() {
        throw new UnsupportedOperationException();
    }
}