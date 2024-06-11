import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar. ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 @Test
 public void testConstructor() {
 BrentSolver brentSolver = new BrentSolver();
 Assert.assertEquals(Double.NaN, brentSolver.getRoot(), 0.0);
 }

 @Test(expected = FunctionException.class)
 public void testFindRootWithException() throws FunctionException {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 throw new FunctionException("Exception message");
 }
 };
 BrentSolver brentSolver = new BrentSolver();
 brentSolver.findRoot(function, new ConvergenceChecker() {
 @Override
 public int converged(double xLow, double fLow, double xHigh, double fHigh) {
 return ConvergenceChecker.LOW;
 }
 }, 1, 0.0, 0.0, 0.0, 0.0);
 }

 @Test
 public void testFindRootSuccess() throws FunctionException {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 4;
 }
 };
 BrentSolver brentSolver = new BrentSolver();
 ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
 @Override
 public int converged(double xLow, double fLow, double xHigh, double fHigh) {
 if ((Math.abs(xLow - xHigh) < 0.001) || (Math.abs(fLow) < 0.001)) {
 return ConvergenceChecker.BOTH;
 }
 return ConvergenceChecker.NONE;
 }
 };
 double root = brentSolver.findRoot(function, convergenceChecker, 10, -2.0, -1.0, 2.0, 4.0)[0];
 Assert.assertNotNull(root);
 Assert.assertEquals(-1.21710955551775, root, 0.001);
 }

 @Test
 public void testFindRootFailure() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 4;
 }
 };
 BrentSolver brentSolver = new BrentSolver();
 ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
 @Override
 public int converged(double xLow, double fLow, double xHigh, double fHigh) {
 return ConvergenceChecker.NONE;
 }
 };
 boolean result = brentSolver.findRoot(function, convergenceChecker, 10, -2.0, -1.0, 2.0, 4.0)[0];
 Assert.assertFalse(result);
 }
}