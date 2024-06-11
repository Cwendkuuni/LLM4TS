package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;

/**
 * Test class for AbstractEstimator.
 */
public class AbstractEstimatorTest {

    /**
     * Test for the setMaxCostEval and getCostEvaluations methods.
     * @throws Exception any exception
     */
    @Test
    public void testCostEvaluations() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        estimator.setMaxCostEval(10);
        estimator.estimate(createEstimationProblem());
        assertEquals(10, estimator.getCostEvaluations());
    }

    /**
     * Test for the updateJacobian and updateResidualsAndCost methods.
     * @throws Exception any exception
     */
    @Test
    public void testUpdateJacobianAndCost() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        estimator.updateResidualsAndCost();
        assertArrayEquals(new double[]{-1.0, -1.0}, estimator.jacobian, 0.0);
        assertEquals(1.0, estimator.cost, 0.0);
    }

    /**
     * Test for the getRMS and getChiSquare methods.
     * @throws Exception any exception
     */
    @Test
    public void testRMSAndChiSquare() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = createEstimationProblem();
        estimator.estimate(problem);
        assertEquals(1.0, estimator.getRMS(problem), 0.0);
        assertEquals(3.0, estimator.getChiSquare(problem), 0.0);
    }

    /**
     * Test for the getCovariances and guessParametersErrors methods.
     * @throws Exception any exception
     */
    @Test
    public void testCovariancesAndGuessParametersErrors() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = createEstimationProblem();
        estimator.estimate(problem);
        RealMatrix covarianceMatrix = estimator.getCovariances(problem);
        RealVector errors = estimator.guessParametersErrors(problem);
        assertEquals(1.0, covarianceMatrix.getEntry(0, 0), 0.0);
        assertEquals(1.0, covarianceMatrix.getEntry(1, 1), 0.0);
        assertEquals(1.0, errors.getEntry(0), 0.0);
        assertEquals(1.0, errors.getEntry(1), 0.0);
    }

    /**
     * Test for the estimate method with a valid problem.
     * @throws Exception any exception
     */
    @Test
    public void testEstimateValidProblem() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = createEstimationProblem();
        estimator.estimate(problem);
        RealVector parameters = problem.getAllParameters();
        assertEquals(2.0, parameters.getEntry(0), 0.0);
        assertEquals(3.0, parameters.getEntry(1), 0.0);
    }

    /**
     * Test for the estimate method with an invalid problem.
     * @throws Exception any exception
     */
    @Test(expected = EstimationException.class)
    public void testEstimateInvalidProblem() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = new EstimationProblem(new SimpleRegression(), new double[]{-1.0, 1.0}, new double[]{1.0, 2.0});
        estimator.estimate(problem);
    }

    /**
     * Test for the initializeEstimate method with a valid problem.
     * @throws Exception any exception
     */
    @Test
    public void testInitializeEstimateValidProblem() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = createEstimationProblem();
        estimator.initializeEstimate(problem);
        assertEquals(2, estimator.rows);
        assertEquals(2, estimator.cols);
        assertArrayEquals(new double[]{1.0, 2.0}, estimator.parameters, 0.0);
    }

    /**
     * Test for the initializeEstimate method with an invalid problem.
     * @throws Exception any exception
     */
    @Test(expected = EstimationException.class)
    public void testInitializeEstimateInvalidProblem() throws Exception {
        AbstractEstimator estimator = new MyEstimator();
        EstimationProblem problem = new EstimationProblem(new SimpleRegression(), new double[]{1.0}, new double[]{1.0, 2.0});
        estimator.initializeEstimate(problem);
    }

    /**
     * Creates an estimation problem.
     * @return the estimation problem
     */
    private EstimationProblem createEstimationProblem() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[2];
        measurements[0] = new WeightedMeasurement(new double[]{1.0}, 1.0);
        measurements[1] = new WeightedMeasurement(new double[]{2.0}, 1.0);
        EstimatedParameter[] parameters = new EstimatedParameter[2];
        parameters[0] = new EstimatedParameter("x", 0.0);
        parameters[1] = new EstimatedParameter("y", 0.0);
        EstimationProblem problem = new EstimationProblem(new SimpleRegression(), measurements, parameters);
        return problem;
    }

    /**
     * A dummy estimator for testing purposes.
     */
    private static class MyEstimator extends AbstractEstimator {
        // empty
    }

    /**
     * Asserts that two arrays are equal.
     * @param expected the expected array
     * @param actual the actual array
     * @param epsilon the tolerance
     */
    private static void assertArrayEquals(double[] expected, double[] actual, double epsilon) {
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i], epsilon);
        }
    }
}