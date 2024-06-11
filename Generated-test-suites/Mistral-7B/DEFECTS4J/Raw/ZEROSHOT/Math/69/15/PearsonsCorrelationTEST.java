package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.nObs);
    }

    @Test
    public void testRectangularDoubleArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());

        // Check diagonal values
        for (int i = 0; i < 3; i++) {
            assertEquals(1.0, correlationMatrix.getEntry(i, i), 0.0);
        }

        // Check off-diagonal values
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < i; j++) {
                double correlation = 0.0;
                for (int k = 0; k < 3; k++) {
                    correlation += (data[i][k] - mean(data[i])) * (data[j][k] - mean(data[j]));
                }
                double standardDeviationX = stdDev(data[i]);
                double standardDeviationY = stdDev(data[j]);
                double expectedCorrelation = correlation / ((3.0 - 1.0) * standardDeviationX * standardDeviationY);
                assertEquals(expectedCorrelation, correlationMatrix.getEntry(i, j), 0.001);
                assertEquals(expectedCorrelation, correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.1, 3.1}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());

        // Check diagonal values
        for (int i = 0; i < 2; i++) {
            assertEquals(1.0, correlationMatrix.getEntry(i, i), 0.0);
        }

        // Check off-diagonal values
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < i; j++) {
                double correlation = 0.0;
                for (int k = 0; k < 2; k++) {
                    correlation += (matrix.getEntry(i, k) - mean(matrix.getColumnVector(i))) * (matrix.getEntry(j, k) - mean(matrix.getColumnVector(j)));
                }
                double standardDeviationX = stdDev(matrix.getColumnVector(i));
                double standardDeviationY = stdDev(matrix.getColumnVector(j));
                double expectedCorrelation = correlation / ((2.0 - 1.0) * standardDeviationX * standardDeviationY);
                assertEquals(expectedCorrelation, correlationMatrix.getEntry(i, j), 0.001);
                assertEquals(expectedCorrelation, correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    @Test
    public void testCovarianceConstructor() throws MathException {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());

        // Check diagonal values
        for (int i = 0; i < 2; i++) {
            assertEquals(1.0, correlationMatrix.getEntry(i, i), 0.0);
        }

        // Check off-diagonal values
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < i; j++) {
                double correlation = covarianceMatrix.getEntry(i, j) / (Math.sqrt(covarianceMatrix.getEntry(i, i)) * Math.sqrt(covarianceMatrix.getEntry(j, j)));
                assertEquals(correlation, correlationMatrix.getEntry(i, j), 0.001);
                assertEquals(correlation, correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    @Test
    public void testCorrelationMatrixGetters() {
        double[][] data = {{1.0, 2.0, 3.0}, {1.1, 2.1, 3.1}, {1.2, 2.2, 3.2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        RealMatrix correlationPValues = null;
        try {
            correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        } catch (MathException e) {
            // Expected
        }

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(correlationMatrix.getEntry(i, j), correlationMatrix.getEntry(j, i), 0.001);
            }
        }
    }

    private double[] mean(double[] array) {
        double sum = 0.0;
        for (double value : array) {
            sum += value;
        }
        return new double[]{sum / array.length};
    }

    private double stdDev(double[] array) {
        double meanValue = mean(array)[0];
        double sum = 0.0;
        for (double value : array) {
            sum += Math.pow(value - meanValue, 2);
        }
        return Math.sqrt(sum / (array.length - 1));
    }

    private double stdDev(RealMatrix matrix) {
        double sum = 0.0;
        double mean = 0.0;
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            double[] column = matrix.getColumnVector(i);
            mean += mean(column)[0];
            for (double value : column) {
                sum += Math.pow(value - mean(column)[0], 2);
            }
        }
        return Math.sqrt(sum / (matrix.getColumnDimension() - 1));
    }
}