package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyPearsonsCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
        assertEquals(0, correlation.nObs);
    }

    @Test
    public void testPearsonsCorrelationDataArray() {
        double[][] data = { {1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0} };
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.81247141, 0.7433425},
                {0.81247141, 1.0, 0.81247141},
                {0.7433425, 0.81247141, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
        assertEquals(3, correlation.nObs);
    }

    @Test
    public void testPearsonsCorrelationRealMatrix() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.96828495},
                {0.96828495, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
        assertEquals(5, correlation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDataArray() {
        double[][] data = {{1.0, 2.0}, {1.0, 2.0}, {3.0, 4.0}, {4.0}};
        new PearsonsCorrelation(data);
    }

    @Test
    public void testPearsonsCorrelationCovariance() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        RealMatrix covarianceMatrix = new Covariance(data).getCovarianceMatrix();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, 5);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.96828495},
                {0.96828495, 1.0}});
        RealMatrix actualCorrelationMatrix = correlation.getCorrelationMatrix();
        assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
        assertEquals(5, correlation.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.05816749, 0.05816749},
                {0.05816749, 0.05816749}});
        RealMatrix actualCorrelationStandardErrors = correlation.getCorrelationStandardErrors();
        assertEquals(expectedCorrelationStandardErrors, actualCorrelationStandardErrors);
        assertEquals(5, correlation.nObs);
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValues() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(inputMatrix);
        correlation.getCorrelationPValues();
    }

}