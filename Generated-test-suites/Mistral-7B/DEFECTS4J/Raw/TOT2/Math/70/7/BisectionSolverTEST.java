import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.solvers.BisectionSolver;

public class BisectionSolverTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 4, 0}, 3);
        double expectedResult = 1.0;
        double min = 0.0;
        double max = 2.0;

        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(function, min, max);

        assertEquals(expectedResult, result, 0.001);
    }
}