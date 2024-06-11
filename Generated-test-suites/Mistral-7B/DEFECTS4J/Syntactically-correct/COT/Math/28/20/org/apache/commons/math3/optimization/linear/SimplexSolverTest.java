import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization. linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

public class SimplexSolverTest {

 private LinearObjectiveFunction objFunction;
 private LinearConstraint[] constraints;

 @Test
 public void testEmptyConstructor() {
 SimplexSolver solver = new SimplexSolver();
 }

 @Test
 public void testCustomConstructor() {
 SimplexSolver solver = new SimplexSolver(1.0e-8, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEpsilon() {
 SimplexSolver solver = new SimplexSolver(-1.0, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidMaxUlps() {
 SimplexSolver solver = new SimplexSolver(1.0e-8, -5);
 }

 @Test
 public void testGetPivotColumn() {
 // given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0}),
 new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 1.0),
 new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0)},
 LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);

 // when
 int pivotColumn = solver.getPivotColumn(tableau);

 // then
 Assert.assertEquals(1, pivotColumn.intValue());
 }

 @Test(expected = MaxCountExceededException.class)
 public void testDoIterationMaxIterationsExceeded() throws MaxCountExceededException {
 // given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0}),
 new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 1.0),
 new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0)},
 LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);
 solver.setMaxIterationCount(1);

 // when
 solver.doIteration(tableau);

 // then
 Assert.fail();
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testDoIterationUnboundedSolution() throws UnboundedSolutionException {
 // given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0}),
 new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 1.0),
 new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0)},
 LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);

 // when & then
 try {
 solver.doIteration(tableau);
 Assert.fail();
 } catch (UnboundedSolutionException e) {
 // expected
 }
 }

 @Test
 public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 // given
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0}),
 new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 1.0),
 new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0)},
 LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);

 // when
 solver.solvePhase1(tableau);

 // then
 Assert.assertEquals(0.0, tableau.getEntry(0, tableau.getRhsOffset()), solver.epsilon);
 }

 @Test
 public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 // given
 LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0});
 LinearConstraint[] constraints = new LinearConstraint[]{new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 1.0),
 new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0)};
 SimplexTableau tableau = new SimplexTableau(objFunction, constraints, LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);
 SimplexSolver solver = new SimplexSolver(1.0e-8, 5);

 // when
 PointValuePair solution = solver.doOptimize();

 // then
 Assert.assertEquals(1.0, solution.getPoint()[0], solver.epsilon);
 Assert.assertEquals(7.0, solution.getValue(), solver.epsilon);
 }

 @Test
 public void testDoOptimizeWithConstraints() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 // given
 LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0});
 LinearConstraint constraint1 = new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 1.0);
 LinearConstraint constraint2 = new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0);
 LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2};
 SimplexTableau tableau = new SimplexTableau(objFunction, constraints, LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);
 SimplexSolver solver = new SimplexSolver(1.0e-8, 5);

 // when
 PointValuePair solution = solver.doOptimize();

 // then
 Assert.assertEquals(1.0, solution.getPoint()[0], solver.epsilon);
 Assert.assertEquals(4.0, solution.getValue(), solver.epsilon);
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testDoOptimizeNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
 // given
 LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[]{-1.0, 3.0, 2.0}, new double[]{0.0, 1.0, 1.0});
 LinearConstraint constraint1 = new LinearConstraint(1.0, new double[]{1.0, 1.0, 1.0}, 2.0);
 LinearConstraint constraint2 = new LinearConstraint(1.0, new double[]{1.0, 2.0, 1.0}, 3.0);
 LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2};
 SimplexTableau tableau = new SimplexTableau(objFunction, constraints, LinearOptimizer.GOAL_MINIMIZE, false, 1.0e-8, 5);
 SimplexSolver solver = new SimplexSolver(1.0e-8, 5);

 // when & then
 try {
 solver.doOptimize();
 Assert.fail();
 } catch (NoFeasibleSolutionException e) {
 // expected
 }
 }
}