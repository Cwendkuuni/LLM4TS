import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private static UnivariateFunction sinFunction = new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }

 @Override
 public double derivative(double x) {
 return Math.cos(x);
 }
 };

 @Test
 public void testBrentOptimizer_Basic() {
 // Test with default values
 BrentOptimizer optimizer = new BrentOptimizer(sinFunction);
 PointValuePair result = optimizer.optimize();
 assertEquals(0.0, result.getPoint(), 1e-8);
 assertEquals(-1, result.getValue(), 1e-8);
 }

 @Test
 public void testBrentOptimizer_CustomTolerance() {
 // Test with custom tolerance values
 double absTol = 1e-5;
 double relTol = 1e-4;
 BrentOptimizer optimizer = new BrentOptimizer(relTol, absTol, sinFunction);
 PointValuePair result = optimizer.optimize();
 assertEquals(Math.PI / 2, result.getPoint(), absTol + relTol * Math.abs(Math.PI / 2));
 assertEquals(0, result.getValue(), absTol + relTol * Math.abs(Math.sin(Math.PI / 2)));
 }

 @Test
 public void testBrentOptimizer_CustomInitialGuess() {
 // Test with custom initial guess
 BrentOptimizer optimizer = new BrentOptimizer(sinFunction, 1.5);
 PointValuePair result = optimizer.optimize();
 // The exact solution is not known for sin(x), but we can compare the
 // results with the value obtained using InitialGuess.QUADRATIC_INTERPOLATOR
 InitialGuess initialGuess = new InitialGuess.QuadraticInterpolator(sinFunction, 1.5);
 PointValuePair expectedResult = initialGuess.getInitialPoint();
 // For comparison, we use a tolerance of 1e-7 to account for the difference
 // between the initial guess and the exact solution
 assertEquals(expectedResult.getPoint(), result.getPoint(), 1e-7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizer_InvalidAbsTol() {
 BrentOptimizer optimizer = new BrentOptimizer(-1e-5, 1e-4, sinFunction);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBrentOptimizer_InvalidRelTol() {
 BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-4, sinFunction);
 }

 @Test
 public void testBrentOptimizer_CustomConvergenceChecker() {
 // Test with a custom convergence checker
 ConvergenceChecker<PointValuePair> myConvergenceChecker = new ConvergenceChecker<PointValuePair>() {
 @Override
 public boolean converged(int iter, PointValuePair current, PointValuePair previous) {
 return current.getValue() < 1e-5;
 }
 };
 BrentOptimizer optimizer = new BrentOptimizer(sinFunction, myConvergenceChecker);
 PointValuePair result = optimizer.optimize();
 assertEquals(0, result.getValue(), 1e-5);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testBrentOptimizer_NoDerivative() {
 BrentOptimizer optimizer = new BrentOptimizer(new UnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }

 @Override
 public double derivative(double x) {
 throw new UnsupportedOperationException();
 }
 }, sinFunction.getInitialStep());
 }

 @Test
 public void testBrentOptimizer_MaxEval() {
 // Test with MaxEval
 MaxEval maxEval = new MaxEval(3);
 BrentOptimizer optimizer = new BrentOptimizer(sinFunction, maxEval);
 PointValuePair result = optimizer.optimize();
 // The exact solution is not known for sin(x), but we can compare the
 // results with the value obtained using InitialGuess.QUADRATIC_INTERPOLATOR
 InitialGuess initialGuess = new InitialGuess.QuadraticInterpolator(sinFunction, 1.5);
 PointValuePair expectedResult = initialGuess.getInitialPoint();
 // For comparison, we use a tolerance of 1e-7 to account for the difference
 // between the initial guess and the exact solution
 assertEquals(expectedResult.getPoint(), result.getPoint(), 1e-7);
 }

 @Test
 public void testBrentOptimizer_GoalTypeMinimize() {
 // Test with goal type MINIMIZE
 BrentOptimizer optimizer = new BrentOptimizer(Math::exp, GoalType.MINIMIZE);
 PointValuePair result = optimizer.optimize();
 assertEquals(-E.EULER_NUMBER.getDoubleValue(), result.getPoint(), 1e-8);
 assertEquals(1, result.getValue(), 1e-8);
 }

}