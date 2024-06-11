package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TDistributionImpl;

import org.junit.Test;

/**
 * Test class for PearsonsCorrelation
 */
public class PearsonsCorrelationTest {

    @Test
    public void testConstructorNoData() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidData() {
        double[][] data = new double[1][1];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testConstructorRectangularArray() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 1d, 0.8775825619037756, 0.846354252063435 },
            { 0.8775825619037756, 1d, 0.846354252063435 },
            { 0.846354252063435, 0.846354252063435, 1d }
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testConstructorRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 1d, 0.8775825619037756, 0.846354252063435 },
            { 0.8775825619037756, 1d, 0.846354252063435 },
            { 0.846354252063435, 0.846354252063435, 1d }
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testConstructorCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 0.5, 0.5 }, { 0.5, 1, 0.5 }, { 0.5, 0.5, 1 }
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 1d, 0.8164965809277261, 0.8043880512941734 },
            { 0.8164965809277261, 1d, 0.8043880512941734 },
            { 0.8043880512941734, 0.8043880512941734, 1d }
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 1d, 0.8775825619037756, 0.846354252063435 },
            { 0.8775825619037756, 1d, 0.846354252063435 },
            { 0.846354252063435, 0.846354252063435, 1d }
        });
        assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 0.4335232122205574, 0.3458155262610748, 0.331238611648851 },
            { 0.3458155262610748, 0.4335232122205574, 0.331238611648851 },
            { 0.331238611648851, 0.331238611648851, 0.2494141434577627 }
        });
        assertEquals(expected, correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 0.4235513584431533, 0.0898988890177594, 0.0892685946673118 },
            { 0.0898988890177594, 0.4235513584431533, 0.0892685946673118 },
            { 0.0892685946673118, 0.0892685946673118, 0.5439502323852777 }
        });
        assertEquals(expected, correlationPValues);
    }

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        RealMatrix result = new PearsonsCorrelation().computeCorrelationMatrix(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 1d, 0.8775825619037756, 0.846354252063435 },
            { 0.8775825619037756, 1d, 0.846354252063435 },
            { 0.846354252063435, 0.846354252063435, 1d }
        });
        assertEquals(expected, result);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        double[] yArray = new double[] { 2, 4, 6, 8, 10, 12, 14, 16, 18 };
        double correlation = new PearsonsCorrelation().correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidData() {
        double[] xArray = new double[] { 1, 2 };
        double[] yArray = new double[] { 2, 4, 6 };
        new PearsonsCorrelation().correlation(xArray, yArray);
    }

    @Test(expected = MathException.class)
    public void testGetCorrelationPValues() throws MathException {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 0.5, 0.5 }, { 0.5, 1, 0.5 }, { 0.5, 0.5, 1 }
        });
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 0.4235513584431533, 0.0898988890177594, 0.0892685946673118 },
            { 0.0898988890177594, 0.4235513584431533, 0.0892685946673118 },
            { 0.0892685946673118, 0.0892685946673118, 0.5439502323852777 }
        });
        assertEquals(expected, correlationPValues);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 0.5, 0.5 }, { 0.5, 1, 0.5 }, { 0.5, 0.5, 1 }
        });
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
            { 1d, 0.8164965809277261, 0.8043880512941734 },
            { 0.8164965809277261, 1d, 0.8043880512941734 },
            { 0.8043880512941734, 0.8043880512941734, 1d }
        });
        assertEquals(expected, correlationMatrix);
    }
}