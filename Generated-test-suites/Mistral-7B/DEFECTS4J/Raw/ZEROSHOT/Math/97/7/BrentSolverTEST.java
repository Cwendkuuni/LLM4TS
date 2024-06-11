import org.junit.Before;import org.junit.Test;import org.junit.Assert;import org.spaceroots.mantissa.functions.scalar.FunctionUtils;import org.spaceroots.mantissa.functions.scalar.RationalFunction;

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
 if (Math.abs(f1) < 0.0001 && Math.abs(f2) < 0.0001) {
 return ConvergenceChecker.BOTH;
 }
 if (Math.abs(x1 - x2) < 0.0001) {
 return ConvergenceChecker.BOTH;
 }
 if (Math.abs(f1) < Math.abs(f2)) {
 return ConvergenceChecker.LOW;
 } else {
 return ConvergenceChecker.HIGH;
 }
 }
 };
 function = new RationalFunction("2 * x^3 - 3 * x^2 + 2 * x - 1");
 }

 @Test
 public void testFindRoot() throws FunctionException {
 double x0 = 1.0;
 double x1 = 2.0;
 double expectedRoot = -0.4828276086956524;

 solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

 Assert.assertEquals("Root not found", expectedRoot, solver.getRoot(), 0.001);
 }

 @Test(expected = FunctionException.class)
 public void testFindRootWithException() throws FunctionException {
 double x0 = 1.0;
 double x1 = 2.0;
 double x2 = 3.0;
 int maxIter = 100;

 solver.findRoot(new ComputableFunction() {
 @Override
 public double valueAt(double x) {
 throw new ArithmeticException("Test exception");
 }
 }, checker, maxIter, x0, Double.NaN, x1, Double.NaN);
 }

 @Test
 public void testFindRootInvalidInterval() throws FunctionException {
 double x0 = 1.0;
 double x1 = 0.5;
 double expectedRoot = 1.5;

 solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

 Assert.assertEquals("Root not found", expectedRoot, solver.getRoot(), 0.001);
 }
}