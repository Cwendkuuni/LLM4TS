import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(data);
        assertNotNull(pc.computeCorrelationMatrix(matrix));
    }

    @Test
    public void testComputeCorrelationMatrixWithInvalidData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(data);
        assertThrows(IllegalArgumentException.class, () -> {
            pc.computeCorrelationMatrix(matrix);
        });
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        assertTrue(pc.correlation(xArray, yArray) >= -1 && pc.correlation(xArray, yArray) <= 1);
    }

    @Test
    public void testCorrelationWithInvalidData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0};
        double[] yArray = {2.0};
        assertThrows(IllegalArgumentException.class, () -> {
            pc.correlation(xArray, yArray);
        });
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        Array2DRowRealMatrix covarianceMatrix = new Array2DRowRealMatrix(data);
        assertNotNull(pc.covarianceToCorrelation(covarianceMatrix));
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(data);
        assertNotNull(pc.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(data);
        assertNotNull(pc.getCorrelationPValues());
    }

}