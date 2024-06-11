import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationProblem;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.environments.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

 private SimplexSolver solver;
 private SimpleOptimizationProblem problem;

 @Before
 public void setUp() {
 solver = new SimplexSolver();
 // Create a test problem
 LinearObjectiveFunction objFun = new LinearObjectiveFunction(new double[]{-2.0, -3.0}, new double[]{1.0, 1.0});
 LinearConstraint[] constraints = new LinearConstraint[]{
 new LinearConstraint(new double[]{-1.0, -1.0}, 0.0),
 new LinearConstraint(new double[]{1.0, 1.0}, 3.0)};
 problem = new SimpleOptimizationProblem(objFun, constraints);
 }

 @Test
 public void testEmptyConstructor() {
 SimplexSolver emptySolver = new SimplexSolver();
 assertEquals(1.0e-6, emptySolver.epsilon, 0.0);
 }

 @Test
 public void testConstructorWithEpsilon() {
 SimplexSolver epsilonSolver = new SimplexSolver(0.001);
 assertEquals(0.001, epsilonSolver.epsilon, 0.0);
 }

 @Test
 public void testGetPivotColumn() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new SimplexSolver().doOptimize(problem).getTableau());
 Integer pivotColumn = solver.getPivotColumn(tableau);
 assertEquals(2, pivotColumn.intValue());
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotColumnWithNoNegativeCoefficients() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {-1.0, 1.0, 2.0}});
 assertEquals(-1, solver.getPivotColumn(tableau).intValue());
 }

 @Test
 public void testGetPivotRow() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new SimplexSolver().doOptimize(problem).getTableau());
 Integer pivotRow = solver.getPivotRow(2, tableau);
 assertEquals(2, pivotRow.intValue());
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotRowWithNoPositiveEntry() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {-1.0, -1.0, 2.0}});
 assertEquals(-1, solver.getPivotRow(2, tableau).intValue());
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new SimplexSolver().doOptimize(problem).getTableau());
 solver.doIteration(tableau);
 // Check that the pivot element is now 1
 assertEquals(1.0, tableau.getEntry(1, 2), 0.0);
 // Check that the rest of the pivot column is now 0
 for (int i = 0; i < tableau.getHeight(); i++) {
 if (i != 1) {
 assertEquals(0.0, tableau.getEntry(i, 2), 0.0);
 }
 }
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testUnboundedSolutionException() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {-1.0, 0.0, 2.0}});
 solver.doIteration(tableau);
 }

 @Test
 public void testIsPhase1Solved() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new SimplexSolver().doOptimize(problem).getTableau());
 assertTrue(solver.isPhase1Solved(tableau));
 }

 @Test
 public void testIsOptimal() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new SimplexSolver().doOptimize(problem).getTableau());
 assertTrue(solver.isOptimal(tableau));
 }

 @Test
 public void testSolve() throws OptimizationException {
 PointValuePair solution = solver.doOptimize();
 assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -5.0 / 3.0), solution, 0.0);
 }

 @Test
 public void testSolveWithCustomEpsilon() throws OptimizationException {
 SimplexSolver epsilonSolver = new SimplexSolver(0.001);
 PointValuePair solution = epsilonSolver.doOptimize();
 assertEquals(new PointValuePair(new double[]{1.0 / 3.0, 2.0 / 3.0}, -5.0 / 3.0), solution, 0.001);
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testNoFeasibleSolutionException() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {-1.0, -1.0, 2.0}});
 solver.doIteration(tableau);
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testUnboundedSolutionException2() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {-1.0, 0.0, 2.0}});
 solver.doIteration(tableau);
 solver.doIteration(tableau);
 }

 @Test(expected = OptimizationException.class)
 public void testExceptionWithMaxIterations() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new double[][]{{1.0, 1.0, 3.0}, {1.0, 2.0, 1.0}, {-1.0, 0.0, 2.0}});
 ConvergenceChecker checker = new ConvergenceChecker(100);
 solver.setMaxIterations(1);
 try {
 solver.doIteration(tableau, checker);
 fail();
 } catch (OptimizationException e) {
 // expected
 }
 }
}