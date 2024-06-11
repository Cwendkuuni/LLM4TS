import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, -2);
        tableau.setEntry(0, 1, -3);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, 0);

        assertEquals(new Integer(2), SimplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(1, 2, 6);
        tableau.setEntry(2, 2, 3);
        tableau.setEntry(3, 2, 2);

        assertEquals(new Integer(3), SimplexSolver.getPivotRow(tableau, 2));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_Unbounded() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -1);

        SimplexSolver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 1, 1);

        SimplexSolver.doIteration(tableau);

        assertEquals(1.0, tableau.getEntry(1, 0), 0.000001);
        assertEquals(0.0, tableau.getEntry(1, 1), 0.000001);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(1, 2, -1);
        tableau.setEntry(2, 2, 1);

        SimplexSolver.solvePhase1(tableau);

        assertEquals(0.0, tableau.getEntry(0, 2), 0.000001);
        assertEquals(0.0, tableau.getEntry(1, 2), 0.000001);
        assertEquals(0.0, tableau.getEntry(2, 2), 0.000001);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(1, 2, -1);

        SimplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 2);
        tableau.setEntry(0, 1, 3);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 0);
        
        tableau.setEntry(1, 0, -1);
        tableau.setEntry(1, 1, 1);
        tableau.setEntry(1, 2, 1);
        tableau.setEntry(1, 3, 3);

        PointValuePair solution = SimplexSolver.doOptimize(tableau);

        assertEquals(4.0, solution.getPoint()[0], 0.000001);
        assertEquals(3.0, solution.getPoint()[1], 0.000001);
    }

}