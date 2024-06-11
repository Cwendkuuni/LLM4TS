import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 @BeforeClass
 public static void setUp() {
 System.setProperty("org.apache.commons.math3.exception.showDebugInfo", "true");
 }

 @AfterClass
 public static void tearDown() {
 System.clearProperty("org.apache.commons.math3.exception.showDebugInfo");
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer()
 */
 @Test
 public void testBrentOptimizer() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
 assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-8);
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer(double, double)
 * test valid constructor
 */
 @Test
 public void testBrentOptimizerValidConstruction() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
 assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-8);
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer(double, ConvergenceChecker)
 * test valid constructor with ConvergenceChecker
 */
 @Test
 public void testBrentOptimizerWithChecker() {
 ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
 assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
 assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-8);
 assertEquals(checker, optimizer.getConvergenceChecker());
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer(double, double, ConvergenceChecker)
 * test valid constructor with all parameters
 */
 @Test
 public void testBrentOptimizerAllParams() {
 ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
 assertEquals(1e-6, optimizer.getRelativeThreshold(), 1e-8);
 assertEquals(1e-6, optimizer.getAbsoluteThreshold(), 1e-8);
 assertEquals(checker, optimizer.getConvergenceChecker());
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer(double, double) constructor with invalid absolute threshold
 */
 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerInvalidAbsoluteThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, -1e-6);
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer(double, double) constructor with invalid relative threshold
 */
 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerInvalidRelativeThreshold() {
 BrentOptimizer optimizer = new BrentOptimizer(0, 1e-6);
 }

 /**
 * Test method for BrentOptimizer.BrentOptimizer(double, ConvergenceChecker) constructor with null checker
 */
 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizerNullChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test minimization
 */
 @Test
 public void testDoOptimizeMinimization() {
 FunctionOne f = new FunctionOne();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(3.0, result.getPoint(), 1e-6);
 assertEquals(-2.25, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test maximization
 */
 @Test
 public void testDoOptimizeMaximization() {
 FunctionOne f = new FunctionOne();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, GoalType.MAXIMIZE);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(7.0, result.getPoint(), 1e-6);
 assertEquals(2.25, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test interval out of order
 */
 @Test
 public void testDoOptimizeIntervalOutOfOrder() {
 FunctionOne f = new FunctionOne();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 try {
 optimizer.optimize(f, 10, 0);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test interval too small
 */
 @Test
 public void testDoOptimizeIntervalTooSmall() {
 FunctionOne f = new FunctionOne();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 try {
 optimizer.optimize(f, 1, 1);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test function is constant
 */
 @Test
 public void testDoOptimizeFunctionIsConstant() {
 FunctionConstant f = new FunctionConstant(5.0);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(5.0, result.getPoint(), 1e-6);
 assertEquals(5.0, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test function with local minimum inside interval
 */
 @Test
 public void testDoOptimizeLocalMinimum() {
 FunctionLocalMinimum f = new FunctionLocalMinimum(3.0, 4.0);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(3.0, result.getPoint(), 1e-6);
 assertEquals(-1.0, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test function with global minimum outside interval
 */
 @Test
 public void testDoOptimizeGlobalMinimumOutsideInterval() {
 FunctionGlobalMinimum f = new FunctionGlobalMinimum(-1.0, 0.0);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(-1.0, result.getPoint(), 1e-6);
 assertEquals(1.0, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test function with global minimum inside interval
 */
 @Test
 public void testDoOptimizeGlobalMinimumInsideInterval() {
 FunctionGlobalMinimum f = new FunctionGlobalMinimum(0.0, 0.0);
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(0.0, result.getPoint(), 1e-6);
 assertEquals(0.0, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.doOptimize() - test function with multiple local minima
 */
 @Test
 public void testDoOptimizeMultipleLocalMinima() {
 FunctionMultipleLocalMinima f = new FunctionMultipleLocalMinima();
 BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
 UnivariatePointValuePair result = optimizer.optimize(f, 0, 10);
 assertEquals(1.0, result.getPoint(), 1e-6);
 assertEquals(-1.5, result.getValue(), 1e-6);
 }

 /**
 * Test method for BrentOptimizer.best()
 */
 @Test
 public void testBest() {
 UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, -2.0);
 UnivariatePointValuePair b = new UnivariatePointValuePair(3.0, 1.0);
 UnivariatePointValuePair expected = new UnivariatePointValuePair(1.0, -2.0);
 UnivariatePointValuePair result = BrentOptimizer.best(a, b, true);
 assertEquals(expected, result);

 UnivariatePointValuePair a2 = new UnivariatePointValuePair(1.0, -2.0);
 UnivariatePointValuePair b2 = new UnivariatePointValuePair(3.0, 1.0);
 UnivariatePointValuePair expected2 = new UnivariatePointValuePair(3.0, 1.0);
 UnivariatePointValuePair result2 = BrentOptimizer.best(a2, b2, false);
 assertEquals(expected2, result2);
 }

}

/**
 * Custom convergence checker for testing purposes.
 */
class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {

 @Override
 public boolean converged(int iterationNumber, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 return false;
 }

}

class FunctionOne implements UnivariateFunction {

 @Override
 public double value(double x) {
 return x - 1;
 }

 @Override
 public double derivative(double x) {
 return 1;
 }

}

class FunctionConstant implements UnivariateFunction {

 private final double value;

 public FunctionConstant(double value) {
 this.value = value;
 }

 @Override
 public double value(double x) {
 return value;
 }

 @Override
 public double derivative(double x) {
 return 0;
 }

}

class FunctionLocalMinimum implements UnivariateFunction {

 private final double minimum;
 private final double intervalCenter;

 public FunctionLocalMinimum(double minimum, double intervalCenter) {
 this.minimum = minimum;
 this.intervalCenter = intervalCenter;
 }

 @Override
 public double value(double x) {
 return Math.pow(x - intervalCenter, 2) + minimum;
 }

 @Override
 public double derivative(double x) {
 return 2 * (x - intervalCenter);
 }

}

class FunctionGlobalMinimum implements UnivariateFunction {

 private final double minimum;
 private final double intervalStart;

 public FunctionGlobalMinimum(double minimum, double intervalStart) {
 this.minimum = minimum;
 this.intervalStart = intervalStart;
 }

 @Override
 public double value(double x) {
 return Math.pow(x - intervalStart, 2) + minimum;
 }

 @Override
 public double derivative(double x) {
 return 2 * (x - intervalStart);
 }

}

class FunctionMultipleLocalMinima implements UnivariateFunction {

 @Override
 public double value(double x) {
 return Math.pow(x - 1, 2) + Math.sin(x);
 }

 @Override
 public double derivative(double x) {
 return 2 * (x - 1) + Math.cos(x);
 }

}