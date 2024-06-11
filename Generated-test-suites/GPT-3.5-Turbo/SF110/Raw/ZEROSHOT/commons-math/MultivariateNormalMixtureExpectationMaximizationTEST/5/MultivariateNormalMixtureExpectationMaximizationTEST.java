import org.junit.Test;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorException() {
        // Test NotStrictlyPositiveException
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("The parameter must be strictly positive: 0", e.getMessage());
        }

        // Test DimensionMismatchException
        double[][] data1 = {{1.0, 2.0, 3.0}, {4.0, 5.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization m1 = new MultivariateNormalMixtureExpectationMaximization(data1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e1) {
            assertEquals("Array lengths must match for dimension 2 (rows 2 and 3)", e1.getMessage());
        }

        // Test NumberIsTooSmallException
        double[][] data2 = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization m2 = new MultivariateNormalMixtureExpectationMaximization(data2);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e2) {
            assertEquals("the number of components should be larger or equal to 2 (2)", e2.getMessage());
        }
    }

    @Test
    public void testFit() {
        // Test NotStrictlyPositiveException
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            m.fit(initialMixture, -1, 1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("The parameter must be strictly positive: -1", e.getMessage());
        }

        // Test SingularMatrixException
        double[][] data1 = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture1 = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization m1 = new MultivariateNormalMixtureExpectationMaximization(data1);
            m1.fit(initialMixture1);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e1) {
            assertEquals("Unable to solve singular problem", e1.getMessage());
        }

        // Test DimensionMismatchException
        double[][] data2 = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture2 = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization m2 = new MultivariateNormalMixtureExpectationMaximization(data2);
            m2.fit(initialMixture2, 100, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e2) {
            assertEquals("The parameter must be strictly positive: -1.0", e2.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        // Test NotStrictlyPositiveException
        double[][] data = {};
        try {
            MixtureMultivariateNormalDistribution m = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("The parameter must be strictly positive: 0", e.getMessage());
        }

        // Test NumberIsTooSmallException
        double[][] data1 = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MixtureMultivariateNormalDistribution m1 = MultivariateNormalMixtureExpectationMaximization.estimate(data1, 1);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e1) {
            assertEquals("the number of components should be larger or equal to 2 (1)", e1.getMessage());
        }

        // Test NumberIsTooLargeException
        double[][] data2 = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        try {
            MixtureMultivariateNormalDistribution m2 = MultivariateNormalMixtureExpectationMaximization.estimate(data2, 5);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e2) {
            assertEquals("5 is larger than the size of 'data' (4)", e2.getMessage());
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(0.0, m.getLogLikelihood(), 0.0);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNull(m.getFittedModel().getComponents());
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDataRowCompareTo() {
        double[] data = {1.0, 2.0, 3.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(data);
        double[] data1 = {3.0, 2.0, 1.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row1 = new MultivariateNormalMixtureExpectationMaximization.DataRow(data1);
        assertEquals(-1, row.compareTo(row1));
    }

    @Test
    public void testDataRowEquals() {
        double[] data = {1.0, 2.0, 3.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(data);
        double[] data1 = {1.0, 2.0, 3.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row1 = new MultivariateNormalMixtureExpectationMaximization.DataRow(data1);
        assertEquals(row, row1);
    }

    @Test
    public void testDataRowHashCode() {
        double[] data = {1.0, 2.0, 3.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(data);
        double[] data1 = {1.0, 2.0, 3.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row1 = new MultivariateNormalMixtureExpectationMaximization.DataRow(data1);
        assertEquals(row.hashCode(), row1.hashCode());
    }

    @Test
    public void testDataRowGetRow() {
        double[] data = {1.0, 2.0, 3.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(data);
        assertArrayEquals(data, row.getRow(), 0.0);
    }
}