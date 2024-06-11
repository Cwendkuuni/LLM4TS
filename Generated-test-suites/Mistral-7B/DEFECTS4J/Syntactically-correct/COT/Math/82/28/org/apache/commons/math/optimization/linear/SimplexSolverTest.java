import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

 private SimplexSolver solver;

 @Test
 public void testDefaultConstructor() {
 solver = new SimplexSolver();
 }

 @Test
 public void testCustomConstructor() {
 solver = new SimplexSolver(1.0e-7);
 }

 @Test
 public void testGetPivotColumn() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 Integer expected = 0;
 Integer actual = solver.getPivotColumn(tableau);
 Assert.assertEquals(expected, actual);
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testGetPivotColumnWithEmptyTableau() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction[]{},
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.getPivotColumn(tableau);
 }

 @Test
 public void testGetPivotRow() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 Integer expected = 1;
 Integer actual = solver.getPivotRow(1, tableau);
 Assert.assertEquals(expected, actual);
 }

 @Test (expected = NullPointerException.class)
 public void testGetPivotRowWithNullTableau() {
 SimplexTableau tableau = null;
 Integer actual = solver.getPivotRow(1, tableau);
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.doIteration(tableau);
 }

 @Test (expected = UnboundedSolutionException.class)
 public void testDoIterationWithUnboundedSolution() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2, Double.POSITIVE_INFINITY}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.doIteration(tableau);
 }

 @Test
 public void testIsPhase1Solved() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.solvePhase1(tableau);
 boolean expected = true;
 boolean actual = solver.isPhase1Solved(tableau);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIsOptimal() {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 boolean expected = false;
 boolean actual = solver.isOptimal(tableau);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSolvePhase1() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2, Double.POSITIVE_INFINITY}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.solvePhase1(tableau);
 }

 @Test (expected = NoFeasibleSolutionException.class)
 public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2, -1}),
 new LinearConstraint[]{}, LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.solvePhase1(tableau);
 }

 @Test
 public void testDoOptimize() throws OptimizationException {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 2});
 LinearConstraint constraint = new LinearConstraint(new double[]{1}, 0, Double.POSITIVE_INFINITY);
 LinearConstraint constraint2 = new LinearConstraint(new double[]{1}, 0, Double.POSITIVE_INFINITY);
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{constraint, constraint2},
 LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 RealPointValuePair expected = new RealPointValuePair(new double[]{0, 2}, -3);
 RealPointValuePair actual = solver.doOptimize();
 Assert.assertEquals(expected.getPoint(), actual.getPoint(), 1e-6);
 Assert.assertEquals(expected.getValue(), actual.getValue(), 1e-6);
 }

 @Test (expected = OptimizationException.class)
 public void testDoOptimizeWithUnboundedSolution() throws OptimizationException {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 2, Double.POSITIVE_INFINITY});
 LinearConstraint constraint = new LinearConstraint(new double[]{1}, 0, Double.POSITIVE_INFINITY);
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{constraint},
 LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.doIteration(tableau);
 }

 @Test (expected = NoFeasibleSolutionException.class)
 public void testDoOptimizeWithNoFeasibleSolution() throws OptimizationException {
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 2, -1});
 LinearConstraint constraint = new LinearConstraint(new double[]{1}, 0, Double.POSITIVE_INFINITY);
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[]{constraint},
 LinearObjectiveFunction.MINIMIZE, false, 1.0e-6);
 solver.solvePhase1(tableau);
 }

}