import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Typical use case
        // Function: x^2 - 4, Interval: [1, 3]
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(x -> x*x - 4, (xLow, fLow, xHigh, fHigh) -> {
            if (fLow * fHigh < 0) {
                return RootsFinder.MIDDLE;
            } else if (fLow < 0) {
                return RootsFinder.LOW;
            } else {
                return RootsFinder.HIGH;
            }
        }, 100, 1, -3, 3, 5);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1.0e-10);
    }

    // Add more test cases for other methods and edge cases...

}