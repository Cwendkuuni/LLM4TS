import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BisectionSolverTest {

    @Test
    public void testConstructor_withFunctionParam() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        BisectionSolver solver = new BisectionSolver(f);
        
        assertNotNull(solver);
        assertEquals(f, solver.getFunction());
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }
    
    @Test
    public void testConstructor_withoutFunctionParam() {
        BisectionSolver solver = new BisectionSolver();
        
        assertNotNull(solver);
        assertEquals(100, solver.getMaximalIterationCount());
        assertEquals(1E-6, solver.getAbsoluteAccuracy(), 0.0);
    }
    
    @Test
    public void testSolve_withMinMaxInitial() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        BisectionSolver solver = new BisectionSolver(f);
        double result = solver.solve(0, 10, 5);
        
        assertEquals(0.0, result, 1E-6);
    }
    
    @Test
    public void testSolve_withMinMax() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        BisectionSolver solver = new BisectionSolver(f);
        double result = solver.solve(0, 10);
        
        assertEquals(0.0, result, 1E-6);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_withMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        BisectionSolver solver = new BisectionSolver(f);
        double result = solver.solve(0, 1);
    }
}