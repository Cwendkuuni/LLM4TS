import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver();
    }

    @Test
    public void testConstructor() {
        assertEquals(1.0e-6, simplexSolver.epsilon, 0);
        assertEquals(10, simplexSolver.maxUlps);
    }

    @Test
    public void testConstructorWithParams() {
        simplexSolver = new SimplexSolver(0.0001, 5);
        assertEquals(0.0001, simplexSolver.epsilon, 0);
        assertEquals(5, simplexSolver.maxUlps);
    }

    @Test
    public void testGetPivotColumn() {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(0, 0, -5);
        tableau.setEntry(0, 1, 0);
        tableau.setEntry(0, 2, 3);

        assertEquals(1, (int) simplexSolver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(1, 1, 4);
        tableau.setEntry(1, 2, 5);
        tableau.setEntry(2, 1, -3);
        tableau.setEntry(2, 2, 2);

        assertEquals(2, (int) simplexSolver.getPivotRow(tableau, 1));
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 0, 3);
        tableau.setEntry(1, 1, 4);

        simplexSolver.doIteration(tableau);

        assertEquals(1, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(0, 1), 0);
        assertEquals(-15, tableau.getEntry(1, 0), 0);
        assertEquals(0, tableau.getEntry(1, 1), 0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(1, 0, -3);
        tableau.setEntry(1, 1, -4);

        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 0, 4);
        tableau.setEntry(1, 1, 5);
        tableau.setEntry(1, 2, 6);

        simplexSolver.solvePhase1(tableau);

        assertEquals(0, tableau.getEntry(0, 0), 0);
        assertEquals(0, tableau.getEntry(0, 1), 0);
        assertEquals(0, tableau.getEntry(0, 2), 0);
        assertEquals(1, tableau.getEntry(1, 0), 0);
        assertEquals(2, tableau.getEntry(1, 1), 0);
        assertEquals(3, tableau.getEntry(1, 2), 0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);

        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Create a SimplexTableau object
        SimplexTableau tableau = new SimplexTableau(null, null, null, false, 0, 0);

        // Set some dummy values for testing
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        tableau.setEntry(1, 0, 4);
        tableau.setEntry(1, 1, 5);
        tableau.setEntry(1, 2, 6);

        PointValuePair result = simplexSolver.doOptimize();

        assertEquals(0, result.getPoint()[0], 0);
        assertEquals(0, result.getPoint()[1], 0);
        assertEquals(0, result.getPoint()[2], 0);
    }
}