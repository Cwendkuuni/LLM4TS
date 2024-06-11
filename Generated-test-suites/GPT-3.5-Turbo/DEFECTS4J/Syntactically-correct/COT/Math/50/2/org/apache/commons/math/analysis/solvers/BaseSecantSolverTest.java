import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // TODO: Implement computeObjectiveValue() for the test
                return 0;
            }
        };

        int maxEval = 100;
        double min = -10;
        double max = 10;
        double startValue = 0;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(maxEval, null, min, max, startValue, allowedSolution);

        // TODO: Add assertions for the expected result
        // For example:
        // assertEquals(expectedResult, result, delta);
    }
}