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
import org.apache.commons.math.optimization.VectorialPointValuePair;
import static org.junit.Assert.*;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {

    private class DummyDifferentiableMultivariateVectorialFunction implements DifferentiableMultivariateVectorialFunction {
        
        public double[] value(double[] point) throws FunctionEvaluationException {
            return new double[]{point[0] * point[0], point[1] * point[1]};
        }

        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                public double[][] value(double[] point) throws FunctionEvaluationException {
                    return new double[][]{{2 * point[0], 0}, {0, 2 * point[1]}};
                }
            };
        }

    }

    private class DummyLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {

        @Override
        protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            iterate(new DummyDifferentiableMultivariateVectorialFunction());
            return new VectorialPointValuePair(getPoint(), getObjective());
        }

    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        DummyLeastSquaresOptimizer optimizer = new DummyLeastSquaresOptimizer();
        VectorialPointValuePair result = optimizer.optimize(new DummyDifferentiableMultivariateVectorialFunction(), new double[]{1, 2}, new double[]{1, 1}, new double[]{1, 1});
        assertEquals(0.0, result.getValue()[0], 1e-6);
        assertEquals(0.0, result.getValue()[1], 1e-6);
        assertEquals(0.0, optimizer.getCost(), 1e-6);
    }
}