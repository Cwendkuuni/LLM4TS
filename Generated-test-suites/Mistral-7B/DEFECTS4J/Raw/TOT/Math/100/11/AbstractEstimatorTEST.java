package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.descript.DescriptiveStatistics;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private double[][] data;
    private double[] expectedParameters;
    private EstimationProblem problem;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        estimator = new LevenbergMarquardtEstimator();
        data = new double[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        expectedParameters = new double[] { 1.0, 1.0, 1.0 };
        problem = new EstimationProblem() {
            @Override
            public RealVector getObservedValues() {
                return new Array2DRowRealMatrix(data).columnVector(0);
            }

            @Override
            public RealVector getDesignMatrix() {
                return new Array2DRowRealMatrix(data);
            }

            @Override
            public int getMeasurementNoiseLevel() {
                return 0;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2"),
                                                 new EstimatedParameter("p3") };
            }
        };
    }

    /**
     * Tests that {@link AbstractEstimator#estimate} correctly estimates the
     * parameters.
     * @throws Exception if an error occurs
     */
    @Test
    public void testEstimate() throws Exception {
        estimator.estimate(problem);
        RealVector actualParameters = problem.getAllParameters();
        DescriptiveStatistics stats = new DescriptiveStatistics(actualParameters);
        assertArrayEquals(expectedParameters, actualParameters.toArray(), 1e-9);
        assertEquals(expectedParameters[0], stats.getMean(), 1e-9);
        assertEquals(expectedParameters[1], stats.getMean(), 1e-9);
        assertEquals(expectedParameters[2], stats.getMean(), 1e-9);
        RealMatrix jacobian = estimator.getCovariances(problem);
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.estimate(problem.getDesignMatrix(), problem.getObservedValues());
        RealVector coefficients = regression.getCoefficients();
        RealMatrix expectedJacobian = new Array2DRowRealMatrix(data).transpose();
        assertEquals(expectedJacobian, jacobian);
        assertEquals(coefficients, actualParameters);
    }

    /**
     * Tests that {@link AbstractEstimator#estimate} throws an exception if
     * the number of measurements is less than the number of parameters.
     * @throws Exception if an error occurs
     */
    @Test(expected = EstimationException.class)
    public void testEstimateSingular() throws Exception {
        EstimationProblem problem = new EstimationProblem() {
            @Override
            public RealVector getObservedValues() {
                return new Array2DRowRealMatrix(new double[][] { { 1 }, { 2 } })
                      .columnVector(0);
            }

            @Override
            public RealVector getDesignMatrix() {
                return new Array2DRowRealMatrix(new double[][] { { 1, 1 }, { 1, 2 } });
            }

            @Override
            public int getMeasurementNoiseLevel() {
                return 0;
            }

            @Override
            public EstimatedParameter[] getAllParameters() {
                return new EstimatedParameter[] { new EstimatedParameter("p1"),
                                                 new EstimatedParameter("p2") };
            }
        };
        estimator.estimate(problem);
    }

}