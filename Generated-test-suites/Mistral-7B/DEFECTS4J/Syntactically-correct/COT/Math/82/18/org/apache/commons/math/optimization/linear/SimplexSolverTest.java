import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SimplexSolverTest {

 private SimplexSolver simplexSolver;
 private LinearObjectiveFunction objectiveFunction;
 private LinearConstraint[] constraints;

 @Before
 public void setUp() {
 simplexSolver = new SimplexSolver();
 objectiveFunction = new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}, new double[]{0.0});
 constraints = new LinearConstraint[3];
 constraints[0] = new LinearConstraint(new double[]{-1.0, 1.0}, 1.0, "<=", 1.0);
 constraints[1] = new LinearConstraint(new double[]{0.0, 1.0}, 1.0, "=", 1.0);
 constraints[2] = new LinearConstraint(new double[]{1.0, 1.0}, 3.0, "<=" , 3.0);
 }

 @Test
 public void testConstructor() {
 SimplexSolver simplexSolver = new SimplexSolver();
 assertEquals(DEFAULT_EPSILON, simplexSolver.epsilon, 0);
 }

 @Test
 public void testConstructorWithEpsilon() {
 SimplexSolver simplexSolver = new SimplexSolver(0.001);
 assertEquals(0.001, simplexSolver.epsilon, 0);
 }

 @Test
 public void testGetPivotColumn() {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON);
 Integer expectedPivotColumn = 2;
 Integer actualPivotColumn = simplexSolver.getPivotColumn(tableau);
 assertEquals(expectedPivotColumn, actualPivotColumn);
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotColumnWithEmptyTableau() {
 SimplexTableau emptyTableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{}, new double[]{}), new LinearConstraint[0], null, null, DEFAULT_EPSILON);
 Integer pivotColumn = simplexSolver.getPivotColumn(emptyTableau);
 fail("Expected OptimizationException but was not thrown.");
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotColumnWithNullTableau() {
 Integer pivotColumn = simplexSolver.getPivotColumn(null);
 fail("Expected OptimizationException but was not thrown.");
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotRowWithInvalidColumn() {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON);
 Integer invalidColumn = 3;
 Integer pivotRow = simplexSolver.getPivotRow(invalidColumn, tableau);
 fail("Expected OptimizationException but was not thrown.");
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotRowWithEmptyTableau() {
 SimplexTableau emptyTableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{}, new double[]{}), new LinearConstraint[0], null, null, DEFAULT_EPSILON);
 Integer pivotRow = simplexSolver.getPivotRow(0, emptyTableau);
 fail("Expected OptimizationException but was not thrown.");
 }

 @Test(expected = OptimizationException.class)
 public void testGetPivotRowWithNullTableau() {
 Integer pivotRow = simplexSolver.getPivotRow(0, null);
 fail("Expected OptimizationException but was not thrown.");
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON);
 simpexSolver.doIteration(tableau);
 double pivotValue = tableau.getEntry(0, getPivotColumn(tableau));
 assertEquals(-1.0, pivotValue, 0);
 }

 @Test(expected = OptimizationException.class)
 public void testDoIterationWithNullTableau() throws OptimizationException {
 simplexSolver.doIteration(null);
 fail("Expected OptimizationException but was not thrown.");
 }

 @Test(expected = OptimizationException.class)
 public void testDoIterationWithUnboundedSolution() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}, new double[]{-1.0, 1.0}), constraints, null, null, DEFAULT_EPSILON);
 simplexSolver.doIteration(tableau);
 throw new UnboundedSolutionException();
 }

 @Test
 public void testIsPhase1Solved() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON);
 assertTrue(simpleSolver.isPhase1Solved(tableau));
 simplexSolver.doIteration(tableau);
 assertFalse(simpleSolver.isPhase1Solved(tableau));
 }

 @Test
 public void testIsOptimal() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, DEFAULT_EPSILON);
 simplexSolver.solvePhase1(tableau);
 tableau.discardArtificialVariables();
 simplexSolver.doIteration(tableau);
 assertTrue(simpleSolver.isOptimal(tableau));
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testIsOptimalWithNoFeasibleSolution() throws OptimizationException {
 SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0, 3.0}, new double[]{0.0}), constraints, null, null, DEFAULT_EPSILON);
 simplexSolver.solvePhase1(tableau);
 tableau.discardArtificialVariables();
 simplexSolver.doIteration(tableau);
 simplexSolver.doIteration(tableau);
 throw new NoFeasibleSolutionException();
 }

 @Test
 public void testDoOptimize() throws OptimizationException {
 SimplePointValuePair expectedSolution = new RealPointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0, 1.0}, new double[]{-1.0 / 3.0});
 RealPointValuePair actualSolution = simplexSolver.doOptimize();
 assertEquals(expectedSolution.getPoint(), actualSolution.getPoint(), 0.0);
 assertEquals(expectedSolution.getValue(), actualSolution.getValue(), 0.0);
 }
}