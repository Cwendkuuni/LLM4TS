import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnSupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

 @Test
 public void testBisectionSolver() {
 BisectionSolver solver = new BisectionSolver();

 // Test with a simple function
 UnivariateRealFunction function1 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 2;
 }

 @Override
 public int getNumberOfDerivatives() {
 return 0;
 }
 };

 try {
 double root = solver.solve(function1, -3.0, 3.0);
 assertEquals(0.0, root, Precision.EPSILON);
 } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
 throw new RuntimeException(e);
 }

 // Test with a function that is not bracketing the root
 UnivariateRealFunction function2 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }

 @Override
 public int getNumberOfDerivatives() {
 return 0;
 }
 };

 try {
 solver.solve(function2, -2.0, 0.0);
 } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
 // expected
 }

 // Test with a function that is not continuous
 UnivariateRealFunction function3 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 if (x >= 0) {
 return Math.sqrt(Math.abs(x));
 } else {
 throw new UnSupportedOperationException("x should be non-negative");
 }
 }

 @Override
 public int getNumberOfDerivatives() {
 return 0;
 }
 };

 try {
 solver.solve(function3, -1.0, 1.0);
 } catch (UnsupportedOperationException e) {
 // expected
 } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
 throw new RuntimeException(e);
 }

 // Test with a function that has a root with small absolute value
 UnivariateRealFunction function4 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 0.001;
 }

 @Override
 public int getNumberOfDerivatives() {
 return 0;
 }
 };

 try {
 solver.solve(function4, -1.0, 1.0);
 } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
 throw new RuntimeException(e);
 }

 double result = solver.getResult();
 assertEquals(0.0, result, 0.0001, "The root should be very close to zero");
 }
}