import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.direct.MultiDirectional;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(2.0, multiDirectional.khi, 0.0);
        assertEquals(0.5, multiDirectional.gamma, 0.0);
    }

    @Test
    public void testParameterizedConstructor() {
        MultiDirectional md = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, md.khi, 0.0);
        assertEquals(0.7, md.gamma, 0.0);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        multiDirectional.simplex = new RealPointValuePair[2];
        multiDirectional.simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        multiDirectional.simplex[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 0.0);

        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        multiDirectional.iterateSimplex(comparator);
        assertNotNull(multiDirectional.simplex);
        assertEquals(2, multiDirectional.simplex.length);
        assertEquals(0.0, multiDirectional.simplex[0].getValue(), 0.0);
        assertEquals(0.0, multiDirectional.simplex[1].getValue(), 0.0);
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        multiDirectional.simplex = new RealPointValuePair[2];
        multiDirectional.simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        multiDirectional.simplex[1] = new RealPointValuePair(new double[]{3.0, 4.0}, 0.0);

        double coeff = 0.5;
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };

        RealPointValuePair result = multiDirectional.evaluateNewSimplex(multiDirectional.simplex, coeff, comparator);
        assertNotNull(result);
        assertEquals(2, result.getPoint().length);
        assertEquals(0.0, result.getValue(), 0.0);
    }
    
    @Test
    public void test() {
        // more test cases can be added here
        
    }
}