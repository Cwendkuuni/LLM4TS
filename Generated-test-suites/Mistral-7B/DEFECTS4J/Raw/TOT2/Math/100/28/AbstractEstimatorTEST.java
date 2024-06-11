package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

public class AbstractEstimatorTest {
    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new DummyEstimator();
        problem = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public int getParametersNumber() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2, 4.0, 1.0),
                    new WeightedMeasurement(2, 3, 5.0, 2.0),
                    new WeightedMeasurement(3, 4, 6.0, 3.0)
                };
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }

            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(1);
        try {
            estimator.estimate(problem);
            fail("Expected EstimationException");
        } catch (EstimationException e) {
            // expected
        }
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.estimate(problem);
        assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.estimate(problem);
        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.estimate(problem);
        double[] expectedJacobian = {-2.0, -2.0, -2.0, -1.0, -1.0, -1.0};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator.estimate(problem);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(problem.getMeasurements()[0].getX(), problem.getMeasurements()[0].getY());
        regression.addData(problem.getMeasurements()[1].getX(), problem.getMeasurements()[1].getY());
        regression.addData(problem.getMeasurements()[2].getX(), problem.getMeasurements()[2].getY());
        double expectedCost = Math.sqrt(regression.calculateSSR());
        assertEquals(expectedCost, estimator.cost, 0.0);
    }

    @Test
    public void testGetRMS() {
        estimator.estimate(problem);
        EstimationProblem problemWithNoise = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 3;
            }

            @Override
            public int getParametersNumber() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2, 4.0 + 0.1, 1.0),
                    new WeightedMeasurement(2, 3, 5.0 + 0.2, 2.0),
                    new WeightedMeasurement(3, 4, 6.0 + 0.3, 3.0)
                };
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }

            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }
        };
        double rms = estimator.getRMS(problemWithNoise);
        double expectedRMS = Math.sqrt(regression.calculateSSR()) / Math.sqrt(problem.getMeasurements().length);
        expectedRMS += 0.1 * Math.sqrt(2) / Math.sqrt(problem.getMeasurements().length);
        assertEquals(expectedRMS, rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(problem.getMeasurements()[0].getX(), problem.getMeasurements()[0].getY());
        regression.addData(problem.getMeasurements()[1].getX(), problem.getMeasurements()[1].getY());
        regression.addData(problem.getMeasurements()[2].getX(), problem.getMeasurements()[2].getY());
        double expectedChiSquare = regression.calculateSSD() / Math.sqrt(problem.getMeasurements().length);
        assertEquals(expectedChiSquare, chiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.estimate(problem);
        double[][] expectedCovariances = {
            { 0.00895370395865027, 0.00895370395865027 },
            { 0.00895370395865027, 0.00895370395865027 }
        };
        double[][] covariances = estimator.getCovariances(problem);
        assertArrayEquals(expectedCovariances, covariances, 0.0);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.estimate(problem);
        double[] expectedErrors = { 0.141421375, 0.141421375 };
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, errors, 0.0);
    }

    @Test
    public void testInitializeEstimate() {
        estimator.estimate(problem);
        estimator.initializeEstimate(problem);
        assertNull(estimator.measurements);
        assertNull(estimator.parameters);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithSingularMatrix() throws EstimationException {
        EstimationProblem problemWithSingularMatrix = new EstimationProblem() {
            @Override
            public int getMeasurementsNumber() {
                return 2;
            }

            @Override
            public int getParametersNumber() {
                return 2;
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1, 2, 1.0, 1.0),
                    new WeightedMeasurement(2, 2, 1.0, 1.0)
                };
            }

            @Override
            public RealVector getAllParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }

            @Override
            public RealVector getUnboundParameters() {
                return new ArrayRealVector(new double[] { 1.0, 1.0 });
            }
        };
        estimator.initializeEstimate(problemWithSingularMatrix);
        estimator.estimate(problemWithSingularMatrix);
    }

    private static class DummyEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            initializeEstimate(problem);
            // empty implementation
        }
    }
}