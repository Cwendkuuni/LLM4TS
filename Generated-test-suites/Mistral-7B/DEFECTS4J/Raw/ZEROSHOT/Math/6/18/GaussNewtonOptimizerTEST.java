import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.initializer.GaussianRandomStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.RealValuedFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.RealPointValuePair;
import org.apache.commons.math3.optim.initializer.StartingPointGenerator;
import org.apache.commons.math3.optim.linear.RealVector;
import org.apache.commons.math3.optim.nonlinear.functions.SineFunction;
import org.apache.commons.math3.optim.nonlinear.functions.Sum;
import org.apache.commons.math3.optim.nonlinear.gradient.FiniteDifferenceGradient;
import org.apache.commons.math3.optim.solvers.GaussNewtonSolver;
import org.apache.commons.math3.optim.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.initializer.RandomStartingPointGenerator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

 @Test
 public void testGaussNewtonOptimizer() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false);
 RealValuedFunction function = new Sum(new SineFunction(Math.PI / 2.0),
 new SineFunction(Math.PI));
 RealVector startPoint = new ArrayRealVector(new double[]{1.0, 1.0});
 double tolerance = 1e-10;

 StartingPointGenerator pointGenerator = new AveragedStartingPointGenerator(
 new GaussianRandomStartingPointGenerator(2), 10);
 optimizer.setStartingPoint(pointGenerator.getStartingPoint(function));
 SimpleValueChecker checker = new SimpleValueChecker(tolerance);
 optimizer.setConvergenceChecker(checker);
 optimizer.optimize(function);

 RealPointValuePair result = optimizer.getPointValuePair();
 double x = result.getPoint()[0];
 double y = result.getPoint()[1];
 double fx = function.value(new RealVector(new double[]{x, y}));
 double expected = Math.PI / 2.0 + Math.PI;
 assertEquals(expected, fx, tolerance);
 }

 @Test
 public void testGaussNewtonOptimizer_UseLU() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-10));
 RealValuedFunction function = new Sum(new SineFunction(Math.PI / 2.0),
 new SineFunction(Math.PI));
 RealVector startPoint = new ArrayRealVector(new double[]{1.0, 1.0});
 optimizer.setStartingPoint(startPoint);
 optimizer.optimize(function);

 RealPointValuePair result = optimizer.getPointValuePair();
 double x = result.getPoint()[0];
 double y = result.getPoint()[1];
 double fx = function.value(new RealVector(new double[]{x, y}));
 double expected = Math.PI / 2.0 + Math.PI;
 assertEquals(expected, fx, 1e-10);
 }

 @Test
 public void testGaussNewtonOptimizer_Iterations() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleValueChecker(1e-10));
 RealValuedFunction function = new Sum(new SineFunction(Math.PI / 2.0),
 new SineFunction(Math.PI));
 RealVector startPoint = new ArrayRealVector(new double[]{1.0, 1.0});
 optimizer.setStartingPoint(startPoint);
 optimizer.setMaxIterations(10);

 int expectedIterations = 8;
 optimizer.optimize(function);
 int actualIterations = optimizer.getIterations();
 assertEquals(expectedIterations, actualIterations);
 }

 @Test
 public void testGaussNewtonOptimizer_MaxIterationsReached() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleValueChecker(1e-10));
 RealValuedFunction function = new Sum(new SineFunction(Math.PI / 2.0),
 new SineFunction(Math.PI));
 RealVector startPoint = new ArrayRealVector(new double[]{1.0, 1.0});
 optimizer.setStartingPoint(startPoint);
 optimizer.setMaxIterations(1);

 assertFalse(optimizer.optimize(function).isSuccess());
 }
}