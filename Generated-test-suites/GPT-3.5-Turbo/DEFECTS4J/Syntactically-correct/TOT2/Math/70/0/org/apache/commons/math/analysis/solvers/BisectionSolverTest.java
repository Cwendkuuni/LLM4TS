import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BisectionSolverTest {

    // Test case for constructor with function
    @Test
    public void testConstructorWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        assertNotNull(solver);
    }

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    // Test case for solve method with initial value
    @Test
    public void testSolveWithInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 1, 2, 1.5);
            assertEquals(Math.sqrt(2), result, 1e-6);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }
    }

    // Test case for solve method without initial value
    @Test
    public void testSolveWithoutInitial() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        try {
            double result = solver.solve(function, 1, 2);
            assertEquals(Math.sqrt(2), result, 1e-6);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }
    }

    // Test case for solve method with invalid min and max
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithInvalidRange() throws MaxIterationsExceededException {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BisectionSolver solver = new BisectionSolver();
        solver.solve(function, 2, 1);
    }

}