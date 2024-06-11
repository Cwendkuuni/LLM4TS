import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.analysis.solvers.MaxIterationsExceededException;
import org.apache.commons.math3.analysis.solvers.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

 @Test
 public void testSolveBasic() {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };

 BisectionSolver solver = new BisectionSolver();
 try {
 double root = solver.solve(f, -2.5, 2.5);
 assertEquals(-1.0, root, 1e-6);
 } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testSolveEdgeCases() {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4;
 }
 };

 BisectionSolver solver = new BisectionSolver();
 try {
 double root = solver.solve(f, -3, 3);
 assertEquals(2.0, root, 1e-6);
 } catch (FunctionEvaluationException | MaxIterationsExceededException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveFunctionWithNoRoot() {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + 1;
 }
 };

 BisectionSolver solver = new BisectionSolver();
 try {
 solver.solve(f, -5, 5);
 } catch (MaxIterationsExceededException e) {
 // Expected exception
 }
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveMaxIterationsExceeded() {
 UnivariateRealFunction f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };

 BisectionSolver solver = new BisectionSolver(1, 1e-6); // Small maximum iterations count and accuracy
 try {
 solver.solve(f, -1, 1);
 } catch (FunctionEvaluationException e) {
 // Expected exception
 }
 }
}