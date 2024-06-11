import static org.junit.Assert.assertEquals;
import org.junit.Test;


public class UnivariateRealSolverUtilsTest {

    @Test
    public void solve_defaultSolver() {
        double error = 1e-10;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 3);
            assertEquals(2, result, error);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void solve_customSolver() {
        double error = 1e-10;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 5;
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 1, 4, error);
            assertEquals(1.709975946676698, result, error);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void bracket() {
        double error = 1e-10;
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 9;
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 0, -4, 5);
            assertEquals(-3.0, result[0], error);
            assertEquals(3.0, result[1], error);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    public void midpoint() {
        double error = 1e-10;
        double result = UnivariateRealSolverUtils.midpoint(0, 4);
        assertEquals(2.0, result, error);
    }
    
    // Add more test methods for other methods in the class

}