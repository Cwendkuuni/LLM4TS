import org.apache.commons.math3.EstimationException;
import org.apache.commons.math3.estimation.AbstractEstimator;
import org.apache.commons.math3.estimation.EstimationProblem;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new MyEstimator();
        estimator.setMaxCostEval(10);
        int maxCostEval = estimator.getMaxCostEval();
        assertEquals(10, maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new MyEstimator();
        estimator.updateResidualsAndCost();
        int costEvaluations = estimator.getCostEvaluations();
        assertEquals(1, costEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new MyEstimator();
        estimator.updateJacobian();
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        assertEquals(1, jacobianEvaluations);
    }

    @Test(expected = EstimationException.class)
    public void testExceedMaxCostEval() {
        AbstractEstimator estimator = new MyEstimator();
        estimator.setMaxCostEval(1);
        for (int i = 0; i < 3; i++) {
            estimator.updateResidualsAndCost();
        }
    }

    @Test
    public void testGetRMS() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] coefficients = {1.0, 1.1};
        EstimationProblem problem = new EstimationProblem(data, coefficients);
        AbstractEstimator estimator = new MyEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(3.8729833462069065, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] coefficients = {1.0, 1.1};
        EstimationProblem problem = new EstimationProblem(data, coefficients);
        AbstractEstimator estimator = new MyEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(6.0, chiSquare, 0.001);
    }

    @Test
    public void testgetCovariances() throws EstimationException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] coefficients = {1.0, 1.1};
        EstimationProblem problem = new EstimationProblem(data, coefficients);
        AbstractEstimator estimator = new MyEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        Array2DRowRealMatrix covMatrix = new Array2DRowRealMatrix(covariances);
        RealVector vector = covMatrix.getColumnVector(0);
        double[] vectArray = vector.toArray();
        double[] expected = {0.002852833, 0.002852833};
        assertArrayEquals(expected, vectArray, 0.001);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] coefficients = {1.0, 1.1};
        EstimationProblem problem = new EstimationProblem(data, coefficients);
        AbstractEstimator estimator = new MyEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        double[] expected = {0.4590122956639812, 0.4590122956639812};
        assertArrayEquals(expected, errors, 0.001);
    }

    @Test
    public void testInitializeEstimate() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        double[] coefficients = {1.0, 1.1};
        EstimationProblem problem = new EstimationProblem(data, coefficients);
        AbstractEstimator estimator = new MyEstimator();
        estimator.initializeEstimate(problem);
        assertNotNull(estimator.measurements);
        assertNotNull(estimator.parameters);
        assertEquals(3, estimator.rows);
        assertEquals(2, estimator.cols);
        assertNotNull(estimator.jacobian);
        assertNotNull(estimator.residuals);
    }

}

class MyEstimator extends AbstractEstimator {
    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation here
    }

}