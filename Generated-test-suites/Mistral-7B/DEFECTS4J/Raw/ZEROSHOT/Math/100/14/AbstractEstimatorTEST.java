package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.StatFunction;
import org.apache.commons.math3.stat.descript.Statistics;
import org.junit.Test;

import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.RegressionEstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.UnivariateRealFunction;
import org.apache.commons.math.estimation.UnivariateRealFunctionEstimator;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
                updateResidualsAndCost();
            }

        };
        assertEquals(0, estimator.getCostEvaluations());
        estimator.estimate(getDummyRegressionProblem());
        assertEquals(2, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateJacobian() {
                counter++;
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateJacobian();
                updateJacobian();
            }

        };
        assertEquals(0, estimator.getJacobianEvaluations());
        estimator.estimate(getDummyRegressionProblem());
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    @Test
    public void testRMS() throws EstimationException {
        AbstractEstimator estimator = new UnivariateRealFunctionEstimator(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        });
        EstimationProblem problem = new RegressionEstimationProblem(1, 1, new double[] {1, 1, 2, 2, 3, 3}, new double[] {1, 4, 9, 16, 25, 36});
        estimator.estimate(problem);
        assertEquals(5.576025542985275, estimator.getRMS(problem), 1e-7);
    }

    @Test
    public void testChiSquare() throws EstimationException {
        AbstractEstimator estimator = new UnivariateRealFunctionEstimator(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        });
        EstimationProblem problem = new RegressionEstimationProblem(1, 1, new double[] {1, 1, 2, 2, 3, 3}, new double[] {1, 4, 9, 16, 25, 36});
        estimator.estimate(problem);
        assertEquals(51.0, estimator.getChiSquare(problem), 1e-7);
    }

    @Test
    public void testCovariances() throws EstimationException {
        AbstractEstimator estimator = new UnivariateRealFunctionEstimator(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        });
        EstimationProblem problem = new RegressionEstimationProblem(1, 1, new double[] {1, 1, 2, 2, 3, 3}, new double[] {1, 4, 9, 16, 25, 36});
        estimator.estimate(problem);
        RealMatrix covariances = estimator.getCovariances(problem);
        RealVector expected = new ArrayRealVector(new double[] {2.8862643602863283, 2.8862643602863283});
        assertEquals(expected.getDimension(), covariances.getRowDimension(), covariances.getColumnDimension());
        for (int i = 0; i < expected.getDimension(); i++) {
            assertEquals(expected.getEntry(i), covariances.getEntry(i, i), 1e-7);
        }
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new UnivariateRealFunctionEstimator(new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        });
        EstimationProblem problem = new RegressionEstimationProblem(1, 1, new double[] {1, 1, 2, 2, 3, 3}, new double[] {1, 4, 9, 16, 25, 36});
        estimator.estimate(problem);
        double[] expected = new double[] {1.6429762876242956, 1.6429762876242956};
        assertArrayEquals(expected, estimator.guessParametersErrors(problem), 1e-7);
    }

    @Test
    public void testMaxCostEvalReached() {
        AbstractEstimator estimator = new AbstractEstimator() {
            int counter = 0;

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                counter++;
                if (counter >= 5) {
                    throw new EstimationException("maximal number of evaluations exceeded (5)");
                }
            }

            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                updateResidualsAndCost();
                updateResidualsAndCost();
                updateResidualsAndCost();
                updateResidualsAndCost();
                updateResidualsAndCost();
            }

        };
        EstimationProblem problem = getDummyRegressionProblem();
        assertThrows(EstimationException.class, () -> estimator.estimate(problem));
    }

    private EstimationProblem getDummyRegressionProblem() {
        double[] measurementsX = new double[] {1, 1, 2, 2, 3, 3};
        double[] measurementsY = new double[] {1, 4, 9, 16, 25, 36};
        return new RegressionEstimationProblem(1, 1, measurementsX, measurementsY);
    }
}