import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationMatrixFromTwoDArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 0.819152, 0.832496}, {0.819152, 1.0, 0.842973}, {0.832496, 0.842973, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertTrue(MatrixEquals.isEqual(expectedCorrelationMatrix, actualCorrelationMatrix));
    }

    @Test
    public void testCorrelationMatrixFromRealMatrix() {
        RealMatrix inputMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 0.819152, 0.832496}, {0.819152, 1.0, 0.842973}, {0.832496, 0.842973, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertTrue(MatrixEquals.isEqual(expectedCorrelationMatrix, actualCorrelationMatrix));
    }

    @Test
    public void testCorrelationFromCovariance() {
        RealMatrix covarianceMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{0.0, 4.0, 4.0}, {4.0, 9.0, 12.0}, {4.0, 12.0, 16.0}});
        Covariance covariance = new Covariance(covarianceMatrix, 3);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix expectedCorrelationMatrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0, 0.8, 0.8}, {0.8, 1.0, 0.8}, {0.8, 0.8, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertTrue(MatrixEquals.isEqual(expectedCorrelationMatrix, actualCorrelationMatrix));
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationStandardErrors = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{0.468957, 0.468957, 0.468957}, {0.468957, 0.468957, 0.468957}, {0.468957, 0.468957, 0.468957}});
        RealMatrix actualCorrelationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        assertTrue(MatrixEquals.isEqual(expectedCorrelationStandardErrors, actualCorrelationStandardErrors));
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationPValues = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{0.958332, 0.958332, 0.958332}, {0.958332, 0.958332, 0.958332}, {0.958332, 0.958332, 0.958332}});
        RealMatrix actualCorrelationPValues = pearsonCorrelation.getCorrelationPValues();
        assertTrue(MatrixEquals.isEqual(expectedCorrelationPValues, actualCorrelationPValues));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArrayForCorrelation() {
        double[][] data = {{1.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRealMatrixForCorrelation() {
        RealMatrix matrix = new org.apache.commons.math3.linear.Array2DRowRealMatrix(new double[][]{{1.0}});
        new PearsonsCorrelation(matrix);
    }
}