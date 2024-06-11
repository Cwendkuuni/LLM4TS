import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
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
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8, 0.7}, {0.8, 1.0, 0.8}, {0.7, 0.8, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(2, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRealMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0}});
        new PearsonsCorrelation(matrix);
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8, 0.7}, {0.8, 1.0, 0.8}, {0.7, 0.8, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix standardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        RealMatrix expectedStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.670820325, 0.672499835, 0.672499835},
                {0.672499835, 0.670820325, 0.672499835}, {0.672499835, 0.672499835, 0.670820325}});
        assertEquals(expectedStandardErrors, standardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix pValues = pearsonCorrelation.getCorrelationPValues();
        RealMatrix expectedPValues = new Array2DRowRealMatrix(new double[][]{{0.326990571, 0.326990571, 0.326990571},
                {0.326990571, 0.326990571, 0.326990571}, {0.326990571, 0.326990571, 0.326990571}});
        assertEquals(expectedPValues, pValues);
    }

    @Test
    public void testCorrelationMethodWithValidArrays() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        SimpleRegression simpleRegression = new SimpleRegression();
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {2, 4, 6, 8, 10};
        simpleRegression.addData(x, y);
        double expectedCorrelation = simpleRegression.getR();
        assertEquals(expectedCorrelation, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationMethodWithInvalidArrays() {
        double[] xArray = {1};
        double[] yArray = {2, 3, 4};
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationMethodWithInvalidCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}, {0.5, 0.5}});
        new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
    }
}