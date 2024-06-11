import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.Relationship;

public class SimplexSolverTest {

    private SimplexSolver simplexSolver;
    
    private LinearObjectiveFunction function;
    
    private LinearConstraint constraint;

    @Before
    public void setup() {
        simplexSolver = new SimplexSolver();
        function = new LinearObjectiveFunction(new double[]{1d, 2d}, 0d);
        constraint = new LinearConstraint(new double[]{1d, 1d}, Relationship.EQ, 1d);
    }
    
    @Test
    public void testDoOptimize() {
        PointValuePair solution;
        try {
            solution = simplexSolver.doOptimize();
            assertNotNull(solution);
            assertEquals(2, solution.getPoint().length);
            assertEquals(0d, solution.getPoint()[0], 0d);
            assertEquals(0d, solution.getPoint()[1], 0d);
            assertEquals(0d, solution.getValue(), 0d);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoIteration() {
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[]{constraint}, null, false, SimplexSolver.DEFAULT_EPSILON, SimplexSolver.DEFAULT_ULPS);
        try {
            simplexSolver.doIteration(tableau);
            double[] expectedCosts = {2d, -1d, 0d};
            double[] expectedRow = {0.5d, -0.5d};
            assertArrayEquals(expectedCosts, tableau.getCosts(), 0d);
            assertArrayEquals(expectedRow, tableau.getRow(0), 0d);
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testSolvePhase1() {
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[]{constraint}, null, false, SimplexSolver.DEFAULT_EPSILON, SimplexSolver.DEFAULT_ULPS);
        try {
            simplexSolver.solvePhase1(tableau);
            double[] expectedCosts = {3d, -1d, 0d};
            double[] expectedRow = {0.5d, 0.5d};
            assertArrayEquals(expectedCosts, tableau.getCosts(), 0d);
            assertArrayEquals(expectedRow, tableau.getRow(0), 0d);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoOptimize_MaxCountExceededException() {
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[]{constraint}, null, false, SimplexSolver.DEFAULT_EPSILON, SimplexSolver.DEFAULT_ULPS);
        try {
            tableau.setMaxIterations(1);
            simplexSolver.solvePhase1(tableau);
            simplexSolver.doIteration(tableau);
            fail("Expected MaxCountExceededException was not thrown");
        } catch (MaxCountExceededException e) {
            // Expected exception was thrown
        } catch (UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoOptimize_UnboundedSolutionException() {
        try {
            PointValuePair solution = simplexSolver.doOptimize();
            fail("Expected UnboundedSolutionException was not thrown");
        } catch (UnboundedSolutionException | NoFeasibleSolutionException e) {
            // Expected exception was thrown
        } catch (MaxCountExceededException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testDoOptimize_NoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(function, new LinearConstraint[]{constraint}, null, false, SimplexSolver.DEFAULT_EPSILON, SimplexSolver.DEFAULT_ULPS);
        try {
            LinearConstraint[] constraints = {new LinearConstraint(new double[]{-1d, -1d}, Relationship.LEQ, -1d)};
            tableau.setConstraints(constraints);
            simplexSolver.solvePhase1(tableau);
            simplexSolver.doIteration(tableau);
            fail("Expected NoFeasibleSolutionException was not thrown");
        } catch (NoFeasibleSolutionException e) {
            // Expected exception was thrown
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

}