
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        // Test logic of method...
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        // Test logic of method...
    }

    @Test
    public void testEvaluateSimplex() throws FunctionEvaluationException, OptimizationException {
        // Test logic of method...
    }

}
