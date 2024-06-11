
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {
    private final double EPSILON = 1e-6;

    @Test
    public void testSolveWithDefaultSettings() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 1, 0, 2);

        Assert.assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolutionAnySide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 1, 0, 2, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);

        Assert.assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolutionLeftSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 1, 0, 2, BracketingNthOrderBrentSolver.AllowedSolution.LEFT_SIDE);

        Assert.assertEquals(0.999999, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolutionRightSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 1, 0, 2, BracketingNthOrderBrentSolver.AllowedSolution.RIGHT_SIDE);

        Assert.assertEquals(1.000001, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolutionBelowSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 1, 0, 2, BracketingNthOrderBrentSolver.AllowedSolution.BELOW_SIDE);

        Assert.assertEquals(0.999999, result, EPSILON);
    }

    @Test
    public void testSolveWithAllowedSolutionAboveSide() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, x -> x - 1, 0, 2, BracketingNthOrderBrentSolver.AllowedSolution.ABOVE_SIDE);

        Assert.assertEquals(1.000001, result, EPSILON);
    }
}
