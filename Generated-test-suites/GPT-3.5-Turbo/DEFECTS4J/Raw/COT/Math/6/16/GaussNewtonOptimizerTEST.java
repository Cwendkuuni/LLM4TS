import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

  @Test
  public void testGaussNewtonOptimizer() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    assertNotNull(optimizer);
  }

  @Test
  public void testGaussNewtonOptimizerWithConvergenceChecker() {
    ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(checker);
    assertNotNull(optimizer);
  }

  @Test
  public void testGaussNewtonOptimizerWithUseLU() {
    boolean useLU = true;
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(useLU);
    assertNotNull(optimizer);
  }

  @Test
  public void testGaussNewtonOptimizerWithUseLUAndConvergenceChecker() {
    boolean useLU = true;
    ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker();
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(useLU, checker);
    assertNotNull(optimizer);
  }

  @Test
  public void testDoOptimize() {
    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
    PointVectorValuePair result = optimizer.doOptimize();
    assertNotNull(result);
  }

  @Test
  public void testGaussNewtonOptimizerWithNullConvergenceChecker() {
    try {
      GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(null);
      fail("Expected NullArgumentException");
    } catch (NullArgumentException e) {
      // Expected exception
    }
  }

  @Test
  public void testDoOptimizeWithSingularMatrix() {
    try {
      GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
      // Set matrix to be singular (e.g. all entries are equal to 0)
      RealMatrix weightMatrix = MatrixUtils.createRealMatrix(3, 3);
      weightMatrix.setEntry(0, 0, 0);
      weightMatrix.setEntry(1, 1, 0);
      weightMatrix.setEntry(2, 2, 0);
      optimizer.setWeight(weightMatrix);
      optimizer.doOptimize();
      fail("Expected ConvergenceException");
    } catch (ConvergenceException e) {
      // Expected exception
    }
  }
}