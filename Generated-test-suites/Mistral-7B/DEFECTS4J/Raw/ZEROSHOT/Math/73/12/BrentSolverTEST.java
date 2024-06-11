import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
* Test class for BrentSolver
*/
public class BrentSolverTest {

 @Test
 public void testFindRoot() throws Exception {
 // Test case 1: x^2 - 1 = 0 in [0, 1]
 RationalFunction function = new RationalFunction("1", "x", "x", "2");
 BrentSolver brentSolver = new BrentSolver();
 ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };

 double root = brentSolver.findRoot(function, convergenceChecker, 100, 0.0, 1.0, 0.0, 1.0) ? brentSolver.getRoot() : Double.NaN;
 assertEquals(1.0, root, epsilon);

 // Test case 2: x^2 - 4 = 0 in [-2, 2]
 function = new RationalFunction("1", "x", "x", "4");
 brentSolver = new BrentSolver();
 convergenceChecker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };

 root = brentSolver.findRoot(function, convergenceChecker, 100, -2.0, 2.0, -2.0, 4.0) ? brentSolver.getRoot() : Double.NaN;
 assertEquals(0.0, root, epsilon);

 // Test case 3: x^3 - 2 = 0 in [-2, 1]
 RationalFunction functionCubic = new RationalFunction("1", "x", "x", "3", "2");
 brentSolver = new BrentSolver();
 convergenceChecker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };

 root = brentSolver.findRoot(functionCubic, convergenceChecker, 100, -2.0, 1.0, -2.0, 1.5) ? brentSolver.getRoot() : Double.NaN;
 assertEquals(-1.0, root, epsilon);

 // Test case 4: x^3 + x - 1 = 0 in [-1.5, 1.5]
 RationalFunction functionCubicPlusOne = new RationalFunction("1", "x", "x", "3", "1", "-1");
 brentSolver = new BrentSolver();
 convergenceChecker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.NOT_CONVERGED;
 }
 };

 root = brentSolver.findRoot(functionCubicPlusOne, convergenceChecker, 100, -1.5, 1.5, -1.5, 1.5) ? brentSolver.getRoot() : Double.NaN;
 assertTrue(Math.abs(root - 1.0) < epsilon);
 }

}