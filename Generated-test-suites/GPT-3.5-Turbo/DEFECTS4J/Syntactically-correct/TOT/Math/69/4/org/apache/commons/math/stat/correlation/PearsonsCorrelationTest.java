import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationWithValidData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};
        try {
            double correlation = pc.correlation(xArray, yArray);
            assertEquals(1.0, correlation, 0.0001);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException not expected.");
        }
    }

    @Test
    public void testCorrelationWithDifferentLengthData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0};
        try {
            pc.correlation(xArray, yArray);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("invalid array dimensions. xArray has size 3; yArray has 2 elements", e.getMessage());
        }
    }

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        RealMatrix matrix = pc.computeCorrelationMatrix(data);
        assertArrayEquals(expected.getData(), matrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = pc.computeCorrelationMatrix(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        RealMatrix corrStdErrors = pc.getCorrelationStandardErrors();
        assertArrayEquals(expected.getData(), corrStdErrors.getData());
    }

    @Test
    public void testGetCorrelationPValues() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = pc.computeCorrelationMatrix(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        try {
            RealMatrix corrPValues = pc.getCorrelationPValues();
            assertArrayEquals(expected.getData(), corrPValues.getData());
        } catch (MathException e) {
            fail("MathException not expected.");
        }
    }

    @Test
    public void testInvalidData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] invalidData = {{1.0}, {2.0}};
        try {
            PearsonsCorrelation pc = new PearsonsCorrelation(invalidData);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            assertEquals("insufficient data: only 2 rows and 1 columns.", e.getMessage());
        }
    }
}