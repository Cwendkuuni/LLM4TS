package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new DummyEstimationProblem(10, 5);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);

        assertArrayEquals(new WeightedMeasurement[0], estimator.measurements);
        assertArrayEquals(new EstimatedParameter[0], estimator.parameters);
        assertEquals(0, estimator.rows);
        assertEquals(0, estimator.cols);
        assertArrayEquals(new double[0], estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
    }

    @Test(expected = EstimationException.class)
    public void testUpdateResidualsAndCostWithExceededCostEvaluations() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem(10, 5);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }

            @Override
            protected void updateResidualsAndCost() throws EstimationException {
                updateResidualsAndCost(); // call to superclass method with no maxCostEval
            }
        };
        estimator.initializeEstimate(problem);
        estimator.maxCostEval = 1;

        estimator.updateResidualsAndCost();
    }

    @Test
    public void testUpdateJacobian() {
        EstimationProblem problem = new DummyEstimationProblem(3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);
        estimator.updateJacobian();

        WeightedMeasurement wm1 = problem.getMeasurements()[0];
        WeightedMeasurement wm2 = problem.getMeasurements()[1];
        EstimatedParameter param1 = problem.getAllParameters()[0];
        EstimatedParameter param2 = problem.getAllParameters()[1];
        EstimatedParameter param3 = problem.getAllParameters()[2];

        double[] expectedJacobian = {
                -wm1.getPartial(param1.getEstimate()),
                -wm1.getPartial(param2.getEstimate()),
                -wm1.getPartial(param3.getEstimate()),
                -wm2.getPartial(param1.getEstimate()),
                -wm2.getPartial(param2.getEstimate()),
                -wm2.getPartial(param3.getEstimate())
        };

        assertArrayEquals(expectedJacobian, estimator.jacobian);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new DummyEstimationProblem(3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);

        WeightedMeasurement wm1 = new WeightedMeasurement(1, 1, 2);
        WeightedMeasurement wm2 = new WeightedMeasurement(1, 2, 3);
        WeightedMeasurement wm3 = new WeightedMeasurement(1, 3, 4);
        problem.setMeasurements(new WeightedMeasurement[]{wm1, wm2, wm3});

        double rms = estimator.getRMS(problem);
        double expectedRMS = Math.sqrt(1.0 / 3.0 * (2 * 2 + 3 * 3 + 4 * 4));

        assertEquals(expectedRMS, rms, 1e-9);
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new DummyEstimationProblem(3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);

        WeightedMeasurement wm1 = new WeightedMeasurement(1, 1, 1);
        WeightedMeasurement wm2 = new WeightedMeasurement(1, 2, 2);
        WeightedMeasurement wm3 = new WeightedMeasurement(1, 3, 3);
        problem.setMeasurements(new WeightedMeasurement[]{wm1, wm2, wm3});

        double chiSquare = estimator.getChiSquare(problem);
        double expectedChiSquare = 1.0 / 3.0 * (1 * 1 + 2 * 2 + 3 * 3);

        assertEquals(expectedChiSquare, chiSquare, 1e-9);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem(3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);

        WeightedMeasurement wm1 = new WeightedMeasurement(1, 1, 1);
        WeightedMeasurement wm2 = new WeightedMeasurement(1, 2, 2);
        WeightedMeasurement wm3 = new WeightedMeasurement(1, 3, 3);
        problem.setMeasurements(new WeightedMeasurement[]{wm1, wm2, wm3});

        double[][] covariances = estimator.getCovariances(problem);
        double[][] expectedCovariances = {
                {1.0 / 3.0, -1.0 / 3.0, -1.0 / 3.0},
                {-1.0 / 3.0, 1.0 / 3.0, -1.0 / 3.0},
                {-1.0 / 3.0, -1.0 / 3.0, 1.0 / 3.0}
        };

        assertArrayEquals(expectedCovariances, covariances);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem(3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);

        WeightedMeasurement wm1 = new WeightedMeasurement(1, 1, 1);
        WeightedMeasurement wm2 = new WeightedMeasurement(1, 2, 2);
        WeightedMeasurement wm3 = new WeightedMeasurement(1, 3, 3);
        problem.setMeasurements(new WeightedMeasurement[]{wm1, wm2, wm3});

        double[] errors = estimator.guessParametersErrors(problem);
        double[] expectedErrors = {Math.sqrt(1.0 / 3.0), Math.sqrt(1.0 / 3.0), Math.sqrt(1.0 / 3.0)};

        assertArrayEquals(expectedErrors, errors);
    }

    @Test(expected = EstimationException.class)
    public void testGuessParametersErrorsWithNoDegreesOfFreedom() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem(3, 3);
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // empty method
            }
        };
        estimator.initializeEstimate(problem);

        problem.setMeasurements(new WeightedMeasurement[0]);

        estimator.guessParametersErrors(problem);
    }

    private static class DummyEstimationProblem implements EstimationProblem {
        private final int rows;
        private final int cols;
        private WeightedMeasurement[] measurements;
        private EstimatedParameter[] parameters;

        public DummyEstimationProblem(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
        }

        @Override
        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        @Override
        public void setMeasurements(WeightedMeasurement[] measurements) {
            this.measurements = measurements;
        }

        @Override
        public EstimatedParameter[] getAllParameters() {
            return parameters;
        }

        @Override
        public void setAllParameters(EstimatedParameter[] parameters) {
            this.parameters = parameters;
        }

        @Override
        public int getMeasurementsLength() {
            return rows;
        }

        @Override
        public int getAllParametersLength() {
            return cols;
        }

        @Override
        public double getMeasurementWeight(int index) {
            return 1;
        }

        @Override
        public double[] getMeasurementData(int index) {
            return new double[]{0, 0};
        }

        @Override
        public double[] getParameterEstimate(int index) {
            return new double[]{0};
        }

        @Override
        public double getParameterLowerBound(int index) {
            return -Double.MAX_VALUE;
        }

        @Override
        public double getParameterUpperBound(int index) {
            return Double.MAX_VALUE;
        }

        @Override
        public double getParameterStandardDeviation(int index) {
            return 0;
        }

        @Override
        public double getParameterConfidenceInterval(int index, double confidenceLevel) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterTTestStatistic(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterTTestPValue(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterChiSquareTestStatistic(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterChiSquareTestDegreesOfFreedom(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterChiSquareTestPValue(int index, double degreesOfFreedom) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterCorrelationCoefficient(int index, int otherIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterCorrelationCoefficientStandardError(int index, int otherIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterCorrelationCoefficientConfidenceInterval(int index, int otherIndex, double confidenceLevel) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterCorrelationCoefficientTTestStatistic(int index, int otherIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterCorrelationCoefficientTTestPValue(int index, int otherIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterPartialCorrelationCoefficient(int index, int otherIndex1, int otherIndex2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterPartialCorrelationCoefficientStandardError(int index, int otherIndex1, int otherIndex2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterPartialCorrelationCoefficientConfidenceInterval(int index, int otherIndex1, int otherIndex2, double confidenceLevel) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterPartialCorrelationCoefficientTTestStatistic(int index, int otherIndex1, int otherIndex2) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double getParameterPartialCorrelationCoefficientTTestPValue(int index, int otherIndex1, int otherIndex2) {
            throw new UnsupportedOperationException();
        }
    }
}