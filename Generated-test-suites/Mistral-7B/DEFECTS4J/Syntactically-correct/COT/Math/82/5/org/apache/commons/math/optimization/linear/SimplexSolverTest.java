import org.apache.commons.math3.optim.PointsValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.simple.AbstractLinearOptimizerTest;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest extends AbstractLinearOptimizerTest {

 private SimplexSolver solver;

 @Before
 public void setUp() {
 solver = new SimplexSolver(1.0e-6);
 }

 @Test
 public void testConstructorWithDefaultEpsilon() {
 SimplexSolver solver = new SimplexSolver();
 assertEquals(1.0e-6, solver.epsilon, 1.0e-7);
 }

 @Test
 public void testConstructorWithSpecifiedEpsilon() {
 SimplexSolver solver = new SimplexSolver(0.0001);
 assertEquals(0.0001, solver.epsilon, 1.0e-7);
 }

 @Test
 public void testGetPivotColumn() {
 // Given
 SimplexTableau tableau = createSimpleTableau();

 // When
 Integer pivotColumn = solver.getPivotColumn(tableau);

 // Then
 assertNotNull(pivotColumn);
 int expectedPivotColumn = 2;
 assertEquals(expectedPivotColumn, pivotColumn);
 }

 @Test
 public void testGetPivotRow() {
 // Given
 int pivotColumn = 2;
 SimplexTableau tableau = createSimpleTableau();

 // When
 Integer pivotRow = solver.getPivotRow(pivotColumn, tableau);

 // Then
 assertNotNull(pivotRow);
 int expectedPivotRow = 3;
 assertEquals(expectedPivotRow, pivotRow);
 }

 @Test
 public void testDoIteration() throws OptimizationException {
 // Given
 SimplexTableau tableau = createSimpleTableau();

 // When
 solver.doIteration(tableau);

 // Then
 assertEquals(1, tableau.getRow(0).getEntry(2), 1.0e-6);
 for (int i = 0; i < tableau.getHeight(); i++) {
 if (i != 2) {
 assertEquals(0, tableau.getRow(i).getEntry(2), 1.0e-6);
 }
 }
 }

 @Test
 public void testIsPhase1Solved() {
 // Given
 SimplexTableau tableau = createSimpleTableauWithBoundedSolution();

 // When
 boolean isPhase1Solved = solver.isPhase1Solved(tableau);

 // Then
 assertTrue(isPhase1Solved);
 }

 @Test
 public void testIsOptimal() {
 // Given
 SimplexTableau tableau = createSimpleTableauWithOptimalSolution();

 // When
 boolean isOptimal = solver.isOptimal(tableau);

 // Then
 assertTrue(isOptimal);
 }

 @Test(expected = UnboundedSolutionException.class)
 public void testSolvePhase1WithUnboundedSolution() throws OptimizationException {
 // Given
 SimplexTableau tableau = createSimpleTableauWithUnboundedSolution();

 // When
 solver.solvePhase1(tableau);

 // Then
 fail("This method should have thrown an exception");
 }

 @Test(expected = NoFeasibleSolutionException.class)
 public void testSolvePhase1WithNoFeasibleSolution() throws OptimizationException {
 // Given
 SimplexTableau tableau = createSimpleTableauWithNoFeasibleSolution();

 // When
 solver.solvePhase1(tableau);

 // Then
 fail("This method should have thrown an exception");
 }

 @Test
 public void testOptimize() throws OptimizationException {
 // Given
 LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, -2, 3}, new double[]{3, 2, 1});
 LinearConstraintSet constraints = new LinearConstraintSet();
 constraints.addConstraint(new LinearConstraint(2, 1, Relationship.LEQ, 2));
 LinearConstraintSet goalConstraints = new LinearConstraintSet();
 LinearPointValuePair goal = new LinearPointValuePair(new double[]{1, 0, 0}, new double[]{2});
 LinearConstraintSet bounds = new LinearConstraintSet();
 bounds.addConstraint(new LinearConstraint(3, 0, Relationship.LOQ, Double.POSITIVE_INFINITY));
 bounds.addConstraint(new LinearConstraint(3, 1, Relationship.LOQ, Double.POSITIVE_INFINITY));
 bounds.addConstraint(new LinearConstraint(3, 2, Relationship.LOQ, Double.POSITIVE_INFINITY));
 LinearConstraintSet linearConstraints = new LinearConstraintSet();
 LinearConstraint constraint = new LinearConstraint(1, 0, Relationship.LEQ, 1);
 linearConstraints.addConstraint(constraint);
 LinearConstraintSet objectiveType = new LinearConstraintSet();
 objectiveType.addConstraint(new LinearConstraint(1, 2, Relationship.EQ, 1));
 LinearObjectiveFunction linearObjectiveFunction = new LinearObjectiveFunction(new double[]{-1, -2, 3}, new double[]{3}, linearConstraints, objectiveType, bounds);
 problems.add(new Problem(linearObjectiveFunction, constraints, goal, goalConstraints));

 // When
 RealPointValuePair solution = solver.optimize();

 // Then
 assertEquals(new PointsValuePair(new double[]{0.5, 1.5, 0}, new double[]{1.5}), solution);
 }

 private SimplexTableau createSimpleTableau() {
 double[][] coefficients = new double[][]{{0, -1, -2, 3}, {3, -2, 2, 1}, {0, 1, 1, 1}};
 double[] constants = new double[]{-3, 3, 0};
 int numObjectiveFunctions = 1;
 int numConstraints = 2;
 int numVariables = 3;
 SimplexTableau tableau = new SimplexTableau(coefficients, constants, numObjectiveFunctions, numConstraints, numVariables, solver.epsilon);
 return tableau;
 }

 private SimplexTableau createSimpleTableauWithBoundedSolution() {
 double[][] coefficients = new double[][]{{0, -1, -2, 3}, {3, -2, 2, 1}, {0, 1, 1, 1}};
 double[] constants = new double[]{-3, 3, 0};
 int numObjectiveFunctions = 1;
 int numConstraints = 2;
 int numVariables = 3;
 SimplexTableau tableau = new SimplexTableau(coefficients, constants, numObjectiveFunctions, numConstraints, numVariables, solver.epsilon);
 tableau.setEntry(1, 2, 1.0);
 tableau.setEntry(1, numVariables, 2.0);
 tableau.setEntry(2, numVariables, 1.0);
 return tableau;
 }

 private SimplexTableau createSimpleTableauWithOptimalSolution() {
 double[][] coefficients = new double[][]{{0, -1, -2, 3}, {3, -2, 2, 1}, {0, 1, 1, 1}};
 double[] constants = new double[]{-3, 3, 0};
 int numObjectiveFunctions = 1;
 int numConstraints = 2;
 int numVariables = 3;
 SimplexTableau tableau = new SimplexTableau(coefficients, constants, numObjectiveFunctions, numConstraints, numVariables, solver.epsilon);
 tableau.setEntry(1, 2, 0);
 tableau.setEntry(1, numVariables, 1.5);
 tableau.setEntry(2, numVariables, 1.5);
 return tableau;
 }

 private SimplexTableau createSimpleTableauWithUnboundedSolution() {
 double[][] coefficients = new double[][]{{0, -1, -2, 3}, {3, -2, 2, 1}, {0, 1, 1, 1}};
 double[] constants = new double[]{-3, 3, 0};
 int numObjectiveFunctions = 1;
 int numConstraints = 2;
 int numVariables = 3;
 SimplexTableau tableau = new SimplexTableau(coefficients, constants, numObjectiveFunctions, numConstraints, numVariables, solver.epsilon);
 tableau.setEntry(1, numVariables, Double.NEGATIVE_INFINITY);
 return tableau;
 }

 private SimplexTableau createSimpleTableauWithNoFeasibleSolution() {
 double[][] coefficients = new double[][]{{0, -1, -2, 3}, {3, -2, 2, 1}, {0, 1, 1, 1}};
 double[] constants = new double[]{3, 1, 0};
 int numObjectiveFunctions = 1;
 int numConstraints = 2;
 int numVariables = 3;
 SimplexTableau tableau = new SimplexTableau(coefficients, constants, numObjectiveFunctions, numConstraints, numVariables, solver.epsilon);
 tableau.setEntry(2, numVariables, -3);
 return tableau;
 }
}