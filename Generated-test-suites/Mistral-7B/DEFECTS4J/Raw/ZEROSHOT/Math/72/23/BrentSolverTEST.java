import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.*;

public class BrentSolverTest {

 private BrentSolver solver;
 private ConvergenceChecker checker;
 private ComputableFunction function;

 @Before
 public void setUp() {
 solver = new BrentSolver();
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x1, double f1, double x2, double f2) {
 return ConvergenceChecker.NEITHER;
 }
 };
 function = new FunctionUtils.SineFunction();
 }

 @After
 public void tearDown() {
 solver = null;
 checker = null;
 function = null;
 }

 @Test
 public void testFindRoot() throws FunctionException {
 // Test with a simple function: f(x) = sin(x)
 double x0 = 1.0;
 double f0 = Math.sin(x0);
 double x1 = 2.0;
 double f1 = Math.sin(x1);
 int maxIter = 100;

 assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
 double root = solver.getRoot();
 assertEquals(Math.PI / 2.0, root, 0.001);
 }

 @Test(expected = FunctionException.class)
 public void testFindRootException() throws FunctionException {
 // Test with a function that has no root in the given interval
 function = new FunctionUtils.FunctionWithNoRoots();
 x0 = 1.0;
 f0 = function.valueAt(x0);
 x1 = 2.0;
 f1 = function.valueAt(x1);
 maxIter = 100;

 try {
 solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 fail("FunctionException expected");
 } catch (FunctionException e) {
 // Expected
 }
 }

 @Test
 public void testFindRootWithConvergenceChecker() throws FunctionException {
 // Test that the solver stops when the convergence criterion is met
 checker = new ConvergenceChecker() {
 @Override
 public int converged(double x1, double f1, double x2, double f2) {
 if (Math.abs(x2 - x1) < 0.0001) {
 return ConvergenceChecker.LOW;
 }
 if (Math.abs(f2) < 1e-6) {
 return ConvergenceChecker.HIGH;
 }
 return ConvergenceChecker.NEITHER;
 }
 };

 x0 = 1.0;
 f0 = Math.exp(x0);
 x1 = 2.0;
 f1 = Math.exp(x1);
 maxIter = 100;

 assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
 double root = solver.getRoot();
 assertEquals(Math.E, root, 0.001);
 }

}