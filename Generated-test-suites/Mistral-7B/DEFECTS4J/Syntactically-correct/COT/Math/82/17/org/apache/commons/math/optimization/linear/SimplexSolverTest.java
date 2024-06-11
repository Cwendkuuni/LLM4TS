import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.MaxEval;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

 private SimplexSolver solver;

 @Before
 public void setUp() {
 solver = new SimplexSolver();
 }

 /**
 * Test constructor with default epsilon
 */
 @Test
 public void testDefaultConstructor() {
 solver = new SimplexSolver();
 assertEquals(1.0e-6, solver.epsilon, 0.0);
 }

 /**
 * Test constructor with custom epsilon
 */
 @Test
 public void testCustomConstructor() {
 solver = new SimplexSolver(1.0e-8);
 assertEquals(1.0e-8, solver.epsilon, 0.0);
 }

 /**
 * Test getPivotColumn method
 */
 @Test
 public void testGetPivotColumn() {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{}, new LinearConstraint[]{}), null, null, 1.0e-6);
 assertEquals(0, solver.getPivotColumn(tableau).intValue());
 }

 /**
 * Test getPivotRow method with positive entry
 */
 @Test
 public void testGetPivotRowWithPositiveEntry() {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1}, 1, 2, null)}), null, null, 1.0e-6);
 assertEquals(1, solver.getPivotRow(0, tableau).intValue());
 }

 /**
 * Test getPivotRow method with zero entry
 */
 @Test
 public void testGetPivotRowWithZeroEntry() {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1}, 1, 0, null)}), null, null, 1.0e-6);
 assertNull(solver.getPivotRow(0, tableau));
 }

 /**
 * Test getPivotRow method with negative entry
 */
 @Test
 public void testGetPivotRowWithNegativeEntry() {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1}, 1, -1, null)}), null, null, 1.0e-6);
 assertEquals(2, solver.getPivotRow(0, tableau).intValue());
 }

 /**
 * Test doIteration method
 */
 @Test
 public void testDoIteration() throws OptimizationException {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1}, 1, 1, null)}), null, null, 1.0e-6);
 solver.doIteration(tableau);
 assertEquals(1.0, tableau.getEntry(1, 0), 1.0e-6);
 assertEquals(0.0, tableau.getEntry(1, 1), 1.0e-6);
 assertEquals(0.0, tableau.getEntry(2, 0), 1.0e-6);
 }

 /**
 * Test isPhase1Solved method
 */
 @Test
 public void testIsPhase1Solved() {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1}, 1, 1, null)}), null, null, 1.0e-6);
 assertFalse(solver.isPhase1Solved(tableau));
 SimpleTableau tableau2 = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1}, 1, 0, null)}), null, null, 1.0e-6);
 assertTrue(solver.isPhase1Solved(tableau2));
 }

 /**
 * Test isOptimal method
 */
 @Test
 public void testIsOptimal() {
 SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1, -1}),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{1, 1}, 0, Double.POSITIVE_INFINITY, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1, 2}, 0, Double.POSITIVE_INFINITY, null)}), null, null, 1.0e-6);
 assertFalse(solver.isOptimal(tableau));
 SimpleTableau tableau2 = new SimpleTableau(new LinearObjectiveFunction(new double[]{0, -1}, null),
 new LinearConstraintSet(new LinearConstraint[]{new LinearConstraint(1, new double[]{1, 1}, 0, Double.POSITIVE_INFINITY, null)},
 new LinearConstraint[]{new LinearConstraint(2, new double[]{1, 2}, 0, Double.POSITIVE_INFINITY, null)}), null, null, 1.0e-6);
 assertTrue(solver.isOptimal(tableau2));
 }

 /**
 * Test solvePhase1 method
 */
 @Test
 public void testSolvePhase1() throws OptimizationException {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 0});
 LinearConstraint[] constraints = {new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null),
 new LinearConstraint(2, new double[]{1}, 1, 1, null), new LinearConstraint(3, new double[]{1}, 1, 0, null)};
 LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
 SimplexTableau tableau = new SimplexTableau(f, constraintSet, null, null, 1.0e-6);
 solver.solvePhase1(tableau);
 assertEquals(0.5, tableau.getEntry(3, 0), 1.0e-6);
 assertEquals(1.0, tableau.getEntry(3, 3), 1.0e-6);
 assertEquals(1.5, tableau.getEntry(1, 0), 1.0e-6);
 assertEquals(-0.5, tableau.getEntry(1, 1), 1.0e-6);
 assertEquals(-0.5, tableau.getEntry(1, 2), 1.0e-6);
 assertEquals(0.0, tableau.getEntry(2, 0), 1.0e-6);
 assertEquals(-1.0, tableau.getEntry(2, 1), 1.0e-6);
 assertEquals(-1.0, tableau.getEntry(2, 2), 1.0e-6);
 assertEquals(0.0, tableau.getEntry(3, 0), 1.0e-6);
 assertEquals(0.0, tableau.getEntry(3, 1), 1.0e-6);
 assertEquals(0.0, tableau.getEntry(3, 2), 1.0e-6);
 }

 /**
 * Test doOptimize method
 */
 @Test
 public void testDoOptimize() throws OptimizationException {
 LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 0});
 LinearConstraint[] constraints = {new LinearConstraint(1, new double[]{0.5}, 0.5, 1.0, null),
 new LinearConstraint(2, new double[]{1}, 1, 1, null), new LinearConstraint(3, new double[]{1}, 1, 0, null)};
 LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
 LinearPointValuePair solution = solver.doOptimize();
 assertEquals(-0.5, solution.getPoint()[0], 1.0e-6);
 assertEquals(-0.5, solution.getValue(), 1.0e-6);
 }

}