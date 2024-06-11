import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.PrimaRhs;
import org.apache.commons.math3.optimization.linear.PrimeGoalType;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(), new PrimRhs(), PrimeGoalType.MAXIMIZE, false, 1.0e-6, 10);
        assertNotNull(solver.getPivotColumn(tableau));
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(), new PrimRhs(), PrimeGoalType.MAXIMIZE, false, 1.0e-6, 10);
        assertNotNull(solver.getPivotRow(tableau, 1));
    }

    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(), new PrimRhs(), PrimeGoalType.MAXIMIZE, false, 1.0e-6, 10);
        assertDoesNotThrow(() -> solver.doIteration(tableau));
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(), new PrimRhs(), PrimeGoalType.MAXIMIZE, false, 1.0e-6, 10);
        assertThrows(NoFeasibleSolutionException.class, () -> solver.solvePhase1(tableau));
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        assertDoesNotThrow(() -> solver.doOptimize());
    }

}