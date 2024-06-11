import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BisectionSolverTest {

 private UnivariateRealFunction testFunction;
 private BisectionSolver bisectionSolver;

 @Test
 public void testBisectionSolverConstructors() {
 bisectionSolver = new BisectionSolver();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeprecatedSolveWithMinMaxAndInitial() {
 testFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 bisectionSolver.solve(testFunction, 1.0, 3.0, 2.0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeprecatedSolveWithMinAndInitial() {
 testFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 bisectionSolver.solve(testFunction, 1.0, 2.0);
 }

 @Test
 public void testSolveWithFunctionMinMax() throws FunctionEvaluationException, MaxIterationsExceededException {
 testFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 bisectionSolver.solve(testFunction, -2.0, 2.0);
 assertEquals(0.0, bisectionSolver.getResult(), 1E-6);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithFunctionThrowsFunctionEvaluationException() throws MaxIterationsExceededException {
 testFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) throws FunctionEvaluationException {
 throw new FunctionEvaluationException("Test Error");
 }
 };
 bisectionSolver.solve(testFunction, -2.0, 2.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveWithMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
 testFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 bisectionSolver = new BisectionSolver(1, 1E-6);
 bisectionSolver.solve(testFunction, -5.0, 5.0);
 }

 @Test
 public void testVerifyInterval() {
 bisectionSolver = new BisectionSolver();
 testFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 bisectionSolver.verifyInterval(1.0, 2.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyIntervalWithInvalidInterval() {
 bisectionSolver = new BisectionSolver();
 bisectionSolver.verifyInterval(1.0, 1.0);
 }

 @Test
 public void testUnivariateRealSolverUtilsMidpoint() {
 double min = 1.0;
 double max = 3.0;
 double expected = 2.0;
 double result = UnivariateRealSolverUtils.midpoint(min, max);
 assertEquals(expected, result, 1E-6);
 }

 @Test
 public void testClearResult() {
 bisectionSolver = new BisectionSolver();
 bisectionSolver.setResult(1.0, 1);
 bisectionSolver.clearResult();
 assertFalse(bisectionSolver.hasResult());
 }
}