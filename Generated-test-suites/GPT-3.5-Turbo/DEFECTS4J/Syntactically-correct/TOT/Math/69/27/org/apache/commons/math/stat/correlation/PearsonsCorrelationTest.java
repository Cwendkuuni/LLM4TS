import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setup() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        RealMatrix actualMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertTrue(expectedMatrix.equals(actualMatrix));
    }

    @Test
    public void testCorrelationWithValidInput() throws MathException {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double expectedCorrelation = 1.0;
        double actualCorrelation = pearsonsCorrelation.correlation(xArray, yArray);
        assertTrue(expectedCorrelation == actualCorrelation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidInput() throws MathException {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{9, 10, 11}, {10, 21, 17}, {11, 17, 28}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceMatrixData);
        RealMatrix expectedCorrelationMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.625, 0.615}, {0.625, 1.0, 0.629}, {0.615, 0.629, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertTrue(expectedCorrelationMatrix.equals(actualCorrelationMatrix));
    }

    // Other test cases are not included for brevity

}