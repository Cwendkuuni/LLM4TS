package org.apache.commons.math.stat.correlation;

import org.apache.commons.math.MathException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();

        assertEquals(1.0, correlationMatrix.getEntry(0, 0), MathUtils.EPSILON);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), MathUtils.EPSILON);
        assertEquals(1.0, correlationMatrix.getEntry(0, 1), MathUtils.EPSILON);
        assertEquals(1.0, correlationMatrix.getEntry(1, 0), MathUtils.EPSILON);
    }

    @Test
    public void testComputeCorrelationMatrixWithInvalidData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        try {
            PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        double correlation = pearsonsCorrelation.correlation(xArray, yArray);

        assertEquals(1.0, correlation, MathUtils.EPSILON);
    }

    @Test
    public void testCorrelationWithInvalidData() {
        double[] xArray = {1.0};
        double[] yArray = {4.0, 5.0};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        try {
            pearsonsCorrelation.correlation(xArray, yArray);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix covarianceMatrix = pearsonsCorrelation.computeCovarianceMatrix();
        RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);

        assertEquals(1.0, correlationMatrix.getEntry(0, 0), MathUtils.EPSILON);
        assertEquals(1.0, correlationMatrix.getEntry(1, 1), MathUtils.EPSILON);
        assertEquals(1.0, correlationMatrix.getEntry(0, 1), MathUtils.EPSILON);
        assertEquals(1.0, correlationMatrix.getEntry(1, 0), MathUtils.EPSILON);
    }

    @Test
    public void testCovarianceToCorrelationWithEmptyMatrix() {
        RealMatrix emptyMatrix = new BlockRealMatrix();
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        try {
            RealMatrix correlationMatrix = pearsonsCorrelation.covarianceToCorrelation(emptyMatrix);
            fail("Expected MathRuntimeException was not thrown");
        } catch (MathRuntimeException e) {
            // expected exception
        }
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonsCorrelation.getCorrelationStandardErrors();

        assertEquals(0.0, correlationStandardErrors.getEntry(0, 0), MathUtils.EPSILON);
        assertEquals(0.0, correlationStandardErrors.getEntry(1, 1), MathUtils.EPSILON);
        assertEquals(0.0, correlationStandardErrors.getEntry(0, 1), MathUtils.EPSILON);
        assertEquals(0.0, correlationStandardErrors.getEntry(1, 0), MathUtils.EPSILON);
    }

    @Test
    public void testGetCorrelationPValues() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        try {
            RealMatrix correlationPValues = pearsonsCorrelation.getCorrelationPValues();
            assertNotNull(correlationPValues);
        } catch (MathException e) {
            fail("MathException was thrown");
        }
    }

}