import org.junit.Test;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 2}, 0), 
                                Relationship.EQ, new double[]{2});

        assertEquals(1, solver.getPivotColumn(tableau), 0.0001);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 2}, 0), 
                                Relationship.EQ, new double[]{2});
        int col = solver.getPivotColumn(tableau);

        assertEquals(1, solver.getPivotRow(tableau, col), 0.0001);
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 2}, 0), 
                                Relationship.EQ, new double[]{2});

        try {
            solver.doIteration(tableau);
        } catch (Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1, 2}, 0), 
                                Relationship.EQ, new double[]{2});

        try {
            solver.solvePhase1(tableau);
        } catch (Exception e) {
            fail("Exception thrown when not expected: " + e.getMessage());
        }
    }

}