package org.apache.commons.math.estimation;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DSample;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setUp() {
        estimator = new DummyEstimator();
        problem = new EstimationProblem() {
            private double[] data = {1.0, 2.0, 3.0, 4.0, 5.0};
            private double[] parameters = {1.0, 1.0};
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 1.0, 1.0, 1.0, 0.0),
                        new WeightedMeasurement(1.0, 2.0, 1.0, 2.0, 1.0),
                        new WeightedMeasurement(1.0, 3.0, 1.0, 3.0, 2.0),
                        new WeightedMeasurement(1.0, 4.0, 1.0, 4.0, 3.0),
                        new WeightedMeasurement(1.0, 5.0, 1.0, 5.0, 4.0)
                };
            }
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] {new EstimatedParameter("p1", 1), new EstimatedParameter("p2", 1)};
            }
        };
    }

    @Test
    public void testGetRMS() throws EstimationException {
        estimator.setMaxCostEval(Integer.MAX_VALUE);
        estimator.estimate(problem);
        Assert.assertEquals(3.1622776601683795, estimator.getRMS(problem), 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetRMSWithMaxCostEvalReached() throws EstimationException {
        estimator = new DummyEstimator();
        estimator.setMaxCostEval(1);
        estimator.estimate(problem);
    }

    @Test
    public void testGetChiSquare() throws EstimationException {
        estimator.setMaxCostEval(Integer.MAX_VALUE);
        estimator.estimate(problem);
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(new RealArray2DSample(2, 5, 0.0));
        expectedJacobian.setEntry(0, 0, -0.707107);
        expectedJacobian.setEntry(0, 1, -0.707107);
        expectedJacobian.setEntry(1, 0, -0.707107);
        expectedJacobian.setEntry(1, 1, -0.707107);
        RealMatrix jacobian = new Array2DRowRealMatrix(estimator.getJacobian());
        SimpleRegression regression = new SimpleRegression();
        regression.fit(new double[5][2], problem.getMeasurements(), problem.getAllParameters());
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][] {
                {0.00023803757631, -0.00023803757631},
                {-0.00023803757631, 0.00023803757631}
        });
        Assert.assertTrue(jacobian.similar(expectedJacobian, 0.001));
        Assert.assertTrue(jacobian.transpose().multiply(jacobian).similar(expectedCovariances, 0.001));
        Assert.assertEquals(6.18652453965955, estimator.getChiSquare(problem), 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetChiSquareWithSingularProblem() throws EstimationException {
        estimator = new DummyEstimator();
        estimator.estimate(new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[0];
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[0];
            }
        });
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.setMaxCostEval(Integer.MAX_VALUE);
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        Assert.assertEquals(2.561375654282958, errors[0], 0.001);
        Assert.assertEquals(2.561375654282958, errors[1], 0.001);
    }

    @Test
    public void testInitializeEstimate() {
        estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);
        Assert.assertArrayEquals(problem.getMeasurements(), estimator.measurements);
        Assert.assertArrayEquals(problem.getAllParameters(), estimator.parameters);
        Assert.assertArrayEquals(new double[rows * cols], estimator.jacobian);
        Assert.assertArrayEquals(new double[rows], estimator.residuals);
        Assert.assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        Assert.assertEquals(0, estimator.costEvaluations);
        Assert.assertEquals(0, estimator.jacobianEvaluations);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateWithMaxCostEvalReached() throws EstimationException {
        estimator = new DummyEstimator();
        estimator.setMaxCostEval(1);
        estimator.estimate(problem);
    }
}

class DummyEstimator extends AbstractEstimator {
    @Override
    protected void estimate(EstimationProblem problem) throws EstimationException {
        // No-op
    }
}