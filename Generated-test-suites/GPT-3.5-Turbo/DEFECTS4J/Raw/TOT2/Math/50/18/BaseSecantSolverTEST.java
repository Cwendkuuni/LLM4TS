import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {
   
   @Test
   public void testSolve_ExactRoot() {
       // Test when the exact root is found
       BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) {
               return x - 5; // The root is x = 5
           }
       };
       double result = solver.solve(100, f, 0, 10);
       assertEquals(5.0, result, 0.0);
   }
   
   @Test
   public void testSolve_FunctionValueAccuracy() {
       // Test when function value accuracy is reached
       BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) {
               return x - 5; // The root is x = 5
           }
       };
       double result = solver.solve(100, f, 0, 10);
       assertEquals(5.0, result, 0.0);
   }
   
   @Test
   public void testSolve_AccuraciesReached() {
       // Test when absolute and relative accuracies are reached
       BaseSecantSolver solver = new ConcreteSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
       UnivariateRealFunction f = new UnivariateRealFunction() {
           public double value(double x) {
               return x - 5; // The root is x = 5
           }
       };
       double result = solver.solve(100, f, 0, 10);
       assertEquals(5.0, result, 0.0);
   }
}