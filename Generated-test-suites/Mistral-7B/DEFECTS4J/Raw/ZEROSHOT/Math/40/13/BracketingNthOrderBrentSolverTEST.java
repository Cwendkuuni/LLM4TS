import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolveBracketingRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(f, 2) - 2;
        double result = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root found: " + result, 0.0, Precision.absOrRel(result, 1e-6));
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveNonBracketingRoot() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(f, 2) - 1;
        solver.solve(100, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveBracketingRootWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(f, 2) - 2;
        double result = solver.solve(100, function, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
        assertEquals("Root found: " + result, 0.0, Precision.absOrRel(result, 1e-6));
    }

    @Test(expected = NoBracketingException.class)
    public void testSolveNonBracketingRootWithStartValue() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(f, 2) - 1;
        solver.solve(100, function, -3.0, 3.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testSolveBracketingRootWithAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(f, 2) - 2;
        double result = solver.solve(100, function, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
        assertEquals("Root found: " + result, -sqrt(2), Precision.absOrRel(result, 1e-6));
    }

    @Test
    public void testSolveBracketingRootWithAllowedSolutionAbove() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = f -> Math.pow(f, 2) - 2;
        double result = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
        assertEquals("Root found: " + result, sqrt(2), Precision.absOrRel(result, 1e-6));
    }

    private double sqrt(double d) {
        return Math.sqrt(d);
    }

}