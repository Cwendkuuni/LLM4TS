import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setup() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau();
        // Test case 1: no negative coefficients
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(0, 2, 3.0);
        Integer result = simplexSolver.getPivotColumn(tableau);
        assertNull(result);

        // Test case 2: negative coefficients
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, -2.0);
        tableau.setEntry(0, 2, -3.0);
        result = simplexSolver.getPivotColumn(tableau);
        assertEquals(new Integer(1), result);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(2, 0, 1.0);
        tableau.setEntry(3, 0, 1.0);
        tableau.setEntry(0, 1, 2.0);
        tableau.setEntry(1, 1, 3.0);
        tableau.setEntry(2, 1, 4.0);
        tableau.setEntry(3, 1, 5.0);
        Integer result = simplexSolver.getPivotRow(tableau, 1);
        assertEquals(new Integer(0), result);
    }

    @Test
    public void testDoIteration()
        throws MaxCountExceededException, UnboundedSolutionException {

        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(1, 0, 4.0);
        tableau.setEntry(2, 0, 6.0);
        simplexSolver.doIteration(tableau);
        assertEquals(1.0, tableau.getEntry(0, 0), 0.0001);
        assertEquals(2.0, tableau.getEntry(1, 0), 0.0001);
        assertEquals(3.0, tableau.getEntry(2, 0), 0.0001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution()
        throws MaxCountExceededException, UnboundedSolutionException {

        SimplexTableau tableau = new SimplexTableau();
        simplexSolver.doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution()
        throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {

        SimplexTableau tableau = new SimplexTableau();
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1()
        throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {

        SimplexTableau tableau = new SimplexTableau();
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(1, 0, 2.0);
        tableau.setEntry(2, 0, 3.0);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(1, 1, 2.0);
        tableau.setEntry(2, 1, 3.0);
        simplexSolver.solvePhase1(tableau);
        assertEquals(0.0, tableau.getEntry(0, 0), 0.0001);
        assertEquals(0.0, tableau.getEntry(1, 0), 0.0001);
        assertEquals(1.0, tableau.getEntry(2, 0), 0.0001);
        assertEquals(-0.5, tableau.getEntry(0, 1), 0.0001);
        assertEquals(-1.0, tableau.getEntry(1, 1), 0.0001);
        assertEquals(0.5, tableau.getEntry(2, 1), 0.0001);
    }

    @Test
    public void testDoOptimize()
        throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {

        simplexSolver.setFunction(new LinearObjectiveFunction(new double[]{3.0, 4.0}, 0));
        simplexSolver.setGoalType(GoalType.MAXIMIZE);
        simplexSolver.setConstraints(
            new LinearConstraintSet(
                new double[][]{{-1.0, 0.0}, {0.0, -1.0}, {1.0, 1.0}},
                Relationship.LEQ,
                new double[]{0.0, 0.0, 1.0}
            )
        );
        PointValuePair result = simplexSolver.doOptimize();
        assertEquals(0.0, result.getPoint()[0], 0.0001);
        assertEquals(0.0, result.getPoint()[1], 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

}