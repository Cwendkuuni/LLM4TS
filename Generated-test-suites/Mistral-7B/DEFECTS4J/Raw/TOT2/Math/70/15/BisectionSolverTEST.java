import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

 private static final UnivariateRealFunction FUNCTION = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 2;
 }
 };

 @Test
 public void testSolve_Basic() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 double result = solver.solve(FUNCTION, -3.0, 3.0);
 assertEquals("The root should be approximately -1.0", -1.0, result, 1e-6);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolve_MaxIterationsExceeded() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 solver.setMaximalIterationCount(0);
 solver.solve(FUNCTION, -1.0, 1.0);
 fail("Max iterations should be exceeded");
 }

 @Test(expected = ZeroException.class)
 public void testSolve_ZeroFunctionValue() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 UnivariateRealFunction zeroFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return 0;
 }
 };
 try {
 solver.solve(zeroFunction, -1.0, 1.0);
 fail("A function with zero value at given interval should throw ZeroException");
 } catch (ZeroException e) {
 // Expected exception
 }
 }

 @Test
 public void testSolve_DifferentSigns() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 double result = solver.solve(FUNCTION, -3.0, 2.0);
 assertNotEquals("The root should not be in the given interval", -1.0, result, 1e-6);
 }

 @Test
 public void testSolve_SameSigns() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 double result = solver.solve(FUNCTION, 2.0, 3.0);
 assertNotEquals("The root should not be in the given interval", -1.0, result, 1e-6);
 }

 @Test
 public void testSolve_VerySmallInterval() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 double result = solver.solve(FUNCTION, -1.0, -1.0 + 1e-8);
 assertTrue("The root should be found within the interval", Precision.abs(result - (-1.0)) <= 1e-6);
 }

 @Test
 public void testSolve_VeryLargeInterval() throws MaxIterationsExceededException, PrecisionException, ZeroException {
 BisectionSolver solver = new BisectionSolver();
 double result = solver.solve(FUNCTION, -10.0, 10.0);
 assertTrue("The root should be found within the interval", Precision.abs(result - (-1.0)) <= 1e-6);
 }
}