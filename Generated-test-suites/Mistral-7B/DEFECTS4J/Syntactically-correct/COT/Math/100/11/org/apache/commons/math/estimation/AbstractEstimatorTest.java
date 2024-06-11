import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Parameterized.Parameters(name = "{index}: testing {0}")
    public static Collection<Object[]> data() {
        // Create test data here
        return Arrays.asList(new Object[][]{
                {new AbstractEstimator()},
                {new AbstractEstimator()},
                // Add more test data as needed
        });
    }

    @Parameterized.Constructor
    public AbstractEstimatorTest(AbstractEstimator estimator) {
        this.estimator = estimator;
        this.problem = createEstimationProblem();
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.setMaxCostEval(10);
        estimator.estimate(problem);
        assertEquals(0, estimator.getCostEvaluations());

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertEquals(3, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.estimate(problem);
        assertEquals(0, estimator.getJacobianEvaluations());

        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.updateJacobian();
        RealMatrix jacobian = new Array2DRowRealMatrix(estimator.jacobian);
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(new double[][]{
                {-1.0 / Math.sqrt(problem.getMeasurements()[0].getWeight()), 1.0 / Math.sqrt(problem.getMeasurements()[0].getWeight())},
                // Add more rows as needed
        });

        SingularValueDecomposition svd = new SingularValueDecomposition(jacobian);
        RealMatrix expectedSingularValues = svd.getS();

        assertEquals(expectedSingularValues.getEntry(0, 0), Precision.SAFE_EPSILON, 0.0);
        assertEquals(expectedSingularValues.getEntry(1, 1), Precision.SAFE_EPSILON, 0.0);
        // Add more assertions for more singular values as needed
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCost_MaxCostEvalExceeded() {
        estimator.setMaxCostEval(0);
        estimator.estimate(problem);
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        double expectedRMS = Math.sqrt(2.0);
        estimator.estimate(problem);
        double actualRMS = estimator.getRMS(problem);
        assertEquals(expectedRMS, actualRMS, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        double expectedChiSquare = 2.0;
        estimator.estimate(problem);
        double actualChiSquare = estimator.getChiSquare(problem);
        assertEquals(expectedChiSquare, actualChiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.estimate(problem);
        double[][] expectedCovariances = new double[][]{
                {1.0, 0.5},
                {0.5, 1.0},
        };
        double[][] actualCovariances = estimator.getCovariances(problem);

        for (int i = 0; i < expectedCovariances.length; i++) {
            assertArrayEquals(expectedCovariances[i], actualCovariances[i], 0.0);
        }
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.estimate(problem);
        double[] expectedErrors = new double[]{1.0, 0.5};
        double[] actualErrors = estimator.guessParametersErrors(problem);

        assertArrayEquals(expectedErrors, actualErrors, 0.0);
    }

    @Test
    public void testInitializeEstimate() throws EstimationException {
        // Test the initialization of the common parts of the estimation
        // Add assertions for the state of the estimator after the call to initializeEstimate
    }

    @Test
    public void testEstimate() throws EstimationException {
        // Test the estimation method
        // Add assertions for the state of the estimator and the problem after the call to estimate
    }

    private EstimationProblem createEstimationProblem() {
        // Create test estimation problem here
    }
}