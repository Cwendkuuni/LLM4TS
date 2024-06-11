package org.apache.commons.math.stat.correlation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;

/**
 * Test file for the PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation()} .
     */
    @Test
    public void testConstructorEmpty() {
        PearsonsCorrelation pcc = new PearsonsCorrelation();
        assertEquals(null, pcc.getCorrelationMatrix());
        assertEquals(0, pcc.getnObs());
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(double[][])} .
     */
    @Test
    public void testConstructor2DArray() {
        double[][] data = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        PearsonsCorrelation pcc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pcc.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8414709848078965, 0.811138033225246},
                {0.8414709848078965, 1.0, 0.8414709848078965},
                {0.811138033225246, 0.8414709848078965, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pcc.getnObs());
    }

    /**
     * Test method for {@link PearsonsCorrelation#PearsonsCorrelation(RealMatrix)} .
     */
    @Test
    public void testConstructorRealMatrix() {
        double[][] data = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        PearsonsCorrelation pcc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pcc.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8414709848078965, 0.811138033225246},
                {0.8414709848078965, 1.0, 0.8414709848078965},
                {0.811138033225246, 0.8414709848078965, 1.0}}, correlationMatrix.getData());
        assertEquals(3, pcc.getnObs());
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationMatrix()} .
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        PearsonsCorrelation pcc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pcc.getCorrelationMatrix();
        assertArrayEquals(new double[][]{{1.0, 0.8414709848078965, 0.811138033225246},
                {0.8414709848078965, 1.0, 0.8414709848078965},
                {0.811138033225246, 0.8414709848078965, 1.0}}, correlationMatrix.getData());
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationStandardErrors()} .
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        PearsonsCorrelation pcc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pcc.getCorrelationMatrix();
        RealMatrix stdErrors = pcc.getCorrelationStandardErrors();
        assertArrayEquals(new double[][]{{0.3925103154890451, 0.3331468850851537, 0.3129825632450939},
                {0.3331468850851537, 0.3925103154890451, 0.3331468850851537},
                {0.3129825632450939, 0.3331468850851537, 0.3925103154890451}}, stdErrors.getData());
    }

    /**
     * Test method for {@link PearsonsCorrelation#getCorrelationPValues()} .
     *
     * @throws Exception the exception
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        PearsonsCorrelation pcc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pcc.getCorrelationMatrix();
        RealMatrix pValues = pcc.getCorrelationPValues();
        assertArrayEquals(new double[][]{{0.0, 0.34987961566453356, 0.3268378124436814},
                {0.34987961566453356, 0.0, 0.34987961566453356},
                {0.3268378124436814, 0.34987961566453356, 0.0}}, pValues.getData());
    }

}