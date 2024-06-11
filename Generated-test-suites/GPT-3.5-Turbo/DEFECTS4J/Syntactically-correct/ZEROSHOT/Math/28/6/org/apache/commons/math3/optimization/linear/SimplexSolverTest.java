import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimplexSolverTest {

    // Test getPivotColumn method
    @Test
    public void testGetPivotColumn() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();
        
        // Test with a non-negative entry
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        assertEquals(new Integer(1), solver.getPivotColumn(tableau));
        
        // Test with a negative entry
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, 3);
        assertEquals(new Integer(1), solver.getPivotColumn(tableau));
        
        // Test with all negative entries
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -2);
        tableau.setEntry(0, 2, -3);
        assertEquals(new Integer(2), solver.getPivotColumn(tableau));
        
        // Test with all non-negative entries
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 3);
        assertEquals(null, solver.getPivotColumn(tableau));
    }

    // Test getPivotRow method
    @Test
    public void testGetPivotRow() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test with non-zero entry
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 2);
        assertEquals(new Integer(0), solver.getPivotRow(tableau, 0));

        // Test with zero entry
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(1, 0, 0);
        assertEquals(null, solver.getPivotRow(tableau, 0));
    }

    // Test doIteration method
    @Test
    public void testDoIteration() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test with a basic case
        tableau.setEntry(0, 0, 1);
        tableau.setEntry(0, 1, 2);
        tableau.setEntry(0, 2, 1);
        tableau.setEntry(0, 3, 4);
        tableau.setEntry(1, 0, 2);
        tableau.setEntry(1, 1, 4);
        tableau.setEntry(1, 2, 2);
        tableau.setEntry(1, 3, 8);
        tableau.setEntry(2, 0, 3);
        tableau.setEntry(2, 1, 6);
        tableau.setEntry(2, 2, 3);
        tableau.setEntry(2, 3, 12);
        tableau.setEntry(3, 0, 4);
        tableau.setEntry(3, 1, 8);
        tableau.setEntry(3, 2, 4);
        tableau.setEntry(3, 3, 16);

        solver.doIteration(tableau);

        double[][] expectedTableau = {
            {1, 2, 1, 4},
            {1, 2, 1, 4},
            {3, 6, 3, 12},
            {4, 8, 4, 16}
        };

        assertTrue(equalsTableau(tableau.getMatrix(), expectedTableau, 1.0e-6));

    }

    // Test solvePhase1 method
    @Test
    public void testSolvePhase1() {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau();

        // Test with a feasible solution
        tableau.setEntry(0, 0, -1);
        tableau.setEntry(0, 1, -1);
        tableau.setEntry(0, 2, -1);
        tableau.setEntry(0, 3, -1);
        tableau.setEntry(1, 0, 1);
        tableau.setEntry(1, 1, 0);
        tableau.setEntry(1, 2, 0);
        tableau.setEntry(1, 3, 0);
        tableau.setEntry(2, 0, 0);
        tableau.setEntry(2, 1, 1);
        tableau.setEntry(2, 2, 0);
        tableau.setEntry(2, 3, 0);
        tableau.setEntry(3, 0, 0);
        tableau.setEntry(3, 1, 0);
        tableau.setEntry(3, 2, 1);
        tableau.setEntry(3, 3, 0);

        try {
            solver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            fail("Exception thrown: NoFeasibleSolutionException");
        } catch (MaxCountExceededException e) {
            fail("Exception thrown: MaxCountExceededException");
        } catch (UnboundedSolutionException e) {
            fail("Exception thrown: UnboundedSolutionException");
        }

        double[][] expectedTableau = {
            {1, 1, 1, 1},
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0}
        };

        assertTrue(equalsTableau(tableau.getMatrix(), expectedTableau, 1.0e-6));
    }

    // Test doOptimize method
    @Test
    public void testDoOptimize() {
        SimplexSolver solver = new SimplexSolver();
        solver.setFunction(new LinearObjectiveFunction(new double[] { 1, 2, 3 }, 0));
        solver.setConstraints(new LinearConstraintSet(new LinearConstraint[] {
            new LinearConstraint(new double[] { 1, 1, 1 }, Relationship.EQ, 1),
            new LinearConstraint(new double[] { 1, 0, 0 }, Relationship.LEQ, 1),
            new LinearConstraint(new double[] { 0, 1, 0 }, Relationship.LEQ, 1),
            new LinearConstraint(new double[] { 0, 0, 1 }, Relationship.LEQ, 1)
        }));

        try {
            PointValuePair solution = solver.doOptimize();

            double[] expectedSolution = { 1.0, 0.0, 0.0 };
            assertTrue(equals(solution.getPoint(), expectedSolution, 1.0e-6));
        } catch (NoFeasibleSolutionException e) {
            fail("Exception thrown: NoFeasibleSolutionException");
        } catch (MaxCountExceededException e) {
            fail("Exception thrown: MaxCountExceededException");
        } catch (UnboundedSolutionException e) {
            fail("Exception thrown: UnboundedSolutionException");
        }
    }

    // Helper method to compare two tableau matrices
    private boolean equalsTableau(double[][] a, double[][] b, double eps) {
        if (a.length != b.length || a[0].length != b[0].length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (Math.abs(a[i][j] - b[i][j]) > eps) {
                    return false;
                }
            }
        }
        return true;
    }

    // Helper method to compare two arrays
    private boolean equals(double[] a, double[] b, double eps) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (Math.abs(a[i] - b[i]) > eps) {
                return false;
            }
        }
        return true;
    }

}