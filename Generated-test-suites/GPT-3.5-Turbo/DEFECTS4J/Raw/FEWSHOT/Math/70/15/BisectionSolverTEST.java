import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.BeforeClass;
import org.junit.Test;

public class BisectionSolverTest {
    private static BisectionSolver solver;
    
    @BeforeClass
    public static void setUp() {
        solver = new BisectionSolver();
    }
    
    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double root = solver.solve(f, -10, 10);
        assertEquals(2, root, 0.0001);
    }
    
    @Test
    public void testSolve_MinEqualsMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        double root = solver.solve(f, 0, 0);
        assertEquals(0, root, 0.0001);
    }
    
    @Test
    public void testSolve_FuncThrowsException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        
        assertThrows(FunctionEvaluationException.class, () -> solver.solve(f, -10, 10));
    }
    
    @Test
    public void testSolve_MaxIterationsExceededException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -10, 10, 0));
    }
}