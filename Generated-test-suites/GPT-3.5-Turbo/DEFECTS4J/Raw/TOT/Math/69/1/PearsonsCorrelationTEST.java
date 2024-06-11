import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    private double[][] data;

    @Before
    public void setUp() {
        data = new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.0001);
        assertEquals(0.9819805061, correlationMatrix.getEntry(0, 1), 0.0001);
        assertEquals(0.9819805061, correlationMatrix.getEntry(1, 0), 0.0001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.0001);
        assertEquals(0.9819805061, correlationMatrix.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);

        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.0001);
        assertEquals(0.9819805061, correlationMatrix.getEntry(0, 1), 0.0001);
        assertEquals(0.9819805061, correlationMatrix.getEntry(1, 0), 0.0001);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 0.0001);
        assertEquals(0.9819805061, correlationMatrix.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(correlationPValues);
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
    }

    @Test
    public void testCorrelation() throws MathException {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};

        double correlation = pearsonsCorrelation.correlation(xArray, yArray);

        assertEquals(1.0, correlation, 0.0001);
    }
}