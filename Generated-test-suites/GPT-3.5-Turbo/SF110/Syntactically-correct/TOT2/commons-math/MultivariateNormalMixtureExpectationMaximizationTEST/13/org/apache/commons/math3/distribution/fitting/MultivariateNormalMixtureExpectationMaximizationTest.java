package org.apache.commons.math3.distribution.fitting;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
    private MultivariateNormalMixtureExpectationMaximization em;

    @Test
    public void testConstructor_validData() {
        em = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(em);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidDataSize() {
        double[][] invalidData = {};
        em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_invalidDataDimensions() {
        double[][] invalidData = {{1.0}, {2.0, 3.0}};
        em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidDataLength() {
        double[][] invalidData = {{1.0, 2.0}};
        em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    // Add more test cases for other methods here

}