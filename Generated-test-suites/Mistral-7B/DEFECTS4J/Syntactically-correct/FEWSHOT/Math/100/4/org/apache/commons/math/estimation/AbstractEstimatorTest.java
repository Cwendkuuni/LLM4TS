package org.apache.commons.cli;

import static org.junit.Assert.*;
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
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertEquals(0.0, estimator.cost, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testInitializeEstimateWithInvalidParameters() {
        EstimationProblem problem = new DummyEstimationProblem();
        problem.setAllParameters(new EstimatedParameter[] { new EstimatedParameter("p1", null) });
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.initializeEstimate(problem);
    }

    @Test
    public void testUpdateJacobian() {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2 });
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
            @Override
            protected void updateJacobian() {
                super.updateJacobian();
                for (int i = 0; i < rows * cols; i++) {
                    assertEquals(0, jacobian[i]);
                }
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        assertEquals(2, estimator.getJacobianEvaluations());
        for (int i = 0; i < rows * cols; i++) {
            if (i < rows * cols / 2) {
                assertEquals(0, jacobian[i]);
            } else {
                assertEquals(3.0, jacobian[i], 0.01);
            }
        }
    }

    @Test(expected = EstimationException.class)
    public void testUpdateJacobianWithMaxCostEvalReached() {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2 });
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
            @Override
            protected void updateJacobian() throws EstimationException {
                super.updateJacobian();
                throw new EstimationException("Maximum number of cost evaluations reached");
            }
            @Override
            public int getMaxCostEval() {
                return 1;
            }
        };
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
    }

    @Test
    public void testEstimateWithConvergence() {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2 });
        EstimatedParameter p1 = new EstimatedParameter("p1", 0.0);
        EstimatedParameter p2 = new EstimatedParameter("p2", 0.0);
        problem.setAllParameters(new EstimatedParameter[] { p1, p2 });
        AbstractEstimator estimator = new DummyEstimator() {
            @Override
            protected double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[] { 1.0, 1.0 };
            }
        };
        estimator.setMaxCostEval(100);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        assertEquals(100, estimator.getCostEvaluations());
        assertEquals(100, estimator.getJacobianEvaluations());
        assertArrayEquals(new double[] { 1.5, 2.5 }, problem.getAllParameters()[0].getValue(), 0.01);
        assertArrayEquals(new double[] { 1.5, 2.5 }, problem.getAllParameters()[1].getValue(), 0.01);
    }

    @Test
    public void testEstimateWithDivergence() {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2 });
        EstimatedParameter p1 = new EstimatedParameter("p1", 0.0);
        EstimatedParameter p2 = new EstimatedParameter("p2", 0.0);
        problem.setAllParameters(new EstimatedParameter[] { p1, p2 });
        AbstractEstimator estimator = new DummyEstimator() {
            @Override
            protected double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return new double[] { 1.0, 1.0 };
            }
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("Could not converge");
            }
        };
        estimator.setMaxCostEval(100);
        estimator.initializeEstimate(problem);

        try {
            estimator.estimate(problem);
            fail("EstimationException expected");
        } catch (EstimationException e) {
            // expected
        }

        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.getJacobianEvaluations());
        assertNull(problem.getAllParameters()[0].getValue());
        assertNull(problem.getAllParameters()[1].getValue());
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, 3.0, 3.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2, wm3 });
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return 0.0;
            }
        };
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(0.0, rms, 0.01);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, 3.0, 3.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2, wm3 });
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 0.0;
            }
        };
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(0.0, chiSquare, 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, 3.0, 3.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2, wm3 });
        EstimatedParameter p1 = new EstimatedParameter("p1", 0.0);
        EstimatedParameter p2 = new EstimatedParameter("p2", 0.0);
        problem.setAllParameters(new EstimatedParameter[] { p1, p2 });
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
        assertEquals(1.0, covariances[0][0], 0.01);
        assertEquals(1.0, covariances[0][1], 0.01);
        assertEquals(1.0, covariances[1][0], 0.01);
        assertEquals(1.0, covariances[1][1], 0.01);
        assertEquals(-0.5, covariances[0][1], 0.01);
        assertEquals(-0.5, covariances[1][0], 0.01);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement(1.0, 1.0, 1.0);
        WeightedMeasurement wm2 = new WeightedMeasurement(1.0, 2.0, 2.0);
        WeightedMeasurement wm3 = new WeightedMeasurement(1.0, 3.0, 3.0);
        problem.setMeasurements(new WeightedMeasurement[] { wm1, wm2, wm3 });
        EstimatedParameter p1 = new EstimatedParameter("p1", 0.0);
        EstimatedParameter p2 = new EstimatedParameter("p2", 0.0);
        problem.setAllParameters(new EstimatedParameter[] { p1, p2 });
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[] { 1.0, 1.0 }, errors, 0.01);
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
    public double getCostFunctionValue() {
        return 0.0;
    }

    @Override
    public WeightedMeasurement getMeasurement(int index) {
        return null;
    }

    @Override
    public EstimatedParameter getParameter(int index) {
        return null;
    }
}

class DummyEstimator extends AbstractEstimator {
    @Override
    protected double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
        return new double[0];
    }
}