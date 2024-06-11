import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;

public class PearsonsCorrelationTest {
    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yArray = {2.0, 4.0, 6.0, 8.0, 10.0};

        double expectedCorrelation = 1.0;
        double actualCorrelation = pearsonsCorrelation.correlation(xArray, yArray);

        assertEquals(expectedCorrelation, actualCorrelation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidArrayDimensions() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 4.0, 6.0, 8.0, 10.0};

        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});

        RealMatrix actualMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);

        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {{5.0, 3.0}, {3.0, 4.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.948683}, {0.948683, 1.0}});

        RealMatrix actualMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);

        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix actualMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix actualMatrix = pearsonsCorrelation.getCorrelationStandardErrors();

        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}});
        pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix actualMatrix = pearsonsCorrelation.getCorrelationPValues();

        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testPearsonsCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}};
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix actualMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(expectedMatrix, actualMatrix);
    }
}