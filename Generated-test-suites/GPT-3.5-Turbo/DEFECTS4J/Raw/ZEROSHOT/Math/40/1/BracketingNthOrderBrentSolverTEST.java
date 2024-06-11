import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    // Test computeObjectiveValue() method

    @Test
    public void testComputeObjectiveValue() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);

        double result = solver.computeObjectiveValue(1.0);
        assertEquals(0.0, result, absoluteAccuracy);

        result = solver.computeObjectiveValue(-1.0);
        assertEquals(2.0, result, absoluteAccuracy);
    }

    // Test doSolve() method

    @Test
    public void testDoSolve() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);

        double result = solver.doSolve();
        assertEquals(1.0, result, absoluteAccuracy);

        solver.setStartValue(-1.0);
        result = solver.doSolve();
        assertEquals(-1.0, result, absoluteAccuracy);
    }

    // Test getMaximalOrder() method

    @Test
    public void testGetMaximalOrder() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);

        int result = solver.getMaximalOrder();
        assertEquals(maximalOrder, result);
    }

    // Test solve() method

    @Test
    public void testSolve() {
        double absoluteAccuracy = 1e-6;
        int maximalOrder = 5;
        double min = -1.0;
        double max = 1.0;
        double startValue = -0.5;
        double expected = -1.0;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        int maxEval = 1000;

        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(absoluteAccuracy, maximalOrder);
        double result = solver.solve(maxEval, null, min, max, startValue, allowedSolution);
        assertEquals(expected, result, absoluteAccuracy);
    }

}