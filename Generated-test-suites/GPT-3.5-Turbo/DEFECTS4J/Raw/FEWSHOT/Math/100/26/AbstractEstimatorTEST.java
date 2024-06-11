// Test for AbstractEstimator

package org.apache.commons.math.estimation;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbstractEstimatorTest {
   
    private AbstractEstimator estimator;
    
    @Before
    public void setUp() {
        estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
    }
    
    @Test
    public void testSetMaxCostEval() {
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.maxCostEval);
    }
    
    @Test
    public void testGetCostEvaluations() {
        estimator.costEvaluations = 50;
        assertEquals(50, estimator.getCostEvaluations());
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        estimator.jacobianEvaluations = 25;
        assertEquals(25, estimator.getJacobianEvaluations());
    }
    
    @Test
    public void testUpdateJacobian() {
        // Not tested as it doesn't return any value
    }
    
    @Test
    public void testIncrementJacobianEvaluationsCounter() {
        estimator.jacobianEvaluations = 10;
        estimator.incrementJacobianEvaluationsCounter();
        assertEquals(11, estimator.jacobianEvaluations);
    }
    
    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        estimator.costEvaluations = 50;
        estimator.maxCostEval = 100;
        estimator.updateResidualsAndCost();
        assertEquals(51, estimator.costEvaluations);
        
        // test for exception
        EstimationException ex = null;
        estimator.maxCostEval = 50;
        try {
            estimator.updateResidualsAndCost();
        } catch(EstimationException e) {
            ex = e;
        }
        assertNotNull(ex);
    }
    
    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement();
        wm1.setResidual(10);
        wm1.setWeight(2);
        WeightedMeasurement wm2 = new WeightedMeasurement();
        wm2.setResidual(20);
        wm2.setWeight(2);
        problem.setMeasurements(new WeightedMeasurement[] {wm1, wm2});
        assertEquals(Math.sqrt((10*10 + 20*20) / 2), estimator.getRMS(problem), 0.0001);
    }
    
    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement();
        wm1.setResidual(10);
        wm1.setWeight(2);
        WeightedMeasurement wm2 = new WeightedMeasurement();
        wm2.setResidual(20);
        wm2.setWeight(2);
        problem.setMeasurements(new WeightedMeasurement[] {wm1, wm2});
        assertEquals((10*10/2 + 20*20/2), estimator.getChiSquare(problem), 0.0001);
    }
    
    @Test
    public void testGetCovariances() throws EstimationException {
        EstimationProblem problem = new EstimationProblem();
        EstimatedParameter param1 = new EstimatedParameter();
        param1.value = 1;
        EstimatedParameter param2 = new EstimatedParameter();
        param2.value = 2;
        problem.setAllParameters(new EstimatedParameter[] {param1, param2});
        WeightedMeasurement wm1 = new WeightedMeasurement();
        wm1.setResidual(10);
        wm1.setWeight(2);
        wm1.setPartial(0, 1);
        wm1.setPartial(1, 2);
        WeightedMeasurement wm2 = new WeightedMeasurement();
        wm2.setResidual(20);
        wm2.setWeight(2);
        wm2.setPartial(0, 3);
        wm2.setPartial(1, 4);
        problem.setMeasurements(new WeightedMeasurement[] {wm1, wm2});
        double[][] covariances = estimator.getCovariances(problem);
        assertEquals(2, covariances.length);
        assertEquals(2, covariances[0].length);
        assertEquals(1, covariances[0][0], 0.0001);
        assertEquals(0, covariances[0][1], 0.0001);
        assertEquals(0, covariances[1][0], 0.0001);
        assertEquals(0, covariances[1][1], 0.0001);
    }
    
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement wm1 = new WeightedMeasurement();
        wm1.setResidual(10);
        wm1.setWeight(2);
        WeightedMeasurement wm2 = new WeightedMeasurement();
        wm2.setResidual(20);
        wm2.setWeight(2);
        problem.setMeasurements(new WeightedMeasurement[] {wm1, wm2});
        EstimatedParameter param1 = new EstimatedParameter();
        param1.value = 1;
        EstimatedParameter param2 = new EstimatedParameter();
        param2.value = 2;
        problem.setAllParameters(new EstimatedParameter[] {param1, param2});
        double[] errors = estimator.guessParametersErrors(problem);
        assertEquals(2, errors.length);
        assertEquals(Math.sqrt((10*10 + 20*20) / 2), errors[0], 0.0001);
        assertEquals(Math.sqrt((10*10 + 20*20) / 2), errors[1], 0.0001);
        
        // test for exception
        EstimationException ex = null;
        problem.setMeasurements(new WeightedMeasurement[] {});
        try {
            estimator.guessParametersErrors(problem);
        } catch(EstimationException e) {
            ex = e;
        }
        assertNotNull(ex);
    }

}