package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyPearsonsCorrelation() {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        assertNull(pearsonsCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testCreatePearsonsCorrelationFromData() {
        double[][] data = new double[][]{{0.1, 0.2, 0.3}, {0.4, 0.5, 0.6}, {0.7, 0.8, 0.9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertEquals(1.0, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.825624239848262, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.825624239848262, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(2, 2), 0.001);
        assertEquals(3, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testCreatePearsonsCorrelationFromMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(0.86602540378444, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.8, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.666666666666667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(3, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testCreatePearsonsCorrelationFromCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.5}, {1.5, 2.5}});
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 2);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        assertEquals(2, correlationMatrix.getRowDimension());
        assertEquals(2, correlationMatrix.getColumnDimension());
        assertEquals(0.8, correlationMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.832098550561718, correlationMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.832098550561718, correlationMatrix.getEntry(1, 0), 0.001);
        assertEquals(0.666666666666667, correlationMatrix.getEntry(1, 1), 0.001);
        assertEquals(2, pearsonsCorrelation.getNObs());
    }

    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = new double[][]{{0.1, 0.2, 0.3}, {0.4, 0.5, 0.6}, {0.7, 0.8, 0.9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        assertEquals(3, correlationStandardErrors.getRowDimension());
        assertEquals(3, correlationStandardErrors.getColumnDimension());
        assertEquals(0.288639535777855, correlationStandardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.288639535777855, correlationStandardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.288639535777855, correlationStandardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.342020143332158, correlationStandardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.342020143332158, correlationStandardErrors.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{0.1, 0.2, 0.3}, {0.4, 0.5, 0.6}, {0.7, 0.8, 0.9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertEquals(0.0007874027398372, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(0.0022294841432241, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.0022294841432241, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.013428958106961, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.013428958106961, correlationPValues.getEntry(2, 2), 0.001);
    }

}