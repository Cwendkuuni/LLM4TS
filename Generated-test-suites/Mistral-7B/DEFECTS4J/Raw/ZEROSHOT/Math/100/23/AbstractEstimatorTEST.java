package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.descript.Statistics;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private WeightedMeasurement[] measurements;
    private EstimatedParameter[] parameters;
    private double[][] covariances;

    @Before
    public void setUp() {
        estimator = new MyAbstractEstimator();
        measurements = new WeightedMeasurement[3];
        parameters = new EstimatedParameter[2];
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = new WeightedMeasurement(new ArrayRealVector(new double[] {i + 1, i + 2}),
                1.0, i + 1.0);
        }
        parameters[0] = new EstimatedParameter("p1", 0.0);
        parameters[1] = new EstimatedParameter("p2", 0.0);
    }

    @Test
    public void testGetRMS() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double expectedRMS = Math.sqrt(Statistics.variance(new double[] {0.25, 0.25, 0.25}));
        assertEquals(expectedRMS, estimator.getRMS(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))), null));
    }

    @Test
    public void testGetChiSquare() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double expectedChiSquare = 0.25 * (Math.pow(0.25, 2.0) + Math.pow(0.25, 2.0) + Math.pow(0.25, 2.0));
        assertEquals(expectedChiSquare, estimator.getChiSquare(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))), null));
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        RealMatrix jacobian = new RealMatrixImpl(estimator.jacobian);
        RealMatrix transposeJacobian = jacobian.transpose();
        RealMatrix inverseTransposeJacobian = transposeJacobian.inverse();
        covariances = inverseTransposeJacobian.getData();
        RealArray2D expectedCovariancesData = MatrixUtils.dense2DArray(new double[][] {
            {0.166666666666667, -0.055555555555556},
            {-0.055555555555556, 0.133333333333333}
        });
        RealMatrix expectedCovariances = new RealMatrixImpl(expectedCovariancesData);
        RealMatrix actualCovariances = new RealMatrixImpl(covariances);
        SingularValueDecomposition svd = new SingularValueDecomposition(actualCovariances);
        RealMatrix expectedSvd = new SingularValueDecomposition(expectedCovariances);
        assertEquals(expectedSvd.getSingularValues(), svd.getSingularValues());
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double[] expectedErrors = new double[] {0.5, 0.5};
        assertArrayEquals(expectedErrors, estimator.guessParametersErrors(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))), null));
    }

    @Test(expected = EstimationException.class)
    public void testSetMaxCostEvalAndGetEstimateWithMaxCostEvalReached() throws EstimationException {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
    }

    @Test
    public void testSetMaxCostEvalAndGetEstimateWithMaxCostEvalNotReached() throws EstimationException {
        estimator.setMaxCostEval(3);
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double[] expectedParameters = new double[] {1.0, 1.0};
        RealVector actualParameters = estimator.getAllParameters();
        assertArrayEquals(expectedParameters, actualParameters.toArray());
    }

    @Test
    public void testSetMaxCostEvalAndGetEstimateWithMaxCostEvalExceededDuringIterations() throws EstimationException {
        estimator.setMaxCostEval(1);
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.setParameters(new EstimatedParameter[] {new EstimatedParameter("p1", 1.0), new EstimatedParameter("p2", 1.0)});
        // This will cause cost evaluations exceeding maxCostEval
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double[] expectedParameters = new double[] {1.0, 1.0};
        RealVector actualParameters = estimator.getAllParameters();
        assertArrayEquals(expectedParameters, actualParameters.toArray());
    }

    @Test
    public void testUpdateJacobianAndResiduals() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double[] expectedJacobian = new double[] {-1.5, -0.5, 1.5, 0.5};
        assertArrayEquals(expectedJacobian, estimator.jacobian, 1e-10);
        double[] expectedResiduals = new double[] {0.25, 0.25, 0.25};
        assertArrayEquals(expectedResiduals, estimator.residuals, 1e-10);
    }

    @Test
    public void testSetAndGetEstimateParameters() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.setParameters(new EstimatedParameter[] {new EstimatedParameter("p1", 1.0), new EstimatedParameter("p2", 2.0)});
        EstimatedParameter[] actualParameters = estimator.getParameters();
        assertArrayEquals(new EstimatedParameter[] {new EstimatedParameter("p1", 1.0), new EstimatedParameter("p2", 2.0)}, actualParameters);
    }

    @Test
    public void testGetCost() throws EstimationException {
        estimator.initializeEstimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        estimator.estimate(new EstimationProblem(new RealMatrix(MatrixUtils.dense2DArray(new double[][] {
                {1.0, 2.0, 1.0},
                {1.5, 2.5, 1.5},
                {2.0, 3.0, 2.0}
            }))));
        double expectedCost = 0.25 * (Math.pow(0.25, 2.0) + Math.pow(0.25, 2.0) + Math.pow(0.25, 2.0));
        assertEquals(expectedCost, estimator.getCost(), 1e-10);
    }

    private static class MyAbstractEstimator extends AbstractEstimator {
        @Override
        protected void estimate(EstimationProblem problem) throws EstimationException {
            super.estimate(problem);
            OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression(problem.getAllParameters(), problem.getMeasurements());
            setParameters(regression.estimateRegressionParameters());
        }
    }

}