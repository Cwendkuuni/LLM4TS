import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() {
        // Create a BisectionSolver object
        BisectionSolver solver = new BisectionSolver();

        // Test with a simple linear function
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result1 = solver.solve(f1, 0, 10);
        assertEquals(5.0, result1, 0.001);
        
        // Test with a quadratic function
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        double result2 = solver.solve(f2, 0, 10);
        assertEquals(1.0, result2, 0.001);
        
        // Test with a function that has multiple roots
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) {
                return (x - 1) * (x - 2) * (x - 3);
            }
        };
        double result3 = solver.solve(f3, 0, 10);
        assertTrue(result3 >= 1.0 && result3 <= 3.0);
    }
}