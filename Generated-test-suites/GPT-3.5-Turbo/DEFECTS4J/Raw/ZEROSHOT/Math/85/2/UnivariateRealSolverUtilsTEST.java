import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

   @Test
   public void testSolve() {
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) throws FunctionEvaluationException {
               return x * x - 4;
           }
       };

       double x0 = 0;
       double x1 = 5;

       try {
           double result = UnivariateRealSolverUtils.solve(f, x0, x1);
           Assert.assertTrue(result > 0 && result < 2);
       } catch (ConvergenceException | FunctionEvaluationException e) {
           Assert.fail(e.toString());
       }
   }

   @Test
   public void testSolveWithAccuracy() {
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) throws FunctionEvaluationException {
               return x - 2;
           }
       };

       double x0 = 0;
       double x1 = 5;
       double absoluteAccuracy = 0.01;

       try {
           double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
           Assert.assertEquals(2.0, result, absoluteAccuracy);
       } catch (ConvergenceException | FunctionEvaluationException e) {
           Assert.fail(e.toString());
       }
   }

   @Test
   public void testBracket() {
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) throws FunctionEvaluationException {
               return x * x - 4;
           }
       };

       double initial = 1;
       double lowerBound = 0;
       double upperBound = 5;

       try {
           double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
           Assert.assertTrue(result[0] > lowerBound && result[0] < initial);
           Assert.assertTrue(result[1] > initial && result[1] < upperBound);
       } catch (ConvergenceException | FunctionEvaluationException e) {
           Assert.fail(e.toString());
       }
   }

   @Test
   public void testBracketWithMaxIterations() {
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) throws FunctionEvaluationException {
               return x - 2;
           }
       };

       double initial = 1;
       double lowerBound = 0;
       double upperBound = 5;
       int maximumIterations = 100;

       try {
           double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
           Assert.assertTrue(result[0] > lowerBound && result[0] < initial);
           Assert.assertTrue(result[1] > initial && result[1] < upperBound);
       } catch (ConvergenceException | FunctionEvaluationException e) {
           Assert.fail(e.toString());
       }
   }

   @Test
   public void testMidpoint() {
       double a = 0;
       double b = 2;

       double result = UnivariateRealSolverUtils.midpoint(a, b);
       Assert.assertEquals(1.0, result, 0.0);
   }
   
}