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
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;
        assertEquals(2.160246899469287, optimizer.getRMS(), 1e-10);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0};
        double[] weights = {1.0, 1.0, 1.0};
        optimizer.residuals = residuals;
        optimizer.residualsWeights = weights;
        assertEquals(14.0, optimizer.getChiSquare(), 1e-10);
    }

    @Test
    public void testOptimize() throws Exception {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] startPoint = {1.0, 1.0};
        double[] target = {2.0, 3.0};
        double[] weights = {1.0, 1.0};
        TestVectorialFunction function = new TestVectorialFunction();
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        assertEquals(0.0, result.getValue(), 1e-10);
    }
}

class ConcreteLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    protected VectorialPointValuePair doOptimize() {
        // Implement optimization algorithm for testing purposes
        return new VectorialPointValuePair(point, cost);
    }
}

class TestVectorialFunction implements DifferentiableMultivariateVectorialFunction {
    public double[] value(double[] point) {
        return new double[]{0.0, 0.0};
    }

    public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {
            public double[][] value(double[] point) {
                return new double[][]{{1.0, 0.0}, {0.0, 1.0}};
            }
        };
    }
}