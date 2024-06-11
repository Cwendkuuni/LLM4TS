package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.distribution.TDistribution;
import org.apache.commons.math.distribution.TDistributionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataInvalidShape() {
        double[][] data = new double[1][];
        new PearsonsCorrelation(data);
    }

    @Test
    public void testDataRectangular() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        int nObs = pc.nObs;

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-12);
        assertEquals(0.408248290463863, correlationMatrix.getEntry(0, 1), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(0, 2), 1e-12);
        assertEquals(0.408248290463863, correlationMatrix.getEntry(1, 0), 1e-12);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(1, 2), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(2, 0), 1e-12);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(2, 2), 1e-12);

        assertEquals(3, nObs);
    }

    @Test
    public void testDataMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        int nObs = pc.nObs;

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-12);
        assertEquals(0.408248290463863, correlationMatrix.getEntry(0, 1), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(0, 2), 1e-12);
        assertEquals(0.408248290463863, correlationMatrix.getEntry(1, 0), 1e-12);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(1, 2), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(2, 0), 1e-12);
        assertEquals(1.0, correlationMatrix.getEntry(2, 1), 1e-12);
        assertEquals(0.632455512754669, correlationMatrix.getEntry(2, 2), 1e-12);

        assertEquals(3, nObs);
    }

    @Test
    public void testCovariance() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(new Covariance(covarianceMatrix));
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-12);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 1e-12);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 1e-12);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testCovarianceMatrix() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();

        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-12);
        assertEquals(0.5, correlationMatrix.getEntry(0, 1), 1e-12);
        assertEquals(0.5, correlationMatrix.getEntry(1, 0), 1e-12);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), 1e-12);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.5}, {1.5, 2.5}, {2.0, 2.5}, {2.5, 2.5}, {3.0, 2.5}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix pValues = pc.getCorrelationPValues();

        assertEquals(5, correlationMatrix.getRowDimension());
        assertEquals(5, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 1e-12);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 1e-12);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 1e-12);
        assertEquals(0.2, correlationMatrix.getEntry(1, 1), 1e-12);
        assertEquals(0.6, correlationMatrix.getEntry(1, 4), 1e-12);
        assertEquals(0.6, correlationMatrix.getEntry(4, 1), 1e-12);
        assertEquals(0.2, correlationMatrix.getEntry(4, 4), 1e-12);

        assertEquals(5, pValues.getRowDimension());
        assertEquals(5, pValues.getColumnDimension());
        assertEquals(0.2, pValues.getEntry(0, 1), 1e-12);
        assertEquals(0.2, pValues.getEntry(1, 0), 1e-12);
        assertEquals(0.6, pValues.getEntry(1, 4), 1e-12);
        assertEquals(0.6, pValues.getEntry(4, 1), 1e-12);
        assertEquals(0.958333930541746, pValues.getEntry(4, 4), 1e-12);
    }
}