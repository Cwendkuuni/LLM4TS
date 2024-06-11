import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

   @Test
   public void testSetSimplex() {
      AbstractSimplex simplex = new AbstractSimplex();
      SimplexOptimizer optimizer = new SimplexOptimizer();
      optimizer.setSimplex(simplex);
      assertEquals(simplex, optimizer.getSimplex());
   }

   @Test
   public void testOptimizeInternal() {
      int maxEval = 1000;
      MultivariateFunction f = new MultivariateFunction() {
         public double value(double[] point) {
            return point[0] + point[1];
         }
      };
      GoalType goalType = GoalType.MINIMIZE;
      OptimizationData[] optData = {};
      SimplexOptimizer optimizer = new SimplexOptimizer();
      PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
      assertNotNull(result);
   }

   @Test
   public void testDoOptimize() {
      AbstractSimplex simplex = new AbstractSimplex();
      ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
      SimplexOptimizer optimizer = new SimplexOptimizer(checker);
      optimizer.setSimplex(simplex);
      PointValuePair result = optimizer.doOptimize();
      assertNotNull(result);
   }

}