
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.random.Well19937c;

import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    @Test
    public void testConstructorWithValidInput() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        }catch (Exception e){
            Assert.fail("Constructor throws an exception for valid input");
        }
    }

    @Test
    public void testConstructorWithInvalidInput() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            Assert.fail("Constructor does not throw DimensionMismatchException for invalid input");
        }catch (DimensionMismatchException e){
            // pass
        }catch (Exception e){
            Assert.fail("Constructor throws an unexpected exception for invalid input");
        }
    }

    @Test
    public void testDensityWithValidInput() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {1.0, 2.0, 3.0};
            double density = mnd.density(vals);
            Assert.assertEquals("Density calculation is incorrect", 0.06349363593424097, density, 0.00000001);
        }catch (Exception e){
            Assert.fail("Density method throws an exception for valid input");
        }
    }

    @Test
    public void testDensityWithInvalidInput() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {1.0, 2.0};
            double density = mnd.density(vals);
            Assert.fail("Density method does not throw DimensionMismatchException for invalid input");
        }catch (DimensionMismatchException e){
            // pass
        }catch (Exception e){
            Assert.fail("Density method throws an unexpected exception for invalid input");
        }
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] actualMeans = mnd.getMeans();
            Assert.assertArrayEquals("getMeans method does not return the correct means", means, actualMeans, 0.0);
        }catch (Exception e){
            Assert.fail("getMeans method throws an exception");
        }
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[][] actualCovariances = mnd.getCovariances().getData();
            Assert.assertArrayEquals("getCovariances method does not return the correct covariances", covariances, actualCovariances);
        }catch (Exception e){
            Assert.fail("getCovariances method throws an exception");
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] std = {1.0, 1.0, 1.0};
            double[] actualStd = mnd.getStandardDeviations();
            Assert.assertArrayEquals("getStandardDeviations method does not return the correct standard deviations", std, actualStd, 0.0);
        }catch (Exception e){
            Assert.fail("getStandardDeviations method throws an exception");
        }
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
            double[] vals = mnd.sample();
            Assert.assertEquals("Sample method does not return the correct number of dimensions", 3, vals.length);
        }catch (Exception e){
            Assert.fail("Sample method throws an exception");
        }
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        try{
            MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
            double[] vals = {1.0, 2.0, 3.0};
            double exponentTerm = mnd.getExponentTerm(vals);
            Assert.assertEquals("getExponentTerm method does not return the correct exponent term", 0.367879441, exponentTerm, 0.00000001);
        }catch (Exception e){
            Assert.fail("getExponentTerm method throws an exception");
        }
    }
}
