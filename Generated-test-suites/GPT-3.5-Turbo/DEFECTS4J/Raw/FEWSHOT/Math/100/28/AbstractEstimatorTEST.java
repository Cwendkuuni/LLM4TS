package org.apache.commons.math.estimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AbstractEstimatorTest {
    private static final double EPSILON = 1e-10;
    
    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = createEstimator();
        estimator.setMaxCostEval(10);
        assertEquals(10, estimator.getMaxCostEval());
    }
    
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = createEstimator();
        estimator.setCostEvaluations(5);
        assertEquals(5, estimator.getCostEvaluations());
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = createEstimator();
        estimator.setJacobianEvaluations(3);
        assertEquals(3, estimator.getJacobianEvaluations());
    }
    
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = createEstimator();
        estimator.updateJacobian();
        assertNotNull(estimator.getJacobian());
    }
    
    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = createEstimator();
        estimator.updateResidualsAndCost();
        assertNotNull(estimator.getResiduals());
        assertEquals(1, estimator.getCost(), EPSILON);
    }
    
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem();
        double rms = estimator.getRMS(problem);
        assertEquals(0, rms, EPSILON);
    }
    
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem();
        double chiSquare = estimator.getChiSquare(problem);
        assertEquals(0, chiSquare, EPSILON);
    }
    
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem();
        double[][] covariances = estimator.getCovariances(problem);
        assertNotNull(covariances);
    }
    
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem();
        double[] errors = estimator.guessParametersErrors(problem);
        assertNotNull(errors);
    }
    
    @Test(expected=EstimationException.class)
    public void testGuessParametersErrorsNoDegreesOfFreedom() throws EstimationException {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem(3, 3);
        estimator.guessParametersErrors(problem);
    }
    
    @Test
    public void testInitializeEstimate() {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem();
        estimator.initializeEstimate(problem);
        assertNotNull(estimator.getMeasurements());
        assertNotNull(estimator.getParameters());
        assertNotNull(estimator.getJacobian());
        assertNotNull(estimator.getResiduals());
        assertEquals(Double.POSITIVE_INFINITY, estimator.getCost(), EPSILON);
    }
    
    @Test
    public void testEstimate() throws EstimationException {
        AbstractEstimator estimator = createEstimator();
        EstimationProblem problem = createProblem();
        estimator.estimate(problem);
        double[] parameters = problem.getAllParameters();
        assertNotNull(parameters);
    }
    
    private AbstractEstimator createEstimator() {
        return new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {
                // Do nothing
            }
        };
    }
    
    private EstimationProblem createProblem() {
        return createProblem(1, 1);
    }
    
    private EstimationProblem createProblem(int numMeasurements, int numParameters) {
        WeightedMeasurement[] measurements = new WeightedMeasurement[numMeasurements];
        EstimatedParameter[] parameters = new EstimatedParameter[numParameters];
        return new EstimationProblem(measurements, parameters);
    }
}