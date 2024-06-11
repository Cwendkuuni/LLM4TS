import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolverStub();
    }

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunctionStub();
        double min = 0;
        double max = 10;
        double startValue = 5;
        BaseSecantSolver.AllowedSolution allowedSolution = BaseSecantSolver.AllowedSolution.ANY_SIDE;

        double result = solver.solve(1000, f, min, max, startValue, allowedSolution);

        // Assert result is within expected range
        assertTrue(result >= min && result <= max);
    }

    private class BaseSecantSolverStub extends BaseSecantSolver {

        public BaseSecantSolverStub() {
            super(1e-6, Method.REGULA_FALSI);
        }

        @Override
        protected double computeObjectiveValue(double x) {
            return x * x - 4;
        }
    }

    private class UnivariateRealFunctionStub implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return x * x - 4;
        }
    }
}