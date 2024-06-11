import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_returnsExactRoot_whenFunctionValueIsZero() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Return 0 for min bound
                if (x == getMin()) {
                    return 0.0;
                }
                // Return 0 for max bound
                if (x == getMax()) {
                    return 0.0;
                }
                return super.computeObjectiveValue(x);
            }
        };

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        }, 4, 6);

        Assert.assertEquals(5.0, root, 1e-6);
    }

    // Continue generating more test cases based on the identified edge cases and exceptions
}