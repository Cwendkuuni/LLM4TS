import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

 private UnivariateRealFunction f;
 private BisectionSolver solver;

 @Test
 public void testConstructorWithFunction() {
 solver = new BisectionSolver(new MyTestFunction());
 }

 @Test
 public void testConstructor() {
 solver = new BisectionSolver();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSolveWithMinMax() {
 solver.solve(1.0, 2.0);
 }

 @Test
 public void testSolveWithMinMaxAndInitial() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyTestFunction();
 solver = new BisectionSolver();
 double root = solver.solve(f, 1.0, 2.0, 1.5);
 assertEquals(0.5, root, 1e-6);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyTestFunction();
 solver = new BisectionSolver();
 solver.setMaxIterationCount(1);
 solver.solve(f, -1.0, 1.0);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testFunctionEvaluationException() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyThrowingFunction();
 solver = new BisectionSolver();
 solver.solve(f, -1.0, 1.0);
 }

 @Test
 public void testSolveWithValidInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyTestFunction();
 solver = new BisectionSolver();
 double root = solver.solve(f, -1.0, 1.0);
 assertEquals(0.0, root, 1e-6);
 }

 @Test
 public void testSolveWithInvalidInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyTestFunction();
 solver = new BisectionSolver();
 try {
 solver.solve(f, 1.0, 1.0);
 } catch (Exception e) {
 assertEquals("The interval [1.0, 1.0] does not bracket a real root.", e.getMessage());
 }
 }

 @Test
 public void testSolveWithInitialOutsideInterval() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyTestFunction();
 solver = new BisectionSolver();
 try {
 solver.solve(f, -1.0, 1.0, 2.0);
 } catch (Exception e) {
 assertEquals("The initial guess 2.0 is outside the interval [-1.0, 1.0].", e.getMessage());
 }
 }

 @Test
 public void testSolveWithMaxIterationsReached() throws FunctionEvaluationException, MaxIterationsExceededException {
 f = new MyTestFunction();
 solver = new BisectionSolver();
 solver.setMaxIterationCount(1);
 try {
 solver.solve(f, -1.0, 1.0);
 } catch (MaxIterationsExceededException e) {
 // Expected
 }
 }

 static class MyTestFunction implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return x * x - 1.0;
 }
 }

 static class MyThrowingFunction implements UnivariateRealFunction {
 @Override
 public double value(double x) throws FunctionEvaluationException {
 throw new FunctionEvaluationException();
 }
 }
}