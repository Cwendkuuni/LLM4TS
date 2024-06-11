import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class BrentSolverTest {

    private BrentSolver brentSolver;
    private ComputableFunction function;
    private ConvergenceChecker checker;

    @Before
    public void setUp() {
        brentSolver = new BrentSolver();
        function = mock(ComputableFunction.class);
        checker = mock(ConvergenceChecker.class);
    }

    @Test
    public void testFindRoot() throws FunctionException {
        double x0 = 1.0;
        double f0 = 2.0;
        double x1 = 3.0;
        double f1 = 1.0;

        Mockito.when(function.valueAt(x0)).thenReturn(f0);
        Mockito.when(function.valueAt(x1)).thenReturn(f1);

        boolean result = brentSolver.findRoot(function, checker, 10, x0, f0, x1, f1);

        Mockito.verify(checker, Mockito.times(1)).converged(x0, f0, x1, f1);
        assertEquals(true, result);
    }
}