package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    MultiDirectional multiDirectional;
    Comparator<RealPointValuePair> comparator;
    
    @Before
    public void setUp() {
    	multiDirectional = new MultiDirectional();
    	comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
    }
    
    @After
    public void tearDown() {
    	multiDirectional = null;
    	comparator = null;
    }
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
    	// Create sample simplex
    	double[] vertex = new double[] {1.0, 2.0, 3.0};
    	RealPointValuePair[] simplex = new RealPointValuePair[] {
            new RealPointValuePair(vertex, 0.0, false),
            new RealPointValuePair(vertex, 1.0, false),
            new RealPointValuePair(vertex, 2.0, false)
        };
    	multiDirectional.setSimplex(simplex);
    	
    	// Call iterateSimplex method
    	multiDirectional.iterateSimplex(comparator);
    	
    	// Check if simplex is updated
    	assertTrue(Arrays.equals(simplex, multiDirectional.getSimplex()));
    }
    
    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
    	// Create original simplex
    	double[] vertex = new double[] {1.0, 2.0, 3.0};
    	RealPointValuePair[] original = new RealPointValuePair[] {
            new RealPointValuePair(vertex, 0.0, false),
            new RealPointValuePair(vertex, 1.0, false),
            new RealPointValuePair(vertex, 2.0, false)
        };
    	
    	// Call evaluateNewSimplex method
    	RealPointValuePair result = multiDirectional.evaluateNewSimplex(original, 0.5, comparator);
    	
    	// Check if result is not null
    	assertTrue(result != null);
    }
    
    @Test
    public void testKhi() {
    	assertEquals(2.0, multiDirectional.khi, 0.0);
    }
    
    @Test
    public void testGamma() {
    	assertEquals(0.5, multiDirectional.gamma, 0.0);
    }
    
}