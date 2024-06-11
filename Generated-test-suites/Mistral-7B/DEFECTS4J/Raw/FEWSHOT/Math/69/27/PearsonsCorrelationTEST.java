package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testDataConstructor() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8}, {0.8, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);

        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8}, {0.8, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);

        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8}, {0.8, 1.0}}, correlationMatrix.getData());
        assertEquals(4, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 2);

        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8}, {0.8, 1.0}}, correlationMatrix.getData());
        assertEquals(2, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testInvalidDataConstructor() {
        double[][] data = {{1.0}, {2.0}};
        assertThrows(IllegalArgumentException.class, () -> new PearsonsCorrelation(data));
    }

    @Test
    public void testCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8}, {0.8, 1.0}}, correlationMatrix.getData());
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        double[][] standardErrors = correlationStandardErrors.getData();
        assertArrayEquals(new double[][]{{0.0, 0.37370873}, {0.37370873, 0.0}}, standardErrors);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        double[][] pValues = correlationPValues.getData();
        assertArrayEquals(new double[][]{{0.44123762, 0.61904565}, {0.61904565, 0.44123762}}, pValues);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        assertArrayEquals(new double[][]{{1.0, 0.8}, {0.8, 1.0}}, correlationMatrix.getData());
    }

    @Test
    public void testCheckSufficientData() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}});
        assertDoesNotThrow(() -> PearsonsCorrelation.checkSufficientData(matrix));

        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}});
        assertThrows(IllegalArgumentException.class, () -> PearsonsCorrelation.checkSufficientData(emptyMatrix));

        RealMatrix singleColumnMatrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}, {1.0}});
        assertThrows(IllegalArgumentException.class, () -> PearsonsCorrelation.checkSufficientData(singleColumnMatrix));
    }
}