import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraint[] constraints;

    @Before
    public void setup() {
        double[][] f = {{1.0, 2.0}, {-1.0, -2.0}};
        LinearConstraint constraint1 = new LinearConstraint(Arrays.asList(1.0, 1.0), 3.0);
        LinearConstraint constraint2 = new LinearConstraint(Arrays.asList(1.0, 2.0), 2.0);
        LinearConstraint constraint3 = new LinearConstraint(Arrays.asList(2.0, 1.0), 1.0);

        this.objectiveFunction = new LinearObjectiveFunction(Arrays.asList(1.0, 2.0), 3.0);
        this.constraints = new LinearConstraint[] {constraint1, constraint2, constraint3};

        this.solver = new SimplexSolver();
    }

    @Test
    public void testDefaultConstructor() {
        SimplexSolver simplexSolver = new SimplexSolver();
        assertEquals(1.0e-6, simplexSolver.epsilon, 0);
    }

    @Test
    public void testConstructorWithEpsilon() {
        SimplexSolver simplexSolver = new SimplexSolver(0.001);
        assertEquals(0.001, simplexSolver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        Integer pivotRow = solver.getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        solver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        assertFalse(solver.isPhase1Solved(tableau));

        tableau.setEntry(1, 1, tableau.getEntry(1, 1) + 1.0);
        tableau.setEntry(1, 0, tableau.getEntry(1, 0) - 1.0);

        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        solver.solvePhase1(tableau);
        tableau.discardArtificialVariables();

        assertFalse(solver.isOptimal(tableau));

        tableau.setEntry(1, 1, tableau.getEntry(1, 1) + 1.0);
        tableau.setEntry(1, 0, tableau.getEntry(1, 0) - 1.0);

        assertTrue(solver.isOptimal(tableau));
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(objectiveFunction, constraints, null, null, 1.0e-6);
        RealPointValuePair solution = solver.doOptimize();

        PointValuePair expectedSolution = new PointValuePair(new double[] {3.0 / 2.0, 3.0 / 2.0}, 3.0);
        assertEquals(expectedSolution, solution);
    }
}