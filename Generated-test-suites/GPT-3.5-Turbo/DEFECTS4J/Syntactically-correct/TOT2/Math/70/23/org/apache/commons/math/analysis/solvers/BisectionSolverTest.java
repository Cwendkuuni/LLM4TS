import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0, 1, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        });
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithInitialValue() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        }, 0, 1, 0.5);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    return x;
                }
            }, 0, 1);
            fail("MaxIterationsExceededException not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolveWithFunctionValueException() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    throw new RuntimeException("Function evaluation exception");
                }
            }, 0, 1);
            fail("FunctionEvaluationException not thrown");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolveWithInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    return x * x - 1;
                }
            }, 1, 0);
            fail("InvalidIntervalException not thrown");
        } catch (InvalidIntervalException e) {
            // Expected exception
        }
    }
}