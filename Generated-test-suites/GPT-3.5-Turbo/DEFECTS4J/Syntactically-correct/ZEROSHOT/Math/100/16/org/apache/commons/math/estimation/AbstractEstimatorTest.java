import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new ConcreteEstimator();
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new ConcreteEstimator();
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.updateJacobian();
        // Test jacobian array is not null
        assertNotNull(estimator.jacobian);
        // Test jacobian array length matches the number of rows and columns
        assertEquals(estimator.rows * estimator.cols, estimator.jacobian.length);
        // Test all elements in jacobian array are initialized to 0
        for (double element : estimator.jacobian) {
            assertEquals(0, element, 0.0);
        }
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.jacobianEvaluations);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        AbstractEstimator estimator = new ConcreteEstimator();
        estimator.updateResidualsAndCost();
        // Test costEvaluations is incremented by 1
        assertEquals(1, estimator.costEvaluations);
        // Test cost is not infinity
        assertNotEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.0);
        // Test residuals array is not null
        assertNotNull(estimator.residuals);
        // Test residuals array length matches the number of rows
        assertEquals(estimator.rows, estimator.residuals.length);
        // Test all elements in residuals array are initialized to 0
        for (double element : estimator.residuals) {
            assertEquals(0, element, 0.0);
        }
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new ConcreteEstimator();
        EstimationProblem problem = new ConcreteEstimationProblem();
        double rms = estimator.getRMS(problem);
        // Test rms is not NaN or infinity
        assertNotEquals(Double.NaN, rms, 0.0);
        assertNotEquals(Double.POSITIVE_INFINITY, rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new ConcreteEstimator();
        EstimationProblem problem = new ConcreteEstimationProblem();
        double chiSquare = estimator.getChiSquare(problem);
        // Test chiSquare is not NaN or infinity
        assertNotEquals(Double.NaN, chiSquare, 0.0);
        assertNotEquals(Double.POSITIVE_INFINITY, chiSquare, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesWhenSingular() throws EstimationException {
        AbstractEstimator estimator = new ConcreteEstimator();
        EstimationProblem problem = new SingularEstimationProblem();
        estimator.getCovariances(problem);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new ConcreteEstimator();
        EstimationProblem problem = new NonSingularEstimationProblem();
        double[][] covariances = estimator.getCovariances(problem);
        // Test covariances array is not null
        assertNotNull(covariances);
        // Test covariances array dimensions match the number of parameters
        assertEquals(problem.getAllParameters().length, covariances.length);
        assertEquals(problem.getAllParameters().length, covariances[0].length);
        // Test all elements in covariances array are not NaN or infinity
        for (double[] row : covariances) {
            for (double element : row) {
                assertNotEquals(Double.NaN, element, 0.0);
                assertNotEquals(Double.POSITIVE_INFINITY, element, 0.0);
            }
        }
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsWhenDegreesOfFreedomUnsatisfied() throws EstimationException {
        AbstractEstimator estimator = new ConcreteEstimator();
        EstimationProblem problem = new InsufficientDegreesOfFreedomEstimationProblem();
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new ConcreteEstimator();
        EstimationProblem problem = new EstimationProblem();
        double[] errors = estimator.guessParametersErrors(problem);
        // Test errors array is not null
        assertNotNull(errors);
        // Test errors array length matches the number of parameters
        assertEquals(problem.getAllParameters().length, errors.length);
        // Test all elements in errors array are not NaN or infinity
        for (double element : errors) {
            assertNotEquals(Double.NaN, element, 0.0);
            assertNotEquals(Double.POSITIVE_INFINITY, element, 0.0);
        }
    }

    private class ConcreteEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // Dummy implementation
        }
    }

    private class ConcreteEstimationProblem implements EstimationProblem {
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
    }

    private class SingularEstimationProblem implements EstimationProblem {
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
    }

    private class NonSingularEstimationProblem implements EstimationProblem {
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
    }

    private class InsufficientDegreesOfFreedomEstimationProblem implements EstimationProblem {
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
    }
}