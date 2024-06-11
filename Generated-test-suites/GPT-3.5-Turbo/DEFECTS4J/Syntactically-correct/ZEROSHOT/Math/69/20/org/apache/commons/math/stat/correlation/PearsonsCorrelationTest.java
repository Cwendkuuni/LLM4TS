import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    private double[][] data;
    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setUp() {
        data = new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        pearsonsCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testConstructor() {
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testGetCorrelationMatrix() {
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        RealMatrix stdErrorsMatrix = pearsonsCorrelation.getCorrelationStandardErrors();
        assertNotNull(stdErrorsMatrix);
        assertEquals(3, stdErrorsMatrix.getRowDimension());
        assertEquals(3, stdErrorsMatrix.getColumnDimension());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix pValuesMatrix = pearsonsCorrelation.getCorrelationPValues();
        assertNotNull(pValuesMatrix);
        assertEquals(3, pValuesMatrix.getRowDimension());
        assertEquals(3, pValuesMatrix.getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(inputMatrix);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrixFromData() {
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testCorrelation() throws IllegalArgumentException {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};

        double expectedCorrelation = 1.0;

        double actualCorrelation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, actualCorrelation, 0.00001);
    }
}