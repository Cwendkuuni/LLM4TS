package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link PearsonsCorrelation}.
 */
public class PearsonsCorrelationTest {

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        Assert.assertNull(pc.correlationMatrix);
        Assert.assertEquals(0, pc.nObs);
    }

    /**
     * Test creating a PearsonsCorrelation from a rectangular array.
     */
    @Test
    public void testCreateFromRectangularArray() {
        double[][] data = new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0, 0.8, 0.6},
                {0.8, 1.0, 0.8},
                {0.6, 0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test creating a PearsonsCorrelation from a RealMatrix.
     */
    @Test
    public void testCreateFromRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0, 0.8, 0.6},
                {0.8, 1.0, 0.8},
                {0.6, 0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test creating a PearsonsCorrelation from a Covariance.
     */
    @Test
    public void testCreateFromCovariance() {
        double[][] data = new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0},
                {7.0, 8.0}
        };
        Covariance covariance = new Covariance(data);
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, covariance.getN());
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0, 0.7416},
                {0.7416, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test creating a PearsonsCorrelation from an empty RealMatrix.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testCreateFromEmptyRealMatrix() {
        RealMatrix emptyMatrix = new BlockRealMatrix(new double[0][0]);
        new PearsonsCorrelation(emptyMatrix, 0);
    }

    /**
     * Test creating a PearsonsCorrelation from a RealMatrix with less than two columns.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testCreateFromRealMatrixWithLessThanTwoColumns() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{
                {1.0}
        });
        new PearsonsCorrelation(matrix);
    }

    /**
     * Test creating a PearsonsCorrelation from a RealMatrix with less than two rows.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testCreateFromRealMatrixWithLessThanTwoRows() {
        RealMatrix matrix = new BlockRealMatrix(new double[]{1.0});
        new PearsonsCorrelation(matrix);
    }

    /**
     * Test getting the correlation matrix.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {1.0, 0.8, 0.6},
                {0.8, 1.0, 0.8},
                {0.6, 0.8, 1.0}
        });
        RealMatrix actual = pc.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test getting the correlation standard errors.
     */
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {0.4828, 0.4828, 0.4828},
                {0.4828, 0.4828, 0.4828},
                {0.4828, 0.4828, 0.4828}
        });
        RealMatrix actual = pc.getCorrelationStandardErrors();
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test getting the correlation p-values.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix expected = new BlockRealMatrix(new double[][]{
                {0.0, 1.0, 1.0},
                {1.0, 0.0, 1.0},
                {1.0, 1.0, 0.0}
        });
        RealMatrix actual = pc.getCorrelationPValues();
        Assert.assertEquals(expected, actual);
    }
}