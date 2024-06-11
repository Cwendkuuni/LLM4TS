import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.MaxCountExceededException;

public class SimplexSolverTest {

    private LinearConstraint[] constraints;
    private GoalType goalType;
    private boolean restrictToNonNegative;

    @Before
    public void setUp() {
        constraints = new LinearConstraint[3];
        constraints[0] = new LinearConstraint(new double[] {1, 2, 3}, Relationship.LEQ, 10);
        constraints[1] = new LinearConstraint(new double[] {4, 5, 6}, Relationship.GEQ, 20);
        constraints[2] = new LinearConstraint(new double[] {7, 8, 9}, Relationship.EQ, 30);
        goalType = GoalType.MAXIMIZE;
        restrictToNonNegative = true;
    }

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver(1e-8, 20);
        assertNotNull(solver);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][] {{1, 2, -1}, {3, -4, 0}}, new double[] {2, 1}, Restriction.EQ);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(2), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][] {{1, 0, 4, 1}, {0, 2, -3, 1}, {0, 3, 5, 0}}, new double[] {24, 6, 5}, Relationship.LEQ);
        Integer pivotRow = solver.getPivotRow(tableau, 2);
        assertEquals(Integer.valueOf(1), pivotRow);
    }

    @Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][] {{2, 1, -1, 1}, {1, -1, 0, -1}, {-1, 2, -1, 3}}, new double[] {4, 1, 2}, Relationship.LEQ);
        solver.doIteration(tableau);
        PointValuePair solution = tableau.getSolution();
        assertEquals(2.0, solution.getPoint()[0], 1e-8);
        assertEquals(1.0, solution.getPoint()[1], 1e-8);
    }

    @Test(expected=UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][] {{-1, 0, -3, -1}, {1, -2, -5, -1}}, new double[] {2, -1}, Relationship.LEQ);
        solver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][] {{1, 1, 1}, {-1, -1, 0}}, new double[] {4, -1}, Relationship.LEQ);
        solver.solvePhase1(tableau);
        PointValuePair solution = tableau.getSolution();
        assertEquals(2.0, solution.getPoint()[0], 1e-8);
        assertEquals(1.0, solution.getPoint()[1], 1e-8);
    }

    @Test(expected=NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new double[][] {{1, 1, -1}, {-1, -1, 0}}, new double[] {4, -1}, Relationship.LEQ);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        double[] expected = new double[] {0.0, 0.0, 0.0};
        assertArrayEquals(expected, solution.getPoint(), 1e-8);
    }

    @Test(expected=NoFeasibleSolutionException.class)
    public void testDoOptimizeWithNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearConstraint[] constraints = new LinearConstraint[2];
        constraints[0] = new LinearConstraint(new double[] {1, 0, 0}, Relationship.EQ, 1);
        constraints[1] = new LinearConstraint(new double[] {0, 1, 0}, Relationship.EQ, 1);
        solver.setConstraints(constraints);
        PointValuePair solution = solver.doOptimize();
    }
}