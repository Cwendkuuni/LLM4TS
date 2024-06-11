import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

  // Test case for PowellOptimizer constructor
  @Test
  public void testConstructor() {
    double rel = 1e-10;
    double abs = 1e-20;
    ConvergenceChecker<PointValuePair> checker = new SimpleConvergenceChecker<PointValuePair>();

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs, checker);

    assertNotNull(optimizer);
  }

  // Test case for doOptimize() method with MINIMIZE goal type
  @Test
  public void testDoOptimizeMinimize() {
    double rel = 1e-10;
    double abs = 1e-20;

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

    double[] startPoint = {0.0, 0.0, 0.0};
    double objectiveValue = optimizer.computeObjectiveValue(startPoint);

    GoalType goal = GoalType.MINIMIZE;
    optimizer.withStartPoint(startPoint).withGoalType(goal).withMaxIterations(1000);

    PointValuePair result = optimizer.doOptimize();

    assertNotNull(result);
    assertEquals(goal, optimizer.getGoalType());
    assertTrue(result.getValue() <= objectiveValue);
  }

  // Test case for doOptimize() method with MAXIMIZE goal type
  @Test
  public void testDoOptimizeMaximize() {
    double rel = 1e-10;
    double abs = 1e-20;

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

    double[] startPoint = {0.0, 0.0, 0.0};
    double objectiveValue = optimizer.computeObjectiveValue(startPoint);

    GoalType goal = GoalType.MAXIMIZE;
    optimizer.withStartPoint(startPoint).withGoalType(goal).withMaxIterations(1000);

    PointValuePair result = optimizer.doOptimize();

    assertNotNull(result);
    assertEquals(goal, optimizer.getGoalType());
    assertTrue(result.getValue() >= objectiveValue);
  }

  // Test case for newPointAndDirection() method
  @Test
  public void testNewPointAndDirection() {
    double[] p = {1.0, 2.0, 3.0};
    double[] d = {0.5, 0.5, 0.5};
    double optimum = 0.25;

    PowellOptimizer optimizer = new PowellOptimizer(1e-10, 1e-20);
    double[][] result = optimizer.newPointAndDirection(p, d, optimum);

    assertNotNull(result);
    assertArrayEquals(new double[] {1.125, 2.125, 3.125}, result[0], 1e-10);
    assertArrayEquals(new double[] {0.125, 0.125, 0.125}, result[1], 1e-10);
  }

  // Test case for LineSearch class search() method
  @Test
  public void testLineSearch() {
    double rel = 1e-10;
    double abs = 1e-20;

    PowellOptimizer optimizer = new PowellOptimizer(rel, abs);

    double[] p = {1.0, 2.0, 3.0};
    double[] d = {0.5, 0.5, 0.5};

    PowellOptimizer.LineSearch lineSearch = optimizer.new LineSearch(rel, abs);

    UnivariatePointValuePair result = lineSearch.search(p, d);

    assertNotNull(result);
    assertTrue(result.getValue() >= 0.0);
  }

}