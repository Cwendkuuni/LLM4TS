
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        double[][] expectedMatrix = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], correlationMatrix.getEntry(i, j), 0.001);
            }
        }
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(data);
        double[][] expectedMatrix = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], correlationMatrix.getEntry(i, j), 0.001);
            }
        }
    }
    
    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expectedCorrelation = 1.0;
        double correlationValue = correlation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, correlationValue, 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrayLengths() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1, 2};
        double[] yArray = {4, 5, 6};
        correlation.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{2.0, 1.0}, {1.0, 2.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        double[][] expectedMatrix = {{1.0, 0.5}, {0.5, 1.0}};
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], correlationMatrix.getEntry(i, j), 0.001);
            }
        }
    }
}
