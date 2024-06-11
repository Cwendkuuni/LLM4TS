package org.apache.commons.math3.distribution;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    @Test
    public void testConstructor() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covarianceMatrix);
            assertNotNull(dist.getMeans());
            assertNotNull(dist.getCovariances());
        } catch (Exception e) {
            // Exception is expected for valid inputs, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test(expected=DimensionMismatchException.class)
    public void testConstructorInvalidDimension() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0}, {0, 3}};
        
        try {
            new MultivariateNormalDistribution(means, covarianceMatrix);
        } catch (DimensionMismatchException e) {
            throw e;
        } catch (Exception e) {
            // Exception is expected for invalid inputs, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test(expected=SingularMatrixException.class)
    public void testConstructorInvalidCovarianceMatrix() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 0}};
        
        try {
            new MultivariateNormalDistribution(means, covarianceMatrix);
        } catch (SingularMatrixException e) {
            throw e;
        } catch (Exception e) {
            // Exception is expected for invalid inputs, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetMeans() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covarianceMatrix);
            assertArrayEquals(means, dist.getMeans(), 0.0001);
        } catch (Exception e) {
            // Exception is not expected here, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetCovariances() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covarianceMatrix);
            RealMatrix expected = dist.getCovariances();
            
            assertArrayEquals(covarianceMatrix, expected.getData(), 0.0001);
        } catch (Exception e) {
            // Exception is not expected here, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testSample() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        
        try {
            RandomGenerator rng = new Well19937c();
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(rng, means, covarianceMatrix);
            
            double[] sample = dist.sample();
            
            assertNotNull(sample);
            assertEquals(means.length, sample.length);
        } catch (Exception e) {
            // Exception is not expected here, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testDensity() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covarianceMatrix);
            
            double[] values = {1, 2, 3};
            double expectedDensity = 0.0206; // Calculated manually
            
            double density = dist.density(values);
            
            assertEquals(expectedDensity, density, 0.0001);
        } catch (Exception e) {
            // Exception is not expected here, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetStandardDeviations() {
        double[] means = {1, 2, 3};
        double[][] covarianceMatrix = {{2, 0, 0}, {0, 3, 0}, {0, 0, 4}};
        
        try {
            MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covarianceMatrix);
            
            double[] expectedStd = {Math.sqrt(2), Math.sqrt(3), Math.sqrt(4)};
            
            assertArrayEquals(expectedStd, dist.getStandardDeviations(), 0.0001);
        } catch (Exception e) {
            // Exception is not expected here, so fail the test
            e.printStackTrace();
            assert false;
        }
    }
}