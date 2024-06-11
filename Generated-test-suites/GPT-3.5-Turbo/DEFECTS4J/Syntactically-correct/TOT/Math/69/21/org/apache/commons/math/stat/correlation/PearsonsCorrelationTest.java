import org.apache.commons.math.MathException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {
    private PearsonsCorrelation pearsonsCorrelation;
    private double[][] data;
    
    @Before
    public void setUp() {
        data = new double[][] {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        double[][] expectedCorrelationMatrix = new double[][] {
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0}
        };
        double[][] actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix().getData();
        for (int i = 0; i < expectedCorrelationMatrix.length; i++) {
            for (int j = 0; j < expectedCorrelationMatrix[0].length; j++) {
                assertEquals(expectedCorrelationMatrix[i][j], actualCorrelationMatrix[i][j], 0.0001);
            }
        }
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] expectedCorrelationStandardErrors = new double[][] {
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        };
        double[][] actualCorrelationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors().getData();
        for (int i = 0; i < expectedCorrelationStandardErrors.length; i++) {
            for (int j = 0; j < expectedCorrelationStandardErrors[0].length; j++) {
                assertEquals(expectedCorrelationStandardErrors[i][j], actualCorrelationStandardErrors[i][j], 0.0001);
            }
        }
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] expectedCorrelationPValues = new double[][] {
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        };
        double[][] actualCorrelationPValues = pearsonsCorrelation.getCorrelationPValues().getData();
        for (int i = 0; i < expectedCorrelationPValues.length; i++) {
            for (int j = 0; j < expectedCorrelationPValues[0].length; j++) {
                assertEquals(expectedCorrelationPValues[i][j], actualCorrelationPValues[i][j], 0.0001);
            }
        }
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] expectedCorrelationMatrix = new double[][] {
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0}
        };
        double[][] actualCorrelationMatrix = pearsonsCorrelation.computeCorrelationMatrix(data).getData();
        for (int i = 0; i < expectedCorrelationMatrix.length; i++) {
            for (int j = 0; j < expectedCorrelationMatrix[0].length; j++) {
                assertEquals(expectedCorrelationMatrix[i][j], actualCorrelationMatrix[i][j], 0.0001);
            }
        }
    }
    
    @Test
    public void testCorrelation() throws IllegalArgumentException {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expectedCorrelation = 1.0;
        double actualCorrelation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, actualCorrelation, 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelation() throws IllegalArgumentException {
        double[] xArray = {1.0};
        double[] yArray = {4.0, 5.0};
        pearsonsCorrelation.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(data);
        double expectedVariance = MathUtils.variance(data[0]);
        for (int i = 0; i < expectedCorrelationMatrix.getRowDimension(); i++) {
            expectedCorrelationMatrix.setEntry(i, i, 1.0);
            for (int j = 0; j < i; j++) {
                double correlation = covarianceMatrix.getEntry(i, j) / Math.sqrt(expectedVariance * expectedVariance);
                expectedCorrelationMatrix.setEntry(i, j, correlation);
                expectedCorrelationMatrix.setEntry(j, i, correlation);
            }
        }
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        for (int i = 0; i < expectedCorrelationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < expectedCorrelationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedCorrelationMatrix.getEntry(i, j), actualCorrelationMatrix.getEntry(i, j), 0.0001);
            }
        }
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testInvalidCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = null;
        pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
    }
    
    @Test
    public void testCheckSufficientData() {
        RealMatrix matrix = new BlockRealMatrix(data);
        pearsonsCorrelation.checkSufficientData(matrix);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testInsufficientData() {
        RealMatrix matrix = new BlockRealMatrix(1, 1);
        pearsonsCorrelation.checkSufficientData(matrix);
    }
}