import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testAbsoluteAccuracyConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidMaximalOrderConstructor() {
        new BracketingNthOrderBrentSolver(1e-6, 1);
    }

    @Test
    public void testRelativeAccuracyConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-10, 1e-6, 1e-10, 5);
        assertEquals(1e-10, solver.getRelativeAccuracy(), 0.0);
    }

    @Test
    public void testAllowedSolutionSetter() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        solver.solve(100, f, 0.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(AllowedSolution.ANY_SIDE, solver.getAllowedSolution());
    }

    private UnivariateFunction f = new UnivariateFunction() {
        @Override
        public double value(double x) {
            return x * x - 4;
        }
    };

}