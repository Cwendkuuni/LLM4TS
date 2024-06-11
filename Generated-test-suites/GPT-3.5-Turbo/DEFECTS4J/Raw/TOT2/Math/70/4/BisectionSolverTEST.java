import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        BisectionSolver solver = new BisectionSolver(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        });
        
        assertNotNull(solver);
    }

    @Test
    public void testConstructorWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();

        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInitialValue() {
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 1, 3, 2);
        
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithoutInitialValue() {
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 1, 3);
        
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        
        solver.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2;
            }
        }, 1, 3);
    }

    @Test
    public void testSolveWithInvalidInterval() {
        BisectionSolver solver = new BisectionSolver();
        
        try {
            solver.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    return x - 2;
                }
            }, 3, 1);
        } catch (Exception e) {
            assertTrue(e instanceof FunctionEvaluationException);
        }
    }

}