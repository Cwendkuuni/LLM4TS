package org.apache.commons.math3.distribution.fitting;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    
    @Test
    public void testConstructor_ValidData() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }
    
    @Test
    public void testConstructor_EmptyData_ExceptionThrown() {
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testFit_MaxIterationsLessThanOne_ExceptionThrown() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        try {
            em.fit(initialMixture, 0, 0.1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testFit_ThresholdLessThanDoubleMinValue_ExceptionThrown() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        try {
            em.fit(initialMixture, 100, Double.MIN_VALUE);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testEstimate_NumComponentsLessThanTwo_ExceptionThrown() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MixtureMultivariateNormalDistribution mmnd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testEstimate_NumComponentsGreaterThanDataLength_ExceptionThrown() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MixtureMultivariateNormalDistribution mmnd = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            assertTrue(true);
        }
    }
}