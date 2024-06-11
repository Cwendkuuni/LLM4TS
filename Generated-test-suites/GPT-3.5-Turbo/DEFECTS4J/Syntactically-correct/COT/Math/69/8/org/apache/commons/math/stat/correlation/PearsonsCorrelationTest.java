
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        double[][] expected = {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        };
        assertArrayEquals(expected, correlation.computeCorrelationMatrix(data).getData());
    }

    @Test
    public void testCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expected = 1.0;
        assertEquals(expected, correlation.correlation(xArray, yArray), 0.0001);
    }

    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix covarianceMatrix = new BlockRealMatrix(matrixData);
        double[][] expectedData = {
            {1.0, 0.9701425001453319, 0.9701425001453319},
            {0.9701425001453319, 1.0, 0.9701425001453319},
            {0.9701425001453319, 0.9701425001453319, 1.0}
        };
        RealMatrix expected = new BlockRealMatrix(expectedData);
        assertArrayEquals(expected.getData(), correlation.covarianceToCorrelation(covarianceMatrix).getData());
    }
}
