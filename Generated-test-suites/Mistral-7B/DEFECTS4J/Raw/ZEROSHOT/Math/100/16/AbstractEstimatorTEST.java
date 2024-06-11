import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class AbstractEstimatorTest {

    @Test
    public void testGettersAndSetters() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        assertEquals(0, estimator.getCostEvaluations());
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.setRows(3);
        estimator.setCols(2);
        estimator.setMaxCostEval(10);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0, 1.0, 1.0);
        }
        estimator.setMeasurements(measurements);
        estimator.setParameters(new EstimatedParameter[3]);

        estimator.updateJacobian();
        double[] expectedJacobian = new double[]{
                0.0, -1.0, 
                0.0, -1.0, 
                0.0, -1.0
        };
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        estimator.setRows(3);
        estimator.setCols(2);
        estimator.setMaxCostEval(10);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0, 1.0, 1.0);
        }
        estimator.setMeasurements(measurements);
        estimator.setParameters(new EstimatedParameter[3]);

        estimator.updateResidualsAndCost();
        double[] expectedResiduals = new double[]{1.0, 1.0, 1.0};
        assertArrayEquals(expectedResiduals, estimator.residuals, 0.0);
        assertEquals(1, estimator.getCostEvaluations());
        assertEquals(Math.sqrt(3.0), estimator.cost, 0.0);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public double[] getWeights() {
                return new double[0];
            }
        };
        estimator.setRows(3);
        estimator.setCols(2);
        estimator.setMaxCostEval(10);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0, 1.0, 1.0);
        }
        estimator.setMeasurements(measurements);
        estimator.setParameters(new EstimatedParameter[3]);

        estimator.initializeEstimate(problem);

        double[] expectedRMS = new double[3];
        for (int i = 0; i < expectedRMS.length; i++) {
            expectedRMS[i] = Math.sqrt(Math.abs(measurements[i].getResidual()) / problem.getWeight(i));
        }
        assertArrayEquals(expectedRMS, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }

            @Override
            public double[] getWeights() {
                return new double[0];
            }
        };
        estimator.setRows(3);
        estimator.setCols(2);
        estimator.setMaxCostEval(10);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0, 1.0, 1.0);
        }
        estimator.setMeasurements(measurements);
        estimator.setParameters(new EstimatedParameter[3]);

        estimator.initializeEstimate(problem);

        double[] expectedChiSquare = new double[1];
        expectedChiSquare[0] = 3.0;
        assertEquals(expectedChiSquare[0], estimator.getChiSquare(problem), 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public double[] getWeights() {
                return new double[0];
            }
        };
        estimator.setRows(3);
        estimator.setCols(2);
        estimator.setMaxCostEval(10);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0, new double[]{1.0, 1.0}, 1.0);
        }
        estimator.setMeasurements(measurements);
        estimator.setParameters(new EstimatedParameter[3]);

        estimator.initializeEstimate(problem);

        double[][] expectedCovariances = new double[][]{
                {1.0 / 3.0, 0},
                {0, 1.0 / 3.0}
        };
        Array2DRowRealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedCovariances);
        ArrayRealVector actualVector = new ArrayRealVector(estimator.getCovariances(problem));
        RealMatrixImpl actualMatrix = new RealMatrixImpl(actualVector.toArray());
        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty implementation
            }
        };
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[2];
            }

            @Override
            public double[] getWeights() {
                return new double[0];
            }
        };
        estimator.setRows(3);
        estimator.setCols(2);
        estimator.setMaxCostEval(10);

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(i, 1.0, new double[]{1.0, 1.0}, 1.0);
        }
        estimator.setMeasurements(measurements);
        estimator.setParameters(new EstimatedParameter[3]);

        estimator.initializeEstimate(problem);

        double[] expectedErrors = new double[]{1.0 / Math.sqrt(3.0), 1.0 / Math.sqrt(3.0)};
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0.0);
    }

}