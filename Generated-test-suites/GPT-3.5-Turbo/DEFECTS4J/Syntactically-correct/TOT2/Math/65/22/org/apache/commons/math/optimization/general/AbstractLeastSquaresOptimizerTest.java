import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testSetMaxIterations() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        optimizer.setMaxIterations(200);
        assertEquals(200, optimizer.getMaxIterations());
    }

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double rms = optimizer.calculateRMS(residuals, weights);
        assertEquals(2.160246899469287, rms, 1e-10);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double chiSquare = optimizer.calculateChiSquare(residuals, weights);
        assertEquals(14.0, chiSquare, 1e-10);
    }

}