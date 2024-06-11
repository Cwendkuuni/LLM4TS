package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;

import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testInitializeEstimate() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();

        estimator.initializeEstimate(problem);

        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertNull(estimator.jacobian);
        assertNull(estimator.residuals);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertEquals(Double.POSITIVE_INFINITY, estimator.cost);
    }

    @Test
    public void testUpdateJacobian() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);

        estimator.updateJacobian();

        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertNotNull(estimator.jacobian);
        assertArrayEquals(new double[0], estimator.jacobian);
        assertEquals(1, estimator.jacobianEvaluations);
        assertEquals(0, estimator.costEvaluations);
        assertEquals(0.0, estimator.cost);
    }

    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();

        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertNotNull(estimator.residuals);
        assertArrayEquals(new double[0], estimator.residuals);
        assertEquals(2, estimator.jacobianEvaluations);
        assertEquals(1, estimator.costEvaluations);
        assertNotEquals(0.0, estimator.cost);
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();

        double rms = estimator.getRMS(problem);

        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertEquals(2, estimator.jacobianEvaluations);
        assertEquals(2, estimator.costEvaluations);
        assertNotEquals(0.0, estimator.cost);
        assertEquals(rms, Math.sqrt(estimator.cost / problem.getMeasurements().length));
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);

        estimator.updateResidualsAndCost();

        double chiSquare = estimator.getChiSquare(problem);

        assertEquals(problem.getMeasurements().length, estimator.rows);
        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertEquals(2, estimator.jacobianEvaluations);
        assertEquals(2, estimator.costEvaluations);
        assertNotEquals(0.0, estimator.cost);
        assertEquals(chiSquare, estimator.cost);
    }

    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);

        estimator.updateJacobian();

        double[][] covariances = estimator.getCovariances(problem);

        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertEquals(problem.getAllParameters().length, covariances.length);
        assertEquals(problem.getAllParameters().length, covariances[0].length);
        assertEquals(2, estimator.jacobianEvaluations);
        assertEquals(2, estimator.costEvaluations);
        assertNotEquals(0.0, estimator.cost);
    }

    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();
        estimator.initializeEstimate(problem);

        estimator.updateJacobian();

        double[] errors = estimator.guessParametersErrors(problem);

        assertEquals(problem.getAllParameters().length, estimator.cols);
        assertArrayEquals(new double[0], errors);
        assertEquals(3, estimator.jacobianEvaluations);
        assertEquals(3, estimator.costEvaluations);
        assertNotEquals(0.0, estimator.cost);
    }

    @Test
    public void testEstimateThrowsException() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();

        assertThrows(EstimationException.class, () -> {
            estimator.estimate(problem);
        });

        assertEquals(0, estimator.costEvaluations);
        assertEquals(0, estimator.jacobianEvaluations);
        assertEquals(0.0, estimator.cost);
    }

    @Test
    public void testEstimateSuccess() throws EstimationException {
        EstimationProblem problem = new DummyEstimationProblem();
        AbstractEstimator estimator = new DummyEstimator();

        estimator.setMaxCostEval(10);
        estimator.estimate(problem);

        RealVector parameters = problem.getAllParameters();
        RealVector estimatedParameters = problem.getEstimatedParameters();

        assertEquals(1, estimator.costEvaluations);
        assertEquals(1, estimator.jacobianEvaluations);
        assertNotEquals(0.0, estimator.cost);
        assertArrayEquals(estimatedParameters.toArray(), parameters.toArray());
    }

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new DummyEstimator();

        estimator.setMaxCostEval(10);
        int maxCostEval = estimator.getMaxCostEval();

        assertEquals(10, maxCostEval);
    }

    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new DummyEstimator();

        estimator.updateResidualsAndCost();
        estimator.updateResidualsAndCost();

        int costEvaluations = estimator.getCostEvaluations();

        assertEquals(2, costEvaluations);
    }

    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new DummyEstimator();

        estimator.updateJacobian();
        estimator.updateJacobian();

        int jacobianEvaluations = estimator.getJacobianEvaluations();

        assertEquals(2, jacobianEvaluations);
    }

    /**
     * Dummy implementation of {@link EstimationProblem} interface for testing purposes.
     */
    private static class DummyEstimationProblem implements EstimationProblem {

        private static final double EPSILON = 1.0e-10;

        private final WeightedMeasurement[] measurements;
        private final EstimatedParameter[] parameters;

        public DummyEstimationProblem() {
            measurements = new WeightedMeasurement[] {
                    new WeightedMeasurement(0.0, 2.0, 1.0),
                    new WeightedMeasurement(1.0, 3.0, 2.0),
                    new WeightedMeasurement(2.0, 5.0, 3.0)
            };
            parameters = new EstimatedParameter[] {
                    new EstimatedParameter("p1", 0.0),
                    new EstimatedParameter("p2", 0.0)
            };
        }

        public WeightedMeasurement[] getMeasurements() {
            return measurements;
        }

        public EstimatedParameter[] getAllParameters() {
            return parameters;
        }

        public WeightedMeasurement getMeasurement(int index) {
            return measurements[index];
        }

        public EstimatedParameter getParameter(String name) {
            int index = Arrays.asList(parameters).indexOf(p -> p.getName().equals(name));
            return parameters[index];
        }

        public double getWeight(int index) {
            return measurements[index].getWeight();
        }

        public double getResidual(int index) {
            return measurements[index].getResidual();
        }

        public double[] getPartial(int index, double[] parameters) {
            return new double[] { measurements[index].getPartial(parameters[index]) };
        }

        public RealMatrix getJacobian(double[] parameters) throws EstimationException {
            throw new UnsupportedOperationException();
        }

        public RealMatrix getJacobianTranspose(double[] parameters) throws EstimationException {
            throw new UnsupportedOperationException();
        }

        public double[] getErrorCovariance(double[] parameters) throws EstimationException {
            throw new UnsupportedOperationException();
        }

        public double getChiSquare(double[] parameters) throws EstimationException {
            throw new UnsupportedOperationException();
        }

        public double getChiSquareReduction(double[] parameters) throws EstimationException {
            throw new UnsupportedOperationException();
        }

        public RealVector getEstimatedParameters() {
            return new Array2DRowRealMatrix(new double[][] { { 0.0, 0.0 } }).getColumnVector(0);
        }

        public RealVector getParameters() {
            return new Array2DRowRealMatrix(new double[][] { { 0.0, 0.0 } }).getColumnVector(0);
        }

        public double getRootMeanSquare() {
            throw new UnsupportedOperationException();
        }

        public double getRMSGradientNorm() {
            throw new UnsupportedOperationException();
        }

        public double getChiSquareGradientNorm() {
            throw new UnsupportedOperationException();
        }

        public double getLogLikelihood() {
            throw new UnsupportedOperationException();
        }

        public double getMaximumLikelihoodEstimate() {
            throw new UnsupportedOperationException();
        }

        public double getLogLikelihoodEvaluation(double[] parameters) {
            throw new UnsupportedOperationException();
        }

        public double getLogLikelihoodReduction(double[] parameters) {
            throw new UnsupportedOperationException();
        }

        public double getLogLikelihoodMaximum() {
            throw new UnsupportedOperationException();
        }

        public double getLogLikelihoodReductionAtMaximum() {
            throw new UnsupportedOperationException();
        }

        public double getEss() {
            throw new UnsupportedOperationException();
        }

        public double getAkaikeInfoCriterion() {
            throw new UnsupportedOperationException();
        }

        public double getBayesInfoCriterion() {
            throw new UnsupportedOperationException();
        }

        public double getCriticalValue(double significanceLevel, int degreesOfFreedom) {
            throw new UnsupportedOperationException();
        }

        public boolean isConverged() {
            throw new UnsupportedOperationException();
        }

        public double getParameterError(int index) {
            throw new UnsupportedOperationException();
        }

        public Pair<Double, Double> getConfidenceInterval(int index, double confidenceLevel) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Dummy implementation of {@link WeightedMeasurement} for testing purposes.
     */
    private static class DummyWeightedMeasurement implements WeightedMeasurement {

        private final double weight;
        private final double residual;
        private final double[] partials;

        public DummyWeightedMeasurement(double weight, double residual) {
            this.weight = weight;
            this.residual = residual;
            this.partials = new double[1];
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public double getResidual() {
            return residual;
        }

        @Override
        public double[] getPartial(int index) {
            partials[0] = getPartial(parameters[index]);
            return partials;
        }

        private double getPartial(double parameter) {
            return Math.pow(parameter, 2);
        }

        @Override
        public void setParameter(int index, double parameter) {
            // empty
        }

        @Override
        public String getDescription() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Dummy implementation of {@link EstimatedParameter} for testing purposes.
     */
    private static class DummyEstimatedParameter implements EstimatedParameter {

        private final String name;
        private final double value;

        public DummyEstimatedParameter(String name, double value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double getValue() {
            return value;
        }

        @Override
        public void setValue(double value) {
            this.value = value;
        }
    }
}