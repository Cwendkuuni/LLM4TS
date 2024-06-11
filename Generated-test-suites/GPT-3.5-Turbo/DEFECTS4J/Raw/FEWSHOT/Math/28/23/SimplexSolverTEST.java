
package org.apache.commons.math3.optimization.linear;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, restrictToNonNegative(), epsilon, maxUlps);
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        assertEquals(pivotColumn, expectedPivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, restrictToNonNegative(), epsilon, maxUlps);
        Integer pivotRow = simplexSolver.getPivotRow(tableau, pivotColumn);
        assertEquals(pivotRow, expectedPivotRow);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, restrictToNonNegative(), epsilon, maxUlps);
        simplexSolver.doIteration(tableau);
        // Check if the iteration has been done correctly
        assertEquals(tableau.getSolution(), expectedSolution);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, restrictToNonNegative(), epsilon, maxUlps);
        simplexSolver.solvePhase1(tableau);
        // Check if Phase 1 of the simplex method has been solved correctly
        // e.g. if W is zero
        assertEquals(tableau.getEntry(0, tableau.getRhsOffset()), 0d, epsilon);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(function, constraints, goalType, restrictToNonNegative(), epsilon, maxUlps);
        PointValuePair result = simplexSolver.doOptimize();
        // Check if the optimization has been done correctly
        assertEquals(result, expectedResult);
    }
}
