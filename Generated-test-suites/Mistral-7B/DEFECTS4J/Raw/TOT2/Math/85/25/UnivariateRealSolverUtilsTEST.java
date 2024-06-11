import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolveBasic() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double result = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
 assertEquals(0.5, result, 0.001);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveConvergenceException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double result = UnivariateRealSolverUtils.solve(function, 0.0, 0.0);
 fail("Should have thrown ConvergenceException");
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyThrowingFunction();
 double result = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
 fail("Should have thrown FunctionEvaluationException");
 }

 @Test
 public void testSolveIllegalArgumentException() {
 try {
 UnivariateRealSolverUtils.solve(null, 0.0, 1.0);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBracketBasic() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
 assertArrayEquals(new double[]{-1.0, 0.0}, result);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketConvergenceException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0);
 fail("Should have thrown ConvergenceException");
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBracketFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyThrowingFunction();
 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
 fail("Should have thrown FunctionEvaluationException");
 }

 @Test
 public void testBracketIllegalArgumentException() {
 try {
 UnivariateRealSolverUtils.bracket(null, 0.0, -1.0, 1.0);
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double result = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 0.001);
 assertEquals(0.5, result, 0.001);
 }

 @Test(expected = ConvergenceException.class)
 public void testSolveWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double result = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 0.0);
 UnivariateRealSolver solver = UnivariateRealSolverFactory.newBrentSolver();
 solver.setAbsoluteAccuracy(0.0);
 try {
 solver.solve(function, -1.0, 1.0);
 fail("Should have thrown ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyThrowingFunction();
 try {
 UnivariateRealSolverUtils.solve(function, -1.0, 1.0, 0.001);
 fail("Should have thrown FunctionEvaluationException");
 } catch (FunctionEvaluationException e) {
 // Expected
 }
 }

 @Test
 public void testBracketWithMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 10);
 assertArrayEquals(new double[]{-1.0, 0.0}, result);
 }

 @Test(expected = ConvergenceException.class)
 public void testBracketWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyTestFunction();
 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, 0.0, 0.0, 1);
 fail("Should have thrown ConvergenceException");
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBracketWithFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
 UnivariateRealFunction function = new MyThrowingFunction();
 double[] result = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 10);
 fail("Should have thrown FunctionEvaluationException");
 }

 @Test
 public void testMidpoint() {
 double result = UnivariateRealSolverUtils.midpoint(1.0, 2.0);
 assertEquals(1.5, result, 0.0);
 }
}

class MyTestFunction implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 0.25;
 }
}

class MyThrowingFunction implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("Test exception");
 }
}