import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolverConstruction() {
        SimplexSolver solver = new SimplexSolver();
        SimplexSolver solverWithEpsilon = new SimplexSolver(1.0e-5);
        assertEquals(1.0e-6, solver.epsilon, 0);
        assertEquals(1.0e-5, solverWithEpsilon.epsilon, 0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        Integer pivotRow = new SimplexSolver().getPivotRow(1, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().doIteration(tableau);

        double[] expectedTableau = {-0.9999999999999992D, 1.000000000000001D, 0.0, 0.0, 0.0, 0.0, 0.0, -1.000000000000001D, 0.0};
        assertArrayEquals(expectedTableau, tableau.getTableau(), 0);
    }

    @Test
    public void testIsPhase1Solved() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        new SimplexSolver().doIteration(tableau);

        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        assertFalse(new SimplexSolver().isOptimal(tableau));

        new SimplexSolver().doIteration(tableau);

        new SimplexSolver().doIteration(tableau);

        assertTrue(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);

        double[] expectedTableau = {-0.9999999999999992D, 1.000000000000001D, 0.0, 0.0, 0.0, 0.0, 0.0, -1.000000000000001D, 0.0};
        assertArrayEquals(expectedTableau, tableau.getTableau(), 0);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, null),
                new LinearConstraintSet(new LinearConstraint[] {
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[]{-1.0, 1.0}, Relationship.LEQ, 2.0)
                }), GoalType.MINIMIZE, true, 1.0e-6);

        RealPointValuePair result = new SimplexSolver().doOptimize();

        PointValuePair expectedResult = new PointValuePair(-0.9999999999999992, new double[]{1.0, 0.0});
        assertEquals(expectedResult, result);
    }

}