import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.NoBracketingException;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    private double absoluteAccuracy = 1e-6;
    private int maximalOrder = 5;

    @Test
    public void testSolve() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        QuinticFunction function = new QuinticFunction();
        double root = solver.solve(100, function, -10, 10, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        double expected = -0.732672;
        Assert.assertEquals(expected, root, absoluteAccuracy);
    }

    @Test
    public void testSolveMaxEval() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        QuinticFunction function = new QuinticFunction();
        double root = solver.solve(5, function, -10, 10, BracketingNthOrderBrentSolver.AllowedSolution.LEFT_SIDE);
        Assert.assertTrue(Double.isNaN(root));
    }

    @Test
    public void testSolveMinOrder2() {
        try {
            new BracketingNthOrderBrentSolver(absoluteAccuracy, 1);
        } catch (NumberIsTooSmallException e) {
            return;
        }
        Assert.fail("An exception should have been thrown");
    }

    @Test
    public void testSolveExactRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        QuinticFunction function = new QuinticFunction();
        double root = solver.solve(100, function, -10, 10, 4.0, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
        double expected = 4.0;
        Assert.assertEquals(expected, root, absoluteAccuracy);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        QuinticFunction function = new QuinticFunction();
        solver.solve(100, function, 0, 10, BracketingNthOrderBrentSolver.AllowedSolution.ANY_SIDE);
    }

}