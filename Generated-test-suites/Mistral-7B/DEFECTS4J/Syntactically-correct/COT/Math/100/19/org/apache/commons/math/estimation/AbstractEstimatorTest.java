import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationException;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            setMaxCostEval(10);
        }};
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testSetAndGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimate(createEstimationProblem());
            updateResidualsAndCost();
        }};
        assertEquals(0, estimator.getCostEvaluations());
        estimator.updateResidualsAndCost();
        assertEquals(1, estimator.getCostEvaluations());
    }

    @Test
    public void testSetAndGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateJacobian();
        }};
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.updateJacobian();
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateJacobian();
        }};
        double[] expectedJacobian = new double[]{
                -1.0, -1.0,
                -1.0, 1.0,
                -1.0, 1.0
        };
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateResidualsAndCost();
        }};
        double[] expectedResiduals = {1.0, 2.0, 3.0};
        assertArrayEquals(expectedResiduals, estimator.residuals, 0.0);
        double expectedCost = Math.sqrt(10.0);
        assertEquals(expectedCost, estimator.cost, 0.0);
    }

    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateResidualsAndCost();
        }};
        EstimationProblem problem = createEstimationProblem();
        double expectedRMS = Math.sqrt(1.0 / 3.0);
        assertEquals(expectedRMS, estimator.getRMS(problem), 0.0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateResidualsAndCost();
        }};
        EstimationProblem problem = createEstimationProblem();
        double expectedChiSquare = 10.0 / 3.0;
        assertEquals(expectedChiSquare, estimator.getChiSquare(problem), 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateJacobian();
        }};
        EstimationProblem problem = createEstimationProblem();
        double[][] expectedCovariances = {
                {0.3333333333333333, 0.3333333333333333},
                {0.3333333333333333, 0.3333333333333333}
        };
        RealMatrixImpl covariancesMatrix = (RealMatrixImpl) estimator.getCovariances(problem);
        Array2DRowRealMatrix covariances = (Array2DRowRealMatrix) covariancesMatrix.transpose();
        assertArrayEquals(expectedCovariances[0], covariances.getColumnVector(0).toArray(), 0.0);
        assertArrayEquals(expectedCovariances[1], covariances.getColumnVector(1).toArray(), 0.0);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
            updateJacobian();
        }};
        EstimationProblem problem = createEstimationProblem();
        double[] expectedErrors = {2.0, 2.0};
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(expectedErrors, errors, 0.0);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {{
            setMaxCostEval(0);
        }};
        estimator.estimate(createEstimationProblem());
    }

    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new AbstractEstimator() {{
            initializeEstimator(createEstimationProblem());
        }};
        assertEquals(3, estimator.rows);
        assertEquals(2, estimator.cols);
        assertEquals(3, estimator.measurements.length);
        assertEquals(2, estimator.parameters.length);
        assertArrayEquals(new double[]{}, estimator.jacobian, 0.0);
        assertArrayEquals(new double[]{}, estimator.residuals, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 0.0);
    }

    private EstimationProblem createEstimationProblem() {
        WeightedMeasurement[] measurements = {
                new WeightedMeasurement(1.0, 1.0),
                new WeightedMeasurement(2.0, 2.0),
                new WeightedMeasurement(3.0, 3.0)
        };
        EstimatedParameter[] parameters = {
                new EstimatedParameter("p1"),
                new EstimatedParameter("p2")
        };
        return new EstimationProblem(parameters, measurements);
    }

}