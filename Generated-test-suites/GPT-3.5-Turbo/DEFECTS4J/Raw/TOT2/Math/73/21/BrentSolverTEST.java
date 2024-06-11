import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_ValidCase() throws FunctionException {
        // Prepare
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(anyDouble())).thenReturn(0.0);

        // Test
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, -1.0, -1.0, 1.0, 1.0);

        // Verify
        assertTrue(rootFound);
        assertEquals(0.0, solver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRoot_ExceptionThrown() throws FunctionException {
        // Prepare
        ComputableFunction function = mock(ComputableFunction.class);
        ConvergenceChecker checker = mock(ConvergenceChecker.class);
        when(function.valueAt(anyDouble())).thenThrow(new FunctionException("Function evaluation error"));

        // Test
        BrentSolver solver = new BrentSolver();

        // Verify
        assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, -1.0, -1.0, 1.0, 1.0));
    }

    @Test
    public void testGetRoot() {
        // Prepare & Test
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 1e-10);
    }

    // Add more test cases for other methods if needed

}