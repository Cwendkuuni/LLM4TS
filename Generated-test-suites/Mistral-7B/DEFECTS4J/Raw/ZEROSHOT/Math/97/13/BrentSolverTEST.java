import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions. scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test file for BrentSolver class
 */
public class BrentSolverTest {

 private BrentSolver solver;
 private ConvergenceChecker checker;
 private ComputableFunction function;

 @Before
 public void setUp() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double xLow, double fLow, double xHigh, double fHigh) {
 return ConvergenceChecker.NEITHER;
 }
 };
 function = new RationalFunction("x^2 - 2");
 }

 @Test
 public void testFindRootSimpleCase() throws FunctionException {
 double x0 = 1.0;
 double x1 = 2.0;
 double root = 1.5;
 assertTrue(solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
 assertTrue(Math.abs(solver.getRoot() - root) < 0.01);
 }

 @Test
 public void testFindRootDivergentCase() throws FunctionException {
 double x0 = 1.0;
 double x1 = 2.0;
 assertFalse(solver.findRoot(new RationalFunction("x^2 + x"), checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
 }

 @Test
 public void testFindRootConvergenceTestPassedLow() throws FunctionException {
 double x0 = 1.0;
 double x1 = 1.1;
 double root = 1.0;
 assertTrue(solver.findRoot(new RationalFunction("x^2 - 2"), checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
 assertTrue(Math.abs(solver.getRoot() - root) < 0.01);
 }

 @Test
 public void testFindRootConvergenceTestPassedHigh() throws FunctionException {
 double x0 = 1.0;
 double x1 = 0.9;
 double root = 1.0;
 assertTrue(solver.findRoot(new RationalFunction("x^2 - 2"), checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
 assertTrue(Math.abs(solver.getRoot() - root) < 0.01);
 }

 @Test
 public void testFindRootConvergenceTestFailed() throws FunctionException {
 double x0 = 1.0;
 double x1 = 2.0;
 double root = 1.5;
 assertFalse(solver.findRoot(new RationalFunction("x^2 - 3"), checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)));
 }
}