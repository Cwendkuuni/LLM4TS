
/*
Tested Methods:
- PearsonsCorrelation()
- getCorrelationMatrix()
- computeCorrelationMatrix(RealMatrix)
- covarianceToCorrelation(RealMatrix)
- checkSufficientData(RealMatrix)
- correlation(double[], double[])
*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNotNull(pearsonsCorrelation);
    }
    
    @Test
    public void testComputeCorrelationMatrix() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2,3},{4,5,6}});
        RealMatrix correlationMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{{1,1},{1,1}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }
    
    @Test
    public void testCovarianceToCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(new double[][]{{1,2},{2,4}});
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{{1,1},{1,1}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataNotEnoughRows() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1,2}});
        pearsonsCorrelation.checkSufficientData(matrix);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientDataNotEnoughColumns() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1},{2}});
        pearsonsCorrelation.checkSufficientData(matrix);
    }
    
    @Test
    public void testCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double[] xArray = {1,2,3};
        double[] yArray = {4,5,6};
        double correlation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(1.0, correlation, 0.0001);
    }

    // Additional test cases can be added for each method to increase test coverage
}
