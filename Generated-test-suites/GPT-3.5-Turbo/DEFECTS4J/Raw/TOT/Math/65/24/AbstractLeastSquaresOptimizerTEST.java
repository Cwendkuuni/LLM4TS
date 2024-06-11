import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.OptimizationException;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.analysis.MultivariateMatrixFunction;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

  private DifferentiableMultivariateVectorialFunction function;
  private double[] target;
  private double[] weights;
  private double[] startPoint;

  @Before
  public void setUp() {
    function = new DifferentiableMultivariateVectorialFunction() {
      @Override
      public double[] value(double[] point) throws FunctionEvaluationException {
        double[] result = new double[2];
        result[0] = point[0] + 2 * point[1] + 3 * point[2];
        result[1] = 4 * point[1] + 5 * point[2] + 6 * point[3];
        return result;
      }

      @Override
      public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {
          @Override
          public double[][] value(double[] point) throws FunctionEvaluationException {
            double[][] result = new double[2][4];
            result[0][0] = 1.0;
            result[0][1] = 2.0;
            result[0][2] = 3.0;
            result[0][3] = 0.0;
            result[1][0] = 0.0;
            result[1][1] = 4.0;
            result[1][2] = 5.0;
            result[1][3] = 6.0;
            return result;
          }
        };
      }
    };

    target = new double[] {4.0, 7.0};
    weights = new double[] {1.0, 1.0};
    startPoint = new double[] {0.0, 0.0, 0.0, 0.0};
  }

  @Test
  public void testOptimize() throws FunctionEvaluationException, OptimizationException {
    DifferentiableMultivariateVectorialOptimizer optimizer = new MyOptimizer();
    VectorialPointValuePair result = optimizer.optimize(function, target, weights, startPoint);
    double[] point = result.getPoint();
    double[] value = function.value(point);

    assertEquals(1.0, point[0], 1e-6);
    assertEquals(1.0, point[1], 1e-6);
    assertEquals(1.0, point[2], 1e-6);
    assertEquals(-1.0, point[3], 1e-6);

    assertEquals(4.0, value[0], 1e-6);
    assertEquals(7.0, value[1], 1e-6);
  }

  @Test(expected = OptimizationException.class)
  public void testOptimizeInvalidDimensions() throws FunctionEvaluationException, OptimizationException {
    DifferentiableMultivariateVectorialOptimizer optimizer = new MyOptimizer();
    double[] invalidTarget = new double[] {4.0};
    optimizer.optimize(function, invalidTarget, weights, startPoint);
  }

  private static class MyOptimizer extends AbstractLeastSquaresOptimizer {
    @Override
    protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException {
      return null; // Not implemented for testing purposes
    }
  }

}