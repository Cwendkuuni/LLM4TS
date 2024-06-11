import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

 @Test
 public void testSolverConstructor() {
 BisectionSolver solver = new BisectionSolver();
 assertEquals(100, solver.getMaxIterations());
 assertEquals(1e-6, solver.getRelativeAccuracy(), 1e-8);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFunctionArgumentNull() {
 BisectionSolver solver = new BisectionSolver(null);
 }

 @Test
 public void testSolverWithValidFunction() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 2;
 }
 };
 BisectionSolver solver = new BisectionSolver(function);
 double root = solver.solve(function, -2.5, 2.5);
 assertEquals(-1.0, root, 1e-6);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolverMaxIterationsExceeded() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 BisectionSolver solver = new BisectionSolver(function);
 try {
 solver.solve(function, -10, 10);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolverFunctionEvaluationException() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new ArithmeticException("Division by zero");
 }
 };
 BisectionSolver solver = new BisectionSolver(function);
 try {
 solver.solve(function, -1, 1);
 fail("Expected FunctionEvaluationException");
 } catch (FunctionEvaluationException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSolverNumberIsTooSmallException() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 1e-12;
 }
 };
 BisectionSolver solver = new BisectionSolver(function);
 try {
 solver.solve(function, -1, 1);
 fail("Expected NumberIsTooSmallException");
 } catch (NumberIsTooSmallException e) {
 // expected
 }
 }

 @Test
 public void testSolverWithInitialGuess() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 2;
 }
 };
 BisectionSolver solver = new BisectionSolver(function, 0.5);
 double root = solver.solve(function, -2.5, 2.5);
 assertEquals(-1.0, root, 1e-6);
 }

 @Test
 public void testSolverWithMinMaxBracketingRoot() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 0.5, 2) - 0.25;
 }
 };
 BisectionSolver solver = new BisectionSolver(function);
 double root = solver.solve(function, -1.2, 1.2);
 assertEquals(0.5, root, 1e-6);
 }

 @Test
 public void testSolverWithMinMaxNotBracketingRoot() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 0.5, 2) - 0.25;
 }
 };
 BisectionSolver solver = new BisectionSolver(function);
 try {
 solver.solve(function, -1.5, 1.5);
 fail("Expected MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }
}