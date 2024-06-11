import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractEstimatorTest {

    @Test
    public void testSetMaxCostEval() {
        AbstractEstimator estimator = new ConcreteEstimator(); // Assuming ConcreteEstimator extends AbstractEstimator
        estimator.setMaxCostEval(100);
        assertEquals(100, estimator.getMaxCostEval());
    }

    @Test
    public void testGetRMS() {
        EstimationProblem problem = new EstimationProblem();
        AbstractEstimator estimator = new ConcreteEstimator(); // Assuming ConcreteEstimator extends AbstractEstimator
        double rms = estimator.getRMS(problem);
        // Add assert statements to verify the RMS value
    }

    @Test
    public void testGetChiSquare() {
        EstimationProblem problem = new EstimationProblem();
        AbstractEstimator estimator = new ConcreteEstimator(); // Assuming ConcreteEstimator extends AbstractEstimator
        double chiSquare = estimator.getChiSquare(problem);
        // Add assert statements to verify the Chi-Square value
    }

    @Test
    public void testGuessParametersErrors() {
        EstimationProblem problem = new EstimationProblem();
        AbstractEstimator estimator = new ConcreteEstimator(); // Assuming ConcreteEstimator extends AbstractEstimator
        double[] errors = estimator.guessParametersErrors(problem);
        // Add assert statements to verify the errors in estimated parameters
    }

    // Add more test cases for other public methods in AbstractEstimator

}