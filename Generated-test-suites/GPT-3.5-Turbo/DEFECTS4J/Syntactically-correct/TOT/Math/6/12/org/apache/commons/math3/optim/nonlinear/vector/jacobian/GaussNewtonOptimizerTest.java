import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;

import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

  @Test
  public void testDoOptimize_NormalCase() {
    // Set up
    double[] target = {1.0, 2.0, 3.0, 4.0};
    RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}});
    double[] startPoint = {0.0, 0.0, 0.0, 0.0};

    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
    optimizer.setTarget(target);
    optimizer.setWeight(weight);
    optimizer.setStartPoint(startPoint);

    // Execute
    PointVectorValuePair result = optimizer.doOptimize();

    // Verify
    Assert.assertEquals(4, result.getPoint().length);
    Assert.assertTrue(result.getPoint()[0] > 0.0);
    Assert.assertTrue(result.getPoint()[1] > 0.0);
    Assert.assertTrue(result.getPoint()[2] > 0.0);
    Assert.assertTrue(result.getPoint()[3] > 0.0);
  }

  @Test
  public void testDoOptimize_NullChecker() {
    // Set up
    double[] target = {1.0, 2.0, 3.0, 4.0};
    RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}});
    double[] startPoint = {0.0, 0.0, 0.0, 0.0};

    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
    optimizer.setTarget(target);
    optimizer.setWeight(weight);
    optimizer.setStartPoint(startPoint);
    optimizer.setConvergenceChecker(null);

    // Verify
    try {
      optimizer.doOptimize();
      Assert.fail("Expected NullArgumentException");
    } catch (NullArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDoOptimize_SingularMatrix() {
    // Set up
    double[] target = {1.0, 2.0, 3.0, 4.0};
    RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}});
    double[] startPoint = {0.0, 0.0, 0.0, 0.0};

    GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
    optimizer.setTarget(target);
    optimizer.setWeight(weight);
    optimizer.setStartPoint(startPoint);

    // Change the weight matrix to cause a singular matrix exception
    weight.setEntry(0, 0, 0.0);

    // Verify
    try {
      optimizer.doOptimize();
      Assert.fail("Expected ConvergenceException");
    } catch (ConvergenceException e) {
      Assert.assertEquals(LocalizedFormats.UNABLE_TO_SOLVE_SINGULAR_PROBLEM, e.getPattern());
    }
  }

}