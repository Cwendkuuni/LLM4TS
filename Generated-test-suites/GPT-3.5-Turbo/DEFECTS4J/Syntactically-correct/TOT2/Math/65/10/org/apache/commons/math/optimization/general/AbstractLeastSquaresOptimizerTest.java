import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGetRMS() {
        // Test when residualsWeights are all 1
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        optimizer.residualsWeights = new double[]{1, 1, 1};
        optimizer.residuals = new double[]{1, 2, 3};
        double expectedRMS = Math.sqrt((1*1 + 2*2 + 3*3) / 3);
        assertEquals(expectedRMS, optimizer.getRMS(), 1e-15);
    }

    @Test
    public void testGetChiSquare() {
        // Test when residualsWeights are all 1
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        optimizer.residualsWeights = new double[]{1, 1, 1};
        optimizer.residuals = new double[]{1, 2, 3};
        double expectedChiSquare = (1*1 + 2*2 + 3*3);
        assertEquals(expectedChiSquare, optimizer.getChiSquare(), 1e-15);
    }

    @Test
    public void testGuessParametersErrors() {
        // Test when rows > cols
        AbstractLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
        optimizer.function = new TestDifferentiableMultivariateVectorialFunction();
        double[] errors = optimizer.guessParametersErrors();

        // Check if errors array is not null and has correct length
        assertNotNull(errors);
        assertEquals(optimizer.cols, errors.length);
    }

    // Private inner class for testing purposes
    private class TestLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
        @Override
        protected VectorialPointValuePair doOptimize() {
            return null; // Not needed for testing
        }
    }

    // Private inner class for testing purposes
    private class TestDifferentiableMultivariateVectorialFunction
            implements DifferentiableMultivariateVectorialFunction {
        @Override
        public double[] value(double[] point) {
            return new double[0]; // Not needed for testing
        }

        @Override
        public MultivariateMatrixFunction jacobian() {
            return null; // Not needed for testing
        }
    }

}