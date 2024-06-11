import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

 private UnivariateRealFunction function;
 private BisectionSolver solver;

 @Test
 public void testConstructorWithFunction() {
 solver = new BisectionSolver(function);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullFunction() {
 solver = new BisectionSolver(null);
 }

 @Test
 public void testSolveWithMinMaxAndInitial() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 4.0;
 }
 };
 solver = new BisectionSolver();
 solver.solve(function, -2.5, 2.5, 0.0);
 double expectedResult = -1.5;
 assertEquals(expectedResult, solver.getResult(), 0.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithNullMin() throws FunctionEvaluationException, MaxIterationsExceededException {
 solver.solve(function, null, 2.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolveWithNullMax() throws FunctionEvaluationException, MaxIterationsExceededException {
 solver.solve(function, -2.5, null);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithFunctionThrowsExceptionAtMin() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("Test exception");
 }
 };
 solver.solve(function, -2.5, 2.5);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveWithFunctionThrowsExceptionAtMidpoint() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 if (x > 0.0) {
 throw new FunctionEvaluationException("Test exception");
 }
 return x * x;
 }
 };
 solver.solve(function, -1.0, 1.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveWithMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 solver = new BisectionSolver();
 solver.setMaxIterations(1);
 solver.solve(function, -1.0, 1.0);
 }

 @Test
 public void testSolveWithFunctionAndMinMax() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 solver = new BisectionSolver();
 solver.solve(function, -3.0, 3.0);
 double expectedResult = 0.0;
 assertEquals(expectedResult, solver.getResult(), 0.01);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveWithFunctionAndMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 solver = new BisectionSolver();
 solver.setMaxIterations(1);
 solver.solve(function, -3.0, 3.0);
 }

 @Test
 public void testSolveWithMinMax() throws FunctionEvaluationException, MaxIterationsExceededException {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - 0.5;
 }
 };
 solver = new BisectionSolver();
 solver.solve(function, -3.0, 3.0);
 double expectedResult = 0.0;
 assertEquals(expectedResult, solver.getResult(), 0.01);
 }
}