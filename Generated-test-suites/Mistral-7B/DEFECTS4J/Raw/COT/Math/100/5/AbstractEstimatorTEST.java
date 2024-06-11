import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionConstants;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        assertNull(estimator.getCostEvaluations());

        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());

        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        assertNull(estimator.getJacobianEvaluations());

        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());

        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.setMaxCostEval(1);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(1.5, 1.5),
                    new WeightedMeasurement(2.0, 0.5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double rms = estimator.getRMS(problem);
        assertEquals(Precision.round(PrecisionUtils.round(1.9585224151119687d, PrecisionConstants.SIGFIGS_4SD), 0.0000001d), rms, 0.000001d);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(1.5, 1.5),
                    new WeightedMeasurement(2.0, 0.5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateResidualsAndCost();
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(Precision.round(PrecisionUtils.round(2.8866734443245507d, PrecisionConstants.SIGFIGS_4SD), 0.000001d), chiSquare, 0.000001d);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(1.5, 1.5),
                    new WeightedMeasurement(2.0, 0.5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(2, covariances[1].length);
        Arrays.assertEquals(new double[][] { { 1.572841322564865d, 0.7864206612824325d }, { 0.7864206612824325d, 1.4136215331840835d } }, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                    new WeightedMeasurement(1.0, 2.0),
                    new WeightedMeasurement(1.5, 1.5),
                    new WeightedMeasurement(2.0, 0.5)
                };
            }

            @Override
            public double[] getAllParameters() {
                return new double[] { 1, 2 };
            }
        };
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // not implemented
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2, errors.length);
        Arrays.assertEquals(new double[] { 0.1374601720332651d, 0.1227278587929582d }, errors);
    }
}