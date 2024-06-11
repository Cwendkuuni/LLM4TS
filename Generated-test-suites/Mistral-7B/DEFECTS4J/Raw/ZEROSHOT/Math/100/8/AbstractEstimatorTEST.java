package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descript.moment.StandardDeviation;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator = new MyAbstractEstimator();

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator.setMaxCostEval(10);
        estimator.estimate(createProblem());
        assertEquals(0, estimator.getCostEvaluations());

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        assertEquals(10, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator.estimate(createProblem());
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();

        assertEquals(3, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator.estimate(createProblem());
        estimator.updateJacobian();

        double[] expectedJacobian = {-1.0, -2.0, -3.0, -1.0, -2.0, -3.0, -1.0, -2.0, -3.0};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostThrowsException() {
        estimator.setMaxCostEval(1);
        estimator.estimate(createProblem());

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = createProblem();
        estimator.estimate(problem);

        double rms = estimator.getRMS(problem);
        assertEquals(1.0, rms, 0.001);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = createProblem();
        estimator.estimate(problem);

        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(1.0, chiSquare, 0.001);
    }

    @Test
    public void testGetCovariances() {
        EstimationProblem problem = createProblem();
        estimator.estimate(problem);

        double[][] covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new Array2DRowRealMatrix(new double[][]{{0.1, 0.05}, {0.05, 0.1}});
        assertArrayEquals(expectedCovariances.getData(), covariances, 0.001);
    }

    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = createProblem();
        estimator.estimate(problem);

        double[] errors = estimator.guessParametersErrors(problem);
        RealVector expectedErrors = new Array2DRowRealMatrix(new double[][]{{0.5, 0.25}, {0.25, 0.5}}).getColumnVector(0);
        assertArrayEquals(expectedErrors.toArray(), errors, 0.001);
    }

    @Test(expected = EstimationException.class)
    public void testGetCovariancesThrowsException() {
        EstimationProblem problem = createProblem();
        estimator.estimate(problem);

        problem.getMeasurements()[0].setWeight(Double.NEGATIVE_INFINITY);
        estimator.getCovariances(problem);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateThrowsException() {
        EstimationProblem problem = createProblem();
        problem.getMeasurements()[0].setWeight(Double.NEGATIVE_INFINITY);

        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem createProblem() {
        double[][] x = new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        double[] b = {1.0, 1.0, 1.0};

        EstimationProblem problem = new EstimationProblem() {
            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[]{new EstimatedParameter("p1", 0.0), new EstimatedParameter("p2", 0.0)};
            }

            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[]{new WeightedMeasurement(x[0], b[0], 1.0), new WeightedMeasurement(x[1], b[1], 1.0), new WeightedMeasurement(x[2], b[2], 1.0)};
            }

            @Override
            public RealVector getUnboundParameters() {
                return new Array2DRowRealMatrix(x).convertToVector();
            }
        };

        return problem;
    }
}

class MyAbstractEstimator extends AbstractEstimator {
    @Override
    public void estimate(EstimationProblem problem) throws EstimationException {
        // empty implementation
    }
}