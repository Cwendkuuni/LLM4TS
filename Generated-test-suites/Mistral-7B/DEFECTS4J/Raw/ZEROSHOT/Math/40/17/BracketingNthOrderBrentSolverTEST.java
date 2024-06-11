import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolverWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = (x) -> Math.pow(x, 2) - 4;
        double expectedResult = 2.0;
        double min = -3.0;
        double max = 3.0;

        double result = solver.solve(100, function, min, max);
        assertEquals("The solver did not return the expected result.", expectedResult, result, 0.001);
    }

    @Test
    public void testSolverWithCustomValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 8);
        UnivariateFunction function = (x) -> Math.sin(x) - 0.5;
        double expectedResult = 0.959935854024659;
        double min = -3.0;
        double max = 3.0;

        double result = solver.solve(100, function, min, max);
        assertEquals("The solver did not return the expected result.", expectedResult, result, 0.001);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolverWithNoBracketing() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = (x) -> Math.pow(x, 3) + 1;
        double min = -1.5;
        double max = 1.5;

        solver.solve(100, function, min, max);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverWithNegativeMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(-5, 1e-8);
    }

}