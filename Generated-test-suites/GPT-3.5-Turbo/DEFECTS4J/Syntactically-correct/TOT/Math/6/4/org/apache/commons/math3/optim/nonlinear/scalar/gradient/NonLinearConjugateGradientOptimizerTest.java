import org.junit.Test;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

  @Test
  public void testDoOptimize_minimizeGoalType() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
      ConjugateGradientFormula.FLETCHER_REEVES,
      new SimpleValueChecker()
    );
    optimizer.setInitialStep(0.5);

    double[] startPoint = new double[] {1.5, -2.0, 3.0};
    int maxEvaluations = 100;
    GoalType goalType = GoalType.MINIMIZE;

    double[] expectedOptimalPoint = new double[] {-10.0, 0.0, 5.0};
    double expectedOptimalValue = -50.0;

    PointValuePair actual = optimizer.doOptimize(startPoint, goalType, maxEvaluations);

    assertTrue(assertArrayEquals(expectedOptimalPoint, actual.getPoint()));
    assertEquals(expectedOptimalValue, actual.getValue(), 1e-5);
  }

  @Test
  public void testDoOptimize_maximizeGoalType() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
      ConjugateGradientFormula.POLAK_RIBIERE,
      new SimpleValueChecker()
    );
    optimizer.setInitialStep(0.5);

    double[] startPoint = new double[] {1.5, -2.0, 3.0};
    int maxEvaluations = 100;
    GoalType goalType = GoalType.MAXIMIZE;

    double[] expectedOptimalPoint = new double[] {10.0, 0.0, -5.0};
    double expectedOptimalValue = 50.0;

    PointValuePair actual = optimizer.doOptimize(startPoint, goalType, maxEvaluations);

    assertTrue(assertArrayEquals(expectedOptimalPoint, actual.getPoint()));
    assertEquals(expectedOptimalValue, actual.getValue(), 1e-5);
  }

  @Test
  public void testDoOptimize_noOptimalPointFound() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
      ConjugateGradientFormula.FLETCHER_REEVES,
      new SimpleValueChecker()
    );
    optimizer.setInitialStep(0.5);

    double[] startPoint = new double[] {1.0, 1.0};
    int maxEvaluations = 10;
    GoalType goalType = GoalType.MINIMIZE;

    PointValuePair actual = optimizer.doOptimize(startPoint, goalType, maxEvaluations);

    assertNull(actual); // No optimal point found within the given maximum evaluations allowed
  }

  @Test(expected = MathIllegalStateException.class)
  public void testDoOptimize_unableToBracketOptimumInLineSearch() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(
      ConjugateGradientFormula.POLAK_RIBIERE,
      new SimpleValueChecker()
    );

    double[] startPoint = new double[] {1.5, -2.0, 3.0};
    int maxEvaluations = 100;
    GoalType goalType = GoalType.MINIMIZE;

    optimizer.doOptimize(startPoint, goalType, maxEvaluations);
  }

}