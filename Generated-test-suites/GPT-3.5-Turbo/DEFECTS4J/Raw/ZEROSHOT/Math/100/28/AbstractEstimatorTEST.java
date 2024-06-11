import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

    // Test getMaxCostEval and setMaxCostEval methods
    @Test
    public void testMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    // Test getCostEvaluations method
    @Test
    public void testCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.maxCostEval = 100;
        estimator.costEvaluations = 50;
        assertEquals(50, estimator.getCostEvaluations());
    }

    // Test getJacobianEvaluations method
    @Test
    public void testJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.jacobianEvaluations = 20;
        assertEquals(20, estimator.getJacobianEvaluations());
    }

    // Test updateJacobian method
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.rows = 2;
        estimator.cols = 3;
        estimator.measurements = new WeightedMeasurement[2];
        estimator.parameters = new EstimatedParameter[3];
        estimator.jacobian = new double[6];
        estimator.updateJacobian();
        // Assert that all elements of jacobian are 0
        for (double j : estimator.jacobian) {
            assertEquals(0, j, 0);
        }
    }

    // Test incrementJacobianEvaluationsCounter method
    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.jacobianEvaluations);
    }

    // Test updateResidualsAndCost method
    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        estimator.maxCostEval = 100;
        estimator.costEvaluations = 100;
        estimator.updateResidualsAndCost();
    }

    // Test getRMS method
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1, 1);
        measurements[1] = new WeightedMeasurement(2, 4);
        measurements[2] = new WeightedMeasurement(3, 9);
        problem.setMeasurements(measurements);
        double rms = estimator.getRMS(problem);
        assertEquals(2.160246899, rms, 0.000001);
    }

    // Test getChiSquare method
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(2, 4);
        measurements[1] = new WeightedMeasurement(3, 9);
        problem.setMeasurements(measurements);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(8.333333333, chiSquare, 0.000001);
    }

    // Test getCovariances method
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(2, 4);
        measurements[1] = new WeightedMeasurement(3, 9);
        problem.setMeasurements(measurements);
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(0.75, covariances[0][0], 0.000001);
        assertEquals(-0.5, covariances[0][1], 0.000001);
        assertEquals(-0.5, covariances[1][0], 0.000001);
        assertEquals(0.333333333, covariances[1][1], 0.000001);
    }

    // Test guessParametersErrors method
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        measurements[0] = new WeightedMeasurement(1, 1);
        measurements[1] = new WeightedMeasurement(2, 4);
        measurements[2] = new WeightedMeasurement(3, 9);
        problem.setMeasurements(measurements);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(0.894427191, errors[0], 0.000001);
        assertEquals(0.447213595, errors[1], 0.000001);
        assertEquals(0.447213595, errors[2], 0.000001);
    }

    // Test initializeEstimate method
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(2, 4);
        measurements[1] = new WeightedMeasurement(3, 9);
        problem.setMeasurements(measurements);
        estimator.initializeEstimate(problem);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertSame(measurements, estimator.measurements);
        assertEquals(2, estimator.rows);
        assertEquals(0, estimator.cols);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0);
    }

    // Test estimate method
    @Test(expected = EstimationException.class)
    public void testEstimate() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimatorImpl();
        EstimationProblem problem = new EstimationProblem();
        estimator.estimate(problem);
    }

    private static class AbstractEstimatorImpl extends AbstractEstimator {
        public void estimate(EstimationProblem problem) throws EstimationException { }
        public void setMaxCostEval(int maxCostEval) { }
        public int getMaxCostEval() { return 0; }
    }

}