import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateFunction function;
 private BrentOptimizer optimizer;
 private double absoluteThreshold = 0.001;
 private double relativeThreshold = 1e-6;
 private ConvergenceChecker<PointsValuePair> convergenceChecker;

 @Before
 public void setUp() {
 function = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) * Math.exp(-x);
 }

 @Override
 public int getNumberOfVariables() {
 return 1;
 }
 };
 optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
 }

 @Test
 public void testFindMinimum() {
 // Test finding the minimum of the given function
 double expectedMinimum = -1.24839511522972;
 PointsValuePair result = optimizer.optimize(function, 0.0, 2.0, new ValueInitializer<Double>() {
 @Override
 public Double initialize() {
 return 1.0;
 }
 });
 assertEquals(expectedMinimum, result.getPoint(), 1e-6);
 assertEquals(-expectedMinimum, result.getValue(), 1e-6);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeAbsoluteThreshold() {
 optimizer = new BrentOptimizer(relativeThreshold, -absoluteThreshold, convergenceChecker);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testRelativeThresholdBelowMacheps() {
 optimizer = new BrentOptimizer(-relativeThreshold, absoluteThreshold, convergenceChecker);
 }

 @Test
 public void testConvergenceChecker() {
 convergenceChecker = new ConvergenceChecker<PointsValuePair>() {
 @Override
 public boolean converged(int iterations, PointsValuePair previous, PointsValuePair current) {
 return current.getValue() < previous.getValue() && iterations > 10;
 }
 };
 optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, convergenceChecker);
 optimizer.optimize(function, 0.0, 2.0, new ValueInitializer<Double>() {
 @Override
 public Double initialize() {
 return 1.0;
 }
 });
 fail("The optimization should not have converged with the given convergence checker.");
 }
}