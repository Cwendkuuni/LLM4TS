import org.junit.Test;
import static org.junit.Assert.*;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDoSolve() {
        // Test case 1: Function with a single root
        UnivariateFunction function1 = x -> x - 5;
        BracketingNthOrderBrentSolver solver1 = new BracketingNthOrderBrentSolver();
        double solution1 = solver1.solve(100, function1, 0, 10);
        assertEquals(5.0, solution1, 0.0001);

        // Test case 2: Function with multiple roots
        UnivariateFunction function2 = x -> x * (x - 5) * (x + 5);
        BracketingNthOrderBrentSolver solver2 = new BracketingNthOrderBrentSolver();
        double solution2 = solver2.solve(100, function2, -10, 10);
        assertEquals(-5.0, solution2, 0.0001);

        // Test case 3: Function with no root
        UnivariateFunction function3 = x -> x * x + 1;
        BracketingNthOrderBrentSolver solver3 = new BracketingNthOrderBrentSolver();
        double solution3 = solver3.solve(100, function3, 0, 10);
        assertTrue(Double.isNaN(solution3));

        // Test case 4: Function with a single root exactly at the lower bound
        UnivariateFunction function4 = x -> x;
        BracketingNthOrderBrentSolver solver4 = new BracketingNthOrderBrentSolver();
        double solution4 = solver4.solve(100, function4, 0, 10);
        assertEquals(0.0, solution4, 0.0001);

        // Test case 5: Function with a single root exactly at the upper bound
        UnivariateFunction function5 = x -> x;
        BracketingNthOrderBrentSolver solver5 = new BracketingNthOrderBrentSolver();
        double solution5 = solver5.solve(100, function5, -10, 0);
        assertEquals(0.0, solution5, 0.0001);
    }

}