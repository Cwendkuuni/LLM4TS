import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {
    
    private PearsonsCorrelation pearsonsCorrelation;
    
    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }
    
    @Test
    public void testConstructorNoData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNotNull(pc);
        assertNull(pc.getCorrelationMatrix());
    }
    
    @Test
    public void testConstructorDoubleArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        assertNotNull(pc);
        assertNotNull(pc.getCorrelationMatrix());
    }
    
    @Test
    public void testConstructorRealMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        assertNotNull(pc);
        assertNotNull(pc.getCorrelationMatrix());
    }
    
    @Test
    public void testConstructorCovariance() {
        Covariance covariance = new Covariance(matrix);
        PearsonsCorrelation pc = new PearsonsCorrelation(covariance);
        assertNotNull(pc);
        assertNotNull(pc.getCorrelationMatrix());
    }
    
    @Test
    public void testConstructorCovarianceMatrix() {
        RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(covarianceData);
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 10);
        assertNotNull(pc);
        assertNotNull(pc.getCorrelationMatrix());
    }
    
    @Test
    public void testGetCorrelationMatrix() {
        assertNotNull(pearsonsCorrelation.getCorrelationMatrix());
    }
    
    @Test
    public void testGetCorrelationStandardErrors() {
        assertNotNull(pearsonsCorrelation.getCorrelationStandardErrors());
    }
    
    @Test
    public void testGetCorrelationPValues() throws MathException {
        assertNotNull(pearsonsCorrelation.getCorrelationPValues());
    }
    
    @Test
    public void testComputeCorrelationMatrixRealMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testComputeCorrelationMatrixDoubleArray() {
         double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
    }
    
    @Test
    public void testCorrelation() throws MathException {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertNotNull(correlation);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationInvalidArrayLength() throws MathException {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5};
        pearsonsCorrelation.correlation(xArray, yArray);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationInsufficientData() throws MathException {
        double[] xArray = {1};
        double[] yArray = {4};
        pearsonsCorrelation.correlation(xArray, yArray);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(covarianceData);
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationNullCovarianceMatrix() {
        RealMatrix covarianceMatrix = null;
        pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
    }
    
    @Test
    public void testCheckSufficientData() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        pearsonsCorrelation.checkSufficientData(matrix);
    }
    
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientDataInvalidMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1}});
        pearsonsCorrelation.checkSufficientData(matrix);
    }
}