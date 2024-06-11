import static org.junit.Assert.*;
import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Before;
import org.junit.Test;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;
    
    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }
    
    // Test for computeCorrelationMatrix(RealMatrix matrix)
    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix expected = new BlockRealMatrix(new double[][] {
            {1.0, 0.9999999999999998, 0.9999999999999998},
            {0.9999999999999998, 1.0, 0.9999999999999998},
            {0.9999999999999998, 0.9999999999999998, 1.0}
        });
        RealMatrix result = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        assertEquals(expected, result);
    }
    
    // Test for computeCorrelationMatrix(double[][] data)
    @Test
    public void testComputeCorrelationMatrixWithArray() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix expected = new BlockRealMatrix(new double[][] {
            {1.0, 0.9999999999999998, 0.9999999999999998},
            {0.9999999999999998, 1.0, 0.9999999999999998},
            {0.9999999999999998, 0.9999999999999998, 1.0}
        });
        RealMatrix result = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertEquals(expected, result);
    }
    
    // Test for correlation(double[] xArray, double[] yArray)
    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double expected = 0.9999999999999998;
        double result = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expected, result, 0.0);
    }
    
    // Test for correlation(double[] xArray, double[] yArray) with different array lengths
    @Test(expected = MathRuntimeException.class)
    public void testCorrelationWithDifferentArrayLengths() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0};
        pearsonsCorrelation.correlation(xArray, yArray);
    }
    
    // Test for getCorrelationMatrix()
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][] {
            {1.0, 0.9999999999999998, 0.9999999999999998},
            {0.9999999999999998, 1.0, 0.9999999999999998},
            {0.9999999999999998, 0.9999999999999998, 1.0}
        });
        RealMatrix result = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(expected, result);
    }
    
    // Test for getCorrelationStandardErrors()
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][] {
            {0.0, 8.429369388977535E-16, 8.429369388977535E-16},
            {8.429369388977535E-16, 0.0, 8.429369388977535E-16},
            {8.429369388977535E-16, 8.429369388977535E-16, 0.0}
        });
        RealMatrix result = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(expected, result);
    }
    
    // Test for getCorrelationPValues()
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        RealMatrix matrix = new BlockRealMatrix(data);
        pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][] {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        RealMatrix result = pearsonsCorrelation.getCorrelationPValues();
        assertEquals(expected, result);
    }
}