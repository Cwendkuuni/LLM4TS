import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PearsonsCorrelationTest {
    
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        double[][] expectedMatrix = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        for(int i=0; i<correlationMatrix.getRowDimension(); i++) {
            for(int j=0; j<correlationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], correlationMatrix.getEntry(i, j), 0.0001);
            }
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double expectedCorrelation = 1.0;
        double computedCorrelation = correlation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, computedCorrelation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrayLength() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {4};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{2.0, 1.0}, {1.0, 2.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        double[][] expectedMatrix = {{1.0, 0.5}, {0.5, 1.0}};
        for(int i=0; i<correlationMatrix.getRowDimension(); i++) {
            for(int j=0; j<correlationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], correlationMatrix.getEntry(i, j), 0.0001);
            }
        }
    }

    @Test
    public void testComputeCorrelationMatrixWithDataMatrix() {
        double[][] data = {{2, 4, 6}, {8, 10, 12}, {14, 16, 18}};
        RealMatrix dataMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(dataMatrix);
        double[][] expectedMatrix = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        for(int i=0; i<correlationMatrix.getRowDimension(); i++) {
            for(int j=0; j<correlationMatrix.getColumnDimension(); j++) {
                assertEquals(expectedMatrix[i][j], correlationMatrix.getEntry(i, j), 0.0001);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientRows() {
        double[][] data = {{1, 2, 3}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(new BlockRealMatrix(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataWithInsufficientColumns() {
        double[][] data = {{1}, {2}, {3}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(new BlockRealMatrix(data));
    }
}