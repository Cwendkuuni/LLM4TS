
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolveMinAndMax() {
        double min = 0.0;
        double max = 1.0;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(min, max);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test
    public void testSolveWithInitial() {
        double min = 0.0;
        double max = 1.0;
        double initial = 0.5;
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(min, max, initial);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        double min = 0.0;
        double max = 1.0;
        BisectionSolver solver = new BisectionSolver();
        solver.setMaximalIterationCount(1);
        solver.solve(min, max);
    }

    @Test
    public void testSolveFunctionEvaluationException() {
        double min = 0.0;
        double max = 1.0;
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        
        BisectionSolver solver = new BisectionSolver(dummyFunction);
        try {
            solver.solve(min, max);
            fail("FunctionEvaluationException should be thrown");
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            // FunctionEvaluationException should be thrown
        }
    }
}

