import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

 private UnivariateRealFunction function;
 private BisectionSolver solver;

 @Before
 public void setUp() {
 function = new Function("f(x) = x^2 - 4");
 solver = new BisectionSolver();
 }

 /**
 * Test constructor with valid function.
 */
 @Test
 public void testValidFunction() {
 solver.solve(function, -2.0, 2.0);
 assertEquals(0.0, solver.getResult().getValue(), 1e-6);
 }

 /**
 * Test constructor with null function.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testNullFunction() {
 new BisectionSolver(null);
 }

 /**
 * Test constructor with invalid function (function returns infinities).
 */
 @Test(expected = FunctionEvaluationException.class)
 public void testInvalidFunction() {
 UnivariateRealFunction invalidFunction = new Function("f(x) = 1/x");
 new BisectionSolver(invalidFunction);
 }

 /**
 * Test deprecated constructor with valid function.
 */
 @Test
 public void testDeprecatedConstructorValidFunction() {
 solver.solve(function, -2.0, 2.0);
 assertEquals(0.0, solver.getResult().getValue(), 1e-6);
 }

 /**
 * Test deprecated constructor with null function.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testDeprecatedConstructorNullFunction() {
 solver = new BisectionSolver(null);
 }

 /**
 * Test deprecated constructor with invalid function (function returns infinities).
 */
 @Test(expected = FunctionEvaluationException.class)
 public void testDeprecatedConstructorInvalidFunction() {
 UnivariateRealFunction invalidFunction = new Function("f(x) = 1/x");
 solver = new BisectionSolver(invalidFunction);
 }

 /**
 * Test solver with valid function, minimal accuracy.
 */
 @Test
 public void testMinimalAccuracy() {
 solver.setMaximalIterationCount(1);
 solver.solve(function, -2.0, 2.0);
 assertNotNull(solver.getResult());
 assertFalse(solver.isConverged());
 }

 /**
 * Test solver with valid function, maximal iterations.
 */
 @Test(expected = MaxIterationsExceededException.class)
 public void testMaxIterations() {
 solver.setMaximalIterationCount(0);
 solver.solve(function, -2.0, 2.0);
 }

 /**
 * Test solver with valid function, bracketing failure.
 */
 @Test(expected = FunctionEvaluationException.class)
 public void testBracketingFailure() {
 solver.solve(new Function("f(x) = x^2 - 4"), 2.0, 2.0);
 }

 /**
 * Test solver with valid function, convergence.
 */
 @Test
 public void testConvergence() {
 solver.solve(function, -3.0, 3.0);
 assertEquals(0.0, solver.getResult().getValue(), 1e-6);
 }

 /**
 * Test solver with valid function, convergence at boundary 1.
 */
 @Test
 public void testConvergenceAtBoundary1() {
 solver.solve(new Function("f(x) = x^2 - 4"), -2.0, 2.0);
 assertEquals(0.0, solver.getResult().getValue(), 1e-6);
 }

 /**
 * Test solver with valid function, convergence at boundary 2.
 */
 @Test
 public void testConvergenceAtBoundary2() {
 solver.solve(new Function("f(x) = x^2 - 4"), -2.0, 0.0);
 assertEquals(2.0, solver.getResult().getValue(), 1e-6);
 }

 /**
 * Test solver with valid function, convergence at root.
 */
 @Test
 public void testConvergenceAtRoot() {
 solver.solve(new Function("f(x) = x^2 - 4"), -2.0, 0.0);
 assertEquals(0.0, solver.getResult().getValue(), 1e-6);
 }
}

class Function implements UnivariateRealFunction {
 private final String name;

 public Function(String name) {
 this.name = name;
 }

 @Override
 public double value(double x) {
 System.out.println("Computing " + name + " for x = " + x);
 switch (name) {
 case "f(x) = x^2 - 4":
 return x * x - 4;
 case "1/x":
 if (x == 0) {
 throw new ArithmeticException("Division by zero");
 }
 return 1.0 / x;
 default:
 throw new AssertionError("Invalid function: " + name);
 }
 }
}