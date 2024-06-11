import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

 @Test
 public void testBisectionSolver() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2;
 }
 };

 // Test with given initial brackets
 double root = BisectionSolver.getInstance().solve(function, -3.0, 2.0);
 assertEquals("Root is not correct", 1.0, root, 0.01);

 // Test with default initial brackets
 root = BisectionSolver.getInstance().solve(function);
 assertEquals("Root is not correct", 1.0, root, 0.01);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testBisectionSolverMaxIterations() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2;
 }
 };

 BisectionSolver solver = new BisectionSolver();
 solver.setMaxIterations(1);
 solver.solve(function, -3.0, 2.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBisectionSolverSmallAccuracy() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2;
 }
 };

 BisectionSolver solver = new BisectionSolver();
 solver.setAbsoluteAccuracy(0.0);
 solver.solve(function, -3.0, 2.0);
 }

 @Test
 public void testBisectionSolverNegativeFunction() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return -Math.pow(x, 2) + 2;
 }
 };

 double root = BisectionSolver.getInstance().solve(function, -3.0, 2.0);
 assertEquals("Root is not correct", -1.0, root, 0.01);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testBisectionSolverDivergentFunction() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new UnsupportedOperationException();
 }
 };

 BisectionSolver solver = new BisectionSolver();
 solver.solve(function, -3.0, 2.0);
 }
}