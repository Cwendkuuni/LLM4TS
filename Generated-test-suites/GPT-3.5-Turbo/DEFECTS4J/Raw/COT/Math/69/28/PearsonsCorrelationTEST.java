import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MathException;
import org.junit.BeforeClass;
import org.junit.Test;

public class PearsonsCorrelationTest {
    
    private static PearsonsCorrelation correlation;
    
    @BeforeClass
    public static void setup() {
        correlation = new PearsonsCorrelation();
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        RealMatrix matrix = correlation.computeCorrelationMatrix(data);
        double[][] result = matrix.getData();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expected[i][j], result[i][j], 0.000001);
            }
        }
    }
    
    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expected = 1.0;
        double result = correlation.correlation(xArray, yArray);
        assertEquals(expected, result, 0.000001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidDimensions() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        correlation.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{9, 3}, {3, 4}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        double[][] expected = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix matrix = correlation.covarianceToCorrelation(covarianceMatrix);
        double[][] result = matrix.getData();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expected[i][j], result[i][j], 0.000001);
            }
        }
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] correlationMatrixData = {{1.0, 0.5}, {0.5, 1.0}};
        correlation = new PearsonsCorrelation(new BlockRealMatrix(correlationMatrixData));
        double[][] expected = {{0.0, 0.288675}, {0.288675, 0.0}};
        RealMatrix matrix = correlation.getCorrelationStandardErrors();
        double[][] result = matrix.getData();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expected[i][j], result[i][j], 0.000001);
            }
        }
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] correlationMatrixData = {{1.0, 0.5}, {0.5, 1.0}};
        correlation = new PearsonsCorrelation(new BlockRealMatrix(correlationMatrixData));
        double[][] expected = {{0.0, 0.292893}, {0.292893, 0.0}};
        RealMatrix matrix = correlation.getCorrelationPValues();
        double[][] result = matrix.getData();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expected[i][j], result[i][j], 0.000001);
            }
        }
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        double[][] correlationMatrixData = {{1.0, 0.5}, {0.5, 1.0}};
        correlation = new PearsonsCorrelation(new BlockRealMatrix(correlationMatrixData));
        double[][] expected = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix matrix = correlation.getCorrelationMatrix();
        double[][] result = matrix.getData();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(expected[i][j], result[i][j], 0.000001);
            }
        }
    }
}