
import org.junit.Test;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxEvaluationsExceededException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.analysis.MultivariateMatrixFunction;
import org.apache.commons.math.exception.LocalizedFormats;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testGetRMS() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        double expectedRMS = 0;
        double actualRMS = optimizer.getRMS();
        assertEquals(expectedRMS, actualRMS, 0);
    }

    @Test
    public void testGetChiSquare() {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        double expectedChiSquare = 0;
        double actualChiSquare = optimizer.getChiSquare();
        assertEquals(expectedChiSquare, actualChiSquare, 0);
    }

    @Test
    public void testGetCovariances() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        double[][] expectedCovariances = null;
        double[][] actualCovariances = optimizer.getCovariances();
        assertArrayEquals(expectedCovariances, actualCovariances);
    }

    @Test
    public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
        AbstractLeastSquaresOptimizer optimizer = new TestOptimizer();
        double[] expectedErrors = null;
        double[] actualErrors = optimizer.guessParametersErrors();
        assertArrayEquals(expectedErrors, actualErrors, 0);
    }

    private static class TestOptimizer extends AbstractLeastSquaresOptimizer {
        protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            return null;
        }
    }
}
