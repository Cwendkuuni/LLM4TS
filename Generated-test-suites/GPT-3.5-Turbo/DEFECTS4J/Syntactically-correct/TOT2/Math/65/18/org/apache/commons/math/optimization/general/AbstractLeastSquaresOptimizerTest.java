import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.setMaxIterations(200);
        assertEquals(200, optimizer.getMaxIterations());
    }

    @Test
    public void testSetMaxEvaluations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.setMaxEvaluations(500);
        assertEquals(500, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);
        double expectedRMS = Math.sqrt((1.0 + 4.0 + 9.0) / 3);
        assertEquals(expectedRMS, optimizer.getRMS(), 0.0001);
    }

    // Add more test cases for other methods

}