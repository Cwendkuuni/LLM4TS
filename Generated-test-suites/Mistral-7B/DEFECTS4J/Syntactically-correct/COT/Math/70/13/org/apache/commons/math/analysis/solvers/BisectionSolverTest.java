package org.apache.commons.math.analysis.solvers.test;

 import org.apache.commons.math3.analysis.UnivariateRealFunction;
 import org.apache.commons.math3.analysis.solvers.BisectionSolver;
 import org.apache.commons.math3.exception.MaxIterationsExceededException;
 import org.apache.commons.math3.exception.NumberIsTooSmallException;
 import org.junit.Test;
 import static org.junit.Assert.*;

 public class BisectionSolverTest {

 @Test
 public void testConstructor() {
 BisectionSolver bisectionSolver = new BisectionSolver();
 assertNotNull(bisectionSolver);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullFunction() {
 new BisectionSolver(null);
 }

 @Test
 public void testSolveBasic() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 2;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 double root = bisectionSolver.solve(function, -3.0, 3.0);
 assertEquals(0.0, root, 1e-6);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testSolveFunctionEvalException() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 throw new FunctionEvaluationException("Function evaluation error");
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 bisectionSolver.solve(function, 0.0, 1.0);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolveMaxIterationsExceeded() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) / x;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 try {
 bisectionSolver.solve(function, 0.0, 1.0, 0.0);
 fail("Should have thrown MaxIterationsExceededException");
 } catch (MaxIterationsExceededException e) {
 // expected
 }
 }

 @Test
 public void testSolveNegativeMin() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + 1;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 try {
 bisectionSolver.solve(function, -1.0, 1.0);
 fail("Should have thrown IllegalArgumentException for negative minimum");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSolveNegativeMax() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) + 1;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 try {
 bisectionSolver.solve(function, 1.0, -1.0);
 fail("Should have thrown IllegalArgumentException for negative maximum");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSolveMinGreaterMax() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 try {
 bisectionSolver.solve(function, 2.0, 1.0);
 fail("Should have thrown IllegalArgumentException for minimum greater than maximum");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSolveZeroFunction() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return 0.0;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 double root = bisectionSolver.solve(function, -1.0, 1.0);
 assertEquals(0.0, root, 1e-6);
 }

 @Test
 public void testSolveZeroMin() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 try {
 bisectionSolver.solve(function, 0.0, 1.0);
 fail("Should have thrown NumberIsTooSmallException for zero minimum");
 } catch (NumberIsTooSmallException e) {
 // expected
 }
 }

 @Test
 public void testSolveZeroMax() {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x;
 }
 };
 BisectionSolver bisectionSolver = new BisectionSolver();
 try {
 bisectionSolver.solve(function, 1.0, 0.0);
 fail("Should have thrown NumberIsTooSmallException for zero maximum");
 } catch (NumberIsTooSmallException e) {
 // expected
 }
 }
 }