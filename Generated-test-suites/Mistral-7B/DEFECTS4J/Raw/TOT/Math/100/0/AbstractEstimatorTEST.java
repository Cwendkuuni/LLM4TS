package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setup() {
        estimator = new MyAbstractEstimator();
        problem = new EstimationProblem() {
            @Override
            public WeightedMeasurement[] getMeasurements() {
                return new WeightedMeasurement[] {
                        new WeightedMeasurement(1.0, 2.0, 0.5),
                        new WeightedMeasurement(2.0, 3.0, 0.5),
                        new WeightedMeasurement(3.0, 4.0, 0.5)
                };
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"), new EstimatedParameter("p2") };
            }

            @Override
            public double[] getParameters() {
                return new double[] { 0, 0 };
            }

            @Override
            public void setParameters(double[] parameters) {
                // empty
            }
        };
    }

    @Test
    public void testGetRMS() {
        estimator.estimate(problem);
        double rms = estimator.getRMS(problem);
        assertEquals(Math.sqrt(E.EPSILON), rms, 0.0);
    }

    @Test
    public void testGetChiSquare() {
        estimator.estimate(problem);
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(3.0, chiSquare, 0.0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.estimate(problem);
        RealMatrix cov = estimator.getCovariances(problem);
        RealVector v1 = new ArrayRealVector(new double[] { 1.0, 1.0 });
        RealVector v2 = new ArrayRealVector(new double[] { 1.0, -1.0 });
        RealVector x1 = new ArrayRealVector(new double[] { 1.0, 1.0 });
        RealVector x2 = new ArrayRealVector(new double[] { 1.0, -1.0 });

        assertArrayEquals(new double[] { 1.0 / 3.0, -1.0 / 3.0 }, cov.operateMult(v1, x1).toArray());
        assertArrayEquals(new double[] { -1.0 / 3.0, 1.0 / 3.0 }, cov.operateMult(v1, x2).toArray());
        assertArrayEquals(new double[] { 2.0 / 3.0, 2.0 / 3.0 }, cov.operateMult(v2, x1).toArray());
        assertArrayEquals(new double[] { 2.0 / 3.0, 2.0 / 3.0 }, cov.operateMult(v2, x2).toArray());
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.estimate(problem);
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(Math.sqrt(E.EPSILON), errors[0], 0.0);
        assertEquals(Math.sqrt(E.EPSILON), errors[1], 0.0);
    }

    @Test
    public void testUpdateResidualsAndCost() {
        estimator = new MyAbstractEstimator();
        problem.setMaxCostEval(1);
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();
        assertEquals(1.0, estimator.getCostEvaluations(), 0.0);
        assertEquals(1.0, estimator.getJacobianEvaluations(), 0.0);
        SimpleRegression regression = new SimpleRegression();
        double[] expectedResiduals = new double[] { -1.0, 0.0, 1.0 };
        assertArrayEquals(expectedResiduals, estimator.residuals, 0.0);
        assertEquals(Math.sqrt(3.0), estimator.cost, 0.0);

        estimator.updateResidualsAndCost();
        assertEquals(2.0, estimator.getCostEvaluations(), 0.0);
        assertEquals(1.0, estimator.getJacobianEvaluations(), 0.0);
        assertThrows(EstimationException.class, () -> estimator.updateResidualsAndCost());
    }

    private static class MyAbstractEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            // empty
        }
    }

}