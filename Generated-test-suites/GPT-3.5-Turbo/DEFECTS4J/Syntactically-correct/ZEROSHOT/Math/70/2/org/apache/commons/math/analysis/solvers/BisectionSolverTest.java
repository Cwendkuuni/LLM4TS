import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {
    private BisectionSolver solver;
    
    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }
    
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, 3);
            assertEquals(2.0, result, 1E-6);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 3, 0);
            fail("Exception should be thrown");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }
    
    @Test
    public void testSolveWithMaxIterationsExceeded() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        try {
            double result = solver.solve(f, 0, 3, 100);
            fail("Exception should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Test passed
        } catch (FunctionEvaluationException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testSolveWithFunctionEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };

        try {
            double result = solver.solve(f, 0, 3);
            fail("Exception should be thrown");
        } catch (MaxIterationsExceededException e) {
            fail("Unexpected exception thrown");
        } catch (FunctionEvaluationException e) {
            // Test passed
        }
    }
}