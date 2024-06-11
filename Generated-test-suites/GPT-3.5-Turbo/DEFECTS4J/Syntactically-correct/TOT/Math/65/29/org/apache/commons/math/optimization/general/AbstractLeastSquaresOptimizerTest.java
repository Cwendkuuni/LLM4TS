
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.exception.LocalizedFormats;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.exception.MaxEvaluationsExceededException;
import org.apache.commons.math.exception.MaxIterationsExceededException;
import org.apache.commons.math.optimization.FunctionEvaluationException;
import org.apache.commons.math.optimization.general.AbstractLeastSquaresOptimizer.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testMaxIterationsExceeded() {
        double[] startPoint = { 1.0, 2.0, 3.0 };
        double[] target = { 4.0, 5.0, 6.0 };
        double[] weights = { 1.0, 1.0, 1.0 };

        DifferentiableMultivariateVectorialOptimizer optimizer = new DummyOptimizer();
        optimizer.setMaxIterations(5);

        Exception exception = assertThrows(OptimizationException.class, () ->
                optimizer.optimize(new DummyFunction(), target, weights, startPoint));

        assertEquals(LocalizedFormats.MAX_ITERATIONS_EXCEEDED, exception.getMessage());
    }

    @Test
    public void testMaxEvaluationsExceeded() {
        double[] startPoint = { 1.0, 2.0, 3.0 };
        double[] target = { 4.0, 5.0, 6.0 };
        double[] weights = { 1.0, 1.0, 1.0 };

        DifferentiableMultivariateVectorialOptimizer optimizer = new DummyOptimizer();
        optimizer.setMaxEvaluations(5);

        Exception exception = assertThrows(OptimizationException.class, () ->
                optimizer.optimize(new DummyFunction(), target, weights, startPoint));

        assertEquals(LocalizedFormats.MAX_EVALUATIONS_EXCEEDED, exception.getMessage());
    }

    @Test
    public void testDimensionsMismatch() {
        double[] startPoint = { 1.0, 2.0, 3.0 };
        double[] target = { 4.0, 5.0, 6.0 };
        double[] weights = { 1.0, 1.0 };

        DifferentiableMultivariateVectorialOptimizer optimizer = new DummyOptimizer();

        Exception exception = assertThrows(OptimizationException.class, () ->
                optimizer.optimize(new DummyFunction(), target, weights, startPoint));

        assertEquals(LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, exception.getMessage());
    }

    @Test
    public void testOptimize() {
        double[] startPoint = { 1.0, 2.0, 3.0 };
        double[] target = { 4.0, 5.0, 6.0 };
        double[] weights = { 1.0, 1.0, 1.0 };

        DifferentiableMultivariateVectorialOptimizer optimizer = new DummyOptimizer();

        VectorialPointValuePair result = optimizer.optimize(new DummyFunction(), target, weights, startPoint);

        assertNotNull(result);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getPoint(), 0.001);
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, result.getValue(), 0.001);
    }

    private static class DummyOptimizer extends AbstractLeastSquaresOptimizer {
        protected VectorialPointValuePair doOptimize()
                throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            return new VectorialPointValuePair(getStartPoint(), getObjectiveFunction().value(getStartPoint()));
        }
    }

    private static class DummyFunction implements DifferentiableMultivariateVectorialFunction {
        public double[] value(double[] params) {
            return new double[]{0.0, 0.0, 0.0};
        }

        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                public double[][] value(double[] params) throws IllegalArgumentException {
                    return new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}};
                }
            };
        }
    }
}

