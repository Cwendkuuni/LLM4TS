import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolveDefault() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1.0;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double expected = 1.5;
 UnivariateRealSolver solver = LazyHolder.FACTORY.newDefaultSolver();
 double result = UnivariateRealSolverUtils.solve(function, x0, x1);
 assertEquals(expected, result, 1e-6);
 }

 @Test
 public void testSolveDefaultWithAccuracy() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1.0;
 }
 };
 double x0 = 1.0;
 double x1 = 2.0;
 double absoluteAccuracy = 1e-6;
 double expected = 1.5;
 UnivariateRealSolver solver = LazyHolder.FACTORY.newDefaultSolver();
 solver.setAbsoluteAccuracy(absoluteAccuracy);
 double result = UnivariateRealSolverUtils.solve(function, x0, x1, absoluteAccuracy);
 assertEquals(expected, result, absoluteAccuracy);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveNullFunction() throws Exception {
 UnivariateRealFunction function = null;
 double x0 = 1.0;
 double x1 = 2.0;
 UnivariateRealSolverUtils.solve(function, x0, x1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveInvalidInterval() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1.0;
 }
 };
 double x0 = 3.0;
 double x1 = 1.0;
 UnivariateRealSolverUtils.solve(function, x0, x1);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketFailure() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1.0;
 }
 };
 double initial = 1.0;
 double lowerBound = 1.0;
 double upperBound = 1.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBracketFunctionEvaluationException() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("Evaluation failed");
 }
 };
 double initial = 1.0;
 double lowerBound = 1.0;
 double upperBound = 1.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketNullFunction() throws Exception {
 UnivariateRealFunction function = null;
 double initial = 1.0;
 double lowerBound = 1.0;
 double upperBound = 1.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBracketInvalidInterval() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1.0;
 }
 };
 double initial = 1.0;
 double lowerBound = 2.0;
 double upperBound = 1.0;
 UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 }

 @Test
 public void testBracketSuccess() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1.0;
 }
 };
 double initial = 1.0;
 double lowerBound = 0.0;
 double upperBound = 2.0;
 double[] expected = {1.0, 1.5};
 double[] result = UnivariateRealSolverUtils.bracket(function, initial, lowerBound, upperBound);
 assertEquals(expected[0], result[0], 1e-6);
 assertEquals(expected[1], result[1], 1e-6);
 }

 @Test
 public void testMidpoint() throws Exception {
 double a = 1.0;
 double b = 2.0;
 double expected = 1.5;
 double result = UnivariateRealSolverUtils.midpoint(a, b);
 assertEquals(expected, result, 1e-6);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetupNullFunction() throws Exception {
 UnivariateRealFunction function = null;
 UnivariateRealSolverUtils.setup(function);
 }

 private static class LazyHolder {
 UnivariateRealSolverFactory FACTORY = UnivariateRealSolverFactory.getInstance();
 }

}