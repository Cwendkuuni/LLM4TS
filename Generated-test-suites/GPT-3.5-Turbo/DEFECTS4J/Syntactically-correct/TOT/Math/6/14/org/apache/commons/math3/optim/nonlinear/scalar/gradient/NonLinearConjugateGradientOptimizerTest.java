import org.junit.Test;
import org.apache.commons.math3.optimization.general.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.TestFunction;

public class NonLinearConjugateGradientOptimizerTest {

  @Test
  public void testOptimize_Minimize() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    TestFunction function = new TestFunction();
    optimizer.setMaxEvaluations(1000);
    optimizer.setConvergenceChecker(new SimpleValueChecker(1e-9, 1e-9));

    PointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, new double[] {1.0});
    assertNotNull(result);
    assertEquals(GoalType.MINIMIZE, result.getGoalType());
    assertEquals(function.getMinValue(), result.getValue(), 1e-9);
    assertArrayEquals(function.getMinPoint(), result.getPoint(), 1e-9);
  }

  @Test
  public void testOptimize_Maximize() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    TestFunction function = new TestFunction();
    optimizer.setMaxEvaluations(1000);
    optimizer.setConvergenceChecker(new SimpleValueChecker(1e-9, 1e-9));

    PointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, new double[] {1.0});
    assertNotNull(result);
    assertEquals(GoalType.MAXIMIZE, result.getGoalType());
    assertEquals(function.getMaxValue(), result.getValue(), 1e-9);
    assertArrayEquals(function.getMaxPoint(), result.getPoint(), 1e-9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOptimize_NullObjectiveFunction() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setMaxEvaluations(1000);
    optimizer.setConvergenceChecker(new SimpleValueChecker(1e-9, 1e-9));

    optimizer.optimize(null, GoalType.MINIMIZE, new double[] {1.0});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOptimize_NullGoalType() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    TestFunction function = new TestFunction();
    optimizer.setMaxEvaluations(1000);
    optimizer.setConvergenceChecker(new SimpleValueChecker(1e-9, 1e-9));

    optimizer.optimize(function, null, new double[] {1.0});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOptimize_NullStartPoint() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    TestFunction function = new TestFunction();
    optimizer.setMaxEvaluations(1000);
    optimizer.setConvergenceChecker(new SimpleValueChecker(1e-9, 1e-9));

    optimizer.optimize(function, GoalType.MINIMIZE, null);
  }

  @Test(expected = MathIllegalStateException.class)
  public void testOptimize_NoBracket() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    TestFunction function = new TestFunction();
    optimizer.setMaxEvaluations(1000);
    optimizer.setConvergenceChecker(new SimpleValueChecker(1e-9, 1e-9));

    optimizer.optimize(function, GoalType.MINIMIZE, new double[] {Double.NaN});
  }
}