import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.weighting.WeightedMeasurement;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator est = new AbstractEstimator();
        est.setMaxCostEval(10);
        assertEquals(10, est.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator est = new AbstractEstimator();
        est.costEvaluations = 5;
        assertEquals(5, est.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator est = new AbstractEstimator();
        est.jacobianEvaluations = 3;
        assertEquals(3, est.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator est = new AbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        double[] parameters = new double[2];
        est.initializeEstimate(new DummyEstimationProblem(measurements, parameters));

        // Test empty jacobian
        est.updateJacobian();
        assertArrayEquals(new double[0], est.jacobian, 0.0);

        // Test non empty jacobian
        measurements[0] = new WeightedMeasurement(1.0, 1.0, 1.0);
        measurements[1] = new WeightedMeasurement(2.0, 2.0, 2.0);
        for (int i = 0; i < 4; i++) {
            est.updateJacobian();
            double expected = i < 2 ? -Math.sqrt(measurements[i / 2].getWeight()) : 0;
            for (int j = 0; j < 2; j++) {
                assertEquals(expected, est.jacobian[j * 2 + i % 2], 0.001);
            }
        }
    }

    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator est = new AbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        double[] parameters = new double[2];
        EstimationProblem problem = new DummyEstimationProblem(measurements, parameters);
        est.initializeEstimate(problem);

        est.updateResidualsAndCost();
        assertEquals(Math.sqrt(2.0), est.cost, 0.001);

        for (int i = 0; i < 2; i++) {
            assertEquals(Math.sqrt(measurements[i].getWeight()) * measurements[i].getResidual(),
                         est.residuals[i], 0.001);
        }

        try {
            est.updateResidualsAndCost();
            fail("Expected EstimationException");
        } catch (EstimationException e) {
            // Expected
        }
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator est = new AbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        double[] parameters = new double[2];
        EstimationProblem problem = new DummyEstimationProblem(measurements, parameters);
        est.initializeEstimate(problem);

        est.updateResidualsAndCost();
        assertEquals(Math.sqrt(est.cost / 2.0), est.getRMS(problem), 0.001);

        measurements[0].setResidual(Double.NaN);
        measurements[1].setResidual(Double.NaN);
        est.updateResidualsAndCost();
        assertNaN(est.getRMS(problem));
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator est = new AbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        double[] parameters = new double[2];
        EstimationProblem problem = new DummyEstimationProblem(measurements, parameters);
        est.initializeEstimate(problem);

        est.updateResidualsAndCost();
        assertEquals(est.cost, est.getChiSquare(problem), 0.001);

        for (int i = 0; i < 2; i++) {
            measurements[i].setWeight(Double.NaN);
        }
        est.updateResidualsAndCost();
        assertNaN(est.getChiSquare(problem));
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator est = new AbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        double[] parameters = new double[2];
        EstimationProblem problem = new DummyEstimationProblem(measurements, parameters);
        est.initializeEstimate(problem);

        est.updateJacobian();
        double[][] result = est.getCovariances(problem);

        // The covariance matrix is symmetric, we check only the diagonal
        for (int i = 0; i < 2; i++) {
            assertEquals(result[i][i], result[i][i], 0.001);
        }

        // Test singular problem
        measurements[0].setWeight(0.0);
        est.initializeEstimate(problem);
        try {
            est.getCovariances(problem);
            fail("Expected EstimationException");
        } catch (EstimationException e) {
            // Expected
        }
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator est = new AbstractEstimator();
        WeightedMeasurement[] measurements = new WeightedMeasurement[3];
        double[] parameters = new double[2];
        EstimationProblem problem = new DummyEstimationProblem(measurements, parameters);
        est.initializeEstimate(problem);

        double[] expected = new double[2];
        expected[0] = Math.sqrt(est.getChiSquare(problem) / (3 - 2));
        expected[1] = Math.sqrt(est.getChiSquare(problem) / (3 - 2));

        est.updateJacobian();
        double[] errors = est.guessParametersErrors(problem);
        for (int i = 0; i < 2; i++) {
            assertEquals(expected[i], errors[i], 0.001);
        }

        problem = new DummyEstimationProblem(measurements, parameters, 2);
        errors = est.guessParametersErrors(problem);
        for (int i = 0; i < 2; i++) {
            assertEquals(0, errors[i], 0.001);
        }
    }

    private static void assertArrayEquals(double[] expected, double[] actual, double tolerance) {
        for (int i = 0; i < expected.length; i++) {
            double difference = Math.abs(expected[i] - actual[i]);
            if (difference > tolerance) {
                fail("Expected: " + Arrays.toString(expected) + ", but was: " +
                     Arrays.toString(actual));
            }
        }
    }

    private static void assertNaN(double value) {
        assertEquals(Double.NaN, value, 0.0);
    }

    private static class DummyEstimationProblem implements EstimationProblem {

        private double[] weights;
        private WeightedMeasurement[] measurements;
        private double[] parameters;
        private int numParams;

        public DummyEstimationProblem(WeightedMeasurement[] measurements, double[] parameters) {
            this(measurements, parameters, measurements.length);
        }

        public DummyEstimationProblem(WeightedMeasurement[] measurements, double[] parameters, int numParams) {
            this.measurements = measurements;
            this.parameters = parameters;
            this.numParams = numParams;
            weights = new double[numParams];
            for (int i = 0; i < numParams; i++) {
                weights[i] = 1.0;
            }
        }

        @Override
        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        @Override
        public double[] getUnboundParameters() {
            return parameters;
        }

        @Override
        public double[] getAllParameters() {
            return parameters;
        }

        @Override
        public double[] getWeights() {
            return weights;
        }

        @Override
        public int getNumParameters() {
            return numParams;
        }

    }

}