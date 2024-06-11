package org.apache.commons.math.estimation;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixImpl;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition.UATEIGSingularValueDecomposition;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for AbstractEstimator class.
 */
public class AbstractEstimatorTest {

    private AbstractEstimator estimator;
    private EstimationProblem problem;

    @Before
    public void setup() {
        estimator = new StubEstimator();
        problem = new EstimationProblem() {
            @Override
            public RealMatrix getData() {
                return new RealMatrixImpl(new double[][] {
                        {1, 1},
                        {1, 2},
                        {1, 3},
                        {1, 4}
                });
            }

            @Override
            public RealVector getB() {
                return new ArrayRealVector(new double[] {1, 1, 1, 1});
            }

            @Override
            public int getMeasurementNo() {
                return 4;
            }

            @Override
            public int getParameterNo() {
                return 2;
            }
        };
    }

    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    @Test
    public void testGetCostEvaluations() {
        estimator = new StubEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(0, estimator.getCostEvaluations());

        estimator.setMaxCostEval(5);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        assertEquals(5, estimator.getCostEvaluations());

        estimator.setMaxCostEval(10);
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        estimator.updateCostAndJacobian();
        assertEquals(10, estimator.getCostEvaluations());
    }

    @Test
    public void testGetJacobianEvaluations() {
        estimator = new StubEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(10, estimator.getJacobianEvaluations());
    }

    @Test
    public void testUpdateJacobian() {
        estimator = new StubEstimator();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);

        RealMatrix jacobian = new RealMatrixImpl(estimator.jacobian);
        RealMatrix expectedJacobian = new RealMatrixImpl(new double[][] {
                {-0.03243, -0.03243},
                {-0.03243, -0.06486},
                {-0.03243, -0.09729},
                {-0.03243, -0.12972}
        });

        estimator.updateJacobian();
        assertEquals(expectedJacobian.getRowMatrix(0), jacobian.getRowMatrix(0));
        assertEquals(expectedJacobian.getRowMatrix(1), jacobian.getRowMatrix(1));
        assertEquals(expectedJacobian.getRowMatrix(2), jacobian.getRowMatrix(2));
        assertEquals(expectedJacobian.getRowMatrix(3), jacobian.getRowMatrix(3));
    }

    @Test
    public void testGetRMS() {
        estimator = new StubEstimator();
        double rms = estimator.getRMS(problem);
        assertTrue(rms > 0);
    }

    @Test
    public void testGetChiSquare() {
        estimator = new StubEstimator();
        double chiSquare = estimator.getChiSquare(problem);
        assertTrue(chiSquare > 0);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        estimator = new StubEstimator();
        RealMatrix covariances = estimator.getCovariances(problem);
        RealMatrix expectedCovariances = new SingularValueDecomposition(
                new RealMatrixImpl(new double[][] {
                        {-0.05863, 0.02932},
                        {0.02932, 0.03873}
                })).getU();
        UATEIGSingularValueDecomposition inv = new SingularValueDecomposition(
                expectedCovariances).getInverse();
        RealMatrix transposeInv = inv.getTranspose().getMatrix();
        RealMatrix expectedTransposeInv = new SingularValueDecomposition(
                new RealMatrixImpl(new double[][] {
                        {-2.05863, -1.02932},
                        {-1.02932, -0.96919},
                        {-1.02932, -0.96919},
                        {-1.02932, -0.96919}
                })).getU();
        RealMatrix product = new RealMatrixImpl(estimator.jacobian).transpose().multiply(covariances);
        RealMatrix expectedProduct = transposeInv.multiply(new RealMatrixImpl(new double[][] {
                {-1, 0, 0, 0},
                {0, -1, 0, 0},
                {0, 0, -1, 0},
                {0, 0, 0, -1}
        }));
        assertEquals(expectedProduct, product);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsSingularProblem() throws EstimationException {
        EstimationProblem singularProblem = new EstimationProblem() {
            @Override
            public RealMatrix getData() {
                return new RealMatrixImpl(new double[][] {
                        {1, 1},
                        {1, 1}
                });
            }

            @Override
            public RealVector getB() {
                return new ArrayRealVector(new double[] {1, 1});
            }

            @Override
            public int getMeasurementNo() {
                return 2;
            }

            @Override
            public int getParameterNo() {
                return 2;
            }
        };
        estimator.guessParametersErrors(singularProblem);
    }

    @Test
    public void testInitializeEstimate() {
        estimator = new StubEstimator();
        estimator.initializeEstimate(problem);
        assertArrayEquals(new double[] {1, 1, 1, 1}, estimator.measurements);
        assertArrayEquals(new double[] {0, 0}, estimator.parameters);
        assertEquals(4, estimator.rows);
        assertEquals(2, estimator.cols);
        assertArrayEquals(new double[4 * 2], estimator.jacobian);
        assertArrayEquals(new double[4], estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
    }

    @Test(expected = EstimationException.class)
    public void testEstimateException() throws EstimationException {
        estimator = new StubEstimatorWithException();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
    }

    @Test
    public void testEstimateNoConvergence() throws EstimationException {
        estimator = new StubEstimatorWithNoConvergence();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost, 1e-14);
    }

    private static class StubEstimator extends AbstractEstimator {
        @Override
        public void estimate(EstimationProblem problem) throws EstimationException {
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
            updateCostAndJacobian();
        }

        private void updateCostAndJacobian() throws EstimationException {
            updateResidualsAndCost();
        }
    }

    private static class StubEstimatorWithException extends AbstractEstimator {
        @Override
        protected void updateCostAndJacobian() throws EstimationException {
            throw new EstimationException("Exception occurred");
        }
    }

    private static class StubEstimatorWithNoConvergence extends AbstractEstimator {
        @Override
        protected void updateCostAndJacobian() throws EstimationException {
            ++costEvaluations;
            if (costEvaluations > 100) {
                throw new EstimationException("No convergence reached");
            }
            cost -= 0.01;
            double[][] jacobian = new double[4][2];
            for (int i = 0; i < 4; ++i) {
                jacobian[i][0] = Math.sin(E.PI_DIVIDED_BY_TWO.doubleValue() * i);
                jacobian[i][1] = Math.sin(E.PI_DIVIDED_BY_TWO.doubleValue() * (i + 1));
            }
            System.arraycopy(jacobian[0], 0, estimator.jacobian, 0, 2);
            System.arraycopy(jacobian[1], 0, estimator.jacobian, 2, 2);
            System.arraycopy(jacobian[2], 0, estimator.jacobian, 4, 2);
            System.arraycopy(jacobian[3], 0, estimator.jacobian, 6, 2);
        }
    }
}