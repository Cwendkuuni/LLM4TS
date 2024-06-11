import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

   @Test
   public void testConstructor_validArgs() {
      BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
      assertNotNull(optimizer);
   }

   @Test(expected = NumberIsTooSmallException.class)
   public void testConstructor_invalidRelativeThreshold() {
      BrentOptimizer optimizer = new BrentOptimizer(-0.1, 0.01);
   }

   @Test(expected = NotStrictlyPositiveException.class)
   public void testConstructor_invalidAbsoluteThreshold() {
      BrentOptimizer optimizer = new BrentOptimizer(0.1, -0.01);
   }

   @Test
   public void testDoOptimize_minimizationGoalType() {
      BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
      optimizer.setMin(0);
      optimizer.setStartValue(2);
      optimizer.setMax(4);
      optimizer.setGoalType(GoalType.MINIMIZE);
      UnivariatePointValuePair result = optimizer.doOptimize();

      assertNotNull(result);
      assertEquals(2, result.getPoint(), 0.01);
      // Add additional assertions for the objective function value
   }

   @Test
   public void testDoOptimize_maximizationGoalType() {
      BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
      optimizer.setMin(0);
      optimizer.setStartValue(2);
      optimizer.setMax(4);
      optimizer.setGoalType(GoalType.MAXIMIZE);
      UnivariatePointValuePair result = optimizer.doOptimize();

      assertNotNull(result);
      assertEquals(2, result.getPoint(), 0.01);
      // Add additional assertions for the objective function value
   }

   // Add more test cases for each method in BrentOptimizer

}