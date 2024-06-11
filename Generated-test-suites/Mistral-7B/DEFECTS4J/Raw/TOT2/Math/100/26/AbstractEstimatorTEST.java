package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test for {@link AbstractEstimator#setMaxCostEval(int)} and
     * {@link AbstractEstimator#getCostEvaluations()}, with valid arguments.
     */
    @Test
    public void testSetAndGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(0, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
        estimator.setMaxCostEval(5);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(5, estimator.getCostEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#setMaxCostEval(int)} and
     * {@link AbstractEstimator#getCostEvaluations()}, with an invalid argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetCostEvaluationsNegative() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(-1);
    }

    /**
     * Test for {@link AbstractEstimator#setMaxCostEval(int)} and
     * {@link AbstractEstimator#getCostEvaluations()}, with a zero argument.
     */
    @Test
    public void testSetAndGetCostEvaluationsZero() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.setMaxCostEval(0);
        assertEquals(0, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
        estimator.setMaxCostEval(5);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(5, estimator.getCostEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#setJacobianEvaluations(int)} and
     * {@link AbstractEstimator#getJacobianEvaluations()}, with valid arguments.
     */
    @Test
    public void testSetAndGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }

            @Override
            protected void updateJacobian() {
                // no-op
            }
        };
        estimator.setJacobianEvaluations(10);
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
        estimator.setJacobianEvaluations(5);
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#setJacobianEvaluations(int)} and
     * {@link AbstractEstimator#getJacobianEvaluations()}, with an invalid
     * argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetJacobianEvaluationsNegative() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }

            @Override
            protected void updateJacobian() {
                // no-op
            }
        };
        estimator.setJacobianEvaluations(-1);
    }

    /**
     * Test for {@link AbstractEstimator#setJacobianEvaluations(int)} and
     * {@link AbstractEstimator#getJacobianEvaluations()}, with a zero argument.
     */
    @Test
    public void testSetAndGetJacobianEvaluationsZero() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }

            @Override
            protected void updateJacobian() {
                // no-op
            }
        };
        estimator.setJacobianEvaluations(0);
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.setJacobianEvaluations(5);
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    /**
     * Test for {@link AbstractEstimator#getRMS(EstimationProblem)} with a valid
     * estimation problem.
     */
    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2),
                    new WeightedMeasurement(3, 1),
                    new WeightedMeasurement(4, 3),
                    new WeightedMeasurement(5, 5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 0, 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        assertEquals(2.302555111512323, estimator.getRMS(problem), 0.000001);
    }

    /**
     * Test for {@link AbstractEstimator#getChiSquare(EstimationProblem)} with a
     * valid estimation problem.
     */
    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2),
                    new WeightedMeasurement(3, 1),
                    new WeightedMeasurement(4, 3),
                    new WeightedMeasurement(5, 5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 0, 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        assertEquals(13.177639602361113, estimator.getChiSquare(problem), 0.000001);
    }

    /**
     * Test for {@link AbstractEstimator#getCovariances(EstimationProblem)} with a
     * valid estimation problem.
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2),
                    new WeightedMeasurement(3, 1),
                    new WeightedMeasurement(4, 3),
                    new WeightedMeasurement(5, 5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 0, 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        RealMatrix covariances = estimator.getCovariances(problem);
        assertEquals(3, covariances.getRowDimension());
        assertEquals(3, covariances.getColumnDimension());
        assertEquals(1.1449634161886341, covariances.getEntry(0, 0), 0.000001);
        assertEquals(0.2836824585622121, covariances.getEntry(0, 1), 0.000001);
        assertEquals(0.4255103314562252, covariances.getEntry(0, 2), 0.000001);
        assertEquals(0.2836824585622121, covariances.getEntry(1, 0), 0.000001);
        assertEquals(0.5673649171244243, covariances.getEntry(1, 1), 0.000001);
        assertEquals(0.4255103314562252, covariances.getEntry(1, 2), 0.000001);
        assertEquals(0.4255103314562252, covariances.getEntry(2, 0), 0.000001);
        assertEquals(0.5673649171244243, covariances.getEntry(2, 1), 0.000001);
        assertEquals(1.1449634161886341, covariances.getEntry(2, 2), 0.000001);
    }

    /**
     * Test for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}
     * with a valid estimation problem.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2),
                    new WeightedMeasurement(3, 1),
                    new WeightedMeasurement(4, 3),
                    new WeightedMeasurement(5, 5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 0, 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // no-op
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(3, errors.length);
        assertEquals(1.5152708237355435, errors[0], 0.000001);
        assertEquals(1.132356448975721, errors[1], 0.000001);
        assertEquals(1.5152708237355435, errors[2], 0.000001);
    }

    /**
     * Test for {@link AbstractEstimator#estimate(EstimationProblem)} with a valid
     * estimation problem.
     */
    @Test
    public void testEstimate() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2),
                    new WeightedMeasurement(3, 1),
                    new WeightedMeasurement(4, 3),
                    new WeightedMeasurement(5, 5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 0, 1, 2 };
            }
        };
        AbstractEstimator estimator = new MyEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] parameters = problem.getAllParameters();
        assertEquals(0.5, parameters[0], 0.000001);
        assertEquals(1.5, parameters[1], 0.000001);
        assertEquals(2.5, parameters[2], 0.000001);
    }
}

class MyEstimator extends AbstractEstimator {
    @Override
    protected void updateJacobian() {
        // no-op
    }

    @Override
    protected void updateResidualsAndCost() throws EstimationException {
        // no-op
    }
}