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

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    private AbstractLeastSquaresOptimizer optimizer;
    private double[] target;
    private double[] weights;
    private double[] startPoint;
    private DifferentiableMultivariateVectorialFunction function;
    private MultivariateMatrixFunction jacobian;
    
    @Before
    public void setUp() {
        optimizer = new AbstractLeastSquaresOptimizerImpl();

        target = new double[]{1.0, 2.0, 3.0};
        weights = new double[]{1.0, 1.0, 1.0};
        startPoint = new double[]{0.0, 0.0, 0.0};

        function = new DifferentiableMultivariateVectorialFunction() {
            public double[] value(double[] point) {
                return new double[]{point[0], point[1], point[2]};
            }
            public MultivariateMatrixFunction jacobian() {
                return new MultivariateMatrixFunction() {
                    public double[][] value(double[] point) {
                        return new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
                    }
                };
            }
        };
    }

    @Test
    public void testOptimize() throws FunctionEvaluationException, OptimizationException {
        VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
        double[] expectedPoint = new double[]{1.0, 2.0, 3.0};
        double[] expectedValue = new double[]{1.0, 2.0, 3.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 0.0001);
        assertArrayEquals(expectedValue, result.getValue(), 0.0001);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxIterationsExceeded() throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxIterations(5);
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testMaxEvaluationsExceeded() throws FunctionEvaluationException, OptimizationException {
        optimizer.setMaxEvaluations(5);
        optimizer.optimize(function, target, weights, startPoint);
    }

    @Test(expected = OptimizationException.class)
    public void testDimensionsMismatch() throws FunctionEvaluationException, OptimizationException {
        function = new DifferentiableMultivariateVectorialFunction() {
            public double[] value(double[] point) {
                return new double[]{point[0], point[1]};
            }
            public MultivariateMatrixFunction jacobian() {
                return new MultivariateMatrixFunction() {
                    public double[][] value(double[] point) {
                        return new double[][]{{1, 0}, {0, 1}, {0, 0}};
                    }
                };
            }
        };
        optimizer.optimize(function, target, weights, startPoint);
    }
    
    private class AbstractLeastSquaresOptimizerImpl extends AbstractLeastSquaresOptimizer {
        protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
            updateJacobian();
            updateResidualsAndCost();
            throw new OptimizationException();
        }
    }

}