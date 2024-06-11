package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.EstimationException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecomposition.UATEIGSingularValueDecomposition;
import org.apache.commons.math3.stat.Statistics;
import org.junit.Test;

/**
 * Test class for {@link AbstractEstimator}.
 */
public class AbstractEstimatorTest {

    /**
     * Test method for {@link AbstractEstimator#setMaxCostEval(int)}.
     */
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }

    /**
     * Test method for {@link AbstractEstimator#getCostEvaluations()}.
     */
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();
        assertEquals(2, estimator.getCostEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#getJacobianEvaluations()}.
     */
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.updateJacobian();
        estimator.updateJacobian();
        assertEquals(2, estimator.getJacobianEvaluations());
    }

    /**
     * Test method for {@link AbstractEstimator#updateJacobian()}.
     */
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createTestEstimationProblem());
        estimator.updateJacobian();
        double[] expectedJacobian = new double[] { -1.0, -1.0, -1.0 };
        assertArrayEquals(expectedJacobian, estimator.jacobian, 0.0);
    }

    /**
     * Test method for {@link AbstractEstimator#updateResidualsAndCost()}.
     */
    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createTestEstimationProblem());
        estimator.updateResidualsAndCost();
        double expectedResidual = 1.0;
        double expectedCost = Math.sqrt(1.0 + 1.0 + 1.0) * expectedResidual * expectedResidual;
        assertEquals(expectedCost, estimator.cost, 0.0);
        assertArrayEquals(new double[] { expectedResidual, expectedResidual, expectedResidual },
                         estimator.residuals, 0.0);
    }

    /**
     * Test method for {@link AbstractEstimator#getRMS(EstimationProblem)}.
     */
    @Test
    public void testGetRMS() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createTestEstimationProblem());
        estimator.updateResidualsAndCost();
        double expectedRMS = Math.sqrt(estimator.cost / 3.0);
        assertEquals(expectedRMS, estimator.getRMS(createTestEstimationProblem()), 0.0);
    }

    /**
     * Test method for {@link AbstractEstimator#getChiSquare(EstimationProblem)}.
     */
    @Test
    public void testGetChiSquare() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createTestEstimationProblem());
        estimator.updateResidualsAndCost();
        double expectedChiSquare = 3.0 * (1.0 / 0.1) * (1.0 / 0.1);
        assertEquals(expectedChiSquare, estimator.getChiSquare(createTestEstimationProblem()), 0.0);
    }

    /**
     * Test method for {@link AbstractEstimator#getCovariances(EstimationProblem)}.
     */
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createTestEstimationProblem());
        estimator.updateJacobian();
        double[][] expectedCovariances = new double[][] { { 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 },
                                                           { 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 },
                                                           { 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0 } };
        RealMatrix expectedCovarianceMatrix = new RealMatrixImpl(expectedCovariances);
        RealMatrix covariances = estimator.getCovariances(createTestEstimationProblem());
        assertTrue(expectedCovarianceMatrix.isSimilarTo(covariances, 1e-9));
    }

    /**
     * Test method for {@link AbstractEstimator#guessParametersErrors(EstimationProblem)}.
     */
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(createTestEstimationProblem());
        estimator.updateJacobian();
        double[] errors = estimator.guessParametersErrors(createTestEstimationProblem());
        assertEquals(3, errors.length);
        double expectedError = Math.sqrt(2.0 / 3.0);
        for (int i = 0; i < errors.length; ++i) {
            assertEquals(expectedError, errors[i], 0.0);
        }
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     */
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        EstimationProblem problem = createTestEstimationProblem();
        estimator.initializeEstimate(problem);
        assertEquals(3, estimator.rows);
        assertEquals(3, estimator.cols);
        assertFalse(Arrays.equals(estimator.measurements, problem.getMeasurements()));
        assertFalse(Arrays.equals(estimator.parameters, problem.getAllParameters()));
        assertArrayEquals(new double[3 * 3], estimator.jacobian, 0.0);
        assertArrayEquals(new double[3], estimator.residuals, 0.0);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}.
     */
    @Test(expected = EstimationException.class)
    public void testEstimateWithInvalidProblem() {
        AbstractEstimator estimator = new TestAbstractEstimator();
        estimator.initializeEstimate(new TestEstimationProblemWithInvalidMeasurements());
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}.
     */
    @Test
    public void testEstimate() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        EstimationProblem problem = createTestEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        double[] expectedParameters = new double[] { 1.0, 2.0, 3.0 };
        assertArrayEquals(expectedParameters, problem.getAllParameters(), 0.0);
    }

    /**
     * Test method for {@link AbstractEstimator#initializeEstimate(EstimationProblem)}.
     * Verifies that the jacobian is updated correctly when a new problem is passed.
     */
    @Test
    public void testInitializeEstimateWithNewProblem() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        EstimationProblem problem1 = createTestEstimationProblem();
        estimator.initializeEstimate(problem1);
        estimator.estimate(problem1);
        EstimationProblem problem2 = createTestEstimationProblemWithDifferentMeasurements();
        estimator.initializeEstimate(problem2);
        estimator.estimate(problem2);
        SingularValueDecomposition svd = new UATEIGSingularValueDecomposition(estimator.jacobian);
        assertFalse(svd.isSymmetric());
    }

    /**
     * Test method for {@link AbstractEstimator#estimate(EstimationProblem)}.
     * Verifies that the Jacobian is updated correctly after estimation.
     */
    @Test
    public void testEstimateWithJacobianUpdate() throws EstimationException {
        AbstractEstimator estimator = new TestAbstractEstimator();
        EstimationProblem problem = createTestEstimationProblem();
        estimator.initializeEstimate(problem);
        estimator.estimate(problem);
        EstimationProblem problem2 = createTestEstimationProblemWithDifferentMeasurements();
        estimator.initializeEstimate(problem2);
        estimator.estimate(problem2);
        SingularValueDecomposition svd = new UATEIGSingularValueDecomposition(estimator.jacobian);
        assertTrue(svd.isSymmetric());
    }

    /**
     * Creates a test estimation problem.
     *
     * @return the test estimation problem
     */
    private EstimationProblem createTestEstimationProblem() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[] {
            new TestWeightedMeasurement(1.0, 1.0, 1.0, 0.1),
            new TestWeightedMeasurement(2.0, 2.0, 2.0, 0.1),
            new TestWeightedMeasurement(3.0, 3.0, 3.0, 0.1)
        };
        EstimationProblem problem = new TestEstimationProblem(measurements);
        return problem;
    }

    /**
     * Creates a test estimation problem with different measurements.
     *
     * @return the test estimation problem
     */
    private EstimationProblem createTestEstimationProblemWithDifferentMeasurements() {
        WeightedMeasurement[] measurements = new WeightedMeasurement[] {
            new TestWeightedMeasurement(1.0, 1.1, 1.0, 0.1),
            new TestWeightedMeasurement(2.0, 2.1, 2.0, 0.1),
            new TestWeightedMeasurement(3.0, 3.1, 3.0, 0.1)
        };
        EstimationProblem problem = new TestEstimationProblem(measurements);
        return problem;
    }

    /**
     * Test estimation problem.
     */
    public static class TestEstimationProblem implements EstimationProblem {

        /** The measurements. */
        private WeightedMeasurement[] measurements;

        /**
         * Constructor.
         *
         * @param measurements the measurements
         */
        public TestEstimationProblem(WeightedMeasurement[] measurements) {
            this.measurements = measurements;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EstimatedParameter[] getAllParameters() {
            return new EstimatedParameter[] { new TestEstimatedParameter(0), new TestEstimatedParameter(1),
                                             new TestEstimatedParameter(2) };
        }
    }

    /**
     * Test weighted measurement.
     */
    public static class TestWeightedMeasurement implements WeightedMeasurement {

        /** The weight. */
        private double weight;

        /** The residual. */
        private double residual;

        /** The x value. */
        private double x;

        /** The y value. */
        private double y;

        /** The partials. */
        private double[] partials;

        /**
         * Constructor.
         *
         * @param x the x value
         * @param y the y value
         * @param weight the weight
         * @param residual the residual
         */
        public TestWeightedMeasurement(double x, double y, double weight, double residual) {
            this.x = x;
            this.y = y;
            this.weight = weight;
            this.residual = residual;
            this.partials = new double[] { 1.0, 1.0, 1.0 };
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getResidual() {
            return residual;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getWeight() {
            return weight;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getX() {
            return x;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getY() {
            return y;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double[] getPartial(int index) {
            return new double[] { 1.0 };
        }
    }

    /**
     * Test estimation problem with invalid measurements.
     */
    public static class TestEstimationProblemWithInvalidMeasurements implements EstimationProblem {

        /**
         * {@inheritDoc}
         */
        @Override
        public WeightedMeasurement[] getMeasurements() {
            return new WeightedMeasurement[0];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EstimatedParameter[] getAllParameters() {
            return new EstimatedParameter[0];
        }
    }

    /**
     * Test estimated parameter.
     */
    public static class TestEstimatedParameter implements EstimatedParameter {

        /** The index. */
        private int index;

        /**
         * Constructor.
         *
         * @param index the index
         */
        public TestEstimatedParameter(int index) {
            this.index = index;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIndex() {
            return index;
        }
    }

}