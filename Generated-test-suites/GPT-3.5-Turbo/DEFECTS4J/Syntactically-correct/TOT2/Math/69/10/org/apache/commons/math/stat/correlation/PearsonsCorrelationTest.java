import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {2.0, 4.0, 5.0, 4.0, 6.0};
        double expectedCorr = 0.9103664780794049;
        try {
            double actualCorr = pc.correlation(xArray, yArray);
            assertEquals(expectedCorr, actualCorr, 0.0001);
        } catch (MathRuntimeException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testInvalidInputForCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double[] xArray = {1.0, 2.0};
        double[] yArray = {2.0, 4.0, 5.0};
        try {
            pc.correlation(xArray, yArray);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testComputeCorrelationMatrixFromData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testComputeCorrelationMatrixFromRealMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(new BlockRealMatrix(data));
        assertNotNull(pc.getCorrelationMatrix());
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = pc.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testInvalidInputForComputeCorrelationMatrixFromData() {
        double[][] data = {{1.0}, {4.0, 5.0, 6.0}};
        try {
            new PearsonsCorrelation(data);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testInvalidInputForComputeCorrelationMatrixFromRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(1, 3);
        try {
            new PearsonsCorrelation(matrix);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        try {
            assertNotNull(pc.getCorrelationPValues());
        } catch (MathException e) {
            fail("Exception should not be thrown");
        }
    }

}