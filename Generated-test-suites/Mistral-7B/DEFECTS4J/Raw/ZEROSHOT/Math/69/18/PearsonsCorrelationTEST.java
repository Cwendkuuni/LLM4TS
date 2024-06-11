package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyPearsonCorrelation() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertEquals(null, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationMatrixFromDataArray() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                { 1.0, 0.81915225, 0.85360619 },
                { 0.81915225, 1.0, 0.87758256 },
                { 0.85360619, 0.87758256, 1.0 }
        });
        assertEquals(expected, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationMatrixFromRealMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][] {
            { 1.0, 2.0, 3.0 },
            { 4.0, 5.0, 6.0 },
            { 7.0, 8.0, 9.0 }
        });
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                { 1.0, 0.81915225, 0.85360619 },
                { 0.81915225, 1.0, 0.87758256 },
                { 0.85360619, 0.87758256, 1.0 }
        });
        assertEquals(expected, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationMatrixFromCovariance() {
        double[][] covarianceData = {
            { 1.0, 0.5, 0.5 },
            { 0.5, 1.0, 0.5 },
            { 0.5, 0.5, 1.0 }
        };
        Covariance covariance = new Covariance(covarianceData);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                { 1.0, 0.8, 0.8 },
                { 0.8, 1.0, 0.8 },
                { 0.8, 0.8, 1.0 }
        });
        assertEquals(expected, pearsonCorrelation.getCorrelationMatrix());
        assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix stdErrors = pearsonCorrelation.getCorrelationStandardErrors();
        double[][] expected = { { 0.57735027, 0.51617042, 0.54170197 },
                { 0.51617042, 0.57735027, 0.55842261 },
                { 0.54170197, 0.55842261, 0.57735027 } };
        assertArrayEquals(expected, stdErrors.getData(), 0.001);
    }

    @Test(expected = RuntimeException.class)
    public void testCorrelationMatrixFromInvalidData() {
        double[][] data = { { 1.0, 2.0 }, { 4.0, 5.0 } };
        new PearsonsCorrelation(data);
    }

    @Test(expected = RuntimeException.class)
    public void testCorrelationMatrixFromInvalidRealMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][] {
            { 1.0, 2.0 },
            { 4.0, 5.0 },
            { 7.0, 8.0 }
        });
        new PearsonsCorrelation(inputMatrix);
    }
}