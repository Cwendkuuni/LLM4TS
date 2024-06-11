import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar. ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 private ComputableFunction function;
 private ConvergenceChecker checker;
 private BrentSolver solver;

 @Test
 public void testFindRoot_SimpleCase() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = Math.pow(x0, 2) - 2;
 double x1 = 2.0;
 double f1 = Math.pow(x1, 2) - 2;

 Assert.assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
 Assert.assertNotEqual(Double.NaN, solver.getRoot());
 }

 @Test
 public void testFindRoot_Convergence() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.exp(1.0 / x) - 1.0;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return Math.abs(fx) < 0.001 ? ConvergenceChecker.LOW : ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = Math.exp(1.0 / x0) - 1.0;
 double x1 = 2.0;
 double f1 = Math.exp(1.0 / x1) - 1.0;

 Assert.assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
 Assert.assertNotEqual(Double.NaN, solver.getRoot());
 }

 @Test(expected = FunctionException.class)
 public void testFindRoot_FunctionException() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 throw new FunctionException("Function error");
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = Double.NaN; // should cause FunctionException
 double x1 = 2.0;
 double f1 = 1.0;

 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (FunctionException e) {
 // expected
 }
 Assert.assertEquals(Double.NaN, solver.getRoot());
 }

 @Test(expected = FunctionException.class)
 public void testFindRoot_FunctionException2() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 throw new FunctionException("Function error");
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = 2.0;
 double f1 = Math.exp(1.0 / x1) - 1.0;

 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (FunctionException e) {
 // expected
 }
 Assert.assertEquals(Double.NaN, solver.getRoot());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return 0.0;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 try {
 solver.findRoot(function, checker, 100, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException2() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return 0.0;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = Double.NaN;
 double f0 = Double.NaN;
 double x1 = 1.0;
 double f1 = 1.0;

 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException3() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return 0.0;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = Double.NaN;
 double f1 = 1.0;

 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException4() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return 0.0;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = 1.0;
 double f1 = Double.NaN;

 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException5() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return 0.0;
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = 1.0;
 double f1 = 1.0;
 double maxIter = -1;

 try {
 solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException6() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 try {
 solver.findRoot(null, checker, 100, 1.0, 0.0, 2.0, 0.0);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException7() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = null;
 BrentSolver solver = new BrentSolver();

 try {
 solver.findRoot(function, checker, 100, 1.0, 0.0, 2.0, 0.0);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException8() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 double maxIter = -1;
 try {
 solver.findRoot(function, checker, maxIter, 1.0, 0.0, 2.0, 0.0);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException9() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 double x0 = Double.NaN;
 double f0 = Double.NaN;
 try {
 solver.findRoot(function, checker, 100, x0, f0, 1.0, 0.0);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException10() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = Double.NaN;
 try {
 solver.findRoot(function, checker, 100, x0, f0, 1.0, 0.0);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException11() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = Double.NaN;
 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, 0.0);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException12() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = 1.0;
 double f1 = Double.NaN;
 try {
 solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindRoot_IllegalArgumentException13() {
 ComputableFunction function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.pow(x, 2) - 2;
 }
 };
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 BrentSolver solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = 1.0;
 double x1 = 1.0;
 double f1 = 1.0;
 int maxIter = -1;
 try {
 solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testFindRoot_NoRootFound() {
 function = new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 return Math.sin(x);
 }
 };
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double x2, double fx2) {
 return ConvergenceChecker.UNKNOWN;
 }
 };
 solver = new BrentSolver();

 double x0 = 0.0;
 double f0 = Math.sin(x0);
 double x1 = 0.1;
 double f1 = Math.sin(x1);

 Assert.assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
 Assert.assertEquals(Double.NaN, solver.getRoot());
 }

}