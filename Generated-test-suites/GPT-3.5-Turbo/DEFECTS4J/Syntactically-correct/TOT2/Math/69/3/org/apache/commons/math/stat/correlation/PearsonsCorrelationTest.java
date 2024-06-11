import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.linear.*;
import org.apache.commons.math.MathRuntimeException;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix result = pc.computeCorrelationMatrix(new double[][]{{1, 2}, {3, 4}});
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double result = pc.correlation(new double[]{1, 2, 3}, new double[]{4, 5, 6});
        assertEquals(1.0, result, 0.0001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInvalidCorrelationArrayLength() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        pc.correlation(new double[]{1, 2, 3}, new double[]{4, 5});
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        RealMatrix result = pc.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        pc.getCorrelationStandardErrors();
    }

    @Test
    public void testGetCorrelationPValues() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        try {
            pc.getCorrelationPValues();
        } catch (MathException e) {
            fail("MathException caught");
        }
    }

}