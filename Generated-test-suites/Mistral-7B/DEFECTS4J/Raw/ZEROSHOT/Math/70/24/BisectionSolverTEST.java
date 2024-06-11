import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

 @Test
 public void testBisectionSolver() throws FunctionEvaluationException, MaxIterationsExceededException {

 // Test case 1: f(x) = x^2 - 1
 UnivariateRealFunction function1 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };

 double[] expectedResult1 = {0.0};
 double min1 = -2.0;
 double max1 = 2.0;
 double initial1 = 1.0;
 double result1 = new BisectionSolver().solve(function1, min1, max1, initial1);
 assertArrayEquals(expectedResult1, new double[] {result1});

 // Test case 2: f(x) = exp(x) - x - 1
 UnivariateRealFunction function2 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.exp(x) - x - 1;
 }
 };

 double[] expectedResult2 = {0.6321205501263743};
 double min2 = -3.0;
 double max2 = 0.5;
 double initial2 = 1.0;
 double result2 = new BisectionSolver().solve(function2, min2, max2, initial2);
 assertEquals(expectedResult2[0], result2, 0.00001);

 // Test case 3: f(x) = sin(x) - x
 UnivariateRealFunction function3 = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) - x;
 }
 };

 double[] expectedResult3 = {0.436113514035641};
 double min3 = -3.0;
 double max3 = 3.0;
 double initial3 = 1.0;
 double result3 = new BisectionSolver().solve(function3, min3, max3, initial3);
 assertEquals(expectedResult3[0], result3, 0.00001);
 }
}