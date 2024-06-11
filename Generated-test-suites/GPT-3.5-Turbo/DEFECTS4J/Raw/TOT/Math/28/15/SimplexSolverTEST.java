import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;


public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(2, 2);
        
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -3.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(1, 1, -4.0);
        
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        
        Assert.assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(2, 2);
        
        tableau.setEntry(2, 0, 2.0);
        tableau.setEntry(2, 1, 5.0);
        tableau.setEntry(3, 0, 3.0);
        tableau.setEntry(3, 1, 4.0);
        
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 1);
        
        Assert.assertEquals(3, pivotRow.intValue());
    }

    @Test
    public void testDoIteration() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(3, 3);
        
        tableau.setEntry(0, 0, 2.0);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 2.0);
        tableau.setEntry(1, 0, 1.0);
        tableau.setEntry(1, 1, 1.0);
        tableau.setEntry(1, 2, 3.0);
        tableau.setEntry(2, 0, 3.0);
        tableau.setEntry(2, 1, 2.0);
        tableau.setEntry(2, 2, 1.0);
        
        simplexSolver.doIteration(tableau);
        
        Assert.assertEquals(0.0, tableau.getEntry(0, 0), 1e-6);
        Assert.assertEquals(1.0, tableau.getEntry(0, 1), 1e-6);
        Assert.assertEquals(2.0, tableau.getEntry(0, 2), 1e-6);
        Assert.assertEquals(0.0, tableau.getEntry(1, 0), 1e-6);
        Assert.assertEquals(0.0, tableau.getEntry(1, 1), 1e-6);
        Assert.assertEquals(2.0, tableau.getEntry(1, 2), 1e-6);
        Assert.assertEquals(0.0, tableau.getEntry(2, 0), 1e-6);
        Assert.assertEquals(5.0, tableau.getEntry(2, 1), 1e-6);
        Assert.assertEquals(-5.0, tableau.getEntry(2, 2), 1e-6);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationWithUnboundedSolutionException() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(2, 2);
        
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -3.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(1, 1, -4.0);
        
        simplexSolver.doIteration(tableau);
    }

    @Test
    public void testSolvePhase1() {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(3, 5);
        
        tableau.setEntry(0, 0, 1.0);
        tableau.setEntry(0, 1, 1.0);
        tableau.setEntry(0, 2, 1.0);
        tableau.setEntry(0, 3, 1.0);
        tableau.setEntry(0, 4, 1.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(1, 1, 0.0);
        tableau.setEntry(1, 2, 0.0);
        tableau.setEntry(1, 3, 0.0);
        tableau.setEntry(1, 4, 0.0);
        tableau.setEntry(2, 0, 0.0);
        tableau.setEntry(2, 1, -1.0);
        tableau.setEntry(2, 2, 0.0);
        tableau.setEntry(2, 3, 0.0);
        tableau.setEntry(2, 4, 0.0);
        
        try {
            simplexSolver.solvePhase1(tableau);
        } catch (NoFeasibleSolutionException e) {
            Assert.fail("Expected NoFeasibleSolutionException not thrown");
        } catch (UnboundedSolutionException e) {
            Assert.fail("Expected UnboundedSolutionException not thrown");
        } catch (MaxCountExceededException e) {
            Assert.fail("Unexpected MaxCountExceededException");
        }

        try {
            Assert.assertEquals(0.0, tableau.getEntry(0, 0), 1e-6);
            Assert.assertEquals(0.0, tableau.getEntry(0, 1), 1e-6);
            Assert.assertEquals(0.0, tableau.getEntry(0, 2), 1e-6);
            Assert.assertEquals(1.0, tableau.getEntry(0, 3), 1e-6);
            Assert.assertEquals(1.0, tableau.getEntry(0, 4), 1e-6);
        } catch (IndexOutOfBoundsException e) {
            Assert.fail("Unexpected IndexOutOfBoundsException");
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolutionException() throws NoFeasibleSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(2, 2);
        
        tableau.setEntry(0, 0, -2.0);
        tableau.setEntry(0, 1, -3.0);
        tableau.setEntry(1, 0, -1.0);
        tableau.setEntry(1, 1, -4.0);
        
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() {
        SimplexSolver simplexSolver = new SimplexSolver();
        
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{3, 1}, 0);
        
        Relationship[] relationships = {Relationship.LEQ, Relationship.GEQ};
        double[][] coefficients = {{1, 1}, {-1, 4}};
        double[] values = {5, 4};
        
        SimplexTableau tableau = new SimplexTableau(function, relationships, coefficients, values);
        
        try {
            PointValuePair solution = simplexSolver.doOptimize(tableau);
            
            Assert.assertEquals(0.0, solution.getPoint()[0], 1e-6);
            Assert.assertEquals(5.0, solution.getPoint()[1], 1e-6);
        } catch (NoFeasibleSolutionException | UnboundedSolutionException ex) {
            Assert.fail("Unexpected exception");
        }
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeWithUnboundedSolutionException() throws UnboundedSolutionException {
        SimplexSolver simplexSolver = new SimplexSolver();
        
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{2, -3, 1}, 0);
        
        Relationship[] relationships = {Relationship.LEQ, Relationship.GEQ, Relationship.GEQ};
        double[][] coefficients = {{1, -1, 2}, {1, 1, 1}, {1, -2, -1}};
        double[] values = {1, 3, -1};
        
        SimplexTableau tableau = new SimplexTableau(function, relationships, coefficients, values);
        
        simplexSolver.doOptimize(tableau);
    }

}