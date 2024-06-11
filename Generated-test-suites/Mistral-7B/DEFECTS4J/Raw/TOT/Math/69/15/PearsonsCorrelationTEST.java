package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test construction from double array.
     */
    @Test
    public void testConstructionFromDoubleArray() {
        double[][] data = {{1.0, 2.0, 1.5}, {2.0, 3.0, 2.5}, {3.0, 4.0, 3.5}, {1.5, 2.5, 1.8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.866, 0.817}, {0.866, 1.0, 0.847}, {0.817, 0.847, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    /**
     * Test construction from RealMatrix.
     */
    @Test
    public void testConstructionFromRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);

        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8, 0.88}, {0.8, 1.0, 0.95}, {0.88, 0.95, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    /**
     * Test construction from Covariance.
     */
    @Test
    public void testConstructionFromCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{2.25, 1.1}, {1.1, 2.25}});
        Covariance covariance = new Covariance(covarianceMatrix);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);

        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.842}, {0.842, 1.0}});
        RealMatrix actualCorrelationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        Assert.assertEquals(expectedCorrelationMatrix, actualCorrelationMatrix);
    }

    /**
     * Test getCorrelationStandardErrors() method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {{1.0, 2.0, 1.5}, {2.0, 3.0, 2.5}, {3.0, 4.0, 3.5}, {1.5, 2.5, 1.8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix expectedStandardErrorsMatrix = new Array2DRowRealMatrix(new double[][]{{0.529, 0.465}, {0.465, 0.529}});
        RealMatrix actualStandardErrorsMatrix = pearsonsCorrelation.getCorrelationStandardErrors();

        Assert.assertEquals(expectedStandardErrorsMatrix, actualStandardErrorsMatrix);
    }

    /**
     * Test getCorrelationPValues() method.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 1.5}, {2.0, 3.0, 2.5}, {3.0, 4.0, 3.5}, {1.5, 2.5, 1.8}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix expectedPValuesMatrix = new Array2DRowRealMatrix(new double[][]{{0.114, 0.114}, {0.114, 0.114}});
        RealMatrix actualPValuesMatrix = pearsonsCorrelation.getCorrelationPValues();

        Assert.assertEquals(expectedPValuesMatrix, actualPValuesMatrix);
    }
}