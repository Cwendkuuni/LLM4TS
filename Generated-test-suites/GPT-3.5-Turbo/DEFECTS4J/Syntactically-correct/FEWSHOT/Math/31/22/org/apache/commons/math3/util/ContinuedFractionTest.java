
package org.apache.commons.math3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.ZeroException;
import org.junit.Test;

public class ContinuedFractionTest {
    
    private static final double EPSILON = 1e-9;
    
    class TestContinuedFraction extends ContinuedFraction {

        @Override
        protected double getA(int n, double x) {
            // TODO: implement test cases specific a coefficients
            return 0;
        }

        @Override
        protected double getB(int n, double x) {
            // TODO: implement test cases specific b coefficients
            return 0;
        }
        
    }
    
    @Test
    public void testEvaluate() throws Exception {
        TestContinuedFraction fraction = new TestContinuedFraction();
        
        // TODO: implement test cases for evaluate method
        
    }
    
}
