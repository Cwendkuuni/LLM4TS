import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.NumberIsTooSmallException;
import org.apache.commons.math.exception.NoBracketingException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    private static BracketingNthOrderBrentSolver solver;
    
    @BeforeClass
    public static void setUp() {
        solver = new BracketingNthOrderBrentSolver();
    }

    @Test
    public void testSolve_HappyPath() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x - 4;
            }
        };

        double result = solver.solve(100, function, 0, 10, AllowedSolution.ANY_SIDE);

        assertEquals(4.0, result, 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolve_MaximalOrderIsTooSmall() {
        solver.solve(100, null, 0, 10, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = NoBracketingException.class)
    public void testSolve_NoBracketingException() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x;
            }
        };

        solver.solve(100, function, 0, 10, AllowedSolution.ANY_SIDE);
    }

}