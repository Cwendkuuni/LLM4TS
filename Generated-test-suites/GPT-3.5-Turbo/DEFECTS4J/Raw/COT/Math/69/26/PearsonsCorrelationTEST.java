import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationStandardErrors());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.getCorrelationPValues());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        assertNotNull(pearsonsCorrelation.computeCorrelationMatrix(data));
    }

    @Test
    public void testComputeCorrelationMatrixWithRealMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonsCorrelation.computeCorrelationMatrix(matrix));
    }

    @Test
    public void testCorrelation() throws IllegalArgumentException {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation.correlation(xArray, yArray));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidArrayDimensions() throws IllegalArgumentException {
        double[] xArray = {1.0, 2.0};
        double[] yArray = {3.0, 4.0, 5.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = pearsonsCorrelation.computeCovarianceMatrix(data);
        assertNotNull(pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix));
    }

    @Test
    public void testPearsonsCorrelationWithRealMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testPearsonsCorrelationWithCovariance() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        assertNotNull(pearsonsCorrelation);
    }

    @Test
    public void testPearsonsCorrelationWithCovarianceMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(2, 2);
        covarianceMatrix.setEntry(0, 0, 1.0);
        covarianceMatrix.setEntry(1, 1, 2.0);
        covarianceMatrix.setEntry(0, 1, 0.5);
        covarianceMatrix.setEntry(1, 0, 0.5);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        assertNotNull(pearsonsCorrelation);
    }
}