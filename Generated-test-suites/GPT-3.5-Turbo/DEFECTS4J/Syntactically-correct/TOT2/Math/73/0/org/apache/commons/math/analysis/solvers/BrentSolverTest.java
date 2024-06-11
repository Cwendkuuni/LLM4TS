import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4; // function x^2 - 4
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        boolean rootFound = solver.findRoot(function, checker, 100, 1.0, -3.0, 2.0, 0.0);
        
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 0.0001);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solv.eeroot = 5.0;
        
        assertEquals(5.0, solver.getRoot(), 0.0001);
    }

}