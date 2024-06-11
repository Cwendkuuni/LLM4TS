import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Create test objects
        BaseSecantSolver solver = new BaseSecantSolverTestImpl();

        // Test cases
        // Test case 1
        int maxEval1 = 1000;
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min1 = 0;
        double max1 = 10;
        double startValue1 = 2;
        AllowedSolution allowed1 = AllowedSolution.ANY_SIDE;
        double expected1 = 5.0;
        double result1 = solver.solve(maxEval1, f1, min1, max1, startValue1, allowed1);
        assertEquals(expected1, result1, 0.001);

        // Test case 2
        int maxEval2 = 1000;
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min2 = 0;
        double max2 = 10;
        double startValue2 = 2;
        AllowedSolution allowed2 = AllowedSolution.ABOVE_SIDE;
        double expected2 = 4.0;
        double result2 = solver.solve(maxEval2, f2, min2, max2, startValue2, allowed2);
        assertEquals(expected2, result2, 0.001);

        // Test case 3
        int maxEval3 = 1000;
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double min3 = 0;
        double max3 = 10;
        double startValue3 = 2;
        AllowedSolution allowed3 = AllowedSolution.RIGHT_SIDE;
        double expected3 = 4.0;
        double result3 = solver.solve(maxEval3, f3, min3, max3, startValue3, allowed3);
        assertEquals(expected3, result3, 0.001);
    }

    private class BaseSecantSolverTestImpl extends BaseSecantSolver {
        public BaseSecantSolverTestImpl() {
            super(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
        }

        protected double computeObjectiveValue(double x) {
            return x * x;
        }
    }
}