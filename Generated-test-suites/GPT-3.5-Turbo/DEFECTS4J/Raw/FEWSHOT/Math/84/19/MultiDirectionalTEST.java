package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {
    
    private MultiDirectional optimizer;
    
    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(2.0, optimizer.getKhi(), 0.0);
        assertEquals(0.5, optimizer.getGamma(), 0.0);
    }
    
    @Test
    public void testCustomConstructor() {
        optimizer = new MultiDirectional(3.0, 0.6);
        assertEquals(3.0, optimizer.getKhi(), 0.0);
        assertEquals(0.6, optimizer.getGamma(), 0.0);
    }
    
    @Test
    public void testIterateSimplex() {
        // Create a comparator for testing
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                return Double.compare(o1.getValue(), o2.getValue());
            }
        };
        
        try {
            optimizer.iterateSimplex(comparator);
        } catch (Exception e) {
            assertFalse(e instanceof FunctionEvaluationException);
            assertFalse(e instanceof OptimizationException);
            assertFalse(e instanceof IllegalArgumentException);
        }
    }
    
    @Test
    public void testEvaluateNewSimplex() {
        // Create original simplex
        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[] {1.0, 2.0, 3.0}, 0.0);
        original[1] = new RealPointValuePair(new double[] {2.0, 3.0, 4.0}, 1.0);
        original[2] = new RealPointValuePair(new double[] {3.0, 4.0, 5.0}, 2.0);
        
        // Create expected transformed simplex
        RealPointValuePair[] expected = new RealPointValuePair[3];
        expected[0] = original[0];
        expected[1] = new RealPointValuePair(new double[] {0.0, 1.0, 2.0}, Double.NaN, false);
        expected[2] = new RealPointValuePair(new double[] {-1.0, 0.0, 1.0}, Double.NaN, false);
        
        try {
            optimizer.evaluateNewSimplex(original, 0.5, new Comparator<RealPointValuePair>() {
                public int compare(RealPointValuePair o1, RealPointValuePair o2) {
                    return Double.compare(o1.getValue(), o2.getValue());
                }
            });
            
            assertArrayEquals(expected, optimizer.getSimplex());
        } catch (Exception e) {
            assertFalse(e instanceof FunctionEvaluationException);
            assertFalse(e instanceof OptimizationException);
        }
    }
    
}