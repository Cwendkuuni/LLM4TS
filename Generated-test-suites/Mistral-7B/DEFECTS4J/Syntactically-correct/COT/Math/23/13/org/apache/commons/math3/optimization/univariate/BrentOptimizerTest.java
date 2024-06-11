import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonUnaryFunctionEvaluator;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.MinEval;
import org.apache.commons.math3.optim.LocalOptimizer;
import org.apache.commons.math3.optim.SimpleBoundsFlooring;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.Arrays;

public class BrentOptimizerTest {

 private UnivariateFunction function;
 private LocalOptimizer optimizer;

 @Before
 public void setUp() {
 function = mock(UnivariateFunction.class);
 optimizer = new BrentOptimizer(0.01, 0.001);
 }

 @Test
 public void testConstructorWithRelAndAbs() {
 BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
 Assert.assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
 Assert.assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeAbs() {
 new BrentOptimizer(0.01, -0.001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithRelBelowMinRelativeTolerance() {
 new BrentOptimizer(0.0001, 0.001);
 }

 @Test
 public void testDoOptimizeWithMinimizeGoal() throws Exception {
 given(function.value(anyDouble())).willReturn(Math.pow(sin(anyDouble()), 2));
 SimpleBounds bounds = new SimpleBounds(0, 2 * FastMath.PI);
 optimizer.optimize(function, bounds, new InitialGuess(1), new MinEval(1));
 PointValuePair result = optimizer.getResult();
 verify(function).value(result.getPoint());
 Assert.assertEquals(Math.PI, result.getPoint(), 0.0001);
 }

 @Test
 public void testDoOptimizeWithMaximizeGoal() throws Exception {
 given(function.value(anyDouble())).willReturn(Math.pow(sin(anyDouble()), 2));
 SimpleBounds bounds = new SimpleBounds(0, 2 * FastMath.PI);
 optimizer.optimize(function, bounds, new InitialGuess(1), new MaxEval(1));
 PointValuePair result = optimizer.getResult();
 verify(function).value(result.getPoint());
 Assert.assertEquals(-Math.PI, result.getPoint(), 0.0001);
 }

 @Test
 public void testDoOptimizeWithUserDefinedConvergenceChecker() throws Exception {
 LocalOptimizer optimizer = spy(new BrentOptimizer(0.01, 0.001));
 doReturn(true).when(optimizer).isStopped(anyInt(), any(PointValuePair.class), any(PointValuePair.class));
 given(function.value(anyDouble())).willReturn(Math.pow(sin(anyDouble()), 2));
 SimpleBounds bounds = new SimpleBounds(0, 2 * FastMath.PI);
 optimizer.optimize(function, bounds, new InitialGuess(1), new MinEval(1));
 PointValuePair result = optimizer.getResult();
 verify(optimizer).isStopped(eq(0), any(PointValuePair.class), any(PointValuePair.class));
 Assert.assertEquals(Math.PI, result.getPoint(), 0.0001);
 }

 @Test
 public void testIsStoppedWithDefaultCriteria() throws Exception {
 given(function.value(anyDouble())).willReturn(Math.pow(sin(anyDouble()), 2));
 SimpleBounds bounds = new SimpleBounds(0, 2 * FastMath.PI);
 optimizer.optimize(function, bounds, new InitialGuess(1), new MinEval(1));
 PointValuePair result = optimizer.getResult();
 Assert.assertTrue(optimizer.isStopped(anyInt(), any(PointValuePair.class), any(PointValuePair.class)));
 }

 @Test
 public void testIsStoppedWithUserDefinedCriteria() throws Exception {
 LocalOptimizer optimizer = spy(new BrentOptimizer(0.01, 0.001));
 when(optimizer.isStopped(anyInt(), any(PointValuePair.class), any(PointValuePair.class))).thenReturn(false);
 given(function.value(anyDouble())).willReturn(Math.pow(sin(anyDouble()), 2));
 SimpleBounds bounds = new SimpleBounds(0, 2 * FastMath.PI);
 optimizer.optimize(function, bounds, new InitialGuess(1), new MinEval(1));
 PointValuePair result = optimizer.getResult();
 Assert.assertFalse(optimizer.isStopped(anyInt(), any(PointValuePair.class), any(PointValuePair.class)));
 }

 @Test
 public void testOptimizeWithNonUnaryFunctionEvaluator() throws Exception {
 NonUnaryFunctionEvaluator evaluator = mock(NonUnaryFunctionEvaluator.class);
 when(evaluator.evaluate(anyDouble())).thenReturn(Math.pow(sin(anyDouble()), 2));
 optimizer = new BrentOptimizer(0.01, 0.001) {
 @Override
 public double value(double x) throws Exception {
 return evaluator.evaluate(x);
 }
 };
 SimpleBounds bounds = new SimpleBounds(0, 2 * FastMath.PI);
 optimizer.optimize(function, bounds, new InitialGuess(1), new MinEval(1));
 PointValuePair result = optimizer.getResult();
 verify(evaluator).evaluate(result.getPoint());
 Assert.assertEquals(Math.PI, result.getPoint(), 0.0001);
 }

 @Test
 public void testOptimizeWithDifferentBounds() throws Exception {
 UnivariateFunction function = mock(UnivariateFunction.class);
 given(function.value(anyDouble())).willReturn(Math.sin(anyDouble()));
 SimpleBounds bounds = new SimpleBoundsFlooring(new double[] { -3, -2 }, new double[] { 2, 3 });
 optimizer.optimize(function, bounds, new InitialGuess(0), new MinEval(1));
 PointValuePair result = optimizer.getResult();
 Assert.assertEquals(-2, result.getPoint(), 0.0001);
 }

 @Test
 public void testOptimizeWithInvalidGoalType() throws Exception {
 Assert.assertThrows(IllegalArgumentException.class, () -> {
 optimizer.optimize(function, new SimpleBounds(0, 2 * FastMath.PI), new InitialGuess(1), new GoalType(10));
 });
 }
}