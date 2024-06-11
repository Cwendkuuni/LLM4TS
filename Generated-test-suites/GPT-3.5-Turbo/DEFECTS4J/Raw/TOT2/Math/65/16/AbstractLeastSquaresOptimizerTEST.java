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
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;
        double expected = Math.sqrt((1.0 + 4.0 + 9.0) / 3.0);
        assertEquals(expected, optimizer.getRMS(), 0.0001);
    }

    @Test
    public void testGetCovariances() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[][] jTj = {{1.0, 0.5}, {0.5, 2.0}};
        optimizer.jacobian = jTj;
        double[][] expectedCovariances = {{2.0, -0.5}, {-0.5, 0.5}};
        assertArrayEquals(expectedCovariances, optimizer.getCovariances());
    }

    @Test
    public void testOptimize() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] target = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        double[] start = {0.0, 0.0, 0.0};
        try {
            VectorialPointValuePair result = optimizer.optimize(new LinearMultivariateVectorialFunction(),
                    target, weights, start);
            assertNotNull(result);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            fail("Exception thrown");
        }
    }

}