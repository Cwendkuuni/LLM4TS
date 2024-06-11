import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new TestEstimator();
        problem = new TestEstimationProblem();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        assertEquals(0, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        // verify the jacobian matrix is correctly updated
        // and contains the expected values
        double[] expectedJacobian = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0};
        assertArrayEquals(expectedJacobian, estimator.getJacobian());
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.updateResidualsAndCost();
        // verify the residuals array and cost value are correctly updated
        // and contain the expected values
        double[] expectedResiduals = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedResiduals, estimator.getResiduals(), 0.001);
        assertEquals(14.0, estimator.getCost(), 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostMaxCostEvalExceeded() throws EstimationException {
        estimator.setMaxCostEval(2);
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double rms = estimator.getRMS(new TestEstimationProblem());
        assertEquals(2.0, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double chiSquare = estimator.getChiSquare(new TestEstimationProblem());
        assertEquals(17.5, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        double[][] covariances = estimator.getCovariances(new TestEstimationProblem());
        // verify the covariance matrix is correctly computed
        // and contains the expected values
        double[][] expectedCovariances = {
            {0.28333, -0.28333},
            {-0.28333, 0.28333}
        };
        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesSingularProblem() throws EstimationException {
        estimator.getCovariances(new TestEstimationProblemSingular());
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[] errors = estimator.guessParametersErrors(new TestEstimationProblem());
        // verify the errors array is correctly computed
        // and contains the expected values
        double[] expectedErrors = {0.1414214, 0.1414214};
        assertArrayEquals(expectedErrors, errors, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsNoDegreesOfFreedom() throws EstimationException {
        estimator.guessParametersErrors(new TestEstimationProblemNoDegreesOfFreedom());
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.estimate(new TestEstimationProblem());
        // verify the parameters are correctly estimated
        double[] expectedParameters = {1.0, 2.0};
        assertArrayEquals(expectedParameters, problem.getAllParameters(), 0.001);
    }
}


class TestEstimator extends AbstractEstimator {

    public int getMaxCostEval() {
        return maxCostEval;
    }

    public double[] getJacobian() {
        return jacobian;
    }

    public double[] getResiduals() {
        return residuals;
    }

    public double getCost() {
        return cost;
    }

    public void estimate(EstimationProblem problem) throws EstimationException {
        // dummy implementation for testing
        parameters[0].setValue(1.0);
        parameters[1].setValue(2.0);
    }
}


class TestEstimationProblem implements EstimationProblem {

    public WeightedMeasurement[] getMeasurements() {
        WeightedMeasurement[] measurements = {
            new WeightedMeasurement(1.0, 1.0),
            new WeightedMeasurement(2.0, 2.0),
            new WeightedMeasurement(3.0, 3.0)
        };
        return measurements;
    }

    public EstimatedParameter[] getAllParameters() {
        EstimatedParameter[] parameters = {
            new EstimatedParameter(1.0),
            new EstimatedParameter(2.0)
        };
        return parameters;
    }
}


class TestEstimationProblemSingular implements EstimationProblem {

    public WeightedMeasurement[] getMeasurements() {
        WeightedMeasurement[] measurements = {
            new WeightedMeasurement(1.0, 1.0),
            new WeightedMeasurement(1.0, 1.0)
        };
        return measurements;
    }

    public EstimatedParameter[] getAllParameters() {
        EstimatedParameter[] parameters = {
            new EstimatedParameter(1.0),
            new EstimatedParameter(1.0)
        };
        return parameters;
    }
}


class TestEstimationProblemNoDegreesOfFreedom implements EstimationProblem {

    public WeightedMeasurement[] getMeasurements() {
        WeightedMeasurement[] measurements = {
            new WeightedMeasurement(1.0, 1.0),
            new WeightedMeasurement(2.0, 2.0)
        };
        return measurements;
    }

    public EstimatedParameter[] getAllParameters() {
        EstimatedParameter[] parameters = {
            new EstimatedParameter(1.0),
            new EstimatedParameter(2.0)
        };
        return parameters;
    }
}


class WeightedMeasurement {

    private double residual;
    private double weight;

    public WeightedMeasurement(double residual, double weight) {
        this.residual = residual;
        this.weight = weight;
    }

    public double getResidual() {
        return residual;
    }

    public double getWeight() {
        return weight;
    }
}


class EstimatedParameter {

    private double value;

    public EstimatedParameter(double value) {
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}