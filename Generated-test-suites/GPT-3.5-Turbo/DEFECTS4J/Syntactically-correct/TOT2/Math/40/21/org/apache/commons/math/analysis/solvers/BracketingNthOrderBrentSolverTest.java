import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testDefaultConstructor() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(5, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 7);
        assertEquals(1e-8, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(7, solver.getMaximalOrder());
    }

    @Test
    public void testConstructorWithRelativeAccuracy() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 1e-10, 1e-12, 3);
        assertEquals(1e-6, solver.getRelativeAccuracy(), 0.0);
        assertEquals(1e-10, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(1e-12, solver.getFunctionValueAccuracy(), 0.0);
        assertEquals(3, solver.getMaximalOrder());
    }

    // Add more test cases for each public method...

}