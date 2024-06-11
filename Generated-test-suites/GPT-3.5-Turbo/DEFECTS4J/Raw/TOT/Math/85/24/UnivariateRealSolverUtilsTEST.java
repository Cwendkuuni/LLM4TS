import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.*;

public class UnivariateRealSolverUtilsTest {

   @Test
   public void testSolve_WithValidParameters_ShouldReturnZero() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x == 2.0)
               return 0.0;
            else
               return x - 2.0;
         }
      };
      
      double x0 = 1.0;
      double x1 = 3.0;
      
      double result = UnivariateRealSolverUtils.solve(f, x0, x1);
      
      assertEquals(2.0, result, 0.0001);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testSolve_WithNullFunction_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = null;
      double x0 = 1.0;
      double x1 = 3.0;
      
      UnivariateRealSolverUtils.solve(f, x0, x1);
   }
   
   @Test(expected = FunctionEvaluationException.class)
   public void testSolve_WithInvalidFunction_ShouldThrowFunctionEvaluationException() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
         }
      };
      
      double x0 = 1.0;
      double x1 = 3.0;
      
      UnivariateRealSolverUtils.solve(f, x0, x1);
   }
   
   @Test
   public void testSolve_WithValidParametersAndAccuracy_ShouldReturnZero() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x == 2.0)
               return 0.0;
            else
               return x - 2.0;
         }
      };
      
      double x0 = 1.0;
      double x1 = 3.0;
      double absoluteAccuracy = 0.0001;
      
      double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
      
      assertEquals(2.0, result, absoluteAccuracy);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testSolve_WithInvalidAccuracy_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x == 2.0)
               return 0.0;
            else
               return x - 2.0;
         }
      };
      
      double x0 = 1.0;
      double x1 = 3.0;
      double absoluteAccuracy = -0.0001;
      
      UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
   }
   
   @Test
   public void testBracket_WithValidParameters_ShouldReturnBracket() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x >= -2.0 && x <= 2.0)
               return -1.0;
            else
               return x;
         }
      };
      
      double initial = 1.0;
      double lowerBound = -1.0;
      double upperBound = 3.0;
      
      double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
      
      assertEquals(-3.0, result[0], 0.0001);
      assertEquals(4.0, result[1], 0.0001);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testBracket_WithNullFunction_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = null;
      double initial = 1.0;
      double lowerBound = -1.0;
      double upperBound = 3.0;
      
      UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testBracket_WithInvalidParameters_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x >= -2.0 && x <= 2.0)
               return -1.0;
            else
               return x;
         }
      };
      
      double initial = 4.0;
      double lowerBound = -1.0;
      double upperBound = 3.0;
      
      UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
   }
   
   @Test
   public void testBracket_WithValidParametersAndMaxIterations_ShouldReturnBracket() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x >= -2.0 && x <= 2.0)
               return -1.0;
            else
               return x;
         }
      };
      
      double initial = 1.0;
      double lowerBound = -1.0;
      double upperBound = 3.0;
      int maximumIterations = 10;
      
      double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
      
      assertEquals(-3.0, result[0], 0.0001);
      assertEquals(4.0, result[1], 0.0001);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testBracket_WithInvalidMaxIterations_ShouldThrowIllegalArgumentException() throws ConvergenceException, FunctionEvaluationException {
      UnivariateRealFunction f = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            if (x >= -2.0 && x <= 2.0)
               return -1.0;
            else
               return x;
         }
      };
      
      double initial = 1.0;
      double lowerBound = -1.0;
      double upperBound = 3.0;
      int maximumIterations = -10;
      
      UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
   }
   
   @Test
   public void testMidpoint_WithValidParameters_ShouldReturnMidpoint() {
      double a = -3.0;
      double b = 4.0;
      
      double result = UnivariateRealSolverUtils.midpoint(a, b);
      
      assertEquals(0.5, result, 0.0001);
   }

}