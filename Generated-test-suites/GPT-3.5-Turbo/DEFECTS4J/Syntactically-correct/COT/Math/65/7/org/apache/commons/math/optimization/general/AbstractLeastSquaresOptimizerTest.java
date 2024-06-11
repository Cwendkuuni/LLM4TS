
import static org.junit.Assert.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxEvaluationsExceededException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.optimization.DifferentiableMultivariateVectorialOptimizer;
import org.apache.commons.math.optimization.VectorialConvergenceChecker;
import org.apache.commons.math.optimization.VectorialPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class AbstractLeastSquaresOptimizerTest {
  
  private AbstractLeastSquaresOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new AbstractLeastSquaresOptimizer() {
      @Override
      protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
      }
    };
  }
  
  @Test
  public void testSetMaxIterations() {
    optimizer.setMaxIterations(100);
    assertEquals(100, optimizer.getMaxIterations());
  }
  
  @Test
  public void testGetIterations() {
    assertEquals(0, optimizer.getIterations());
  }
  
  @Test
  public void testSetMaxEvaluations() {
    optimizer.setMaxEvaluations(100);
    assertEquals(100, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    assertEquals(0, optimizer.getEvaluations());
  }
  
  @Test
  public void testGetJacobianEvaluations() {
    assertEquals(0, optimizer.getJacobianEvaluations());
  }
  
  @Test
  public void testSetConvergenceChecker() {
    VectorialConvergenceChecker checker = new VectorialConvergenceChecker() {
      public boolean converged(int iteration, double[] previous, double[] current) {
        return false;
      }
    };
    optimizer.setConvergenceChecker(checker);
    assertEquals(checker, optimizer.getConvergenceChecker());
  }
  
}
