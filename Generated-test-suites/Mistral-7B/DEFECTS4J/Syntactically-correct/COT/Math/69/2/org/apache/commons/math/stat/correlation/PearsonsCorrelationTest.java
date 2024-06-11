import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructorNoData() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidData() {
        double[][] data = new double[][]{{1}, {1, 2, 3}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        assertNotNull(correlationMatrix);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1, 0.8, 0.7}, {0.8, 1, 0.8}, {0.7, 0.8, 1}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testComputeCorrelationMatrixWithMeanSubtraction() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1, 0.8, 0.7}, {0.8, 1, 0.8}, {0.7, 0.8, 1}});
        RealMatrix subtractedMatrix = new BlockRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        subtractMean(matrix, subtractedMatrix);
        RealMatrix correlationMatrixWithMeanSubtraction = computeCorrelationMatrix(subtractedMatrix);
        assertEquals(expectedCorrelationMatrix, correlationMatrixWithMeanSubtraction);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();

        RealMatrix expectedCorrelationStandardErrors = new BlockRealMatrix(new double[][]{{0.3, 0.25, 0.25}, {0.25, 0.3, 0.25}, {0.25, 0.25, 0.3}});
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();

        RealMatrix expectedCorrelationPValues = new BlockRealMatrix(new double[][]{{1.0D-3, 2.3294e-5, 2.3294e-5}, {2.3294e-5, 1.0D-3, 2.3294e-5}, {2.3294e-5, 2.3294e-5, 1.0D-3}});
        assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlation = 0.8;
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertEquals(correlation, pearsonCorrelation.correlation(xArray, yArray), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidData() {
        double[] xArray = {1};
        double[] yArray = {};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidDataLength() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 2}, {2, 3}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1, 0.8}, {0.8, 1}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testCheckSufficientData() {
        RealMatrix emptyMatrix = new BlockRealMatrix(new double[][]{{}, {}});
        try {
            new PearsonsCorrelation().checkSufficientData(emptyMatrix);
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 0 rows and 0 columns.", e.getMessage());
        }

        RealMatrix oneRowMatrix = new BlockRealMatrix(new double[][]{{1}});
        try {
            new PearsonsCorrelation().checkSufficientData(oneRowMatrix);
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 1 rows.", e.getMessage());
        }

        RealMatrix oneColumnMatrix = new BlockRealMatrix(new double[][]{{1, 2, 3}});
        try {
            new PearsonsCorrelation().checkSufficientData(oneColumnMatrix);
        } catch (MathRuntimeException e) {
            Assert.assertEquals("insufficient data: only 1 columns.", e.getMessage());
        }

        RealMatrix sufficientDataMatrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        new PearsonsCorrelation().checkSufficientData(sufficientDataMatrix);
    }

    private void subtractMean(RealMatrix matrix, RealMatrix subtractedMatrix) {
        int rows = matrix.getRowDimension();
        int cols = matrix.getColumnDimension();
        for (int i = 0; i < rows; i++) {
            double[] row = matrix.getColumnVector(i);
            double sum = 0;
            for (int j = 0; j < cols; j++) {
                sum += row[j];
            }
            double mean = sum / cols;
            for (int j = 0; j < cols; j++) {
                subtractedMatrix.setEntry(i, j, row[j] - mean);
            }
        }
    }

    private RealMatrix computeCorrelationMatrix(RealMatrix matrix) {
        RealMatrix correlationMatrix = new BlockRealMatrix(matrix.getColumnDimension(), matrix.getColumnDimension());
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            for (int j = 0; j < i; j++) {
                double corr = PearsonsCorrelation.correlation(matrix.getColumnVector(i), matrix.getColumnVector(j));
                correlationMatrix.setEntry(i, j, corr);
                correlationMatrix.setEntry(j, i, corr);
            }
            correlationMatrix.setEntry(i, i, 1d);
        }
        return correlationMatrix;
    }
}