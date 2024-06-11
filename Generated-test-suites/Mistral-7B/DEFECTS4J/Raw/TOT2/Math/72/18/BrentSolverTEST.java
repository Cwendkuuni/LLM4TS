// Basic test cases for each method in BrentSolver.java

import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;

public class BrentSolverTest {

 @Test
 public void testConstructor() {
 BrentSolver solver = new BrentSolver();
 assertNotNull(solver);
 }

 @Test
 public void testFindRoot_SimpleFunction() throws FunctionException {
 SimpleFunction function = new SimpleFunction();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xp, double fp) {
 return ConvergenceChecker.NONE;
 }
 };
 BrentSolver solver = new BrentSolver();
 double x0 = 1.0;
 double x1 = 2.0;
 double root = 1.5;
 solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
 assertEquals(root, solver.getRoot(), epsilon);
 }

 @Test
 public void testFindRoot_FunctionException() throws FunctionException {
 SimpleFunction function = new SimpleFunction();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xp, double fp) {
 return ConvergenceChecker.NONE;
 }
 };
 BrentSolver solver = new BrentSolver();
 double x0 = 1.0;
 double x1 = 2.0;
 function.setThrowException(true); // Set the function to throw an exception
 try {
 solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
 fail("Expected FunctionException");
 } catch (FunctionException e) {
 // Expected
 }
 }

 @Test
 public void testFindRoot_ConvergenceFailure() throws FunctionException {
 SimpleFunction function = new SimpleFunction();
 ConvergenceChecker checker = new ConvergenceChecker() {
 @Override
 public int converged(double x, double fx, double xp, double fp) {
 return ConvergenceChecker.HIGH; // High convergence
 }
 };
 BrentSolver solver = new BrentSolver();
 double x0 = 1.0;
 double x1 = 2.0;
 double root = 1.5;
 solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
 assertFalse(solver.isRootFound());
 }

 // ... add more test cases for other methods and constructors

}