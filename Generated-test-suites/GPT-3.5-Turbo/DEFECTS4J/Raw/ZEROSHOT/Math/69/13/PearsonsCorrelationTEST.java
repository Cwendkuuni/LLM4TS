import org.apache.commons.math.MathException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    private PearsonsCorrelation pearsonsCorrelation;

    @Before
    public void setUp() {
        pearsonsCorrelation = new PearsonsCorrelation();
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        RealMatrix computedMatrix = pearsonsCorrelation.computeCorrelationMatrix(data);
        assertArrayEquals(expectedMatrix.getData(), computedMatrix.getData());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeCorrelationMatrixThrowsExceptionIfInvalidDimensions() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6}
        };
        pearsonsCorrelation.computeCorrelationMatrix(data);
    }

    @Test
    public void testComputeCorrelationMatrixWithRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        RealMatrix computedMatrix = pearsonsCorrelation.computeCorrelationMatrix(matrix);
        assertArrayEquals(expectedMatrix.getData(), computedMatrix.getData());
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {3, 4, 5, 6, 7};
        double expectedCorrelation = 0.9999999999999996;
        double computedCorrelation = pearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(expectedCorrelation, computedCorrelation, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationThrowsExceptionIfLengthsDoNotMatch() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {3, 4, 5, 6};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationThrowsExceptionIfInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {3};
        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][] {
            {1.0, 2.0, 3.0},
            {2.0, 4.0, 6.0},
            {3.0, 6.0, 9.0}
        });
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        RealMatrix computedMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertArrayEquals(expectedMatrix.getData(), computedMatrix.getData());
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationThrowsExceptionIfCovarianceMatrixIsNull() {
        pearsonsCorrelation.covarianceToCorrelation(null);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][] {
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0},
            {1.0, 1.0, 1.0}
        });
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix computedMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(expectedMatrix.getData(), computedMatrix.getData());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][] {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix computedMatrix = pearsonsCorrelation.getCorrelationStandardErrors();
        assertArrayEquals(expectedMatrix.getData(), computedMatrix.getData());
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        RealMatrix expectedMatrix = new BlockRealMatrix(new double[][] {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix computedMatrix = pearsonsCorrelation.getCorrelationPValues();
        assertArrayEquals(expectedMatrix.getData(), computedMatrix.getData());
    }
}