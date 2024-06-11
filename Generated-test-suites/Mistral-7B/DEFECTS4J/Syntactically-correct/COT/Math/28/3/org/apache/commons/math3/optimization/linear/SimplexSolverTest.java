import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

 private SimplexSolver solver;

 @Before
 public void setUp() {
 solver = new SimplexSolver();
 }

 @Test
 public void testConstructor() {
 SimplexSolver s = new SimplexSolver();
 assertEquals(1.0e-6, s.epsilon, 0.0);
 assertEquals(10, s.maxUlps);
 }

 @Test
 public void testGetPivotColumn() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2}),
 new LinearConstraint[]{},
 null,
 null,
 1.0e-6,
 10);
 tableau.setValueAt(0, 1, -3);
 tableau.setValueAt(0, 2, 4);
 tableau.setValueAt(1, 1, 2);
 tableau.setValueAt(1, 2, 1);

 Integer pivotCol = solver.getPivotColumn(tableau);
 assertEquals(0, pivotCol.intValue());
 }

 @Test
 public void testGetPivotRow() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2}),
 new LinearConstraint[]{},
 null,
 null,
 1.0e-6,
 10);
 tableau.setValueAt(0, 1, -3);
 tableau.setValueAt(0, 2, 4);
 tableau.setValueAt(1, 1, 2);
 tableau.setValueAt(1, 2, 1);
 tableau.setValueAt(2, 1, -2);
 tableau.setValueAt(2, 2, 3);

 Integer pivotRow = solver.getPivotRow(tableau, 0);
 assertEquals(1, pivotRow.intValue());
 }

 @Test
 public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2}),
 new LinearConstraint[]{},
 null,
 null,
 1.0e-6,
 10);
 tableau.setValueAt(0, 1, -3);
 tableau.setValueAt(0, 2, 4);
 tableau.setValueAt(1, 1, 2);
 tableau.setValueAt(1, 2, 1);
 tableau.setValueAt(2, 1, -2);
 tableau.setValueAt(2, 2, 3);
 Integer pivotCol = solver.getPivotColumn(tableau);
 Integer pivotRow = solver.getPivotRow(tableau, pivotCol.intValue());

 solver.doIteration(tableau);

 assertEquals(1.0 / (-3), tableau.getEntry(pivotRow.intValue(), pivotCol.intValue()), 0.0);
 for (int i = 0; i < tableau.getHeight(); i++) {
 if (i != pivotRow.intValue()) {
 assertEquals(-3.0 * tableau.getEntry(i, pivotCol.intValue()) / (-3), tableau.getEntry(i, pivotCol.intValue()), 0.0);
 }
 }
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testSolvePhase1Failure() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2}),
 new LinearConstraint[]{},
 null,
 null,
 1.0e-6,
 10);
 tableau.setValueAt(0, 1, -3);
 tableau.setValueAt(0, 2, 4);
 tableau.setValueAt(1, 1, 2);
 tableau.setValueAt(1, 2, 1);
 tableau.setValueAt(2, 1, -2);
 tableau.setValueAt(2, 2, 3);
 tableau.setValueAt(3, 1, 1);
 tableau.setValueAt(3, 2, 1);

 solver.solvePhase1(tableau);
 fail("An UnboundedSolutionException should have been thrown.");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testSolvePhase1MaxIterations() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2}),
 new LinearConstraint[]{},
 null,
 null,
 1.0e-6,
 1);
 tableau.setValueAt(0, 1, -3);
 tableau.setValueAt(0, 2, 4);
 tableau.setValueAt(1, 1, 2);
 tableau.setValueAt(1, 2, 1);
 tableau.setValueAt(2, 1, -2);
 tableau.setValueAt(2, 2, 3);

 try {
 solver.solvePhase1(tableau);
 fail("A MaxCountExceededException should have been thrown.");
 } catch (MaxCountExceededException e) {
 // expected
 }
 }

 @Test
 public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2});
 LinearConstraint[] constraints = new LinearConstraint[]{
 new LinearConstraint(1, 1, 1),
 new LinearConstraint(1, 2, 2)
 };
 PointValuePair result = solver.doOptimize();
 assertEquals(0.0, result.getValue(), 0.0);
 assertEquals(new double[]{1.0 / 3, 2.0 / 3}, result.getPoint(), 0.0);
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testDoOptimizeUnbounded() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2});
 LinearConstraint[] constraints = new LinearConstraint[]{
 new LinearConstraint(1, 1, 1),
 new LinearConstraint(1, 2, 2),
 new LinearConstraint(1, 3, -1)
 };
 try {
 solver.doOptimize();
 fail("An UnboundedSolutionException should have been thrown.");
 } catch (UnboundedSolutionException e) {
 // expected
 }
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testDoOptimizeNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{-1, -2}, new double[]{1, 2});
 LinearConstraint[] constraints = new LinearConstraint[]{
 new LinearConstraint(1, 1, 1),
 new LinearConstraint(1, 2, 2),
 new LinearConstraint(1, 3, -1),
 new LinearConstraint(1, 4, 1)
 };
 try {
 solver.doOptimize();
 fail("A NoFeasibleSolutionException should have been thrown.");
 } catch (NoFeasibleSolutionException e) {
 // expected
 }
 }

}