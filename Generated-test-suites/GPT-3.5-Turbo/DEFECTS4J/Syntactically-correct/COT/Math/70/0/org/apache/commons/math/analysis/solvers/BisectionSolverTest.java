import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {
    
    private BisectionSolver bisectionSolver;
    
    @Before
    public void setUp() {
        bisectionSolver = new BisectionSolver();
    }
    
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            double result = bisectionSolver.solve(f, -10, 10);
            Assert.assertEquals(2.0, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            Assert.fail("MaxIterationsExceededException should not be thrown.");
        } catch (FunctionEvaluationException e) {
            Assert.fail("FunctionEvaluationException should not be thrown.");
        }
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        bisectionSolver.solve(f, -10, 10);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        bisectionSolver.solve(f, -10, 10, 100);
    }
}