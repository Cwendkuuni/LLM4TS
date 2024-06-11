import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.DirectSearchOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;
    
    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(2.0, multiDirectional.khi, 0.0);
        assertEquals(0.5, multiDirectional.gamma, 0.0);
    }
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        RealPointValuePair[] simplex = new RealPointValuePair[2];
        simplex[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        simplex[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 1.0);
        multiDirectional.simplex = simplex;

        // Test reflection step
        multiDirectional.iterateSimplex((a, b) -> Double.compare(a.getValue(), b.getValue()));
        assertEquals(1.0, multiDirectional.simplex[0].getValue(), 0.0);
        assertEquals(0.0, multiDirectional.simplex[0].getPoint()[0], 0.0);
        assertEquals(1.0, multiDirectional.simplex[0].getPoint()[1], 0.0);
        assertEquals(2.0, multiDirectional.simplex[1].getValue(), 0.0);
        assertEquals(3.0, multiDirectional.simplex[1].getPoint()[0], 0.0);
        
        // Test expanded simplex
        multiDirectional.iterateSimplex((a, b) -> Double.compare(a.getValue(), b.getValue()));
        assertEquals(1.0, multiDirectional.simplex[0].getValue(), 0.0);
        assertEquals(1.0, multiDirectional.simplex[0].getPoint()[0], 0.0);
        assertEquals(2.0, multiDirectional.simplex[0].getPoint()[1], 0.0);
        assertEquals(1.0, multiDirectional.simplex[1].getValue(), 0.0);
        assertEquals(0.0, multiDirectional.simplex[1].getPoint()[0], 0.0);
        
        // Test contracted simplex
        multiDirectional.iterateSimplex((a, b) -> Double.compare(a.getValue(), b.getValue()));
        assertEquals(1.0, multiDirectional.simplex[0].getValue(), 0.0);
        assertEquals(2.0, multiDirectional.simplex[0].getPoint()[0], 0.0);
        assertEquals(3.0, multiDirectional.simplex[0].getPoint()[1], 0.0);
        assertEquals(1.0, multiDirectional.simplex[1].getValue(), 0.0);
        assertEquals(2.0, multiDirectional.simplex[1].getPoint()[0], 0.0);
        
        // Test convergence
        try {
            multiDirectional.iterateSimplex((a, b) -> Double.compare(a.getValue(), b.getValue()));
        } catch (OptimizationException e) {
            assertEquals("Maximal number of evaluations exceeded", e.getMessage());
        }
    }

    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = new RealPointValuePair[2];
        original[0] = new RealPointValuePair(new double[]{1.0, 2.0}, 0.0);
        original[1] = new RealPointValuePair(new double[]{2.0, 3.0}, 1.0);
        
        RealPointValuePair best = multiDirectional.evaluateNewSimplex(original, 1.0, (a, b) -> Double.compare(a.getValue(), b.getValue()));
        assertNotNull(best);
        assertNotNull(best.getPoint());
        assertNotNull(best.getValue());
        
        RealPointValuePair[] simplex = multiDirectional.simplex;
        assertNotNull(simplex);
        assertEquals(original.length + 1, simplex.length);
        assertEquals(2.0, simplex[0].getPoint()[0], 0.0);
        assertEquals(3.0, simplex[0].getPoint()[1], 0.0);
    }
}