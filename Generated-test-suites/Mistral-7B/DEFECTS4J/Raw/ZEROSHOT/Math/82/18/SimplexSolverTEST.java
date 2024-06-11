import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealMaxEval;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    private LinearObjectiveFunction objectiveFunction;
    private LinearConstraint[] constraints;
    private double[][] f;

    @Before
    public void setup() {
        simplexSolver = new SimplexSolver();
        objectiveFunction = new LinearObjectiveFunction(new double[][]{{3, 2}, {1, 1}}, new double[]{1, 1});
        constraints = new LinearConstraint[] {
                new LinearConstraint(new double[][]{{-1, 2}, {1, 3}}, Relationship.LEQ, 4),
                new LinearConstraint(new double[][]{{1, -1}, {-1, -3}}, Relationship.LEQ, 0)
        };
        f = new double[][]{{3, 2, -1}, {1, 1, -1}, {0, 0, 0}};
    }

    @Test
    public void testDefaultConstructor() {
        SimplexSolver simplexSolver = new SimplexSolver();
        assertEquals(1.0e-6, simplexSolver.epsilon, 0);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver simplexSolver = new SimplexSolver(0.001);
        assertEquals(0.001, simplexSolver.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        Integer expected = 1;
        Integer actual = simplexSolver.getPivotColumn(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        Integer expectedRow = 1;
        Integer expectedCol = 1;
        Integer actual = simplexSolver.getPivotRow(expectedCol, tableau);
        assertEquals(expectedRow, actual);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        Integer pivotCol = 1;
        Integer pivotRow = 1;
        double pivotValue = 1.5;

        simulateOneIteration(tableau, pivotCol, pivotRow, pivotValue);

        /* Check pivot element was set to 1 */
        assertEquals(pivotValue, tableau.getEntry(pivotRow, pivotCol), 1e-6);
        /* Check rest of pivot column was set to 0 */
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                double expected = -pivotValue * tableau.getEntry(i, pivotCol);
                double actual = tableau.getEntry(i, pivotCol);
                assertEquals(expected, actual, 1e-6);
            }
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        boolean expected = false;
        boolean actual = simplexSolver.isPhase1Solved(tableau);
        assertEquals(expected, actual);

        tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        expected = true;
        actual = simplexSolver.isPhase1Solved(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        tableau.discardArtificialVariables();

        boolean expected = false;
        boolean actual = simplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);

        tableau.setEntry(0, 1, 3.14159265359);
        expected = false;
        actual = simplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);

        tableau.setEntry(0, 1, 3.14159265359 + 1e-6);
        expected = true;
        actual = simplexSolver.isOptimal(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MAXIMIZE, false, 1.0e-6);
        simplexSolver.solvePhase1(tableau);
        tableau.discardArtificialVariables();

        RealPointValuePair expected = new RealPointValuePair(new double[]{1.0 / 3.0, 1.0 / 3.0}, 1.5);
        RealPointValuePair actual = simplexSolver.doOptimize();
        assertEquals(expected.getPoint(), actual.getPoint(), 1e-6);
        assertEquals(expected.getValue(), actual.getValue(), 1e-6);
    }

    private void simulateOneIteration(SimplexTableau tableau, int pivotCol, int pivotRow, double pivotValue) throws OptimizationException {
        simplexSolver.doIteration(tableau);
        assertEquals(pivotCol, tableau.getPivotColumn());
        assertEquals(pivotRow, tableau.getPivotRow());
        assertEquals(pivotValue, tableau.getPivotValue(), 1e-6);
    }
}