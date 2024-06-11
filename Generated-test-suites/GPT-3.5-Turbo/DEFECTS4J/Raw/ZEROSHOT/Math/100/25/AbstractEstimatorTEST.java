import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.setCostEvaluations(50);
        assertEquals(50, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.setJacobianEvaluations(10);
        assertEquals(10, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.setRows(2);
        estimator.setCols(3);
        estimator.setJacobian(new double[6]);

        estimator.updateJacobian();

        double[] expectedJacobian = new double[]{0, 0, 0, 0, 0, 0};
        assertArrayEquals(expectedJacobian, estimator.getJacobian(), 0);
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.setJacobianEvaluations(5);
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(6, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.setCostEvaluations(6);
        estimator.setRows(2);
        estimator.setMeasurements(new WeightedMeasurement[2]);
        estimator.setResiduals(new double[2]);

        estimator.updateResidualsAndCost();

        assertEquals(7, estimator.getCostEvaluations());
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem();
        problem.setMeasurements(new WeightedMeasurement[]{
                new WeightedMeasurement(1, 1),
                new WeightedMeasurement(2, 2),
                new WeightedMeasurement(3, 3)
        });

        double rms = estimator.getRMS(problem);

        assertEquals(1.0, rms, 0);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem();
        problem.setMeasurements(new WeightedMeasurement[]{
                new WeightedMeasurement(1, 1),
                new WeightedMeasurement(2, 2),
                new WeightedMeasurement(3, 3)
        });

        double chiSquare = estimator.getChiSquare(problem);

        assertEquals(14.0, chiSquare, 0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem();
        problem.setMeasurements(new WeightedMeasurement[]{
                new WeightedMeasurement(1, 1),
                new WeightedMeasurement(2, 2),
                new WeightedMeasurement(3, 3)
        });

        double[][] covariances = estimator.getCovariances(problem);

        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem();
        problem.setMeasurements(new WeightedMeasurement[]{
                new WeightedMeasurement(1, 1),
                new WeightedMeasurement(2, 2),
                new WeightedMeasurement(3, 3)
        });

        double[] errors = estimator.guessParametersErrors(problem);

        assertEquals(0, errors[0], 0);
        assertEquals(0, errors[1], 0);
    }

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new EstimationProblem();
        problem.setMeasurements(new WeightedMeasurement[5]);
        problem.setUnboundParameters(new EstimatedParameter[3]);

        estimator.initializeEstimate(problem);

        assertEquals(0, estimator.getCostEvaluations());
        assertEquals(0, estimator.getJacobianEvaluations());
        assertEquals(5, estimator.getRows());
        assertEquals(3, estimator.getCols());
        assertNotNull(estimator.getJacobian());
        assertNotNull(estimator.getResiduals());
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0);
    }

}