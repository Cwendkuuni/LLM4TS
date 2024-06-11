import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {
    
    private BaseSecantSolver solver;

    @Test
    public void testSolve() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2, result, 0.0001);
    }

    @Test
    public void testSolveWithMaxEval() {
        double result = solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3);
        assertEquals(2, result, 0.0001);
    }

    @Test
    public void testSolveWithStartValue() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3, 2.5);
        assertEquals(2, result, 0.0001);
    }

    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 3, 2.5, AllowedSolution.LEFT_SIDE);
        assertEquals(2, result, 0.0001);
    }
}