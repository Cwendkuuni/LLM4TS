import org.junit.Test;
import org.junit.Assert;
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

public class AbstractLeastSquaresOptimizerTest {

  @Test
  public void testGetMaxIterations() {
    AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
      protected VectorialPointValuePair doOptimize() {
        return null;
      }
    };

    optimizer.setMaxIterations(100);
    Assert.assertEquals(100, optimizer.getMaxIterations());
  }

  @Test
  public void testGetConvergenceChecker() {
    AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
      protected VectorialPointValuePair doOptimize() {
        return null;
      }
    };

    VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
    optimizer.setConvergenceChecker(checker);
    Assert.assertEquals(checker, optimizer.getConvergenceChecker());
  }
  
  @Test
  public void testGetIterations() {
    AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
      protected VectorialPointValuePair doOptimize() {
        return null;
      }
    };

    optimizer.incrementIterationsCounter();
    Assert.assertEquals(1, optimizer.getIterations());
  }

  @Test(expected = OptimizationException.class)
  public void testIncrementIterationsCounter() {
    AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
      protected VectorialPointValuePair doOptimize() {
        return null;
      }
    };

    optimizer.setMaxIterations(1);
    optimizer.incrementIterationsCounter();
    optimizer.incrementIterationsCounter();
  }

  @Test
  public void testGuessParametersErrors() throws FunctionEvaluationException, OptimizationException {
    AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
      protected VectorialPointValuePair doOptimize() {
        return null;
      }
    };

    double[] target = {1, 2, 3};
    double[] weights = {1, 1, 1};
    double[] startPoint = {1, 1, 1};

    double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    optimizer.function = new DifferentiableMultivariateVectorialFunction() {
      public double[] value(double[] point) {
        return target;
      }

      public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {
          public double[][] value(double[] point) {
            return covariances;
          }
        };
      }
    };

    double[] errors = optimizer.guessParametersErrors();
    Assert.assertArrayEquals(new double[]{1, 1, 1}, errors, 0.0001);
  }

  @Test(expected = OptimizationException.class)
  public void testGuessParametersErrorsNotEnoughDegreesOfFreedom() throws FunctionEvaluationException, OptimizationException {
    AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer() {
      protected VectorialPointValuePair doOptimize() {
        return null;
      }
    };

    double[] target = {1, 2, 3};
    double[] weights = {1, 1, 1};
    double[] startPoint = {1, 1, 1};

    double[][] covariances = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    optimizer.function = new DifferentiableMultivariateVectorialFunction() {
      public double[] value(double[] point) {
        return target;
      }

      public MultivariateMatrixFunction jacobian() {
        return new MultivariateMatrixFunction() {
          public double[][] value(double[] point) {
            return covariances;
          }
        };
      }
    };

    optimizer.rows = 4;
    optimizer.cols = 4;

    optimizer.guessParametersErrors();
  }

}