import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.stat.correlation.Covariance;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        // Create test data
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix matrix = new BlockRealMatrix(data);

        // Create PearsonsCorrelation instance
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);

        // Check correlation matrix
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertEquals(correlationMatrix.getRowDimension(), 3);
        assertEquals(correlationMatrix.getColumnDimension(), 3);
        assertEquals(correlationMatrix.getEntry(0, 0), 1d, 1e-6);
        assertEquals(correlationMatrix.getEntry(0, 1), 1d, 1e-6);
        assertEquals(correlationMatrix.getEntry(0, 2), 1d, 1e-6);

        // Check correlation standard errors
        RealMatrix stdErrors = correlation.getCorrelationStandardErrors();
        assertEquals(stdErrors.getRowDimension(), 3);
        assertEquals(stdErrors.getColumnDimension(), 3);

        // Check correlation p-values
        try {
            RealMatrix pValues = correlation.getCorrelationPValues();
            assertEquals(pValues.getRowDimension(), 3);
            assertEquals(pValues.getColumnDimension(), 3);
        } catch (MathException e) {
            fail("Failed to compute correlation p-values");
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        try {
            double result = correlation.correlation(xArray, yArray);
            assertEquals(result, 1d, 1e-6);
        } catch (IllegalArgumentException e) {
            fail("Failed to compute correlation");
        }

        double[] shortArray = {1};
        try {
            correlation.correlation(xArray, shortArray);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covMatrixData = {
            {2, 1},
            {1, 2}
        };
        RealMatrix covMatrix = new BlockRealMatrix(covMatrixData);

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix corrMatrix = correlation.covarianceToCorrelation(covMatrix);

        assertEquals(corrMatrix.getRowDimension(), 2);
        assertEquals(corrMatrix.getColumnDimension(), 2);
        assertEquals(corrMatrix.getEntry(0, 0), 1d, 1e-6);
        assertEquals(corrMatrix.getEntry(0, 1), 0.70710678, 1e-6);
        assertEquals(corrMatrix.getEntry(1, 0), 0.70710678, 1e-6);
        assertEquals(corrMatrix.getEntry(1, 1), 1d, 1e-6);
    }
}