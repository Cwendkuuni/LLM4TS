import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testConstructorWithFunction() {
        // Test constructor with UnivariateRealFunction
        // Create a dummy function
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4; // x^2 - 4
            }
        };
        
        BisectionSolver solver = new BisectionSolver(dummyFunction);
        
        // Assert that the solver is not null
        assertNotNull(solver);
    }

    @Test
    public void testConstructor() {
        // Test the default constructor
        BisectionSolver solver = new BisectionSolver();
        
        // Assert that the solver is not null
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithInitial() {
        // Test solve method with initial value
        // Create a dummy function
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        try {
            double result = solver.solve(dummyFunction, 1.0, 3.0, 2.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown during solve method with initial value");
        }
    }

    @Test
    public void testSolveWithoutInitial() {
        // Test solve method without initial value
        // Create a dummy function
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        try {
            double result = solver.solve(dummyFunction, 1.0, 3.0);
            assertEquals(2.0, result, 1e-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown during solve method without initial value");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Test throwing MaxIterationsExceededException
        // Create a dummy function
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        
        // Try to solve with extremely small interval to exceed maximum iterations
        solver.solve(dummyFunction, 1.0, 1.00001);
    }
}