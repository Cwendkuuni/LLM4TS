import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // TODO: Implement method for computing objective value
                return 0;
            }

            @Override
            protected void verifyBracketing(double x0, double x1) {
                // TODO: Implement bracketing verification
            }
        };
    }

    @Test
    public void testSolve() {
        int maxEval = 100;
        UnivariateRealFunction f = null;
        double min = 0;
        double max = 10;
        double startValue = 1;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;
        
        double result = solver.solve(maxEval, f, min, max, startValue, allowedSolution);

        // TODO: Add assertions for expected result
        assertEquals(0, result, 0);
    }

}