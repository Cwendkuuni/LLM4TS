import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {

    private SimplexOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new SimplexOptimizer();
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplex_nullArgument() {
        optimizer.setSimplex(null);
    }

    @Test
    public void testSetSimplex_validArgument() {
        AbstractSimplex simplex = new AbstractSimplex();
        optimizer.setSimplex(simplex);
        assertEquals(simplex, optimizer.getSimplex());
    }

    @Test
    public void testOptimizeInternal_noOptimizationData() {
        int maxEval = 100;
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] point) {
                return 0.0;
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType);
        assertNotNull(result);
        assertEquals(0.0, result.getValue(), 0.0);
        assertArrayEquals(new double[0], result.getPoint(), 0.0);
    }

    @Test
    public void testDoOptimize_noSimplex() {
        try {
            optimizer.doOptimize();
            fail("Exception not thrown");
        } catch (NullArgumentException e) {
            // Expected exception
        }
    }
}