import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.EstimatedParameter;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.RealMatrixImpl;

public class AbstractEstimatorTest {

    private EstimationProblem problem;
    private AbstractEstimator estimator;

    @Before
    public void setUp() {
        problem = new TestEstimationProblem();
        estimator = new TestAbstractEstimator();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        Assert.assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.setCostEvaluations(50);
        Assert.assertEquals(50, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.setJacobianEvaluations(75);
        Assert.assertEquals(75, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        double[] expectedJacobian = new double[testMeasurements.length * testParameters.length];
        Assert.assertArrayEquals(expectedJacobian, estimator.getJacobian(), 0);
    }

    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.incrementJacobianEvaluationsCounter();
        Assert.assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateResidualsAndCost() {
        try {
            estimator.updateResidualsAndCost();
        } catch (EstimationException e) {
            Assert.fail("Should not throw EstimationException");
        }
        double[] expectedResiduals = new double[testMeasurements.length];
        double expectedCost = Math.sqrt(0);
        Assert.assertArrayEquals(expectedResiduals, estimator.getResiduals(), 0);
        Assert.assertEquals(expectedCost, estimator.getCost(), 0);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCost_Exception() throws EstimationException {
        estimator.setMaxCostEval(2);
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double expectedRMS = Math.sqrt(0);
        Assert.assertEquals(expectedRMS, estimator.getRMS(problem), 0);
    }

    @Test
    public void testGetChiSquare() {
        double expectedChiSquare = 0;
        Assert.assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0);
    }

    @Test
    public void testGetCovariances() {
        double[][] expectedCovariances = new double[testParameters.length][testParameters.length];
        Assert.assertArrayEquals(expectedCovariances, estimator.getCovariances(problem));
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariances_Exception() throws EstimationException {
        estimator.updateJacobian();
        estimator.getCovariances(problem);
    }

    @Test
    public void testGuessParametersErrors() {
        double[] expectedErrors = new double[testParameters.length];
        Assert.assertArrayEquals(expectedErrors, estimator.guessParametersErrors(problem), 0);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrors_Exception() throws EstimationException {
        estimator.setMaxCostEval(2);
        estimator.guessParametersErrors(problem);
    }

    @Test
    public void testInitializeEstimate() {
        estimator.initializeEstimate(problem);
        Assert.assertEquals(0, estimator.getCostEvaluations());
        Assert.assertEquals(0, estimator.getJacobianEvaluations());
        Assert.assertEquals(testMeasurements.length, estimator.getRows());
        Assert.assertEquals(testParameters.length, estimator.getCols());
        double[] expectedJacobian = new double[testMeasurements.length * testParameters.length];
        Assert.assertArrayEquals(expectedJacobian, estimator.getJacobian(), 0);
        double[] expectedResiduals = new double[testMeasurements.length];
        Assert.assertArrayEquals(expectedResiduals, estimator.getResiduals(), 0);
        Assert.assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), 0);
    }

    @Test
    public void testEstimate() throws EstimationException {
        estimator.estimate(problem);
        double[] expectedParameters = new double[testParameters.length];
        for (int i = 0; i < testParameters.length; i++) {
            expectedParameters[i] = testParameters[i].getEstimate();
        }
        Assert.assertArrayEquals(expectedParameters, problem.getAllParameters(), 0);
    }

    private class TestEstimationProblem implements EstimationProblem {

        public WeightedMeasurement[] getMeasurements() {
            return testMeasurements;
        }

        public EstimatedParameter[] getAllParameters() {
            return testParameters;
        }

    }
    
    private class TestAbstractEstimator extends AbstractEstimator {

        public TestAbstractEstimator() {
            super();
        }

        public int getMaxCostEval() {
            return maxCostEval;
        }

        public void setCostEvaluations(int costEvaluations) {
            this.costEvaluations = costEvaluations;
        }

        public void setJacobianEvaluations(int jacobianEvaluations) {
            this.jacobianEvaluations = jacobianEvaluations;
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

        public int getRows() {
            return rows;
        }

        public int getCols() {
            return cols;
        }

        public void estimate(EstimationProblem problem) throws EstimationException {
            for (EstimatedParameter parameter : problem.getAllParameters()) {
                parameter.setEstimate(0);
            }
        }

    }

    private WeightedMeasurement[] testMeasurements = new WeightedMeasurement[0];
    private EstimatedParameter[] testParameters = new EstimatedParameter[0];

}