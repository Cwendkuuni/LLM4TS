import static org.junit.Assert.assertEquals;

import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expected = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};

        assertEquals(expected, pc.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrix_EmptyData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] data = {};
        double[][] expected = {};

        assertEquals(expected, pc.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrix_SingleColumn() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] data = {{1}, {2}, {3}};
        double[][] expected = {{1}};

        assertEquals(expected, pc.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrix_SingleRow() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] data = {{1, 2, 3}};
        double[][] expected = {};

        assertEquals(expected, pc.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrix_NullMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] data = null;
        double[][] expected = {};

        assertEquals(expected, pc.computeCorrelationMatrix(data));
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] covarianceMatrix = {{1, 2, 3}, {2, 4, 6}, {3, 6, 9}};
        double[][] expected = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};

        assertEquals(expected, pc.covarianceToCorrelation(covarianceMatrix));
    }

    @Test
    public void testCovarianceToCorrelation_NullMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[][] covarianceMatrix = null;
        double[][] expected = {};

        assertEquals(expected, pc.covarianceToCorrelation(covarianceMatrix));
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expected = 1.0;

        assertEquals(expected, pc.correlation(xArray, yArray), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_ArraysDifferentLength() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        
        pc.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelation_InsufficientData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();

        double[] xArray = {1};
        double[] yArray = {2};
        
        pc.correlation(xArray, yArray);
    }
}