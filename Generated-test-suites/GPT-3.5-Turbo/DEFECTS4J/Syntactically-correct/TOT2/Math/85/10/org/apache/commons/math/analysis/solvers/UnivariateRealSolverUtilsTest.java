import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSettings() {
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result = UnivariateRealSolverUtils.solve(dummyFunction, 0, 10);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolveWithCustomAccuracy() {
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16;
            }
        };
        double result = UnivariateRealSolverUtils.solve(dummyFunction, 0, 10, 0.001);
        assertEquals(4.0, result, 0.001);
    }

    @Test
    public void testBracketSuccess() {
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            public double value(double x) {
                if (x < 5) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        double[] bracket = UnivariateRealSolverUtils.bracket(dummyFunction, 4, 0, 10);
        assertEquals(4.0, bracket[0], 0.0001);
        assertEquals(5.0, bracket[1], 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketFailure() {
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        UnivariateRealSolverUtils.bracket(dummyFunction, 0, 0, 10);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5.0, result, 0.0001);
    }

}