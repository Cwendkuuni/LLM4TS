import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private final double ABSOLUTE_ACCURACY = 1e-6;
    
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(ABSOLUTE_ACCURACY);

        // Test case 1: Function returns zero
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        };
        double result = solver.solve(100, f1, 0, 10);
        assertEquals(0.0, result, ABSOLUTE_ACCURACY);

        // Test case 2: Function returns positive value
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        result = solver.solve(100, f2, 0, 10);
        assertEquals(5.0, result, ABSOLUTE_ACCURACY);

        // Test case 3: Function returns negative value
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 15;
            }
        };
        result = solver.solve(100, f3, 0, 10);
        assertEquals(15.0, result, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolve_withAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(ABSOLUTE_ACCURACY);

        // Test case 1: Function returns zero
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return 0.0;
            }
        };
        double result = solver.solve(100, f1, 0, 10, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, ABSOLUTE_ACCURACY);

        // Test case 2: Function returns positive value
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        result = solver.solve(100, f2, 0, 10, AllowedSolution.RIGHT_SIDE);
        assertEquals(5.0, result, ABSOLUTE_ACCURACY);

        // Test case 3: Function returns negative value
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 15;
            }
        };
        result = solver.solve(100, f3, 0, 10, AllowedSolution.LEFT_SIDE);
        assertEquals(15.0, result, ABSOLUTE_ACCURACY);
    }
}