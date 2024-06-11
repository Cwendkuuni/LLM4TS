import org.junit.Test;
import org.apache.commons.math.estimation.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.setMaxCostEval(1000);
        assert(estimator.getMaxCostEval() == 1000);
    }
    
    @Test
    public void testGetCostEvaluations() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.incrementCostEvaluationsCounter();
        assert(estimator.getCostEvaluations() == 1);
    }
    
    @Test
    public void testGetJacobianEvaluations() {
        AbstractEstimator estimator = new TestEstimator();
        estimator.incrementJacobianEvaluationsCounter();
        assert(estimator.getJacobianEvaluations() == 1);
    }
    
    @Test
    public void testUpdateJacobian() {
        // To be implemented
    }
    
    @Test
    public void testUpdateResidualsAndCost() {
        // To be implemented
    }
    
    @Test
    public void testGetRMS() {
        // To be implemented
    }

    @Test
    public void testGetChiSquare() {
        // To be implemented
    }
    
    @Test
    public void testGetCovariances() {
        // To be implemented
    }

    @Test
    public void testGuessParametersErrors() {
        // To be implemented
    }
    
    @Test
    public void testInitializeEstimate() {
        // To be implemented
    }

    @Test
    public void testEstimate() {
        // To be implemented
    }
}