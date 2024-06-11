import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

 private static final double[] TARGET_VALUES = new double[]{1.0, 2.0, 3.0};
 private static final double[] START_POINT = new double[]{0.0, 0.0, 0.0};
 private static final double[] COST_EXPECTED = new double[]{1.0, 0.5, 0.25};

 @Test
 public void testGaussNewtonOptimizerWithDefaultSettings() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(START_POINT);
 optimizer.optimize();

 PointValuePair result = optimizer.getValue();
 assertArrayEquals(TARGET_VALUES, result.getPoint(), 0.0);
 assertArrayEquals(COST_EXPECTED, result.getValue(), 0.0);
 }

 @Test(expected = NullArgumentException.class)
 public void testGaussNewtonOptimizerWithNullChecker() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
 optimizer.setConvergenceChecker(null);
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(START_POINT);
 optimizer.optimize();
 }

 @Test
 public void testGaussNewtonOptimizerWithCustomChecker() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker(1e-3, 1e-3));
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(START_POINT);
 optimizer.optimize();

 PointValuePair result = optimizer.getValue();
 assertArrayEquals(TARGET_VALUES, result.getPoint(), 1e-3);
 assertArrayEquals(COST_EXPECTED, result.getValue(), 1e-3);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGaussNewtonOptimizerWithInvalidUseLU() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(-1);
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(START_POINT);
 optimizer.optimize();
 }

 @Test
 public void testGaussNewtonOptimizerWithLUDecomposition() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-3, 1e-3));
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(START_POINT);
 optimizer.optimize();

 PointValuePair result = optimizer.getValue();
 assertArrayEquals(TARGET_VALUES, result.getPoint(), 1e-3);
 assertArrayEquals(COST_EXPECTED, result.getValue(), 1e-3);
 }

 @Test(expected = SingularMatrixException.class)
 public void testGaussNewtonOptimizerWithSingularMatrixException() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-3, 1e-3));
 optimizer.setTargetValues(new double[]{Double.NaN, 2.0, 3.0});
 optimizer.setStartPoint(START_POINT);
 optimizer.optimize();
 }

 @Test(expected = ConvergenceException.class)
 public void testGaussNewtonOptimizerWithConvergenceException() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-3, 1e-3));
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(new double[]{1.0, 1.0, 1.0});
 optimizer.optimize();
 }

 @Test
 public void testGaussNewtonOptimizerWithWeightMatrix() {
 RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
     {1.0, 0.0},
     {0.0, 2.0}
 });
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-3, 1e-3));
 optimizer.setTargetValues(TARGET_VALUES);
 optimizer.setStartPoint(START_POINT);
 optimizer.setWeightMatrix(weightMatrix);
 optimizer.optimize();

 PointValuePair result = optimizer.getValue();
 assertArrayEquals(TARGET_VALUES, result.getPoint(), 1e-3);
 assertArrayEquals(COST_EXPECTED, result.getValue(), 1e-3);
 }
}