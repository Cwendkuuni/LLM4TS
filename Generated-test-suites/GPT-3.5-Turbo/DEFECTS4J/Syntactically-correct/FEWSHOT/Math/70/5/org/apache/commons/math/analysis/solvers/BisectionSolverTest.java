import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-9;
    
    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(f, -10, 10);
        
        Assert.assertEquals(2.0, solution, EPSILON);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -10, 10);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException();
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, -10, 10);
    }
    
    @Test
    public void testSolveIntervalSizeSmallerThanAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        double solution = solver.solve(f, 1, 3);
        
        Assert.assertEquals(2.0, solution, EPSILON);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        
        BisectionSolver solver = new BisectionSolver();
        solver.solve(f, 3, 1);
    }
}