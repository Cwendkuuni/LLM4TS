import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.estimation.WeightedMeasurement;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(-1);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                costEvaluations++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(1);
        estimator.estimate(createEstimationProblem());
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int jacobianEvaluations = 0;

            @Override
            protected void incrementJacobianEvaluationsCounter() {
                jacobianEvaluations++;
            }

            @Override
            protected void updateJacobian() {
                incrementJacobianEvaluationsCounter();
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
            }
        };
        estimator.estimate(createEstimationProblem());
        estimator.estimate(createEstimationProblem());
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testExceededMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int costEvaluations = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                costEvaluations++;
                if (costEvaluations > 5) {
                    throw new EstimationException("maximal number of evaluations exceeded (5)");
                }
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
            }
        };
        estimator.setMaxCostEval(5);
        estimator.estimate(createEstimationProblem());
        estimator.estimate(createEstimationProblem());
        estimator.estimate(createEstimationProblem());
        estimator.estimate(createEstimationProblem());
        estimator.estimate(createEstimationProblem());
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        };
        measurements[0] = new WeightedMeasurement(1, 1, 1);
        measurements[1] = new WeightedMeasurement(2, 2, 2);
        measurements[2] = new WeightedMeasurement(3, 3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getRMS(EstimationProblem problem) {
                return super.getRMS(problem);
            }
        };
        assertEquals(Math.sqrt(2.0/3.0), estimator.getRMS(problem), 0.0001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        };
        measurements[0] = new WeightedMeasurement(1, 1, 1);
        measurements[1] = new WeightedMeasurement(2, 2, 4);
        measurements[2] = new WeightedMeasurement(3, 3, 9);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double getChiSquare(EstimationProblem problem) {
                return super.getChiSquare(problem);
            }
        };
        assertEquals(14.0/3.0, estimator.getChiSquare(problem), 0.0001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesThrowsException() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        };
        measurements[0] = new WeightedMeasurement(1, 1, 1);
        measurements[1] = new WeightedMeasurement(2, 2, 4);
        measurements[2] = new WeightedMeasurement(3, 3, 9);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("Unable to compute covariances: singular problem");
            }
        };
        assertThrows(EstimationException.class, () -> estimator.getCovariances(problem));
    }

    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[3];
            }
        };
        measurements[0] = new WeightedMeasurement(1, 1, 1);
        measurements[1] = new WeightedMeasurement(2, 2, 4);
        measurements[2] = new WeightedMeasurement(3, 3, 9);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
                return super.guessParametersErrors(problem);
            }
        };
        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = new double[]{Math.sqrt(1.0/3.0), Math.sqrt(1.0/3.0), Math.sqrt(1.0/3.0)};
        assertArrayEquals(expectedErrors, errors, 0.0001);
    }

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[3];
            }
        };
        measurements[0] = new WeightedMeasurement(1, 1, 1);
        measurements[1] = new WeightedMeasurement(2, 2, 4);
        measurements[2] = new WeightedMeasurement(3, 3, 9);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            protected void initializeEstimate(EstimationProblem problem) {
                super.initializeEstimate(problem);
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.initializeEstimate(problem);
        assertEquals(3, estimator.getCostEvaluations());
        assertEquals(3, estimator.getJacobianEvaluations());
        assertNotNull(estimator.measurements);
        assertNotNull(estimator.parameters);
        assertNotNull(estimator.jacobian);
        assertNotNull(estimator.residuals);
        assertEquals(3, estimator.rows);
        assertEquals(3, estimator.cols);
        assertEquals(9, estimator.jacobian.length);
        assertEquals(0, estimator.cost, 0.0001);
    }

    @Test
    public void testEstimateThrowsException() {
        EstimationProblem problem = new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[3];
            }
        };
        measurements[0] = new WeightedMeasurement(1, 1, 1);
        measurements[1] = new WeightedMeasurement(2, 2, 4);
        measurements[2] = new WeightedMeasurement(3, 3, 9);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                throw new EstimationException("estimation failed");
            }
        };
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createEstimationProblem() {
        return new EstimationProblem() {
            WeightedMeasurement[] measurements = new WeightedMeasurement[3];

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        };
    }

    private static void assertArrayEquals(double[] a, double[] b, double e) {
        for (int i = 0; i < a.length; i++) {
            assertEquals(a[i], b[i], e);
        }
    }

    
}