import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization. linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearPointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.nonlinear.GoalType;
import org.apache.commons.math3.optimization.nonlinear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.nonlinear.SimpleOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

 private SimplexSolver solver;
 private LinearObjectiveFunction objectiveFunction;
 private LinearConstraint[] constraints;

 @Before
 public void setUp() {
 objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{3.0, 1.0});
 constraints = new LinearConstraint[] {
 new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 2.0),
 new LinearConstraint(new double[]{1.0, 2.0}, Operator.LEQUAL, 3.0),
 new LinearConstraint(new double[]{2.0, 1.0}, Operator.LEQUAL, 1.0),
 new LinearConstraint(new double[]{2.0, 2.0}, Operator.LEQUAL, 5.0)};
 solver = new SimplexSolver();
 }

 @Test
 public void testDefaultConstructor() {
 solver = new SimplexSolver();
 assertEquals(1.0e-6, solver.epsilon, 0.0);
 assertEquals(10, solver.maxUlps);
 }

 @Test
 public void testConstructorWithArguments() {
 solver = new SimplexSolver(0.001, 5);
 assertEquals(0.001, solver.epsilon, 0.0);
 assertEquals(5, solver.maxUlps);
 }

 @Test
 public void testGetPivotColumn() throws Exception {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1e-6, 10);
 Integer pivotColumn = solver.getPivotColumn(tableau);
 assertEquals(0, pivotColumn.intValue());
 }

 @Test
 public void testGetPivotRow() throws Exception {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1e-6, 10);
 Integer pivotRow = solver.getPivotRow(tableau, 0);
 assertEquals(1, pivotRow.intValue());
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testDoIterationThrowsUnboundedSolutionException() throws Exception {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1e-6, 10);
 solver.doIteration(tableau);
 fail("Expected UnboundedSolutionException");
 }

 @Test
 public void testSolvePhase1() throws Exception {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1e-6, 10);
 solver.solvePhase1(tableau);
 assertTrue(tableau.isOptimal());
 }

 @Test(expected = MaxCountExceededException.class)
 public void testSolvePhase1ThrowsMaxCountExceededException() throws Exception {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, GoalType.MINIMIZE, null, 1e-6, 1);
 solver.solvePhase1(tableau);
 fail("Expected MaxCountExceededException");
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testSolvePhase1ThrowsNoFeasibleSolutionException() throws Exception {
 LinearConstraint constraint = new LinearConstraint(new double[]{1.0, 1.0}, Operator.LEQUAL, 0.0);
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 1.0}, new double[]{1.0, 1.0});
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, new LinearConstraint[] {constraint}, GoalType.MINIMIZE, null, 1e-6, 10);
 solver.solvePhase1(tableau);
 fail("Expected NoFeasibleSolutionException");
 }

 @Test
 public void testDoOptimize() throws Exception {
 LinearPointValuePair solution = solver.doOptimize();
 assertEquals(2.0, solution.getPoint()[0], 0.0);
 assertEquals(3.0, solution.getPoint()[1], 0.0);
 assertEquals(1.0, solution.getValue(), 0.0);
 }

}