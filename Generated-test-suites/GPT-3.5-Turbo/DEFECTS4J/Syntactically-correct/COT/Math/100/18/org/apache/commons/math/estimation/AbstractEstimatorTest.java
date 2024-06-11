
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.costEvaluations = 50;
        assertEquals(50, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.jacobianEvaluations = 25;
        assertEquals(25, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.measurements = new WeightedMeasurement[2];
        estimator.measurements[0] = new WeightedMeasurement();
        estimator.measurements[0].setWeight(1.0);
        estimator.measurements[0].setPartial(0.0);
        estimator.measurements[1] = new WeightedMeasurement();
        estimator.measurements[1].setWeight(2.0);
        estimator.measurements[1].setPartial(1.0);

        estimator.parameters = new EstimatedParameter[2];
        estimator.parameters[0] = new EstimatedParameter();
        estimator.parameters[1] = new EstimatedParameter();

        estimator.rows = 2;
        estimator.cols = 2;

        estimator.updateJacobian();

        double[] expectedJacobian = {0.0, 0.0, -1.0, -1.0};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCost_MaxCostEvalExceeded()
            throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.maxCostEval = 100;
        estimator.costEvaluations = 100;

        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblemImpl();

        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement();
        measurements[1] = new WeightedMeasurement();

        measurements[0].setResidual(1.0);
        measurements[0].setWeight(1.0);
        measurements[1].setResidual(2.0);
        measurements[1].setWeight(2.0);

        problem.setMeasurements(measurements);

        double expectedRMS = Math.sqrt((1.0 * 1.0 + 2.0 * 2.0) / 2.0);
        assertEquals(expectedRMS, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblemImpl();

        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement();
        measurements[1] = new WeightedMeasurement();

        measurements[0].setResidual(1.0);
        measurements[0].setWeight(1.0);
        measurements[1].setResidual(2.0);
        measurements[1].setWeight(2.0);

        problem.setMeasurements(measurements);

        double expectedChiSquare = (1.0 * 1.0 + 2.0 * 2.0) / (1.0 + 2.0);
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariances_SingularProblem()
            throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblemImpl();

        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement();
        measurements[1] = new WeightedMeasurement();

        measurements[0].setResidual(1.0);
        measurements[0].setWeight(1.0);
        measurements[1].setResidual(2.0);
        measurements[1].setWeight(2.0);

        problem.setMeasurements(measurements);

        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors()
            throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblemImpl();

        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement();
        measurements[1] = new WeightedMeasurement();
        measurements[2] = new WeightedMeasurement();

        measurements[0].setResidual(1.0);
        measurements[0].setWeight(1.0);
        measurements[1].setResidual(2.0);
        measurements[1].setWeight(2.0);
        measurements[2].setResidual(3.0);
        measurements[2].setWeight(3.0);

        problem.setMeasurements(measurements);

        double[] expectedErrors = {
                Math.sqrt(1.0 / 1.0) * Math.sqrt(estimator.getChiSquare(problem) / (3 - 2)),
                Math.sqrt(1.0 / 4.0) * Math.sqrt(estimator.getChiSquare(problem) / (3 - 2)),
                Math.sqrt(1.0 / 9.0) * Math.sqrt(estimator.getChiSquare(problem) / (3 - 2))
        };
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrors_NoDegreesOfFreedom()
            throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblemImpl();

        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement();
        measurements[1] = new WeightedMeasurement();

        measurements[0].setResidual(1.0);
        measurements[0].setWeight(1.0);
        measurements[1].setResidual(2.0);
        measurements[1].setWeight(2.0);

        problem.setMeasurements(measurements);

        estimator.guessParametersErrors(problem);
    }

    private class AbstractEstimatorImpl extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // Do nothing
        }
    }

    private class EstimationProblemImpl implements EstimationProblem {
        private WeightedMeasurement[] measurements;

        public void setMeasurements(WeightedMeasurement[] measurements) {
            this.measurements = measurements;
        }

        @Override
        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        @Override
        public EstimatedParameter[] getAllParameters() {
            return null;
        }
    }
}
