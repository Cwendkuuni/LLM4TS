import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testConstructorWithEpsilon() {
        SimplexSolver solver = new SimplexSolver(0.0001);
        assertEquals(0.0001, solver.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1}, 0), new double[][]{{1, 2, Relationship.LEQ, 3},
                {1, -1, Relationship.LEQ, 1}}, Relationship.LEQ);

        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(3), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1}, 0), new double[][]{{1, 2, Relationship.LEQ, 3},
                {1, -1, Relationship.LEQ, 1}}, Relationship.LEQ);

        Integer pivotRow = solver.getPivotRow(3, tableau);
        assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1}, 0), new double[][]{{1, 2, Relationship.LEQ, 3},
                {1, -1, Relationship.LEQ, 1}}, Relationship.LEQ);

        solver.doIteration(tableau);

        assertEquals(1.0, tableau.getEntry(0, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 1), 0.0);
        assertEquals(2.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(-2.0, tableau.getEntry(1, 1), 0.0);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1}, 0), new double[][]{{1, 2, Relationship.LEQ, 3},
                {1, -1, Relationship.LEQ, 1}}, Relationship.LEQ);

        solver.doIteration(tableau);
        solver.doIteration(tableau); // Unbounded solution
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau1 = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1}, 0), new double[][]{{1, 2, Relationship.LEQ, 3}}, Relationship.LEQ);
        SimplexTableau tableau2 = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1, -1}, 0), new double[][]{{1, 2, -1, Relationship.LEQ, 3}}, Relationship.LEQ);

        assertTrue(solver.isPhase1Solved(tableau1));
        assertFalse(solver.isPhase1Solved(tableau2));
    }

    @Test
    public void testIsOptimal() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau1 = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1}, 0), new double[][]{{1, 2, Relationship.LEQ, 3}}, Relationship.LEQ);
        SimplexTableau tableau2 = new SimplexTableau(new LinearObjectiveFunction(
                new double[]{1, 1, -1}, 0), new double[][]{{1, 2, -1, Relationship.LEQ, 3}}, Relationship.LEQ);

        assertTrue(solver.isOptimal(tableau1));
        assertFalse(solver.isOptimal(tableau2));
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        double[][] A = {{1, 2, -1, Relationship.LEQ, 3}};
        Relationship[] relationship = {Relationship.LEQ};
        SimplexTableau tableau = new SimplexTableau(f, A, relationship);
        boolean exceptionThrown = false;

        try {
            solver.solvePhase1(tableau);
        } catch (OptimizationException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, 2}, 0);
        double[][] A = {{3, 1, Relationship.LEQ, 2}, {-1, 2, Relationship.LEQ, 3}};
        Relationship[] relationship = {Relationship.LEQ, Relationship.LEQ};
        SimplexTableau tableau = new SimplexTableau(f, A, relationship);

        RealPointValuePair solution = solver.doOptimize();

        assertEquals(1.0, solution.getPoint()[0], 0.0);
        assertEquals(0.0, solution.getPoint()[1], 0.0);
        assertEquals(0.0, solution.getValue(), 0.0);
    }
}