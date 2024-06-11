import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrixFromRectangularArray() {
        // Given
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        // When
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        // Then
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.82951156, 0.8838356},
                {0.82951156, 1.0, 0.8838356},
                {0.8838356, 0.8838356, 1.0}});
        Assert.assertArrayEquals("Correlation matrix is not as expected.",
                expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.0);
    }

    @Test
    public void testCorrelationMatrixFromCovariance() {
        // Given
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);

        // When
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        // Then
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81649658},
                {0.81649658, 1.0}});
        Assert.assertArrayEquals("Correlation matrix is not as expected.",
                expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRectangularArray() {
        // Given
        double[][] data = {{1.0, 2.0}, {2.1}};

        // When
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        // Then
        // The exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCovarianceMatrix() {
        // Given
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}});

        // When
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);

        // Then
        // The exception should be thrown
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        // Given
        double[] xArray = {1.0, 2.0, 3.0, 4.0};
        double[] yArray = {2.0, 3.0, 4.0, 5.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);

        // When
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();

        // Then
        Assert.assertEquals("P-values shape is not as expected.", correlationMatrix.getRowDimension(),
                correlationPValues.getRowDimension());
        Assert.assertEquals("P-values column dimension is not as expected.", correlationMatrix.getColumnDimension(),
                correlationPValues.getColumnDimension());
        for (int i = 0; i < correlationMatrix.getRowDimension(); i++) {
            for (int j = 0; j < correlationMatrix.getColumnDimension(); j++) {
                Assert.assertTrue("Expected p-value to be less than 1, but is: " +
                        correlationPValues.getEntry(i, j), correlationPValues.getEntry(i, j) < 1.0);
            }
        }
    }
}