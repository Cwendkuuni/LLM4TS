import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.direct.BaseAbstractMultivariateOptimizer;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test
    public void testConstructorWithCheckerArgument() {
        ConvergenceChecker<PointValuePair> checker = mock(ConvergenceChecker.class);
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);

        assertNotNull(optimizer);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConstructorWithRelAndAbsArguments() {
        double rel = 0.1;
        double abs = 0.01;
        double expectedRel = 0.1;
        double expectedAbs = 0.01;

        SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);

        assertNotNull(optimizer);
        SimpleValueChecker checker = (SimpleValueChecker) optimizer.getConvergenceChecker();
        assertEquals(expectedRel, checker.getRelativeThreshold(), 0);
        assertEquals(expectedAbs, checker.getAbsoluteThreshold(), 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimizeThrowsNullArgumentException() {
        optimizer.doOptimize();
    }

    @Test
    public void testDoOptimize() {
        AbstractSimplex simplex = mock(AbstractSimplex.class);
        BaseAbstractMultivariateOptimizer<MultivariateFunction> optimizer = new SimplexOptimizer();

        OptimizationData[] optData = new OptimizationData[]{simplex};
        optimizer.parseOptimizationData(optData);
        optimizer.init(optData);

        optimizer.optimize(0, null, GoalType.MINIMIZE);

        verify(simplex, times(1)).build(optimizer.getStartPoint());
        verify(simplex, times(1)).evaluate(any(MultivariateFunction.class), any(Comparator.class));
        verify(simplex, times(1)).iterate(any(MultivariateFunction.class), any(Comparator.class));
    }
}