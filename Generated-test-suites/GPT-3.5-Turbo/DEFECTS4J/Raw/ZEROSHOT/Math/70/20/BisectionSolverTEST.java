import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {
   
   private BisectionSolver solver;
   
   @Before
   public void setUp() {
      solver = new BisectionSolver();
   }
   
   @Test
   public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
      UnivariateRealFunction function = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            return (x - 2) * (x + 3); // (x-2)(x+3) = x^2 + x - 6
         }
      };
      
      double root = solver.solve(function, -10, 10);
      
      assertEquals(2.0, root, 1E-6);
   }
   
   @Test
   public void testSolveWithInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
      UnivariateRealFunction function = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            return (x - 2) * (x + 3); // (x-2)(x+3) = x^2 + x - 6
         }
      };
      
      double root = solver.solve(function, -10, 10, 5);
      
      assertEquals(2.0, root, 1E-6);
   }
   
   @Test(expected=MaxIterationsExceededException.class)
   public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
      UnivariateRealFunction function = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            return x * x - 4; // x^2 - 4 = 0, when x = ±2
         }
      };
      
      double root = solver.solve(function, -10, 10);
   }
   
   @Test(expected=FunctionEvaluationException.class)
   public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
      UnivariateRealFunction function = new UnivariateRealFunction() {
         public double value(double x) throws FunctionEvaluationException {
            throw new FunctionEvaluationException(x);
         }
      };
      
      double root = solver.solve(function, -10, 10);
   }
}