import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionTestUtils;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 @Test
 public void testFindRoot_SineFunction_SimpleInterval() throws Exception {
 SineFunction function = new SineFunction();

 double x0 = 1.0;
 double f0 = Math.sin(x0);
 double x1 = 3.0;
 double f1 = Math.sin(x1);

 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xPrev, double fPrev) {
 return (fPrev == 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
 }
 };

 BrentSolver solver = new BrentSolver();
 boolean result = solver.findRoot(function, checker, 10, x0, f0, x1, f1);

 Assert.assertTrue(result);
 Assert.assertEquals(Math.PI, solver.getRoot(), epsilon);
 }

 @Test(expected = FunctionException.class)
 public void testFindRoot_SineFunction_DivergentFunction() throws Exception {
 SineFunction function = new SineFunction();

 double x0 = 1.0;
 double f0 = Math.sin(x0);
 double x1 = 2.0;
 double f1 = Double.NaN;

 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xPrev, double fPrev) {
 return ConvergenceChecker.NONE;
 }
 };

 BrentSolver solver = new BrentSolver();
 solver.findRoot(function, checker, 10, x0, f0, x1, f1);
 }

 @Test(expected = FunctionException.class)
 public void testFindRoot_SineFunction_FunctionThrowsException() throws Exception {
 SineFunction function = new SineFunction() {
 @Override
 public double valueAt(double x) throws FunctionException {
 throw new FunctionException("Exception");
 }
 };

 double x0 = 1.0;
 double f0 = Math.sin(x0);
 double x1 = 3.0;
 double f1 = Math.sin(x1);

 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double f, double xPrev, double fPrev) {
 return ConvergenceChecker.NONE;
 }
 };

 BrentSolver solver = new BrentSolver();
 solver.findRoot(function, checker, 10, x0, f0, x1, f1);
 }

}