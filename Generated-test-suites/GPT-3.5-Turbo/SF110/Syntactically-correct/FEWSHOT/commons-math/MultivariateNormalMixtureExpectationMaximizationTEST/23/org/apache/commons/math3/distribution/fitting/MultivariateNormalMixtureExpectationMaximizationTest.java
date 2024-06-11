package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    private MultivariateNormalMixtureExpectationMaximization mvm;
    
    @Before
    public void setUp() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        try {
            mvm = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Exception thrown in setup: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor() {
        assertEquals(0.0, mvm.getLogLikelihood(), 0.00001);
        assertTrue(mvm.getFittedModel().getComponents().isEmpty());
    }
    
    @Test
    public void testFit() {
    }
    
    @Test
    public void testGetLogLikelihood() {
    }
    
    @Test
    public void testGetFittedModel() {
    }
}