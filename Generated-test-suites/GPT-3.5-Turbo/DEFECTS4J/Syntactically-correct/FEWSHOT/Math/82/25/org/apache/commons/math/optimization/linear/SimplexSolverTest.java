import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumObjectiveFunctions(1);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, -4);
        tableau.setEntry(0, 4, 5);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(3), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumObjectiveFunctions(1);
        tableau.setHeight(4);
        tableau.setWidth(5);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, -2);
        tableau.setEntry(1, 2, 3);
        tableau.setEntry(1, 3, -4);
        tableau.setEntry(1, 4, 5);
        tableau.setEntry(2, 0, 1);
        tableau.setEntry(2, 1, 2);
        tableau.setEntry(2, 2, 3);
        tableau.setEntry(2, 3, 4);
        tableau.setEntry(2, 4, 5);
        tableau.setEntry(3, 0, 1);
        tableau.setEntry(3, 1, 2);
        tableau.setEntry(3, 2, 3);
        tableau.setEntry(3, 3, 0);
        tableau.setEntry(3, 4, 5);
        Integer pivotRow = simplexSolver.getPivotRow(3, tableau);
        assertEquals(Integer.valueOf(2), pivotRow);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, -4);
        tableau.setEntry(0, 4, 5);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(1, 2, 3);
        tableau.setEntry(1, 3, 4);
        tableau.setEntry(1, 4, 5);
        boolean isPhase1Solved = simplexSolver.isPhase1Solved(tableau);
        assertFalse(isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        tableau.setNumObjectiveFunctions(2);
        tableau.setWidth(5);
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(0, 3, -4);
        tableau.setEntry(0, 4, 5);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 2);
        tableau.setEntry(1, 2, 3);
        tableau.setEntry(1, 3, -4);
        tableau.setEntry(1, 4, 5);
        boolean isOptimal = simplexSolver.isOptimal(tableau);
        assertFalse(isOptimal);
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setGoalType(GoalType.MINIMIZE);
        simplexSolver.setRestrictToNonNegative(true);
        simplexSolver.setEpsilon(1.0e-6);

        double[][] coefMatrix = {{2, 3, 1}, {1, 1, 1}};
        double[] constants = {4, 2};
        double[] coeffs = {3, 2, 1};
        LinearObjectiveFunction f = new LinearObjectiveFunction(coeffs, 0);
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(coefMatrix[0], Relationship.GEQ, constants[0]);
        constraints[1] = new LinearConstraint(coefMatrix[1], Relationship.GEQ, constants[1]);

        SimplexTableau tableau = new SimplexTableau(f, constraints, simplexSolver.getGoalType(), true, simplexSolver.getEpsilon());
        RealPointValuePair solution = simplexSolver.doOptimize();
        double[] expectedSolution = {1.2, 0.8, 0};
        for (int i = 0; i < expectedSolution.length; i++) {
            assertEquals(expectedSolution[i], solution.getPoint()[i], simplexSolver.getEpsilon());
        }
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWithUnboundedSolution() {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setGoalType(GoalType.MINIMIZE);
        simplexSolver.setRestrictToNonNegative(true);
        simplexSolver.setEpsilon(1.0e-6);

        double[][] coefMatrix = {{1, 1}, {-1, -1}};
        double[] constants = {0, 0};
        double[] coeffs = {1, 1};
        LinearObjectiveFunction f = new LinearObjectiveFunction(coeffs, 0);
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(coefMatrix[0], Relationship.LEQ, constants[0]);
        constraints[1] = new LinearConstraint(coefMatrix[1], Relationship.GEQ, constants[1]);

        SimplexTableau tableau = new SimplexTableau(f, constraints, simplexSolver.getGoalType(), true, simplexSolver.getEpsilon());
        RealPointValuePair solution = simplexSolver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeWithNoFeasibleSolution() {
        SimplexSolver simplexSolver = new SimplexSolver();
        simplexSolver.setGoalType(GoalType.MINIMIZE);
        simplexSolver.setRestrictToNonNegative(true);
        simplexSolver.setEpsilon(1.0e-6);

        double[][] coefMatrix = {{1, 1}, {2, 2}};
        double[] constants = {0, 0};
        double[] coeffs = {1, 1};
        LinearObjectiveFunction f = new LinearObjectiveFunction(coeffs, 0);
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(coefMatrix[0], Relationship.LEQ, constants[0]);
        constraints[1] = new LinearConstraint(coefMatrix[1], Relationship.LEQ, constants[1]);

        SimplexTableau tableau = new SimplexTableau(f, constraints, simplexSolver.getGoalType(), true, simplexSolver.getEpsilon());
        RealPointValuePair solution = simplexSolver.doOptimize();
    }

}