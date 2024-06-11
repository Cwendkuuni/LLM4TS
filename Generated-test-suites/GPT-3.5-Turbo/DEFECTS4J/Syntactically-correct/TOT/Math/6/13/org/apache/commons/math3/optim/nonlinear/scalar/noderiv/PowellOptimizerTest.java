import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {

    private PowellOptimizer powellOptimizer;

    @Before
    public void setUp() {
        powellOptimizer = new PowellOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimize_minimize() {
        // Typical Use Case
        double[] startPoint = {1.0, 1.0, 1.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double alpha) {
                return Math.pow(alpha - 1, 2);
            }
        };
        PowellOptimizer.GolType goalType = PowellOptimizer.GoalType.MINIMIZE;

        PointValuePair result = powellOptimizer.doOptimize(startPoint, objectiveFunction, goalType);

        // Validate Result
        double[] expectedPoint = {1.0, 1.0, 1.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test
    public void testDoOptimize_maximize() {
        // Typical Use Case
        double[] startPoint = {1.0, 1.0, 1.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double alpha) {
                return -Math.pow(alpha - 1, 2);
            }
        };
        PowellOptimizer.GolType goalType = PowellOptimizer.GoalType.MAXIMIZE;

        PointValuePair result = powellOptimizer.doOptimize(startPoint, objectiveFunction, goalType);

        // Validate Result
        double[] expectedPoint = {1.0, 1.0, 1.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeToleranceTooSmall() {
        // Edge Case: Relative Tolerance is too small
        double relTolerance = 1e-15;
        double absTolerance = 1e-6;

        new PowellOptimizer(relTolerance, absTolerance);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteToleranceNotPositive() {
        // Edge Case: Absolute Tolerance is not positive
        double relTolerance = 1e-6;
        double absTolerance = 0;

        new PowellOptimizer(relTolerance, absTolerance);
    }

    @Test
    public void testDoOptimize_CustomConvergenceChecker() {
        // Edge Case: Custom Convergence Checker
        double[] startPoint = {1.0, 1.0, 1.0};
        UnivariateFunction objectiveFunction = new UnivariateFunction() {
            public double value(double alpha) {
                return Math.pow(alpha - 1, 2);
            }
        };
        PowellOptimizer.GolType goalType = PowellOptimizer.GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Stop after 5 iterations
                return iteration >= 5;
            }
        };

        powellOptimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        PointValuePair result = powellOptimizer.doOptimize(startPoint, objectiveFunction, goalType);

        // Validate Result
        double[] expectedPoint = {1.0, 1.0, 1.0};
        double expectedValue = 0.0;
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        assertEquals(expectedValue, result.getValue(), 1e-6);
    }

}