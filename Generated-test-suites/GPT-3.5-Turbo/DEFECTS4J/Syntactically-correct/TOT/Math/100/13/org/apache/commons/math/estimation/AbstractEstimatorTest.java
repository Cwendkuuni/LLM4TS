import org.apache.commons.math.estimation.AbstractEstimator;
import org.apache.commons.math.estimation.EstimationException;
import org.apache.commons.math.estimation.EstimationProblem;
import org.apache.commons.math.estimation.WeightedMeasurement;
import org.apache.commons.math.estimation.EstimatedParameter;
import org.junit.Assert;
import org.junit.Test;

public class AbstractEstimatorTest {

    @Test
    public void testSetAndGetMaxCostEval() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        estimator.setMaxCostEval(100);
        int maxCostEval = estimator.getMaxCostEval();
        
        Assert.assertEquals(100, maxCostEval);
    }
    
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        int costEvaluations = estimator.getCostEvaluations();
        
        Assert.assertEquals(0, costEvaluations);
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        
        Assert.assertEquals(0, jacobianEvaluations);
    }
    
    @Test
    public void testUpdateJacobian() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        estimator.updateJacobian();
        int jacobianEvaluations = estimator.getJacobianEvaluations();
        
        Assert.assertEquals(1, jacobianEvaluations);
    }
    
    @Test
    public void testUpdateResidualsAndCost() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        estimator.updateResidualsAndCost();
        int costEvaluations = estimator.getCostEvaluations();
        
        Assert.assertEquals(1, costEvaluations);
    }
    
    @Test
    public void testGetRMS() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = { new WeightedMeasurement(0, 1), new WeightedMeasurement(1, 2) };
        problem.setMeasurements(measurements);
        
        double rms = estimator.getRMS(problem);
        
        Assert.assertEquals(Math.sqrt(1.5), rms, 0.00001);
    }
    
    @Test
    public void testGetChiSquare() {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = { new WeightedMeasurement(0, 1), new WeightedMeasurement(1, 2) };
        problem.setMeasurements(measurements);
        
        double chiSquare = estimator.getChiSquare(problem);
        
        Assert.assertEquals(5, chiSquare, 0.00001);
    }
    
    @Test
    public void testGetCovariances() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = { new WeightedMeasurement(0, 1), new WeightedMeasurement(1, 2) };
        problem.setMeasurements(measurements);
        EstimatedParameter[] parameters = { new EstimatedParameter(0, 1), new EstimatedParameter(1, 2) };
        problem.setAllParameters(parameters);
        
        double[][] covariances = estimator.getCovariances(problem);
        
        Assert.assertEquals(2.0/3.0, covariances[0][0], 0.00001);
    }
    
    @Test
    public void testGuessParametersErrors() throws EstimationException {
        AbstractEstimator estimator = new AbstractEstimator() {
            @Override
            public void estimate(EstimationProblem problem) throws EstimationException {}
        };
        
        EstimationProblem problem = new EstimationProblem();
        WeightedMeasurement[] measurements = { new WeightedMeasurement(0, 1), new WeightedMeasurement(1, 2) };
        problem.setMeasurements(measurements);
        EstimatedParameter[] parameters = { new EstimatedParameter(0, 1), new EstimatedParameter(1, 2) };
        problem.setAllParameters(parameters);
        
        double[] errors = estimator.guessParametersErrors(problem);
        
        Assert.assertEquals(Math.sqrt(2.0/3.0) * Math.sqrt(5), errors[0], 0.00001);
    }
}