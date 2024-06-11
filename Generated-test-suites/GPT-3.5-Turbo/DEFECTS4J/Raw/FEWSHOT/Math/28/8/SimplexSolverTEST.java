import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        // Test case 1
        SimplexTableau tableau1 = new SimplexTableau();
        assertEquals(1, SimplexSolver.getPivotColumn(tableau1));

        // Test case 2
        SimplexTableau tableau2 = new SimplexTableau();
        assertEquals(2, SimplexSolver.getPivotColumn(tableau2));

        // Test case 3
        SimplexTableau tableau3 = new SimplexTableau();
        assertEquals(3, SimplexSolver.getPivotColumn(tableau3));
    }

    @Test
    public void testGetPivotRow() {
        // Test case 1
        SimplexTableau tableau1 = new SimplexTableau();
        int col1 = 1;
        assertEquals(1, SimplexSolver.getPivotRow(tableau1, col1));

        // Test case 2
        SimplexTableau tableau2 = new SimplexTableau();
        int col2 = 2;
        assertEquals(2, SimplexSolver.getPivotRow(tableau2, col2));

        // Test case 3
        SimplexTableau tableau3 = new SimplexTableau();
        int col3 = 3;
        assertEquals(3, SimplexSolver.getPivotRow(tableau3, col3));
    }

    @Test
    public void testDoIteration() {
        // Test case 1
        SimplexTableau tableau1 = new SimplexTableau();
        SimplexSolver.doIteration(tableau1);
        assertEquals(1, tableau1.getIterationsCounter());

        // Test case 2
        SimplexTableau tableau2 = new SimplexTableau();
        SimplexSolver.doIteration(tableau2);
        assertEquals(1, tableau2.getIterationsCounter());

        // Test case 3
        SimplexTableau tableau3 = new SimplexTableau();
        SimplexSolver.doIteration(tableau3);
        assertEquals(1, tableau3.getIterationsCounter());
    }

    @Test
    public void testSolvePhase1() {
        // Test case 1
        SimplexTableau tableau1 = new SimplexTableau();
        try {
            SimplexSolver.solvePhase1(tableau1);
        } catch (MaxCountExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Test case 2
        SimplexTableau tableau2 = new SimplexTableau();
        try {
            SimplexSolver.solvePhase1(tableau2);
        } catch (MaxCountExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Test case 3
        SimplexTableau tableau3 = new SimplexTableau();
        try {
            SimplexSolver.solvePhase1(tableau3);
        } catch (MaxCountExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testDoOptimize() {
        // Test case 1
        SimplexSolver solver1 = new SimplexSolver();
        try {
            PointValuePair result1 = solver1.doOptimize();
            assertEquals(0.0, result1.getValue(), 1e-6);
        } catch (MaxCountExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Test case 2
        SimplexSolver solver2 = new SimplexSolver();
        try {
            PointValuePair result2 = solver2.doOptimize();
            assertEquals(0.0, result2.getValue(), 1e-6);
        } catch (MaxCountExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Test case 3
        SimplexSolver solver3 = new SimplexSolver();
        try {
            PointValuePair result3 = solver3.doOptimize();
            assertEquals(0.0, result3.getValue(), 1e-6);
        } catch (MaxCountExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnboundedSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoFeasibleSolutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}