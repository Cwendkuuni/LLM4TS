import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolveMinMidMaxSameSign() {
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(x -> x - 1, 0, 2);
        
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test
    public void testSolveMinMidMaxOppositeSign() {
        BisectionSolver solver = new BisectionSolver();
        
        double result = solver.solve(x -> x - 1, 0, 0.5);
        
        assertEquals(1.0, result, 1e-6);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        
        solver.solve(x -> x - 1, 0, 2, 1.5);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        BisectionSolver solver = new BisectionSolver();
        
        solver.solve(x -> { throw new FunctionEvaluationException(); }, 0, 2);
    }
}