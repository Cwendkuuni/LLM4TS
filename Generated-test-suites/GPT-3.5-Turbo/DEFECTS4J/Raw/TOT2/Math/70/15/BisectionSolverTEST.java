import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        // Typical use case
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x*x - 4;
            }
        };
        try {
            double result = solver.solve(f, 0, 3);
            assertEquals(result, 2.0, 1e-6);
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testSolveWithMinMax() {
        // Edge case where initial value outside the function range
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(0, 2);
            assertEquals(result, 1.0, 1e-6);
        } catch (Exception e) {
            fail("Exception should not be thrown for valid input");
        }
    }

    @Test
    public void testSolveFunction() {
        // Error scenario where function evaluation throws exception
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation error");
            }
        };
        try {
            double result = solver.solve(f, 0, 2);
            fail("RuntimeException should be thrown for function evaluation error");
        } catch (RuntimeException e) {
            // Expected
        } catch (Exception e) {
            fail("RuntimeException should be thrown for function evaluation error");
        }
    }

    @Test
    public void testSolveWithMaxIterations() {
        // Edge case where maximum iterations exceeded
        BisectionSolver solver = new BisectionSolver();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x+1;
            }
        };
        try {
            double result = solver.solve(f, 0, 2);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected
        } catch (Exception e) {
            fail("MaxIterationsExceededException should be thrown");
        }
    }
}