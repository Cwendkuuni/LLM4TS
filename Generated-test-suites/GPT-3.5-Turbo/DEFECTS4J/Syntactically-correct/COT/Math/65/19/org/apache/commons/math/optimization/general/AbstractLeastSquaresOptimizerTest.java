
import org.junit.Test;
import org.apache.commons.math.optimization.general.*;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxEvaluationsExceededException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.apache.commons.math.optimization.SimpleVectorialValueChecker;
import org.apache.commons.math.analysis.DifferentiableMultivariateVectorialFunction;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.linear.InvalidMatrixException;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.apache.commons.math.exception.LocalizedFormats;


public class AbstractLeastSquaresOptimizerTest {
  
  private class TestLeastSquaresOptimizer extends AbstractLeastSquaresOptimizer {
    protected TestLeastSquaresOptimizer() {
      super();
    }
    
    protected VectorialPointValuePair doOptimize() {
      return null;
    }
  }
  
  @Test
  public void testSetMaxIterations() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    optimizer.setMaxIterations(100);
    assert optimizer.getMaxIterations() == 100;
  }
  
  @Test
  public void testGetIterations() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    optimizer.setIterations(50);
    assert optimizer.getIterations() == 50;
  }
  
  @Test
  public void testSetMaxEvaluations() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    optimizer.setMaxEvaluations(1000);
    assert optimizer.getMaxEvaluations() == 1000;
  }
  
  @Test
  public void testGetEvaluations() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    optimizer.setEvaluations(500);
    assert optimizer.getEvaluations() == 500;
  }
  
  @Test
  public void testGetJacobianEvaluations() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    optimizer.setJacobianEvaluations(200);
    assert optimizer.getJacobianEvaluations() == 200;
  }
  
  @Test
  public void testSetConvergenceChecker() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
    optimizer.setConvergenceChecker(checker);
    assert optimizer.getConvergenceChecker() == checker;
  }
  
  @Test
  public void testGetRMS() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    double rms = optimizer.getRMS();
    assert rms == 0.0;
  }
  
  @Test
  public void testGetChiSquare() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    double chiSquare = optimizer.getChiSquare();
    assert chiSquare == 0.0;
  }
  
  @Test
  public void testGetCovariances() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    double[][] covariances = optimizer.getCovariances();
    assert covariances == null;
  }
  
  @Test
  public void testGuessParametersErrors() {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    double[] errors = optimizer.guessParametersErrors();
    assert errors == null;
  }
  
  @Test
  public void testOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
    TestLeastSquaresOptimizer optimizer = new TestLeastSquaresOptimizer();
    DifferentiableMultivariateVectorialFunction f = null;
    double[] target = null;
    double[] weights = null;
    double[] startPoint = null;
    VectorialPointValuePair pair = optimizer.optimize(f, target, weights, startPoint);
    assert pair == null;
  }
}
