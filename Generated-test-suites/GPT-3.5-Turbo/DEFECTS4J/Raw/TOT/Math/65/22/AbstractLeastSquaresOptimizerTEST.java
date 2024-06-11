import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};

        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);

        double expectedRMS = Math.sqrt((1*1 + 2*2 + 3*3 + 4*4 + 5*5) / 5);
        double actualRMS = optimizer.getRMS();

        assertEquals(expectedRMS, actualRMS, 0.0001);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};

        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);

        double expectedChiSquare = (1*1 + 2*2 + 3*3 + 4*4 + 5*5) / 1.0;
        double actualChiSquare = optimizer.getChiSquare();

        assertEquals(expectedChiSquare, actualChiSquare, 0.0001);
    }

    @Test
    public void testGuessParametersErrors() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};

        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);

        double[] expectedErrors = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] actualErrors = optimizer.guessParametersErrors();

        assertArrayEquals(expectedErrors, actualErrors, 0.0001);
    }
    
    @Test
    public void testOptimize() {
        AbstractLeastSquaresOptimizer optimizer = new ConcreteLeastSquaresOptimizer();
        double[] residuals = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] weights = {1.0, 1.0, 1.0, 1.0, 1.0};
        double[] startPoint = {0.0, 0.0, 0.0, 0.0, 0.0};

        optimizer.setResiduals(residuals);
        optimizer.setResidualsWeights(weights);

        VectorialPointValuePair result = optimizer.optimize(new TestFunction(), residuals, weights, startPoint);

        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertNotNull(result.getValue());
        assertNotNull(result.getResiduals());
        assertNotNull(result.getJacobian());
        assertNotNull(result.getCovariances());
    }
}

class ConcreteLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    @Override
    protected VectorialPointValuePair doOptimize() {
        // Concrete implementation of optimization algorithm
        return new VectorialPointValuePair(new double[]{1.0, 1.0, 1.0, 1.0, 1.0}, 0.0);
    }
}

class TestFunction implements DifferentiableMultivariateVectorialFunction {
    @Override
    public double[] value(double[] point) throws FunctionEvaluationException {
        // Test function implementation
        return new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
    }

    @Override
    public MultivariateMatrixFunction jacobian() {
        // Jacobian matrix
        return new TestJacobian();
    }
}

class TestJacobian implements MultivariateMatrixFunction {
    @Override
    public double[][] value(double[] point) throws FunctionEvaluationException {
        // Jacobian matrix implementation
        return new double[][]{{1.0, 0.0, 0.0, 0.0, 0.0},
                              {0.0, 1.0, 0.0, 0.0, 0.0},
                              {0.0, 0.0, 1.0, 0.0, 0.0},
                              {0.0, 0.0, 0.0, 1.0, 0.0},
                              {0.0, 0.0, 0.0, 0.0, 1.0}};
    }
}