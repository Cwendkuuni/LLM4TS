import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.stat.statistics.WeightedMean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = mock(AbstractEstimator.class);
        problem = mock(EstimationProblem.class);
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        Assert.assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.costEvaluations = 5;
        Assert.assertEquals(5, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 3;
        Assert.assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() throws EstimationException {
        int rows = 2, cols = 3;
        double[][] jacobian = new double[rows * cols][];
        estimator.jacobian = jacobian;
        estimator.rows = rows;
        estimator.cols = cols;
        estimator.measurements = new WeightedMeasurement[rows];
        estimator.parameters = new EstimatedParameter[cols];

        for (int i = 0; i < rows; i++) {
            WeightedMeasurement measurement = mock(WeightedMeasurement.class);
            estimator.measurements[i] = measurement;
            when(measurement.getPartial(anyDouble())).thenReturn(1.0);
        }

        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        verify(estimator.measurements).forEach(m -> m.getPartial(anyDouble()));
        verify(estimator, times(rows * cols)).getPartial(anyDouble());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostThrowsEstimationException() throws EstimationException {
        estimator.costEvaluations = AbstractEstimator.MAX_INTEGER;
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        WeightedMean weightedMean = new WeightedMean();
        double[] weights = {1.0, 2.0, 3.0};
        double[] values = {1.0, 2.0, 3.0};
        estimator.getRMS(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return weights[0];
                        }

                        @Override
                        public double getResidual() {
                            return values[0];
                        }
                    },
                    new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return weights[1];
                        }

                        @Override
                        public double getResidual() {
                            return values[1];
                        }
                    },
                    new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return weights[2];
                        }

                        @Override
                        public double getResidual() {
                            return values[2];
                        }
                    }
                };
            }
        });
        double expectedRMS = Math.sqrt(weightedMean.getValue(values) / weights.length);
        Assert.assertEquals(expectedRMS, estimator.getRMS(problem), 0.001);
    }

    @Test
    public void testGetChiSquare() {
        WeightedMean weightedMean = new WeightedMean();
        double[] weights = {1.0, 2.0, 3.0};
        double[] values = {1.0, 2.0, 3.0};
        estimator.getChiSquare(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return weights[0];
                        }

                        @Override
                        public double getResidual() {
                            return values[0];
                        }
                    },
                    new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return weights[1];
                        }

                        @Override
                        public double getResidual() {
                            return values[1];
                        }
                    },
                    new WeightedMeasurement() {
                        @Override
                        public double getWeight() {
                            return weights[2];
                        }

                        @Override
                        public double getResidual() {
                            return values[2];
                        }
                    }
                };
            }
        });
        double expectedChiSquare = 0.0;
        for (int i = 0; i < values.length; i++) {
            expectedChiSquare += Math.pow(values[i], 2) / weights[i];
        }
        Assert.assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = mock(EstimationProblem.class);
        when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[3]);
        when(problem.getAllParameters()).thenReturn(new EstimatedParameter[3]);

        estimator.getCovariances(problem);

        verify(problem).getMeasurements();
        verify(problem).getAllParameters();
        verify(estimator).updateJacobian();
        verify(estimator).getCovariances(any(RealMatrixImpl.class));
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = mock(EstimationProblem.class);
        when(problem.getMeasurements()).thenReturn(new WeightedMeasurement[3]);
        when(problem.getAllParameters()).thenReturn(new EstimatedParameter[3]);
        when(estimator.getCovariances(problem)).thenReturn(new double[][] {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        });

        double[] errors = estimator.guessParametersErrors(problem);

        Assert.assertArrayEquals(new double[]{0.5, 0.5, 0.5}, errors, 0.001);
    }
}