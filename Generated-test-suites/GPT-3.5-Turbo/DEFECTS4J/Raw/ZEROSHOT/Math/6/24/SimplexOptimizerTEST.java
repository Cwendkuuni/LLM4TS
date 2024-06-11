import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex simplex = mock(AbstractSimplex.class);
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.simplex);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullArgument() {
        optimizer.setSimplex(null);
    }

    @Test
    public void testOptimizeInternal() {
        int maxEval = 100;
        MultivariateFunction f = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;

        OptimizationData[] optData = new OptimizationData[1];
        optData[0] = mock(AbstractSimplex.class);

        optimizer.optimizeInternal(maxEval, f, goalType, optData);

        verify(f).value(any(double[].class));
        verify(optData[0]).parseOptimizationData(any(OptimizationData[].class));
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeNullArgument() {
        optimizer.doOptimize();
    }
}