import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testConstructor1() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNotNull(correlation);
    }

    @Test
    public void testConstructor2() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2InvalidData() {
        double[][] data = {
            {1.0, 2.0},
            {4.0, 5.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
    }

    @Test
    public void testConstructor3() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(new BlockRealMatrix(data));
        assertNotNull(correlation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor3InvalidData() {
        double[][] data = {
            {1.0, 2.0},
            {4.0, 5.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(new BlockRealMatrix(data));
    }

    @Test
    public void testConstructor4() {
        Covariance covariance = new Covariance(new BlockRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        }));
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        assertNotNull(correlation);
    }

    @Test(expected = MathRuntimeException.class)
    public void testConstructor4InvalidCovariance() {
        Covariance covariance = new Covariance();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
    }

    @Test
    public void testConstructor5() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        });
        int numberOfObservations = 2;
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, numberOfObservations);
        assertNotNull(correlation);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
        assertEquals(data.length, correlationMatrix.getRowDimension());
        assertEquals(data[0].length, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix standardErrors = correlation.getCorrelationStandardErrors();
        assertNotNull(standardErrors);
        assertEquals(data[0].length, standardErrors.getRowDimension());
        assertEquals(data[0].length, standardErrors.getColumnDimension());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix pValues = correlation.getCorrelationPValues();
        assertNotNull(pValues);
        assertEquals(data[0].length, pValues.getRowDimension());
        assertEquals(data[0].length, pValues.getColumnDimension());
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
        assertEquals(data.length, correlationMatrix.getRowDimension());
        assertEquals(data[0].length, correlationMatrix.getColumnDimension());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidData() {
        double[] xArray = {1.0};
        double[] yArray = {4.0};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceData = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        RealMatrix covarianceMatrix = new BlockRealMatrix(covarianceData);
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
        assertEquals(covarianceData.length, correlationMatrix.getRowDimension());
        assertEquals(covarianceData[0].length, correlationMatrix.getColumnDimension());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataInvalidData() {
        double[][] data = {
            {1.0},
            {2.0}
        };
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(new BlockRealMatrix(data));
    }
}