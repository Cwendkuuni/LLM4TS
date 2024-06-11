import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluationsAndJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            protected void updateJacobian() {
                super.updateJacobian();
                Arrays.fill(jacobian, 1.0);
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[] {0.0};
            }
        });
        estimator.updateJacobian();

        double[] expectedJacobian = new double[] {1.0, 1.0};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalExceeded() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                super.updateResidualsAndCost();
                throw new EstimationException("Maximum cost evaluations exceeded");
            }
        };

        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[1];
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        });
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double getRMS(EstimationProblem problem) {
                return 1.0;
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        });
        assertEquals(1.0, estimator.getRMS(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        }));
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double getChiSquare(EstimationProblem problem) {
                return 1.0;
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        });
        assertEquals(1.0, estimator.getChiSquare(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        }));
    }

    @Test
    public void testGetCovariances() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double[][] getCovariances(EstimationProblem problem)
                    throws EstimationException {
                return new double[][] {
                        {1.0, 0.0},
                        {0.0, 1.0}
                };
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[2];
            }
        });

        double[][] expectedCovariances = new double[][] {
                {1.0, 0.0},
                {0.0, 1.0}
        };
        assertArrayEquals(expectedCovariances, estimator.getCovariances(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[2];
            }
        }).getData(), 0.0);
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }

            @Override
            public double[] guessParametersErrors(EstimationProblem problem)
                    throws EstimationException {
                return new double[] {1.0, 1.0};
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[2];
            }
        });

        double[] expectedErrors = new double[] {1.0, 1.0};
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[2];
            }
        }));
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[0];
            }
        });

        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(1, estimator.getJacobianEvaluations());
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithNegativeNumberOfDegreesOfFreedom() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };

        WeightedMeasurement[] measurements = new WeightedMeasurement[1];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, new double[] {1.0, 1.0});
        estimator.initializeEstimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[3];
            }
        });

        estimator.estimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public double[] getAllParameters() {
                return new double[3];
            }
        });
    }
}