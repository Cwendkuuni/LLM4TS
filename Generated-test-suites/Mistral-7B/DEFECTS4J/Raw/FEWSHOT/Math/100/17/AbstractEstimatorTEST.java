package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(Parameterized.class)
public class AbstractEstimatorTest {

    private static final double EPSILON = 1e-9;

    private AbstractEstimator estimator;
    private EstimationProblem problem;
    private int maxCostEval;

    public AbstractEstimatorTest(int maxCostEval) {
        this.maxCostEval = maxCostEval;
    }

    @Parameterized.Parameters(name = "MaxCostEval={0}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {10},
                {5},
                {100},
                {1000}
        };
        return Arrays.asList(data);
    }

    @Before
    public void setUp() {
        estimator = new MyEstimator();
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return measurements;
            }

            @Override
            public EstimatedParameter[] getUnboundParameters() {
                return parameters;
            }

            @Override
            public EstimationFunction getFunction() {
                return new EstimationFunction() {
                    @Override
                    public double value(RealVector parameters) {
                        return 0;
                    }

                    @Override
                    public RealVector gradient(RealVector parameters) {
                        return new Array2DRowRealMatrix(new double[][]{{1, 1}, {1, 1}}).transpose().mult(parameters);
                    }
                };
            }

            private double[] measurements = new double[]{1, 2};
            private double[] parameters = new double[]{0, 0};
        };
        estimator.setMaxCostEval(maxCostEval);
    }

    @Test
    public void testSetMaxCostEval() {
        assertEquals(maxCostEval, estimator.getMaxCostEval());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxCostEval() {
        estimator.setMaxCostEval(-1);
    }

    @Test
    public void testGetCostEvaluations() {
        assertEquals(0, estimator.getCostEvaluations());
    }

    @Test
    public void testUpdateJacobian() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();
        assertArrayEquals(new double[]{-1, -1}, Arrays.copyOfRange(estimator.jacobian, 0, 2));
        assertEquals(1, estimator.getJacobianEvaluations());
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithMaxCostEvalReached() throws Exception {
        estimator.initializeEstimate(problem);
        for (int i = 0; i < maxCostEval; ++i) {
            estimator.updateResidualsAndCost();
        }
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() throws Exception {
        estimator.initializeEstimate(problem);
        double rms = estimator.getRMS(problem);
        assertTrue(rms > 0);
    }

    @Test
    public void testGetChiSquare() throws Exception {
        estimator.initializeEstimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertTrue(chiSquare > 0);
    }

    @Test
    public void testGuessParametersErrors() throws Exception {
        estimator.initializeEstimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertArrayEquals(new double[]{Math.sqrt(2) * Math.sqrt(maxCostEval / (problem.getMeasurements().length - 2)),
                                       Math.sqrt(2) * Math.sqrt(maxCostEval / (problem.getMeasurements().length - 2))}, errors);
    }

    @Test
    public void testGetCovariances() throws Exception {
        estimator.initializeEstimate(problem);
        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{1 / (problem.getMeasurements().length - 1), 1 / (problem.getMeasurements().length - 1)},
                                                                                    {1 / (problem.getMeasurements().length - 1), 1 / (problem.getMeasurements().length - 1)}});
        assertArrayEquals(expectedCovariances.getData(), covariances);

        estimator.updateJacobian();
        RealVector parameters = new Array2DRowRealMatrix(new double[][]{new double[]{0}, new double[]{1}}).transpose().mult(expectedCovariances.getInverse());
        double[][] covariances2 = estimator.getCovariances(problem);
        Covariance cov = new Covariance();
        RealMatrix covMatrix = cov.cov(params, problem.getAllParameters(), new MersenneTwister(new Random(1)));
        assertArrayEquals(covMatrix.getData(), covariances2);
    }

    @Test
    public void testEstimate() throws Exception {
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(3, estimator.getCostEvaluations());
        assertEquals(3, estimator.getJacobianEvaluations());
    }
}

class MyEstimator extends AbstractEstimator {
    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // implementation not provided here
    }
}