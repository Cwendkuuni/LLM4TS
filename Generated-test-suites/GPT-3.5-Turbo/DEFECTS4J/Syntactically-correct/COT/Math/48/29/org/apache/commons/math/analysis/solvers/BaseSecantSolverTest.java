
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.UnivariateRealFunction;

@RunWith(JUnitParamsRunner.class)
public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_exactRoot() {
        UnivariateRealFunction f = x -> x - 5;
        double root = solver.solve(100, f, 0, 10); // should find the root at x = 5
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_invertedBounds() {
        UnivariateRealFunction f = x -> x - 5;
        double root = solver.solve(100, f, 10, 0); // should find the root at x = 5
        assertEquals(5.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_nonBracketing() {
        UnivariateRealFunction f = x -> x - 5;
        solver.solve(100, f, 1, 2); // should throw ConvergenceException since the function is not bracketed
    }

    @Test
    public void testSolve_allowedSolutions() {
        UnivariateRealFunction f = x -> x - 5;

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        double root1 = solver.solve(100, f, 0, 6); // should find the root at x = 5
        assertEquals(5.0, root1, 1e-6);

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        double root2 = solver.solve(100, f, 4, 10); // should find the root at x = 5
        assertEquals(5.0, root2, 1e-6);

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        double root3 = solver.solve(100, f, 0, 4); // should find the root at x = 5
        assertEquals(5.0, root3, 1e-6);

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        double root4 = solver.solve(100, f, 6, 10); // should find the root at x = 5
        assertEquals(5.0, root4, 1e-6);

        solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double root5 = solver.solve(100, f, 4, 6); // should find the root at x = 5
        assertEquals(5.0, root5, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_invalidMethod() {
        BaseSecantSolver.Method invalidMethod = null;
        solver = new BaseSecantSolver(1e-6, invalidMethod);
    }
}

