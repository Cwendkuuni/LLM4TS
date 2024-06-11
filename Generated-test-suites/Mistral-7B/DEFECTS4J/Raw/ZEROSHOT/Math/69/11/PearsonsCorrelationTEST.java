package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.*;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.StatUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PearsonsCorrelationTest extends TestCase {

    public static Test suite() {
        return new TestSuite(PearsonsCorrelationTest.class);
    }

    public void testCorrelationEmptyMatrix() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.getNObs());
    }

    public void testCorrelation2x2() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.8}, {0.8, 1.0}});
        assertEquals(expected, correlationMatrix);
        assertEquals(4, pc.getNObs());
    }

    public void testCorrelationMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.63656758, 0.72973579},
            {0.63656758, 1.0, 0.74452826},
            {0.72973579, 0.74452826, 1.0}});
        assertEquals(expected, correlationMatrix);
        assertEquals(9, pc.getNObs());
    }

    public void testCorrelation2Variables() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(x, y);
        double expected = StatUtils.pearsonr(x, y)[0];
        assertEquals(expected, correlation, 0.001);
    }

    public void testCorrelationMatrix2Variables() {
        double[] x = {1.0, 2.0, 3.0};
        double[] y = {4.0, 5.0, 6.0};
        double[][] data = {{x[0], y[0]}, {x[1], y[1]}, {x[2], y[2]}};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pc.computeCorrelationMatrix(data);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.63656758},
            {0.63656758, 1.0}});
        assertEquals(expected, correlationMatrix);
    }

    public void testCorrelationStandardErrors() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {0.34202015, 0.34202015, 0.31622777},
            {0.34202015, 0.31622777, 0.34202015},
            {0.31622777, 0.34202015, 0.31622777}});
        assertEquals(expected, correlationStandardErrors);
    }

    public void testCorrelationPValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {0.09187777, 0.09187777, 0.05395954},
            {0.09187777, 0.05395954, 0.09187777},
            {0.05395954, 0.09187777, 0.05395954}});
        assertEquals(expected, correlationPValues);
    }
}