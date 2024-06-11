import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BracketingNthOrderBrentSolverTest {

    private BracketingNthOrderBrentSolver solver;
    private UnivariateFunction function;

    @Before
    public void setUp() {
        solver = new BracketingNthOrderBrentSolver(1e-5, 5);
        function = f -> Math.pow(Math.sin(f), 3) - 2 * Math.pow(Math.E, f);
    }

    @Test
    public void testSolvebracketingroot() {
        double result = solver.solve(100, function, -3.0, 1.0);
        assertTrue(function.value(result) < 1e-5);
    }

    @Test
    public void testSolvebracketingroot2() {
        solver = new BracketingNthOrderBrentSolver(1e-5, 5, 1e-5, 5);
        double result = solver.solve(100, function, -3.0, 1.0, 0.0, AllowedSolution.ABOVE_SIDE);
        assertTrue(function.value(result) < 1e-5);
        assertEquals(result, -1.211525654042781, 1e-5);
    }

    @Test(expected = NoBracketingException.class)
    public void testNobracketingroot() {
        solver = new BracketingNthOrderBrentSolver(1e-5, 5);
        solver.solve(100, function, 1.0, 1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaximalOrder() {
        solver = new BracketingNthOrderBrentSolver(-5, 5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaximalAccuracy() {
        solver = new BracketingNthOrderBrentSolver(1e-5, -5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaximalFunctionValueAccuracy() {
        solver = new BracketingNthOrderBrentSolver(1e-5, 5, 1e-5, -5);
    }

}