import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.estimation.WeightedMeasurement;
import static org.apache.commons.math3.estimation.AbstractEstimator.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(5);
        assertEquals(5, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.updateJacobian();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(5, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 2.0);
        measurements[1] = new WeightedMeasurement(2.0, 1.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem(null, measurements));
        estimator.updateJacobian();
        double[] expectedJacobian = new double[]{-2.0, -2.0};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCost_maxCostEvalReached() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        estimator.setMaxCostEval(1);
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 2.0);
        measurements[1] = new WeightedMeasurement(2.0, 1.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem(null, measurements));
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 2.0);
        measurements[1] = new WeightedMeasurement(2.0, 1.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem(null, measurements));
        estimator.updateResidualsAndCost();
        assertEquals(2.23607, estimator.getRMS(new EstimationProblem(null, measurements)), 0.01);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 2.0);
        measurements[1] = new WeightedMeasurement(2.0, 1.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem(null, measurements));
        estimator.updateResidualsAndCost();
        assertEquals(5.5, estimator.getChiSquare(new EstimationProblem(null, measurements)), 0.01);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 2.0);
        measurements[1] = new WeightedMeasurement(2.0, 1.0, 3.0);
        estimator.initializeEstimate(new EstimationProblem(null, measurements));
        estimator.updateJacobian();
        double[][] expectedCovariances = new double[][]{{0.0711, 0.0289}, {0.0289, 0.1111}};
        Array2DRowRealMatrix actualCovariances = (Array2DRowRealMatrix) estimator.getCovariances(new EstimationProblem(null, measurements));
        assertArrayEquals(expectedCovariances.getData(), actualCovariances.getData(), 0.01);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
            }
        };
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 2.0);
        measurements[1] = new WeightedMeasurement(2.0, 1.0, 3.0);
        measurements[2] = new WeightedMeasurement(3.0, 1.0, 4.0);
        estimator.initializeEstimate(new EstimationProblem(null, measurements));
        estimator.updateJacobian();
        estimator.getCovariances(new EstimationProblem(null, measurements));
        double[] expectedErrors = new double[]{0.4216, 0.4216, 0.4216};
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(new EstimationProblem(null, measurements)), 0.01);
    }

}