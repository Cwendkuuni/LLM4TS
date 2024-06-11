import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.Relationship;

public class SimplexTableauTest {

  private SimplexTableau simplexTableau;

  @Before
  public void setUp() {
    LinearObjectiveFunction f = new LinearObjectiveFunction(
        new double[] {1, 2, 3}, 0);
    simplexTableau = new SimplexTableau(f, null, GoalType.MAXIMIZE, true, 0.01);
  }

  @Test
  public void testInitializeColumnLabels() {
    simplexTableau.initializeColumnLabels();
    List<String> expectedLabels = new ArrayList<>();
    expectedLabels.add("W");
    expectedLabels.add("Z");
    expectedLabels.add("x0");
    expectedLabels.add("x1");
    expectedLabels.add("x2");
    expectedLabels.add("RHS");

    assertEquals(expectedLabels, simplexTableau.getColumnLabels());
  }

  @Test
  public void testCreateTableau() {
    boolean maximize = true;
    RealMatrix tableau = simplexTableau.createTableau(maximize);

    double[][] expectedData = new double[][]{
        {-1, 1, 2, 3, 0, 0, 0},
        {0, -1, -2, -3, 0, 0, 0},
        {0, 1, 2, 3, 0, 0, 0}
    };

    assertArrayEquals(expectedData, tableau.getData());
  }

  @Test
  public void testNormalizeConstraints() {
    Collection<LinearConstraint> constraints = new ArrayList<>();
    LinearConstraint constraint1 = new LinearConstraint(
        new double[] {1, 2, 3}, Relationship.GEQ, 10);
    LinearConstraint constraint2 = new LinearConstraint(
        new double[] {-1, 4, 0}, Relationship.LEQ, 5);
    constraints.add(constraint1);
    constraints.add(constraint2);

    List<LinearConstraint> normalized = simplexTableau.normalizeConstraints(constraints);

    List<LinearConstraint> expectedNormalized = new ArrayList<>();
    LinearConstraint expectedConstraint1 = new LinearConstraint(
        new double[] {1, 2, 3}, Relationship.GEQ, 10);
    LinearConstraint expectedConstraint2 = new LinearConstraint(
        new double[] {1, -4, 0}, Relationship.GEQ, -5);
    expectedNormalized.add(expectedConstraint1);
    expectedNormalized.add(expectedConstraint2);

    assertEquals(expectedNormalized, normalized);
  }

  // Add more test methods for other methods in SimplexTableau class

}