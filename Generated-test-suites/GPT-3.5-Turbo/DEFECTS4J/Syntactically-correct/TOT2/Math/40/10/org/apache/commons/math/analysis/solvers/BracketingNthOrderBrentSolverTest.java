import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.function.Sin;
import org.apache.commons.math.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithRelativeAccuracyAbsoluteAccuracyAndMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-12, 1e-6, 1e-10, 5);
        assertNotNull(solver);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testMaximalOrderGetter() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testSolve() {
        UnivariateFunction sinFunction = new Sin();
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        double result = solver.solve(100, sinFunction, -1, 1);
        assertEquals(0.0, sinFunction.value(result), 1e-6);
    }

}