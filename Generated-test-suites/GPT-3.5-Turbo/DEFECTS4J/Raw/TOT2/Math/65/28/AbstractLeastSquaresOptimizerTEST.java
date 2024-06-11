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
        double[] weights = {0.5, 0.3, 0.2};
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;
        double expectedRMS = Math.sqrt((1.0 * 0.5 + 4.0 * 0.3 + 9.0 * 0.2) / 3);
        assertEquals(expectedRMS, optimizer.getRMS(), 1e-15);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {0.5, 0.3, 0.2};
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;
        double expectedChiSquare = 1.0 / 0.5 + 4.0 / 0.3 + 9.0 / 0.2;
        assertEquals(expectedChiSquare, optimizer.getChiSquare(), 1e-15);
    }

    // Add more test cases for other methods as needed

}