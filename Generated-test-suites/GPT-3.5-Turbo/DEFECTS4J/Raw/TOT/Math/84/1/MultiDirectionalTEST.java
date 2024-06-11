import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;

import static org.junit.Assert.*;

public class MultiDirectionalTest {
    
    private MultiDirectional multiDirectional;
    private RealConvergenceChecker convergenceChecker;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
        convergenceChecker = new RealConvergenceChecker();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertNotNull(multiDirectional);
    }

    @Test
    public void testConstructor() {
        MultiDirectional customMultiDirectional = new MultiDirectional(1.5, 0.75);
        assertNotNull(customMultiDirectional);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Test input values
        double[] point = {1.0, 2.0, 3.0};
        RealPointValuePair[] simplex = new RealPointValuePair[] {
            new RealPointValuePair(point, 1.0),
            new RealPointValuePair(point, 2.0),
            new RealPointValuePair(point, 3.0)
        };
        multiDirectional.setSimplex(simplex);
        assertEquals(0, multiDirectional.getIterations());

        // Test output values
        multiDirectional.iterateSimplex(convergenceChecker);
        assertEquals(1, multiDirectional.getIterations());
        assertNotNull(multiDirectional.getSimplex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithNullSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        multiDirectional.setSimplex(null);
        multiDirectional.iterateSimplex(convergenceChecker);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIterateSimplexWithInsufficientSimplexSize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        double[] point = {1.0, 2.0, 3.0};
        RealPointValuePair[] simplex = new RealPointValuePair[] {
            new RealPointValuePair(point, 1.0),
            new RealPointValuePair(point, 2.0)
        };
        multiDirectional.setSimplex(simplex);
        multiDirectional.iterateSimplex(convergenceChecker);
    }


}