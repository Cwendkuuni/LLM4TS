package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2DRowAccessor;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testRectangularArrayConstructor() {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        int nRows = correlationMatrix.getRowDimension();
        int nCols = correlationMatrix.getColumnDimension();
        assertEquals(3, nRows);
        assertEquals(3, nCols);

        RealArray2DRowAccessor correlationMatrixAccessor = correlationMatrix.getDataFullAccess();
        double[][] correlationMatrixData = correlationMatrixAccessor.getData();

        // Checking diagonal values
        for (int i = 0; i < nRows; i++) {
            assertEquals(1.0, correlationMatrixData[i][i], 1e-14);
        }

        // Checking off diagonal values
        for (int i = 0; i < nRows - 1; i++) {
            for (int j = i + 1; j < nCols; j++) {
                double corr = correlation(data[i], data[j]);
                assertEquals(corr, correlationMatrixData[i][j], 1e-14);
                assertEquals(corr, correlationMatrixData[j][i], 1e-14);
            }
        }

        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        int nRows = correlationMatrix.getRowDimension();
        int nCols = correlationMatrix.getColumnDimension();
        assertEquals(2, nRows);
        assertEquals(2, nCols);

        RealArray2DRowAccessor correlationMatrixAccessor = correlationMatrix.getDataFullAccess();
        double[][] correlationMatrixData = correlationMatrixAccessor.getData();

        // Checking diagonal values
        for (int i = 0; i < nRows; i++) {
            assertEquals(1.0, correlationMatrixData[i][i], 1e-14);
        }

        // Checking off diagonal values
        for (int i = 0; i < nRows - 1; i++) {
            for (int j = i + 1; j < nCols; j++) {
                double corr = correlation(inputMatrix.getColumnVector(i), inputMatrix.getColumnVector(j));
                assertEquals(corr, correlationMatrixData[i][j], 1e-14);
                assertEquals(corr, correlationMatrixData[j][i], 1e-14);
            }
        }

        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRealMatrixInsufficientDataConstructor() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new PearsonsCorrelation(inputMatrix);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testRealMatrixNonSquareMatrixConstructor() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new PearsonsCorrelation(inputMatrix);
    }

    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        int nRows = correlationMatrix.getRowDimension();
        int nCols = correlationMatrix.getColumnDimension();
        assertEquals(2, nRows);
        assertEquals(2, nCols);

        RealArray2DRowAccessor correlationMatrixAccessor = correlationMatrix.getDataFullAccess();
        double[][] correlationMatrixData = correlationMatrixAccessor.getData();

        // Checking diagonal values
        for (int i = 0; i < nRows; i++) {
            assertEquals(1.0, correlationMatrixData[i][i], 1e-14);
        }

        // Checking off diagonal values
        for (int i = 0; i < nRows - 1; i++) {
            for (int j = i + 1; j < nCols; j++) {
                double corr = correlation(covariance.getCovarianceMatrix().getColumnVector(i), covariance.getCovarianceMatrix().getColumnVector(j));
                assertEquals(corr, correlationMatrixData[i][j], 1e-14);
                assertEquals(corr, correlationMatrixData[j][i], 1e-14);
            }
        }

        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testCovarianceNullCovarianceMatrixConstructor() {
        Covariance covariance = new Covariance(null);
        new PearsonsCorrelation(covariance);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        RealArray2DRowAccessor correlationMatrixAccessor = correlationMatrix.getDataFullAccess();
        double[][] correlationMatrixData = correlationMatrixAccessor.getData();

        // Checking diagonal values
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            assertEquals(1.0, correlationMatrixData[i][i], 1e-14);
        }

        // Checking off diagonal values
        for (int i = 0; i < correlationMatrix.getRowDimension() - 1; i++) {
            for (int j = i + 1; j < correlationMatrix.getColumnDimension(); j++) {
                double corr = correlation(data[i], data[j]);
                assertEquals(corr, correlationMatrixData[i][j], 1e-14);
                assertEquals(corr, correlationMatrixData[j][i], 1e-14);
            }
        }
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        int nRows = correlationStandardErrors.getRowDimension();
        int nCols = correlationStandardErrors.getColumnDimension();
        assertEquals(nRows, nCols);

        RealArray2DRowAccessor correlationStandardErrorsAccessor = correlationStandardErrors.getDataFullAccess();
        double[][] correlationStandardErrorsData = correlationStandardErrorsAccessor.getData();

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (i == j) {
                    assertEquals(1.0, correlationStandardErrorsData[i][j], 1e-14);
                } else {
                    double correlation = correlationMatrix.getEntry(i, j);
                    assertEquals(-correlation * Math.sqrt((1 - correlation * correlation) / (pearsonCorrelation.nObs - 2)), correlationStandardErrorsData[i][j], 1e-14);
                    assertEquals(-correlation * Math.sqrt((1 - correlation * correlation) / (pearsonCorrelation.nObs - 2)), correlationStandardErrorsData[j][i], 1e-14);
                }
            }
        }
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 3.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();

        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        int nRows = correlationPValues.getRowDimension();
        int nCols = correlationPValues.getColumnDimension();
        assertEquals(nRows, nCols);

        RealArray2DRowAccessor correlationPValuesAccessor = correlationPValues.getDataFullAccess();
        double[][] correlationPValuesData = correlationPValuesAccessor.getData();

        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                if (i == j) {
                    assertEquals(1.0, correlationPValuesData[i][j], 1e-14);
                } else {
                    double correlation = correlationMatrix.getEntry(i, j);
                    double df = pearsonCorrelation.nObs - 2;
                    double tValue = Math.abs(correlation * Math.sqrt((df / (1 - correlation * correlation))));
                    double pValue = 2 * (1 - new TDistribution(df).cumulativeProbability(tValue));
                    assertEquals(pValue, correlationPValuesData[i][j], 1e-14);
                    assertEquals(pValue, correlationPValuesData[j][i], 1e-14);
                }
            }
        }
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValuesWithInvalidData() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0, 4.0}, {3.0, 2.0, 1.0, 1.0}, {2.0, 1.0, 3.0, 3.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        pearsonCorrelation.getCorrelationPValues();
    }

    private double correlation(double[] xArray, double[] yArray) {
        SimpleRegression regression = new SimpleRegression();
        if (xArray.length != yArray.length || xArray.length < 2) {
            throw new IllegalArgumentException("Invalid array dimensions.");
        }

        for (int i = 0; i < xArray.length; i++) {
            regression.addData(xArray[i], yArray[i]);
        }
        return regression.getR();
    }

    private double correlation(RealMatrix xMatrix, RealMatrix yMatrix) {
        int nRows = xMatrix.getRowDimension();
        int nCols = yMatrix.getColumnDimension();

        if (nRows != nCols || nRows < 2) {
            throw new IllegalArgumentException("Invalid matrix dimensions.");
        }

        double[][] xData = xMatrix.getDataFullAccess().getData();
        double[][] yData = yMatrix.getDataFullAccess().getData();

        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < nRows; i++) {
            regression.addData(xData[i][0], yData[i][0]);
        }
        return regression.getR();
    }
}