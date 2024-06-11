import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

   @Test
   public void testDoOptimize() {
      GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
      optimizer.setTarget(new double[]{1.0, 2.0, 3.0});
      optimizer.setWeight(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}));
      optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});

      PointVectorValuePair result = optimizer.doOptimize();

      double[] point = result.getPoint();
      double[] expectedPoint = new double[]{1.0, 2.0, 3.0};
      assertArrayEquals(expectedPoint, point, 0.0001);

      double[] objectiveValue = optimizer.computeObjectiveValue(point);
      double[] expectedObjectiveValue = new double[]{1.0, 2.0, 3.0};
      assertArrayEquals(expectedObjectiveValue, objectiveValue, 0.0001);

      double[] residuals = optimizer.computeResiduals(objectiveValue);
      double[] expectedResiduals = new double[]{0.0, 0.0, 0.0};
      assertArrayEquals(expectedResiduals, residuals, 0.0001);

      RealMatrix weightedJacobian = optimizer.computeWeightedJacobian(point);
      RealMatrix expectedWeightedJacobian = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
      assertTrue(weightedJacobian.equals(expectedWeightedJacobian));

      double cost = optimizer.computeCost(residuals);
      assertEquals(0.0, cost, 0.0001);
   }

   @Test(expected = NullArgumentException.class)
   public void testDoOptimizeNullChecker() {
      GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
      optimizer.setConvergenceChecker(null);
      optimizer.doOptimize();
   }

   @Test(expected = ConvergenceException.class)
   public void testDoOptimizeSingularMatrix() {
      GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
      optimizer.setTarget(new double[]{1.0, 2.0, 3.0});
      optimizer.setWeight(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0}}));
      optimizer.setStartPoint(new double[]{0.0, 0.0, 0.0});

      optimizer.doOptimize();
   }

}